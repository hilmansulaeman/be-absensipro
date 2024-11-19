//package com.juaracoding.controller;
//
//import com.juaracoding.dto.response.*;
//import com.juaracoding.dto.validasi.*;
//import com.juaracoding.model.Absen;
//import com.juaracoding.model.Userz;
//import com.juaracoding.repo.AbsenRepository;
//import com.juaracoding.security.JwtUtility;
//import com.juaracoding.service.*;
//import com.juaracoding.utils.ConstantMessage;
//import com.juaracoding.utils.GlobalFunction;
//import com.juaracoding.utils.MappingAttribute;
//import jakarta.servlet.http.HttpServletRequest;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.servlet.ModelAndView;
//import org.junit.jupiter.api.Assertions;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.hamcrest.Matchers.instanceOf;
//
//
//
//
//import java.util.*;
//
//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class UnitTestingController {
//
//    @Mock private AbsenService absenService;
//    @Mock private AksesService aksesService;
//    @Mock private UserService userService;
//    @Mock private DivisiService divisiService;
//    @Mock private EmployeeService employeeService;
//    @Mock private KaryawanService karyawanService;
//    @Mock private MenuService menuService;
//    @Mock private MenuHeaderService menuHeaderService;
//    @Mock private JwtUtility jwtUtility;
//    @Mock private PasswordEncoder passwordEncoder;
//    @Mock private HttpServletRequest request;
//    @Mock private MappingAttribute mappingAttribute;
//    @Mock private ModelMapper modelMapper;
//    @Mock private AbsenRepository absenRepository;
//    @Mock private Model model;
//    @Mock private WebRequest requests;
//
//
//    @InjectMocks private AbsenController absenController;
//    @InjectMocks private AksesController aksesController;
//    @InjectMocks private AuthController authController;
//    @InjectMocks private DivisiController divisiController;
//    @InjectMocks private EmployeeController employeeController;
//    @InjectMocks private KaryawanController karyawanController;
//    @InjectMocks private MenuController menuController;
//    @InjectMocks private MenuHeaderController menuHeaderController;
//    @InjectMocks private UserController userController;
//    @InjectMocks private AdminController adminController;
//    @InjectMocks private AbsenNewController absenNewController;
//    @InjectMocks private CheckPageController checkPageController;
//    @InjectMocks private MainController mainController;
//    @InjectMocks private RedirectMenuController redirectMenuController;
//
//    private MockMvc mockMvc;
//    private Userz user;
//    private UserValidasiDTO userDTO;
//    private Absen absen;
//    private AbsenValidationDTO absenValidationDTO;
//    private AbsenResponseDTO absenResponseDTO;
//
//
//    @BeforeEach
//    void setUp() {
//        lenient().when(request.getRequestURI()).thenReturn("/test");
//        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
//        mockMvc = MockMvcBuilders.standaloneSetup(redirectMenuController).build();
//        mockMvc = MockMvcBuilders.standaloneSetup(mainController, redirectMenuController).build();
//
//
//        // Set up a Userz instance and assign it to Absen
//        user = new Userz();
//        user.setIdUser(1L);
//        user.setEmail("test@example.com");
//        user.setUsername("username");
//
//        // Assign the user to the absen entity
//        absen = new Absen();
//        absen.setIdAbsen(1L);
//        absen.setIsDelete((byte) 0);
//        absen.setAbsenIn(new Date());
//        absen.setAbsenOut(new Date());
//        absen.setUserz(user); // Ensure Userz is not null
//
//        // Set up validation and response DTOs
//        absenValidationDTO = new AbsenValidationDTO();
//        absenValidationDTO.setAbsenIn(new Date());
//        absenValidationDTO.setAbsenOut(new Date());
//        absenValidationDTO.setUserId(1L);
//
//        absenResponseDTO = new AbsenResponseDTO();
//        absenResponseDTO.setIdAbsen(1L);
//
//        userDTO = new UserValidasiDTO();
//        userDTO.setEmail("test@example.com");
//        userDTO.setUsername("username");
//    }
//
//    // Test untuk menyimpan data absen
//    @Test
//    void testSaveAbsen() {
//        AbsenValidationDTO absenValidationDTO = new AbsenValidationDTO();
//        absenValidationDTO.setAbsenIn(new Date());
//        absenValidationDTO.setAbsenOut(new Date());
//        AbsenResponseDTO expectedResponse = new AbsenResponseDTO();
//        expectedResponse.setIdAbsen(1L);
//
//        when(absenService.saveAbsen(any(AbsenValidationDTO.class))).thenReturn(expectedResponse);
//
//        ResponseEntity<AbsenResponseDTO> response = absenController.saveAbsen(absenValidationDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(expectedResponse, response.getBody());
//        verify(absenService, times(1)).saveAbsen(absenValidationDTO);
//    }
//
//    // Test untuk mengambil semua data absen
//    @Test
//    void testGetAllAbsens() {
//        List<AbsenResponseDTO> absens = Collections.singletonList(new AbsenResponseDTO());
//        when(absenService.getAllAbsens()).thenReturn(absens);
//
//        ResponseEntity<List<AbsenResponseDTO>> response = absenController.getAllAbsens();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(absens, response.getBody());
//        verify(absenService).getAllAbsens();
//    }
//
//    // Test untuk membuat akses
//    @Test
//    void testCreateAkses() {
//        AksesValidasiDTO aksesDTO = new AksesValidasiDTO();
//        when(aksesService.createAkses(any(AksesValidasiDTO.class), any(HttpServletRequest.class)))
//                .thenReturn(ResponseEntity.ok().build());
//
//        ResponseEntity<Object> response = aksesController.createAkses(aksesDTO, request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(aksesService).createAkses(aksesDTO, request);
//    }
//
//    // Test untuk registrasi user
//    @Test
//    void testRegisterUser() {
//        UserValidasiDTO userDTO = new UserValidasiDTO();
//        userDTO.setEmail("test@example.com");
//        userDTO.setUsername("username");
//        userDTO.setPassword("password");
//
//        when(userService.existsByEmail("test@example.com")).thenReturn(false);
//        when(userService.existsByUsername("username")).thenReturn(false);
//        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
//        when(userService.save(any(Userz.class), any(HttpServletRequest.class)))
//                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
//
//        ResponseEntity<Object> response = authController.registerUser(userDTO, request);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        verify(userService).save(any(Userz.class), eq(request));
//    }
//
//    // Test untuk login user
//    @Test
//    void testLoginUser() {
//        UserValidasiDTO userDTO = new UserValidasiDTO();
//        userDTO.setEmail("test@example.com");
//        userDTO.setPassword("password");
//
//        Userz user = new Userz();
//        user.setPassword("encodedPassword");
//
//        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
//        when(jwtUtility.generateToken(user)).thenReturn("jwtToken");
//
//        ResponseEntity<Object> response = authController.loginUser(userDTO, request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(GlobalFunction.customDataDitemukan(ConstantMessage.SUCCESS_LOGIN, "jwtToken", request).getBody(),
//                response.getBody());
//    }
//
//    // Test untuk mengambil semua divisi
//    @Test
//    void testGetAllDivisi() {
//        List<DivisiResponseDTO> divisiList = Collections.singletonList(new DivisiResponseDTO());
//        Pageable pageable = PageRequest.of(0, 10);
//        when(divisiService.getAllDivisi(pageable, request)).thenReturn(ResponseEntity.ok(divisiList));
//
//        ResponseEntity<Object> response = divisiController.getAllDivisi(pageable, request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(divisiService).getAllDivisi(pageable, request);
//    }
//
//    // Test untuk membuat data employee
//    @Test
//    void testCreateEmployee() {
//        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
//        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO();
//
//        when(employeeService.createEmployee(employeeRequestDTO)).thenReturn(employeeResponseDTO);
//
//        ResponseEntity<EmployeeResponseDTO> response = employeeController.createEmployee(employeeRequestDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(employeeResponseDTO, response.getBody());
//        verify(employeeService).createEmployee(employeeRequestDTO);
//    }
//
//    // Test untuk membuat menu
//    @Test
//    void testCreateMenu() {
//        MenuValidasiDTO menuDTO = new MenuValidasiDTO();
//        MenuResponseDTO menuResponseDTO = new MenuResponseDTO();
//        when(menuService.createMenu(any(MenuValidasiDTO.class))).thenReturn(menuResponseDTO);
//
//        ResponseEntity<MenuResponseDTO> response = menuController.createMenu(menuDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(menuResponseDTO, response.getBody());
//        verify(menuService).createMenu(menuDTO);
//    }
//
//    // Test untuk mendapatkan user berdasarkan ID
//    @Test
//    void testGetUserById() {
//        Userz user = new Userz();
//        when(userService.findById(anyLong(), any(HttpServletRequest.class)))
//                .thenReturn(ResponseEntity.ok(user));
//
//        ResponseEntity<Object> response = userController.getUserById(1L, request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user, response.getBody());
//        verify(userService).findById(1L, request);
//    }
//
//    @Test
//    void testCreateUser() {
//        when(modelMapper.map(any(UserValidasiDTO.class), eq(Userz.class))).thenReturn(user);
//        when(userService.save(any(Userz.class), any(HttpServletRequest.class)))
//                .thenReturn(new ResponseEntity<>(user, HttpStatus.CREATED));
//
//        // Mock BindingResult untuk menghindari NullPointerException
//        BindingResult mockBindingResult = mock(BindingResult.class);
//        when(mockBindingResult.hasErrors()).thenReturn(false);  // Pastikan untuk mengembalikan false agar tidak gagal karena validasi
//
//        org.springframework.ui.Model mockModel = mock(org.springframework.ui.Model.class);
//        ResponseEntity<Object> response = adminController.createUser(userDTO, request, mockBindingResult, mockModel);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        verify(userService).save(any(Userz.class), eq(request));
//    }
//
//
//    @Test
//    void testGetAllUsers() {
//        Page<Userz> users = new PageImpl<>(Collections.singletonList(user));
//        when(userService.findAll(any(Pageable.class), any(HttpServletRequest.class)))
//                .thenReturn(new ResponseEntity<>(users, HttpStatus.OK));
//
//        ResponseEntity<Object> response = adminController.getAllUsers(0, 10, request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(userService).findAll(any(Pageable.class), eq(request));
//    }
//
//    @Test
//    void testUpdateUser() {
//        when(modelMapper.map(any(UserValidasiDTO.class), eq(Userz.class))).thenReturn(user);
//        when(userService.update(anyLong(), any(Userz.class), any(HttpServletRequest.class)))
//                .thenReturn(new ResponseEntity<>(user, HttpStatus.OK));
//
//        // Mock BindingResult untuk menghindari NullPointerException
//        BindingResult mockBindingResult = mock(BindingResult.class);
//        when(mockBindingResult.hasErrors()).thenReturn(false);  // Pastikan untuk mengembalikan false agar tidak gagal karena validasi
//
//        org.springframework.ui.Model mockModel = mock(org.springframework.ui.Model.class);
//        ResponseEntity<Object> response = adminController.updateUser(1L, userDTO, request, mockBindingResult, mockModel);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(userService).update(eq(1L), any(Userz.class), eq(request));
//    }
//
//
//    @Test
//    void testDeleteUser() {
//        when(userService.delete(anyLong(), any(HttpServletRequest.class)))
//                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
//
//        ResponseEntity<Object> response = adminController.deleteUser(1L, request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(userService).delete(1L, request);
//    }
//
//    @Test
//    void testSearchUsers() {
//        Page<Userz> users = new PageImpl<>(Collections.singletonList(user));
//        when(userService.findByParam(any(Pageable.class), eq("email"), eq("test@example.com"), any(HttpServletRequest.class)))
//                .thenReturn(new ResponseEntity<>(users, HttpStatus.OK));
//
//        ResponseEntity<Object> response = adminController.searchUsers("email", "test@example.com", 0, 10, request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(userService).findByParam(any(Pageable.class), eq("email"), eq("test@example.com"), eq(request));
//    }
//
//    @Test
//    void testGetNewAbsenById() {
//        when(absenRepository.findById(1L)).thenReturn(Optional.of(absen));
//
//        ResponseEntity<?> response = absenNewController.getAbsenById(1L);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(1L, ((AbsenResponseDTO) response.getBody()).getIdAbsen());
//        verify(absenRepository, times(1)).findById(1L);
//    }
//    @Test
//    void testCreateNewAbsen() {
//        // Ensure Absen instance has Userz assigned
//        absen.setUserz(user); // attach user to absen if detached
//        when(absenRepository.save(any(Absen.class))).thenReturn(absen);
//
//        BindingResult mockBindingResult = mock(BindingResult.class);
//        when(mockBindingResult.hasErrors()).thenReturn(false);
//
//        Model mockModel = mock(Model.class);
//
//        ResponseEntity<?> response = absenNewController.createAbsen(absenValidationDTO, mockBindingResult, mockModel);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        verify(absenRepository, times(1)).save(any(Absen.class));
//    }
//    @Test
//    void testUpdateNewAbsen() {
//        when(absenRepository.findById(1L)).thenReturn(Optional.of(absen));
//        when(absenRepository.save(any(Absen.class))).thenReturn(absen);
//
//        BindingResult mockBindingResult = mock(BindingResult.class);
//        when(mockBindingResult.hasErrors()).thenReturn(false);
//
//        ResponseEntity<?> response = absenNewController.updateAbsen(1L, absenValidationDTO, mockBindingResult);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(absenRepository, times(1)).save(any(Absen.class));
//    }
//
//    @Test
//    void testDeleteNewAbsen() {
//        when(absenRepository.findById(1L)).thenReturn(Optional.of(absen));
//
//        ResponseEntity<?> response = absenNewController.deleteAbsen(1L);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(absenRepository, times(1)).save(any(Absen.class));
//    }
//    @Test
//    void testAbsenRepositoryFindByIsDelete() {
//        absen.setUserz(user); // Ensure Userz is attached
//        List<Absen> absens = Collections.singletonList(absen);
//        when(absenRepository.findByIsDelete((byte) 0)).thenReturn(absens);
//
//        List<Absen> result = absenRepository.findByIsDelete((byte) 0);
//        System.out.println("Repository returned list size: " + result.size()); // Should print 1
//        assertEquals(1, result.size(), "Expected repository to return exactly one Absen.");
//    }
//
//    @Test
//    void testCheckPageAccessWithAdminAccess() {
//        // Arrange
//        String pageName = "adminPage";
//        String userRole = "ADMIN";
//
//        // Act
//        Map<String, Object> response = checkPageController.checkPageAccess(pageName, userRole, model, requests);
//
//        // Assert
//        assertEquals("adminPage", response.get("pageName"));
//        assertEquals("ADMIN", response.get("userRole"));
//        assertEquals(true, response.get("hasAccess"));
//        assertEquals("Access granted", response.get("status"));
//
//        // Verify that MappingAttribute setAttribute was called
//        verify(mappingAttribute, times(1)).setAttribute(eq(model), eq(response), eq(requests));
//    }
//
//    @Test
//    void testCheckPageAccessWithUserAccessDenied() {
//        // Arrange
//        String pageName = "adminPage";
//        String userRole = "USER";
//
//        // Act
//        Map<String, Object> response = checkPageController.checkPageAccess(pageName, userRole, model, requests);
//
//        // Assert
//        assertEquals("adminPage", response.get("pageName"));
//        assertEquals("USER", response.get("userRole"));
//        assertEquals(false, response.get("hasAccess"));
//        assertEquals("Access denied", response.get("status"));
//
//        // Verify that MappingAttribute setAttribute was called
//        verify(mappingAttribute, times(1)).setAttribute(eq(model), eq(response), eq(requests));
//    }
//
//    @Test
//    void testGetPageAttributes() {
//        // Arrange
//        String pageName = "userPage";
//
//        // Act
//        Map<String, Object> pageAttributes = checkPageController.getPageAttributes(pageName, model, requests);
//
//        // Assert
//        assertEquals("userPage", pageAttributes.get("pageName"));
//        assertEquals("Title for userPage", pageAttributes.get("title"));
//        assertEquals("Description for userPage", pageAttributes.get("description"));
//
//        // Verify that MappingAttribute setAttribute was called
//        verify(mappingAttribute, times(1)).setAttribute(eq(model), eq(pageAttributes), eq(requests));
//    }
//
//    @Test
//    void testPageTwo() throws Exception {
//        // Lakukan permintaan GET ke endpoint "/"
//        MvcResult mvcResult = mockMvc.perform(get("/"))
//                .andExpect(status().isOk())                           // Pastikan status respons adalah 200 OK
//                .andExpect(view().name("auth/login"))                 // Pastikan view yang dikembalikan adalah "auth/login"
//                .andExpect(model().attributeExists("usr"))            // Pastikan model berisi atribut "usr"
//                .andExpect(model().attribute("usr", instanceOf(Userz.class)))  // Pastikan atribut "usr" adalah instance dari Userz
//                .andReturn();
//
//        // Ambil objek Userz dari model untuk memverifikasi detail CAPTCHA
//        Userz usr = (Userz) mvcResult.getModelAndView().getModel().get("usr");
//
//        // Verifikasi CAPTCHA - Gambar dan jawaban CAPTCHA tidak boleh null, dan nilai CAPTCHA awal kosong
//        Assertions.assertNotNull(usr.getImage(), "Captcha image should not be null");
//        Assertions.assertNotNull(usr.getHidden(), "Captcha answer (hidden) should not be null");
//        Assertions.assertEquals("", usr.getCaptcha(), "Captcha field should be initially empty");
//    }
//
//
//    // Test untuk menu Sales One
//    @Test
//    void testGetSalesOne() throws Exception {
//        mockMvc.perform(get("/api/menu/salesone"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("sales/sales_one"))
//                .andExpect(model().attributeExists("usr"));
//    }
//
//    // Test untuk menu Sales Two
//    @Test
//    void testGetSalesTwo() throws Exception {
//        mockMvc.perform(get("/api/menu/salestwo"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("sales/sales_two"));
//    }
//
//    // Test untuk menu Sales Three
//    @Test
//    void testGetSalesThree() throws Exception {
//        mockMvc.perform(get("/api/menu/salesthree"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("sales/sales_three"));
//    }
//
//    // Test untuk menu HR One
//    @Test
//    void testGetHROne() throws Exception {
//        mockMvc.perform(get("/api/menu/hrone"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("hrd/hr_one"));
//    }
//
//    // Test untuk menu Finance One
//    @Test
//    void testGetFinanceOne() throws Exception {
//        mockMvc.perform(get("/api/menu/financeone"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("finance/finance_one"));
//    }
//
//    // Test untuk menu Absensi
//    @Test
//    void testGetAbsensi() throws Exception {
//        mockMvc.perform(get("/api/menu/absen"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("global/absen"));
//    }
//
//    // Test untuk menu Research
//    @Test
//    void testGetResearch() throws Exception {
//        mockMvc.perform(get("/api/menu/research"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("global/research"));
//    }
//
//
//}
