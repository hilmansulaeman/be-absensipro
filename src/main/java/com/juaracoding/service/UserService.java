        package com.juaracoding.service;

        import com.juaracoding.config.OtherConfig;
        import com.juaracoding.core.IService;
        import com.juaracoding.dto.response.UserResponseDTO;
        import com.juaracoding.dto.validasi.RegisDTO;
        import com.juaracoding.dto.validasi.VerifikasiForgotPasswordDTO;
        import com.juaracoding.handler.ResourceNotFoundException;
        import com.juaracoding.handler.ResponseHandler;
        import com.juaracoding.model.Akses;
        import com.juaracoding.model.Userz;
        import com.juaracoding.repo.UserRepository;
        import com.juaracoding.security.BcryptImpl;
        import com.juaracoding.utils.*;
        import jakarta.servlet.http.HttpServletRequest;
        import jakarta.validation.Valid;
        import jakarta.validation.constraints.NotBlank;
        import jakarta.validation.constraints.NotEmpty;
        import jakarta.validation.constraints.NotNull;
        import jakarta.validation.constraints.Pattern;
        import org.modelmapper.ModelMapper;
        import org.modelmapper.TypeToken;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.Pageable;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Service;
        import org.springframework.web.context.request.WebRequest;
        import org.springframework.transaction.annotation.Transactional;
        import org.springframework.web.multipart.MultipartFile;

        import java.util.*;

        @Service
        public class UserService implements IService<Userz> {

            private final UserRepository userRepository;

            private String[] strExceptionArr = new String[2];
            @Autowired
            private ModelMapper modelMapper;

            private String[] strProfile = new String[3];

            private TransformToDTO transformToDTO = new TransformToDTO();
            private Map<String, String> mapColumnSearch = new HashMap<>();
            private Map<String, Object> objectMapper = new HashMap<>();
            private StringBuilder stringBuilder = new StringBuilder();


            @Autowired
            public UserService(UserRepository userRepository) {
                mapColumn();
                strExceptionArr[0] = "UserService";
                this.userRepository = userRepository;
            }

            private void mapColumn() {
                mapColumnSearch.put("id", "ID USER");
                mapColumnSearch.put("nama", "NAMA LENGKAP");
                mapColumnSearch.put("email", "EMAIL");
                mapColumnSearch.put("noHP", "NO HP");
            }

            @Override
            public ResponseEntity<Object> save(Userz userz, HttpServletRequest request) {
                try {
                    userRepository.save(userz);
                    return new ResponseEntity<>("User saved successfully", HttpStatus.CREATED);
                } catch (Exception e) {
                    return new ResponseEntity<>("Error saving user", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            public Map<String, Object> checkRegis(Userz userz, WebRequest request) {
                int intVerification = new Random().nextInt(100000, 999999);
                List<Userz> listUserResult = userRepository.findByEmailOrNoHPOrUsername(
                        userz.getEmail(), userz.getNoHP(), userz.getUsername());

                String emailForSMTP = userz.getEmail();
                try {
                    if (!listUserResult.isEmpty()) {
                        Userz existingUser = listUserResult.get(0);
                        if (existingUser.getIsDelete() != 0) {
                            return checkExistingUser(existingUser, userz, request);
                        } else {
                            existingUser.setPassword(BcryptImpl.hash(userz.getPassword() + userz.getUsername()));
                            existingUser.setToken(BcryptImpl.hash(String.valueOf(intVerification)));
                            existingUser.setTokenCounter(existingUser.getTokenCounter() + 1);
                            existingUser.setModifiedBy(Integer.parseInt(existingUser.getIdUser().toString()));
                            existingUser.setModifiedDate(new Date());
                            userRepository.save(existingUser);
                        }
                    } else {
                        userz.setPassword(BcryptImpl.hash(userz.getPassword() + userz.getUsername()));
                        userz.setToken(BcryptImpl.hash(String.valueOf(intVerification)));
                        userRepository.save(userz);
                    }

                    sendVerificationEmail(userz, intVerification, emailForSMTP);
                } catch (Exception e) {
                    strExceptionArr[1] = "checkRegis(Userz userz) --- LINE 70";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                            HttpStatus.NOT_FOUND, null, "FE01001", request);
                }
                return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CHECK_REGIS,
                        HttpStatus.CREATED, null, null, request);
            }


            private Map<String, Object> checkExistingUser(Userz existingUser, Userz newUser, WebRequest request) {
                if (existingUser.getEmail().equals(newUser.getEmail())) {
                    return new ResponseHandler().generateModelAttribut(
                            ConstantMessage.ERROR_EMAIL_ISEXIST,
                            HttpStatus.CONFLICT,
                            null,
                            "FV01003",
                            request
                    );
                }
                return new HashMap<>();
            }

            private void sendVerificationEmail(Userz userz, int verificationCode, String emailForSMTP) {
                strProfile[0] = "Kode Verifikasi";
                strProfile[1] = userz.getNamaLengkap();
                strProfile[2] = String.valueOf(verificationCode);
                new ExecuteSMTP().sendSMTPToken(emailForSMTP, "Verifikasi Email", strProfile, "\\data\\verifikasi_email.html");
            }


            @Transactional(rollbackFor = Exception.class)
            public Map<String, Object> confirmRegis(Userz userz, String emails, WebRequest request) {
                Optional<Userz> userOptional = userRepository.findByEmail(emails);

                try {
                    if (userOptional.isPresent()) {
                        Userz existingUser = userOptional.get();

                        if (!BcryptImpl.verifyHash(userz.getToken(), existingUser.getToken())) {
                            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_TOKEN_INVALID,
                                    HttpStatus.NOT_ACCEPTABLE, null, "FV01005", request);
                        }

                        // Set status user sebagai aktif setelah token verifikasi berhasil
                        existingUser.setIsDelete((byte) 1);

                        // Atur akses default untuk pengguna baru
                        Akses akses = new Akses();
                        akses.setIdAkses(1L);
                        existingUser.setAkses(akses);

                        userRepository.save(existingUser);
                    } else {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                                HttpStatus.NOT_FOUND, null, "FV01006", request);
                    }
                } catch (Exception e) {
                    strExceptionArr[1] = "confirmRegis(Userz userz) --- LINE 103";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                            HttpStatus.INTERNAL_SERVER_ERROR, null, "FE01002", request);
                }

                return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CHECK_REGIS,
                        HttpStatus.OK, null, null, request);
            }

            @Transactional(rollbackFor = Exception.class)
            public Map<String, Object> doLogin(Userz userz, WebRequest request) {
                // Menggunakan email untuk login
                userz.setUsername(userz.getEmail());
                userz.setNoHP(userz.getEmail());

                List<Userz> listUserResult = userRepository.findByEmailOrNoHPOrUsername(
                        userz.getEmail(), userz.getNoHP(), userz.getUsername());

                Userz nextUser = null;
                try {
                    if (!listUserResult.isEmpty()) {
                        nextUser = listUserResult.get(0);
                        if (!BcryptImpl.verifyHash(userz.getPassword() + nextUser.getUsername(), nextUser.getPassword())) {
                            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_LOGIN_FAILED,
                                    HttpStatus.NOT_ACCEPTABLE, null, "FV01007", request);
                        }

                        // Update informasi login berhasil
                        nextUser.setLastLoginDate(new Date());
                        nextUser.setTokenCounter(0); // Reset percobaan token
                        nextUser.setPasswordCounter(0); // Reset percobaan password

                        // Simpan perubahan ke database
                        userRepository.save(nextUser);
                    } else {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                                HttpStatus.NOT_ACCEPTABLE, null, "FV01008", request);
                    }
                } catch (Exception e) {
                    strExceptionArr[1] = "doLogin(Userz userz, WebRequest request) --- LINE 132";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_LOGIN_FAILED,
                            HttpStatus.INTERNAL_SERVER_ERROR, null, "FE01003", request);
                }

                return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_LOGIN,
                        HttpStatus.OK, nextUser, null, request);
            }

            @Transactional(rollbackFor = Exception.class)
            public Map<String, Object> getNewToken(String emailz, WebRequest request) {
                Optional<Userz> userOptional = userRepository.findByEmail(emailz); // Ambil data dalam bentuk Optional
                String emailForSMTP = "";
                int intVerification = 0;
                Userz userz;

                try {
                    if (userOptional.isPresent()) {
                        intVerification = new Random().nextInt(100000, 999999);
                        userz = userOptional.get(); // Ambil objek Userz dari Optional

                        // Set token baru
                        userz.setToken(BcryptImpl.hash(String.valueOf(intVerification)));
                        userz.setModifiedDate(new Date());
                        userz.setModifiedBy(Integer.parseInt(userz.getIdUser().toString()));

                        emailForSMTP = userz.getEmail();

                        // Simpan perubahan ke database
                        userRepository.save(userz);
                    } else {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                                HttpStatus.NOT_ACCEPTABLE, null, "FV01009", request);
                    }
                } catch (Exception e) {
                    strExceptionArr[1] = "getNewToken(String emailz, WebRequest request) --- LINE 185";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                            HttpStatus.INTERNAL_SERVER_ERROR, null, "FE01004", request);
                }

                // Mengirim email dengan token baru
                strProfile[0] = "TOKEN BARU UNTUK VERIFIKASI GANTI PASSWORD";
                strProfile[1] = userz.getNamaLengkap();
                strProfile[2] = String.valueOf(intVerification);

                if (OtherConfig.getFlagSMTPActive().equalsIgnoreCase("y") && !emailForSMTP.isEmpty()) {
                    new ExecuteSMTP().sendSMTPToken(emailForSMTP, "VERIFIKASI TOKEN REGISTRASI", strProfile, "\\data\\ver_token_baru.html");
                }

                return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_LOGIN,
                        HttpStatus.OK, null, null, request);
            }

            @Transactional(rollbackFor = Exception.class)
            public Map<String, Object> sendMailForgetPwd(String email, WebRequest request) {
                int intVerification = 0;

                // Menggunakan Optional<Userz> jika hanya mengharapkan satu hasil
                Optional<Userz> userOptional = userRepository.findByEmail(email);
                Userz userz;

                try {
                    // Periksa jika pengguna ditemukan
                    if (userOptional.isEmpty()) {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                                HttpStatus.NOT_FOUND, null, "FV01010", request);
                    }

                    // Ambil pengguna pertama dari hasil pencarian
                    intVerification = new Random().nextInt(100000, 999999);
                    userz = userOptional.get();

                    // Set token verifikasi dan perbarui informasi di database
                    userz.setToken(BcryptImpl.hash(String.valueOf(intVerification)));
                    userz.setModifiedDate(new Date());
                    userz.setModifiedBy(Integer.parseInt(userz.getIdUser().toString()));

                    // Simpan perubahan ke database
                    userRepository.save(userz);
                } catch (Exception e) {
                    strExceptionArr[1] = "sendMailForgetPwd(String email) --- LINE 214";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                            HttpStatus.INTERNAL_SERVER_ERROR, null, "FE01005", request);
                }

                // Set data email untuk notifikasi
                strProfile[0] = "TOKEN UNTUK VERIFIKASI LUPA PASSWORD";
                strProfile[1] = userz.getNamaLengkap();
                strProfile[2] = String.valueOf(intVerification);

                /* EMAIL NOTIFICATION */
                if (OtherConfig.getFlagSMTPActive().equalsIgnoreCase("y") && !userz.getEmail().isEmpty()) {
                    new ExecuteSMTP().sendSMTPToken(userz.getEmail(), "VERIFIKASI TOKEN REGISTRASI", strProfile, "\\data\\ver_lupa_pwd.html");
                }

                return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_SEND_NEW_TOKEN,
                        HttpStatus.OK, null, null, request);
            }

            @Transactional(rollbackFor = Exception.class)
            public Map<String, Object> confirmTokenForgotPwd(VerifikasiForgotPasswordDTO forgetPasswordDTO, WebRequest request) {
                String email = forgetPasswordDTO.getEmail();
                String token = forgetPasswordDTO.getToken();

                // Gunakan Optional<Userz> jika email adalah unik
                Optional<Userz> userOptional = userRepository.findByEmail(email);
                Userz userz;

                try {
                    // Periksa jika pengguna ditemukan
                    if (userOptional.isEmpty()) {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_USER_NOT_EXISTS,
                                HttpStatus.NOT_FOUND, null, "FV01011", request);
                    }

                    // Ambil pengguna dari Optional
                    userz = userOptional.get();

                    // Verifikasi token
                    if (!BcryptImpl.verifyHash(token, userz.getToken())) {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_TOKEN_FORGOTPWD_NOT_SAME,
                                HttpStatus.NOT_FOUND, null, "FV01012", request);
                    }
                } catch (Exception e) {
                    strExceptionArr[1] = "confirmTokenForgotPwd(ForgetPasswordDTO forgetPasswordDTO, WebRequest request) --- LINE 250";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                            HttpStatus.INTERNAL_SERVER_ERROR, null, "FE01006", request);
                }

                return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_TOKEN_MATCH,
                        HttpStatus.OK, null, null, request);
            }

            @Transactional(rollbackFor = Exception.class)
            public Map<String, Object> confirmPasswordChange(VerifikasiForgotPasswordDTO verifikasiForgotPasswordDTO, WebRequest request) {
                String email = verifikasiForgotPasswordDTO.getEmail();
                String newPassword = verifikasiForgotPasswordDTO.getPasswordBaru();
                String confirmPassword = verifikasiForgotPasswordDTO.getPassword();

                Optional<Userz> userOptional = userRepository.findByEmail(email);

                try {
                    if (userOptional.isEmpty()) {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                                HttpStatus.NOT_FOUND, null, "FV01012", request);
                    }

                    Userz userz = userOptional.get();

                    // Validasi apakah password baru sama dengan konfirmasi password
                    if (!confirmPassword.equals(newPassword)) {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_PASSWORD_CONFIRM_FAILED,
                                HttpStatus.NOT_FOUND, null, "FV01014", request);
                    }

                    // Update password dan informasi pengguna
                    userz.setPassword(BcryptImpl.hash(newPassword + userz.getUsername()));
                    userz.setIsDelete((byte) 1);
                    userz.setModifiedDate(new Date());
                    userz.setModifiedBy(Integer.parseInt(userz.getIdUser().toString()));

                    // Simpan perubahan ke database
                    userRepository.save(userz);
                } catch (Exception e) {
                    strExceptionArr[1] = "confirmPasswordChange(VerifikasiForgotPasswordDTO verifikasiForgotPasswordDTO, WebRequest request) --- LINE 297";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                            HttpStatus.INTERNAL_SERVER_ERROR, null, "FE01006", request);
                }

                return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_CHANGE_PWD,
                        HttpStatus.OK, null, null, request);
            }

            public Map<String, Object> saveUser(Userz userz, WebRequest request) {
                String strMessage = ConstantMessage.SUCCESS_SAVE;
                Object strUserIdz = request.getAttribute("USR_ID", 1);
                int intVerification = new Random().nextInt(100000, 999999);
                String strToken;

                try {
                    if (strUserIdz == null) {
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                                HttpStatus.NOT_ACCEPTABLE, null, "FV03001", request);
                    }

                    strToken = BcryptImpl.hash(String.valueOf(intVerification));
                    userz.setPassword(strToken);
                    userz.setToken(String.valueOf(intVerification));
                    userz.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
                    userz.setCreatedDate(new Date());

                    userRepository.save(userz);
                } catch (Exception e) {
                    strExceptionArr[1] = "saveUser(Userz userz, WebRequest request) --- LINE 67";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                            HttpStatus.BAD_REQUEST,
                            transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                            "FE03001", request);
                }

                // Siapkan data untuk email notifikasi
                strProfile[0] = "LINK SET PASSWORD";
                strProfile[1] = userz.getNamaLengkap();

                // Inisialisasi ulang stringBuilder sebelum menggunakannya
                stringBuilder.setLength(0);
                strProfile[2] = stringBuilder.append(OtherConfig.getUrlEndPointVerify())
                        .append("/api/authz/v1/userman/vermail?uid=").append(BcryptImpl.hash(userz.getUsername()))
                        .append("&tkn=").append(strToken)
                        .append("&mail=").append(userz.getEmail()).toString();

                // Mengirim email notifikasi
                if (OtherConfig.getFlagSMTPActive().equalsIgnoreCase("y") && !userz.getEmail().isEmpty()) {
                    new ExecuteSMTP().sendSMTPToken(userz.getEmail(), "AKUN TELAH DIBUAT", strProfile, "\\data\\ver_set_pwd.html");
                }

                return new ResponseHandler().generateModelAttribut(strMessage,
                        HttpStatus.CREATED,
                        transformToDTO.transformObjectDataSave(objectMapper, userz.getIdUser(), mapColumnSearch),
                        null, request);
            }


            public Map<String, Object> updateUser(Long idUser, Userz userz, WebRequest request) {
                String strMessage = ConstantMessage.SUCCESS_UPDATE;
                Object strUserIdz = request.getAttribute("USR_ID", 1);

                try {
                    Userz nextUserz = userRepository.findById(idUser).orElse(null);

                    if (nextUserz == null) {
                        return new ResponseHandler().generateModelAttribut(
                                ConstantMessage.WARNING_MENU_NOT_EXISTS,
                                HttpStatus.NOT_ACCEPTABLE,
                                transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                                "FV01002", request
                        );
                    }

                    if (strUserIdz == null) {
                        return new ResponseHandler().generateModelAttribut(
                                ConstantMessage.ERROR_FLOW_INVALID,
                                HttpStatus.NOT_ACCEPTABLE,
                                null,
                                "FV03003", request
                        );
                    }

                    // Update user properties
                    nextUserz.setNamaLengkap(userz.getNamaLengkap());
                    nextUserz.setPassword(BcryptImpl.hash(userz.getPassword() + userz.getUsername()));
                    nextUserz.setTanggalLahir(userz.getTanggalLahir());
                    nextUserz.setEmail(userz.getEmail());
                    nextUserz.setAkses(userz.getAkses());
                    nextUserz.setNoHP(userz.getNoHP());
                    nextUserz.setUsername(userz.getUsername());
                    nextUserz.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
                    nextUserz.setModifiedDate(new Date());

                    // Save updated user
                    userRepository.save(nextUserz);

                } catch (Exception e) {
                    strExceptionArr[1] = "updateUser(Long idUser, Userz userz, WebRequest request) --- LINE 92";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().generateModelAttribut(
                            ConstantMessage.ERROR_SAVE_FAILED,
                            HttpStatus.BAD_REQUEST,
                            transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                            "FE03002", request
                    );
                }

                return new ResponseHandler().generateModelAttribut(
                        strMessage,
                        HttpStatus.CREATED,
                        transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                        null, request
                );
            }

            @Transactional(rollbackFor = Exception.class)
            public Map<String, Object> saveUploadFileUser(List<Userz> listUserz, MultipartFile multipartFile, WebRequest request) {
                String strMessage = ConstantMessage.SUCCESS_SAVE;

                try {
                    List<Userz> listUserResults = userRepository.saveAll(listUserz);

                    if (listUserResults.isEmpty()) {
                        strExceptionArr[1] = "saveUploadFileUser(List<Userz> listUserz, MultipartFile multipartFile, WebRequest request) --- LINE 82";
                        LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("FILE KOSONG"), OtherConfig.getFlagLogging());
                        return new ResponseHandler().generateModelAttribut(
                                ConstantMessage.ERROR_EMPTY_FILE + " -- " + multipartFile.getOriginalFilename(),
                                HttpStatus.BAD_REQUEST, null, "FV03004", request
                        );
                    }

                } catch (Exception e) {
                    strExceptionArr[1] = "saveUploadFileUser(List<Userz> listUserz, MultipartFile multipartFile, WebRequest request) --- LINE 88";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().generateModelAttribut(
                            ConstantMessage.ERROR_SAVE_FAILED,
                            HttpStatus.BAD_REQUEST, null, "FE03002", request
                    );
                }

                return new ResponseHandler().generateModelAttribut(
                        strMessage,
                        HttpStatus.CREATED,
                        transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                        null, request
                );
            }


            public Map<String, Object> findAllUser(Pageable pageable, WebRequest request) {
                List<UserResponseDTO> listUserDTO = null;
                Map<String, Object> mapResult = null;
                Page<Userz> pageUser;
                List<Userz> listUser;

                try {
                    pageUser = userRepository.findByIsDelete(pageable, (byte) 1);
                    listUser = pageUser.getContent();

                    if (listUser.isEmpty()) {
                        return new ResponseHandler().generateModelAttribut(
                                ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper, pageable, mapColumnSearch),
                                "FV03005",
                                request);
                    }

                    // Convert listUser to listUserDTO using UserResponseDTO
                    listUserDTO = modelMapper.map(listUser, new TypeToken<List<UserResponseDTO>>() {
                    }.getType());
                    mapResult = transformToDTO.transformObject(objectMapper, listUserDTO, pageUser, mapColumnSearch);

                } catch (Exception e) {
                    strExceptionArr[1] = "findAllUser(Pageable pageable, WebRequest request)";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().generateModelAttribut(
                            ConstantMessage.ERROR_INTERNAL_SERVER,
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            transformToDTO.transformObjectDataEmpty(objectMapper, pageable, mapColumnSearch),
                            "FE03003",
                            request);
                }

                return new ResponseHandler().generateModelAttribut(
                        ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        null);
            }

            @Override
            public ResponseEntity<Object> update(Long id, Userz userz, HttpServletRequest request) {
                try {
                    // Cari user berdasarkan ID
                    Optional<Userz> existingUser = userRepository.findById(id);

                    if (existingUser.isPresent()) {
                        Userz userToUpdate = existingUser.get();

                        // Update data user
                        if (userz.getNamaLengkap() != null) userToUpdate.setNamaLengkap(userz.getNamaLengkap());
                        if (userz.getEmail() != null) userToUpdate.setEmail(userz.getEmail());
                        if (userz.getNoHP() != null) userToUpdate.setNoHP(userz.getNoHP());
                        if (userz.getPassword() != null) userToUpdate.setPassword(BcryptImpl.hash(userz.getPassword() + userToUpdate.getUsername()));
                        if (userz.getUsername() != null) userToUpdate.setUsername(userz.getUsername());
                        if (userz.getTanggalLahir() != null) userToUpdate.setTanggalLahir(userz.getTanggalLahir());
                        if (userz.getAkses() != null) userToUpdate.setAkses(userz.getAkses());

                        userToUpdate.setModifiedBy(Integer.parseInt(request.getAttribute("USR_ID").toString()));
                        userToUpdate.setModifiedDate(new Date());

                        // Simpan perubahan ke database
                        userRepository.save(userToUpdate);

                        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                    }
                } catch (Exception e) {
                    return new ResponseEntity<>("Error updating user", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            @Override
            public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
                try {
                    userRepository.deleteById(id);
                    return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>("Error deleting user", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            @Override
            public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
                try {
                    // Mengambil data pengguna dengan pagination
                    Page<Userz> pageUserz = userRepository.findByIsDelete(pageable, (byte) 1); // Assuming '1' indicates active users

                    if (pageUserz.isEmpty()) {
                        return new ResponseEntity<>("No users found", HttpStatus.OK);
                    }

                    // Konversi ke DTO jika diperlukan
                    List<UserResponseDTO> userResponseDTOList = modelMapper.map(pageUserz.getContent(), new TypeToken<List<UserResponseDTO>>() {}.getType());

                    Map<String, Object> response = new HashMap<>();
                    response.put("users", userResponseDTOList);
                    response.put("currentPage", pageUserz.getNumber());
                    response.put("totalItems", pageUserz.getTotalElements());
                    response.put("totalPages", pageUserz.getTotalPages());

                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>("Error retrieving users", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            @Override
            public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
                try {
                    Optional<Userz> userOptional = userRepository.findById(id);

                    if (userOptional.isPresent()) {
                        Userz user = userOptional.get();
                        UserResponseDTO userDTO = modelMapper.map(user, UserResponseDTO.class);
                        return new ResponseEntity<>(userDTO, HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
                    }
                } catch (Exception e) {
                    return new ResponseEntity<>("Error retrieving user", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            @Override
            public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
                try {
                    Page<Userz> usersPage;

                    switch (columnName.toLowerCase()) {
                        case "email":
                            usersPage = userRepository.findByEmailContaining(value, pageable);
                            break;
                        case "username":
                            usersPage = userRepository.findByUsernameContaining(value, pageable);
                            break;
                        default:
                            return new ResponseEntity<>("Invalid column name", HttpStatus.BAD_REQUEST);
                    }

                    if (usersPage.isEmpty()) {
                        return new ResponseEntity<>("No users found for the given parameter", HttpStatus.OK);
                    }

                    List<UserResponseDTO> userDTOList = modelMapper.map(usersPage.getContent(), new TypeToken<List<UserResponseDTO>>() {
                    }.getType());

                    Map<String, Object> response = new HashMap<>();
                    response.put("users", userDTOList);
                    response.put("currentPage", usersPage.getNumber());
                    response.put("totalItems", usersPage.getTotalElements());
                    response.put("totalPages", usersPage.getTotalPages());

                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>("Error finding users by parameter", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
                // Menambahkan metode getAllUser
                public List<UserResponseDTO> getAllUser () {
                    List<UserResponseDTO> listUserResponseDTO = null;
                    List<Userz> listUser;

                    try {
                        listUser = userRepository.findByIsDelete((byte) 1);
                        if (listUser.isEmpty()) {
                            return new ArrayList<>();
                        }
                        listUserResponseDTO = modelMapper.map(listUser, new TypeToken<List<UserResponseDTO>>() {
                        }.getType());
                    } catch (Exception e) {
                        strExceptionArr[1] = "getAllUser() --- LINE 304";
                        LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    }
                    return listUserResponseDTO;
                }

                private Page<Userz> getDataByValue (Pageable pageable, String paramColumn, String paramValue){
                    if (paramValue.isEmpty()) {
                        return userRepository.findByIsDelete(pageable, (byte) 1);
                    }
                    switch (paramColumn) {
                        case "id":
                            return userRepository.findByIsDeleteAndIdUser(pageable, (byte) 1, Long.parseLong(paramValue));
                        case "nama":
                            return userRepository.findByIsDeleteAndNamaLengkapContainsIgnoreCase(pageable, (byte) 1, paramValue);
                        case "email":
                            return userRepository.findByIsDeleteAndEmailContainsIgnoreCase(pageable, (byte) 1, paramValue);
                        case "username":
                            return userRepository.findByIsDeleteAndUsernameContainsIgnoreCase(pageable, (byte) 1, paramValue);
                        case "noHP":
                            return userRepository.findByIsDeleteAndNoHPContainsIgnoreCase(pageable, (byte) 1, paramValue);
                        default:
                            return userRepository.findByIsDelete(pageable, (byte) 1);
                    }
                }

                public Map<String, Object> linkMailVerify (String usrId, String token, String mail){
                    Optional<Userz> listUsr = userRepository.findByEmail(mail);

                    if (listUsr.isEmpty()) {
                        strExceptionArr[1] = "linkMailVerify(String usrId, String token,String mail) --- LINE 666";
                        LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("Otentikasi Tidak Valid"), OtherConfig.getFlagLogging());
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                                HttpStatus.NOT_FOUND, null, "FE01001", null);
                    } else {
                        Userz userz = listUsr.get();
                        if (userz.getIsDelete() == 1) {
                            return new ResponseHandler().generateModelAttribut(ConstantMessage.USER_IS_ACTIVE,
                                    HttpStatus.NOT_FOUND, null, null, null);
                        } else {
                            if (BcryptImpl.verifyHash(userz.getToken(), token) && BcryptImpl.verifyHash(String.valueOf(userz.getUsername()), usrId)) {
                                return new ResponseHandler().generateModelAttribut(ConstantMessage.VERIFY_LINK_VALID,
                                        HttpStatus.CREATED, null, null, null);
                            } else {
                                strExceptionArr[1] = "linkMailVerify(String usrId, String token,String mail) --- LINE 683";
                                LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("Otentikasi Tidak Valid"), OtherConfig.getFlagLogging());
                                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                                        HttpStatus.NOT_FOUND, null, "FE01001", null);
                            }
                        }
                    }
                }

                private List<Userz> getDataToExport (String paramColumn, String paramValue){
                    if (paramValue.isEmpty()) {
                        return userRepository.findByIsDelete((byte) 1);
                    }
                    switch (paramColumn) {
                        case "id":
                            return userRepository.findByIsDeleteAndIdUser((byte) 1, Long.parseLong(paramValue));
                        case "nama":
                            return userRepository.findByIsDeleteAndNamaLengkapContainsIgnoreCase((byte) 1, paramValue);
                        case "email":
                            return userRepository.findByIsDeleteAndEmailContainsIgnoreCase((byte) 1, paramValue);
                        case "username":
                            return userRepository.findByIsDeleteAndUsernameContainsIgnoreCase((byte) 1, paramValue);
                        case "noHP":
                            return userRepository.findByIsDeleteAndNoHPContainsIgnoreCase((byte) 1, paramValue);
                        default:
                            return userRepository.findByIsDelete((byte) 1);
                    }
                }

                public List<UserResponseDTO> dataToExport (WebRequest request, String columnFirst, String valueFirst){
                    List<Userz> listUserz;
                    List<UserResponseDTO> listUserResponseDTO;

                    try {
                        if (columnFirst.equals("id")) {
                            try {
                                Long.parseLong(valueFirst);
                            } catch (Exception e) {
                                strExceptionArr[1] = "dataToExport(WebRequest request,String columnFirst,String valueFirst) --- LINE 209";
                                LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                                return new ArrayList<>();
                            }
                        }
                        listUserz = getDataToExport(columnFirst, valueFirst);
                        if (listUserz.isEmpty()) {
                            return new ArrayList<>();
                        }
                        listUserResponseDTO = modelMapper.map(listUserz, new TypeToken<List<UserResponseDTO>>() {
                        }.getType());
                    } catch (Exception e) {
                        strExceptionArr[1] = "dataToExport(WebRequest request,String columnFirst,String valueFirst) --- LINE 243";
                        LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                        return new ArrayList<>();
                    }
                    return listUserResponseDTO;
                }

                public Map<String, Object> deleteUser (Long idUser, WebRequest request){
                    String strMessage = ConstantMessage.SUCCESS_DELETE;
                    Object strUserIdz = request.getAttribute("USR_ID", 1);
                    Userz nextUser;

                    try {
                        nextUser = userRepository.findById(idUser).orElseThrow(() -> new ResourceNotFoundException("User not found"));

                        if (strUserIdz == null) {
                            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                                    HttpStatus.NOT_ACCEPTABLE, null, "FV05007", request);
                        }
                        nextUser.setIsDelete((byte) 0);
                        nextUser.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
                        nextUser.setModifiedDate(new Date());

                        userRepository.save(nextUser);
                    } catch (Exception e) {
                        strExceptionArr[1] = "deleteUser(Long idUser, WebRequest request) --- LINE 344";
                        LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                        return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                                HttpStatus.BAD_REQUEST, null, "FE05007", request);
                    }
                    return new ResponseHandler().generateModelAttribut(strMessage,
                            HttpStatus.OK, null, null, request);
                }
            }


