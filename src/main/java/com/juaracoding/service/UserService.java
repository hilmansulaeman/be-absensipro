package com.juaracoding.service;

import com.juaracoding.core.IService;
import com.juaracoding.model.Userz;
import com.juaracoding.repo.UserRepository;
import com.juaracoding.utils.ConstantMessage;
import com.juaracoding.utils.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IService<Userz> {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Object> save(Userz user, HttpServletRequest request) {
        Optional<Userz> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return GlobalFunction.validasiGagal(ConstantMessage.ERROR_EMAIL_IS_EMPTY, "USER_ALREADY_EXISTS", request);
        }

        userRepository.save(user);
        return GlobalFunction.dataBerhasilDisimpan(request);
    }

    @Override
    public ResponseEntity<Object> update(Long id, Userz user, HttpServletRequest request) {
        Optional<Userz> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        Userz updatedUser = existingUser.get();
        updatedUser.setUsername(user.getUsername());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setAlamat(user.getAlamat());
        updatedUser.setNamaLengkap(user.getNamaLengkap());
        updatedUser.setNoHP(user.getNoHP());
        updatedUser.setTanggalLahir(user.getTanggalLahir());
        updatedUser.setModifiedDate(new java.util.Date());
        userRepository.save(updatedUser);

        return GlobalFunction.dataBerhasilDiubah(request);
    }

    @Override
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request) {
        Optional<Userz> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        userRepository.deleteById(id);
        return GlobalFunction.dataBerhasilDihapus(request);
    }

    @Override
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request) {
        Page<Userz> users = userRepository.findAll(pageable);
        if (users.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        return GlobalFunction.customDataDitemukan("Users fetched successfully", users.getContent(), request);
    }

    @Override
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request) {
        Optional<Userz> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        return GlobalFunction.dataByIdDitemukan(user.get(), request);
    }

    @Override
    public ResponseEntity<Object> findByParam(Pageable pageable, String columnName, String value, HttpServletRequest request) {
        Page<Userz> users;

        switch (columnName.toLowerCase()) {
            case "email":
                users = userRepository.findByEmailContaining(value, pageable);
                break;
            case "username":
                users = userRepository.findByUsernameContaining(value, pageable);
                break;
            default:
                return GlobalFunction.customReponse("INVALID_PARAM", "Parameter tidak valid", request);
        }

        if (users.isEmpty()) {
            return GlobalFunction.dataTidakDitemukan(request);
        }

        return GlobalFunction.customDataDitemukan("Filtered users", users.getContent(), request);
    }
}
