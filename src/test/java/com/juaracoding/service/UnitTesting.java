//    package com.juaracoding.service;
//
//
//    import com.juaracoding.dto.report.AbsenReportDTO;
//    import com.juaracoding.dto.report.EmployeeReportDTO;
//    import com.juaracoding.dto.response.AbsenResponseDTO;
//    import com.juaracoding.dto.response.EmployeeResponseDTO;
//    import com.juaracoding.dto.response.MenuHeaderResponseDTO;
//    import com.juaracoding.dto.response.MenuResponseDTO;
//    import com.juaracoding.dto.validasi.*;
//    import com.juaracoding.model.*;
//    import com.juaracoding.repo.*;
//    import com.juaracoding.utils.GlobalFunction;
//    import jakarta.servlet.http.HttpServletRequest;
//    import org.junit.jupiter.api.BeforeEach;
//    import org.junit.jupiter.api.Test;
//    import org.mockito.InjectMocks;
//    import org.mockito.Mock;
//
//    import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//    import static org.junit.Assert.assertTrue;
//    import static org.mockito.Mockito.verify;
//    import static org.mockito.Mockito.times;
//    import static org.mockito.Mockito.any;
//    import org.mockito.MockitoAnnotations;
//    import org.springframework.data.domain.Page;
//    import org.springframework.data.domain.PageImpl;
//    import org.springframework.data.domain.PageRequest;
//    import org.springframework.http.ResponseEntity;
//
//    import java.util.*;
//
//    import static org.junit.jupiter.api.Assertions.assertEquals;
//    import static org.mockito.Mockito.when;
//
//    class UnitTesting {
//
//        @Mock
//        private UserRepository userRepository;
//
//        @Mock
//        private HttpServletRequest request;
//
//        @Mock
//        private EmployeeRepository employeeRepository;
//
//        @InjectMocks
//        private UserService userService;
//
//        @Mock
//        private AksesRepository aksesRepository;
//
//        @Mock
//        private DivisiRepository divisiRepository;
//
//        @Mock
//        private KaryawanRepository karyawanRepository;
//
//        @InjectMocks
//        private AksesService aksesService;
//
//        @InjectMocks
//        private DivisiService divisiService;
//
//        @InjectMocks
//        private KaryawanService karyawanService;
//
//        @InjectMocks
//        private EmployeeService employeeService;
//
//        @Mock
//        private MenuRepository menuRepository;
//
//        @InjectMocks
//        private MenuService menuService;
//
//        @Mock
//        private MenuHeaderRepository menuHeaderRepository;
//
//        @InjectMocks
//        private MenuHeaderService menuHeaderService;
//
//        @Mock
//        private AbsenRepository absenRepository;
//
//        @InjectMocks
//        private AbsenService absenService;
//
//        @BeforeEach
//        void setUp() {
//            MockitoAnnotations.openMocks(this);
//        }
//
//        @Test
//        void testSaveUser() {
//            Userz user = new Userz();
//            user.setEmail("test@example.com");
//
//            when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
//            when(userRepository.save(user)).thenReturn(user);
//
//            ResponseEntity<Object> response = userService.save(user, request);
//
//            // Casting body menjadi Map atau objek yang sesuai dengan respons Anda
//            Map<String, Object> expectedBody = (Map<String, Object>) GlobalFunction.dataBerhasilDisimpan(request).getBody();
//            Map<String, Object> actualBody = (Map<String, Object>) response.getBody();
//
//            // Bandingkan pesan, status, dan timestamp jika ada di respons
//            assertEquals(expectedBody.get("message"), actualBody.get("message"));
//            assertEquals(expectedBody.get("status"), actualBody.get("status"));
//            assertEquals(expectedBody.get("success"), actualBody.get("success"));
//        }
//
//
//        @Test
//        void testFindUserById() {
//            Userz user = new Userz();
//            user.setIdUser(1L);
//            user.setEmail("test@example.com");
//
//            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//            ResponseEntity<Object> response = userService.findById(1L, request);
//            assertEquals(GlobalFunction.dataByIdDitemukan(user, request), response);
//        }
//
//        @Test
//        void testFindAllUsers() {
//            Page<Userz> users = new PageImpl<>(Collections.singletonList(new Userz()));
//            when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(users);
//
//            ResponseEntity<Object> response = userService.findAll(PageRequest.of(0, 10), request);
//            assertEquals(GlobalFunction.customDataDitemukan("Users fetched successfully", users.getContent(), request), response);
//        }
//
//        @Test
//        void testCreateAkses_Success() {
//            AksesValidasiDTO aksesDTO = new AksesValidasiDTO();
//            aksesDTO.setNamaAkses("Admin");
//            aksesDTO.setDivisiId(1L);
//
//            Divisi divisi = new Divisi();
//            divisi.setIdDivisi(1L);
//
//            when(aksesRepository.findByNamaAkses("Admin")).thenReturn(Optional.empty());
//            when(divisiRepository.findById(1L)).thenReturn(Optional.of(divisi));
//
//            ResponseEntity<Object> response = aksesService.createAkses(aksesDTO, request);
//
//            assertEquals(GlobalFunction.dataBerhasilDisimpan(request).getStatusCode(), response.getStatusCode());
//            verify(aksesRepository, times(1)).save(any(Akses.class));
//        }
//
//        @Test
//        void testCreateAkses_DivisiNotFound() {
//            AksesValidasiDTO aksesDTO = new AksesValidasiDTO();
//            aksesDTO.setNamaAkses("Admin");
//            aksesDTO.setDivisiId(1L);
//
//            when(divisiRepository.findById(1L)).thenReturn(Optional.empty());
//
//            ResponseEntity<Object> response = aksesService.createAkses(aksesDTO, request);
//
//            assertEquals(GlobalFunction.validasiGagal("Divisi tidak ditemukan", "DIVISI_NOT_FOUND", request).getStatusCode(), response.getStatusCode());
//        }
//
//        @Test
//        void testDeleteAkses_Success() {
//            Akses akses = new Akses();
//            akses.setIdAkses(1L);
//
//            when(aksesRepository.findById(1L)).thenReturn(Optional.of(akses));
//
//            ResponseEntity<Object> response = aksesService.deleteAkses(1L, request);
//
//            assertEquals(GlobalFunction.dataBerhasilDihapus(request).getStatusCode(), response.getStatusCode());
//            verify(aksesRepository, times(1)).deleteById(1L);
//        }
//
//        @Test
//        void testDeleteAkses_NotFound() {
//            when(aksesRepository.findById(1L)).thenReturn(Optional.empty());
//
//            ResponseEntity<Object> response = aksesService.deleteAkses(1L, request);
//
//            assertEquals(GlobalFunction.dataTidakDitemukan(request).getStatusCode(), response.getStatusCode());
//        }
//
//        @Test
//        void createDivisi_ShouldReturnDuplicateError_WhenNamaDivisiExists() {
//            DivisiValidasiDTO divisiDTO = new DivisiValidasiDTO();
//            divisiDTO.setNamaDivisi("IT");
//            when(divisiRepository.existsByNamaDivisi(divisiDTO.getNamaDivisi())).thenReturn(true);
//
//            ResponseEntity<Object> response = divisiService.createDivisi(divisiDTO, request);
//
//            assertEquals("Nama divisi sudah ada".toLowerCase(), GlobalFunction.extractMessage(response).toLowerCase());
//        }
//
//        @Test
//        void createDivisi_ShouldSaveDivisi_WhenValid() {
//            DivisiValidasiDTO divisiDTO = new DivisiValidasiDTO();
//            divisiDTO.setNamaDivisi("HR");
//            divisiDTO.setKodeDivisi("HR001");
//
//            when(divisiRepository.existsByNamaDivisi(divisiDTO.getNamaDivisi())).thenReturn(false);
//            when(divisiRepository.existsByKodeDivisi(divisiDTO.getKodeDivisi())).thenReturn(false);
//
//            ResponseEntity<Object> response = divisiService.createDivisi(divisiDTO, request);
//
//            assertEquals("Data berhasil disimpan".toLowerCase(), GlobalFunction.extractMessage(response).toLowerCase());
//            verify(divisiRepository, times(1)).save(any(Divisi.class));
//        }
//
//
//        @Test
//        void updateDivisi_ShouldReturnNotFound_WhenDivisiNotExists() {
//            when(divisiRepository.findById(1L)).thenReturn(Optional.empty());
//
//            ResponseEntity<Object> response = divisiService.updateDivisi(1L, new DivisiValidasiDTO(), request);
//
//            // Mengubah string yang diharapkan dan aktual menjadi lowercase sebelum perbandingan
//            assertEquals("Data tidak ditemukan".toLowerCase(), GlobalFunction.extractMessage(response).toLowerCase());
//        }
//
//        @Test
//        void deleteDivisi_ShouldReturnNotFound_WhenDivisiNotExists() {
//            when(divisiRepository.existsById(1L)).thenReturn(false);
//
//            ResponseEntity<Object> response = divisiService.deleteDivisi(1L, request);
//
//            // Mengubah string yang diharapkan dan aktual menjadi lowercase sebelum perbandingan
//            assertEquals("Data tidak ditemukan".toLowerCase(), GlobalFunction.extractMessage(response).toLowerCase());
//        }
//
//        @Test
//        void createKaryawan_ShouldReturnDuplicateError_WhenEmailExists() {
//            KaryawanValidasiDTO karyawanDTO = new KaryawanValidasiDTO();
//            karyawanDTO.setEmail("test@example.com");
//            when(karyawanRepository.existsByEmail(karyawanDTO.getEmail())).thenReturn(true);
//
//            ResponseEntity<Object> response = karyawanService.createKaryawan(karyawanDTO, request);
//
//            assertEquals("Email sudah terdaftar".toLowerCase(), GlobalFunction.extractMessage(response).toLowerCase());
//        }
//
//        @Test
//        void createKaryawan_ShouldSaveKaryawan_WhenValid() {
//            KaryawanValidasiDTO karyawanDTO = new KaryawanValidasiDTO();
//            karyawanDTO.setEmail("new@example.com");
//            karyawanDTO.setNamaLengkap("John Doe");
//
//            when(karyawanRepository.existsByEmail(karyawanDTO.getEmail())).thenReturn(false);
//
//            ResponseEntity<Object> response = karyawanService.createKaryawan(karyawanDTO, request);
//
//            assertEquals("Data berhasil disimpan".toLowerCase(), GlobalFunction.extractMessage(response).toLowerCase());
//            verify(karyawanRepository, times(1)).save(any(Karyawan.class));
//        }
//
//        @Test
//        void updateKaryawan_ShouldReturnNotFound_WhenKaryawanNotExists() {
//            when(karyawanRepository.findById(1L)).thenReturn(Optional.empty());
//
//            ResponseEntity<Object> response = karyawanService.updateKaryawan(1L, new KaryawanValidasiDTO(), request);
//
//            assertEquals("Data tidak ditemukan".toLowerCase(), GlobalFunction.extractMessage(response).toLowerCase());
//        }
//
//        @Test
//        void deleteKaryawan_ShouldReturnNotFound_WhenKaryawanNotExists() {
//            when(karyawanRepository.existsById(1L)).thenReturn(false);
//
//            ResponseEntity<Object> response = karyawanService.deleteKaryawan(1L, request);
//
//            assertEquals("Data tidak ditemukan".toLowerCase(), GlobalFunction.extractMessage(response).toLowerCase());
//        }
//
//        @Test
//        void deleteKaryawan_ShouldDeleteKaryawan_WhenExists() {
//            when(karyawanRepository.existsById(1L)).thenReturn(true);
//
//            ResponseEntity<Object> response = karyawanService.deleteKaryawan(1L, request);
//
//            assertEquals("Data berhasil dihapus".toLowerCase(), GlobalFunction.extractMessage(response).toLowerCase());
//            verify(karyawanRepository, times(1)).deleteById(1L);
//        }
//
//        @Test
//        void getKaryawanById_ShouldReturnNotFound_WhenKaryawanNotExists() {
//            when(karyawanRepository.findById(1L)).thenReturn(Optional.empty());
//
//            ResponseEntity<Object> response = karyawanService.getKaryawanById(1L, request);
//
//            assertEquals("Data tidak ditemukan".toLowerCase(), GlobalFunction.extractMessage(response).toLowerCase());
//        }
//
//        @Test
//        void getAllKaryawan_ShouldReturnData() {
//            ResponseEntity<Object> response = karyawanService.getAllKaryawan(request);
//            assertEquals("Data karyawan ditemukan".toLowerCase(), GlobalFunction.extractMessage(response).toLowerCase());
//        }
//
//
//        @Test
//        void testCreateEmployee() {
//            EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
//            requestDTO.setFirstName("John");
//            requestDTO.setLastName("Doe");
//            requestDTO.setEmail("john.doe@example.com");
//
//            Employee employee = new Employee();
//            employee.setIdEmployee(1L);
//            employee.setFirstName(requestDTO.getFirstName());
//            employee.setLastName(requestDTO.getLastName());
//            employee.setEmail(requestDTO.getEmail());
//
//            when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
//
//            EmployeeResponseDTO responseDTO = employeeService.createEmployee(requestDTO);
//
//            assertEquals(1L, responseDTO.getIdEmployee());
//            assertEquals("John", responseDTO.getFirstName());
//            assertEquals("Doe", responseDTO.getLastName());
//            assertEquals("john.doe@example.com", responseDTO.getEmail());
//
//            verify(employeeRepository, times(1)).save(any(Employee.class));
//        }
//
//        @Test
//        void testGetEmployeeById() {
//            Employee employee = new Employee();
//            employee.setIdEmployee(1L);
//            employee.setFirstName("John");
//            employee.setLastName("Doe");
//            employee.setEmail("john.doe@example.com");
//
//            when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
//
//            EmployeeResponseDTO responseDTO = employeeService.getEmployeeById(1L);
//
//            assertEquals(1L, responseDTO.getIdEmployee());
//            assertEquals("John", responseDTO.getFirstName());
//            assertEquals("Doe", responseDTO.getLastName());
//            assertEquals("john.doe@example.com", responseDTO.getEmail());
//
//            verify(employeeRepository, times(1)).findById(1L);
//        }
//
//        @Test
//        void testGenerateEmployeeReport() {
//            Employee employee = new Employee();
//            employee.setIdEmployee(1L);
//            employee.setFirstName("John");
//            employee.setLastName("Doe");
//            employee.setEmail("john.doe@example.com");
//
//            when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
//
//            EmployeeReportDTO reportDTO = employeeService.generateEmployeeReport(1L);
//
//            assertEquals(1L, reportDTO.getIdEmployee());
//            assertEquals("John Doe", reportDTO.getFullName());
//            assertEquals("john.doe@example.com", reportDTO.getEmail());
//
//            verify(employeeRepository, times(1)).findById(1L);
//        }
//
//        @Test
//        void createMenu_ShouldReturnMenuResponseDTO() {
//            // Arrange
//            MenuValidasiDTO dto = new MenuValidasiDTO();
//            dto.setNamaMenu("Dashboard");
//            dto.setPathMenu("/dashboard");
//            dto.setEndPoint("/api/dashboard");
//
//            Menu savedMenu = new Menu();
//            savedMenu.setIdMenu(1L);
//            savedMenu.setNamaMenu("Dashboard");
//            savedMenu.setPathMenu("/dashboard");
//            savedMenu.setEndPoint("/api/dashboard");
//
//            when(menuRepository.save(any(Menu.class))).thenReturn(savedMenu);
//
//            // Act
//            MenuResponseDTO response = menuService.createMenu(dto);
//
//            // Assert
//            assertNotNull(response);
//            assertEquals("Dashboard", response.getNamaMenu());
//            verify(menuRepository, times(1)).save(any(Menu.class));
//        }
//
//        @Test
//        void getMenuById_ShouldReturnMenuResponseDTO_WhenMenuExists() {
//            // Arrange
//            Menu menu = new Menu();
//            menu.setIdMenu(1L);
//            menu.setNamaMenu("Settings");
//
//            when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
//
//            // Act
//            Optional<MenuResponseDTO> response = menuService.getMenuById(1L);
//
//            // Assert
//            assertTrue(response.isPresent());
//            assertEquals("Settings", response.get().getNamaMenu());
//        }
//
//        @Test
//        void createMenuHeader_ShouldReturnMenuHeaderResponseDTO() {
//            // Arrange
//            MenuHeaderValidasiDTO dto = new MenuHeaderValidasiDTO();
//            dto.setNamaMenuHeader("Admin");
//            dto.setDeskripsiMenuHeader("Admin Panel");
//
//            MenuHeader savedHeader = new MenuHeader();
//            savedHeader.setIdMenuHeader(1L);
//            savedHeader.setNamaMenuHeader("Admin");
//
//            when(menuHeaderRepository.save(any(MenuHeader.class))).thenReturn(savedHeader);
//
//            // Act
//            MenuHeaderResponseDTO response = menuHeaderService.createMenuHeader(dto);
//
//            // Assert
//            assertNotNull(response);
//            assertEquals("Admin", response.getNamaMenuHeader());
//            verify(menuHeaderRepository, times(1)).save(any(MenuHeader.class));
//        }
//
//        @Test
//        void getMenuHeaderById_ShouldReturnMenuHeaderResponseDTO_WhenMenuHeaderExists() {
//            // Arrange
//            MenuHeader header = new MenuHeader();
//            header.setIdMenuHeader(1L);
//            header.setNamaMenuHeader("Admin");
//
//            when(menuHeaderRepository.findById(1L)).thenReturn(Optional.of(header));
//
//            // Act
//            Optional<MenuHeaderResponseDTO> response = menuHeaderService.getMenuHeaderById(1L);
//
//            // Assert
//            assertTrue(response.isPresent());
//            assertEquals("Admin", response.get().getNamaMenuHeader());
//        }
//
//        @Test
//        void testSaveAbsen() {
//            AbsenValidationDTO validationDTO = new AbsenValidationDTO();
//            validationDTO.setUserId(1L);
//            validationDTO.setAbsenIn(new Date());
//            validationDTO.setAbsenOut(new Date());
//
//            Absen absen = new Absen();
//            absen.setIdAbsen(1L);
//
//            when(absenRepository.save(any(Absen.class))).thenReturn(absen);
//
//            AbsenResponseDTO responseDTO = absenService.saveAbsen(validationDTO);
//            assertEquals(1L, responseDTO.getIdAbsen());
//            verify(absenRepository, times(1)).save(any(Absen.class));
//        }
//
//        @Test
//        void testGetAllAbsens() {
//            Absen absen1 = new Absen();
//            absen1.setIdAbsen(1L);
//
//            Absen absen2 = new Absen();
//            absen2.setIdAbsen(2L);
//
//            when(absenRepository.findAll()).thenReturn(Arrays.asList(absen1, absen2));
//
//            List<AbsenResponseDTO> allAbsens = absenService.getAllAbsens();
//            assertEquals(2, allAbsens.size());
//            verify(absenRepository, times(1)).findAll();
//        }
//
//        @Test
//        void testGetReportByUserId() {
//            Long userId = 1L;
//
//            Absen absen1 = new Absen();
//            absen1.setIdAbsen(1L);
//            absen1.setAbsenIn(new Date());
//            absen1.setAbsenOut(new Date());
//
//            Userz user = new Userz();
//            user.setIdUser(userId);
//            absen1.setUserz(user);
//
//            when(absenRepository.findByUserzId(userId)).thenReturn(Arrays.asList(absen1));
//
//            List<AbsenReportDTO> report = absenService.getReportByUserId(userId);
//            assertEquals(1, report.size());
//            verify(absenRepository, times(1)).findByUserzId(userId);
//        }
//    }