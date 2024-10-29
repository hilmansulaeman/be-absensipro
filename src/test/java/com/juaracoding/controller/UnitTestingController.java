package com.juaracoding.controller;

import com.juaracoding.dto.response.*;
import com.juaracoding.dto.validasi.*;
import com.juaracoding.model.Userz;
import com.juaracoding.security.JwtUtility;
import com.juaracoding.service.*;
import com.juaracoding.utils.ConstantMessage;
import com.juaracoding.utils.GlobalFunction;
import com.juaracoding.utils.MappingAttribute;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitTestingController {

    @Mock private AbsenService absenService;
    @Mock private AksesService aksesService;
    @Mock private UserService userService;
    @Mock private DivisiService divisiService;
    @Mock private EmployeeService employeeService;
    @Mock private KaryawanService karyawanService;
    @Mock private MenuService menuService;
    @Mock private MenuHeaderService menuHeaderService;
    @Mock private JwtUtility jwtUtility;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private HttpServletRequest request;
    @Mock private MappingAttribute mappingAttribute;
    @Mock private ModelMapper modelMapper;


    @InjectMocks private AbsenController absenController;
    @InjectMocks private AksesController aksesController;
    @InjectMocks private AuthController authController;
    @InjectMocks private DivisiController divisiController;
    @InjectMocks private EmployeeController employeeController;
    @InjectMocks private KaryawanController karyawanController;
    @InjectMocks private MenuController menuController;
    @InjectMocks private MenuHeaderController menuHeaderController;
    @InjectMocks private UserController userController;
    @InjectMocks private AdminController adminController;

    private Userz user;
    private UserValidasiDTO userDTO;


    @BeforeEach
    void setUp() {
        lenient().when(request.getRequestURI()).thenReturn("/test");

        user = new Userz();
        user.setIdUser(1L);
        user.setEmail("test@example.com");
        user.setUsername("username");

        userDTO = new UserValidasiDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("username");
    }

    // Test untuk menyimpan data absen
    @Test
    void testSaveAbsen() {
        AbsenValidationDTO absenValidationDTO = new AbsenValidationDTO();
        AbsenResponseDTO absenResponseDTO = new AbsenResponseDTO();
        when(absenService.saveAbsen(any(AbsenValidationDTO.class))).thenReturn(absenResponseDTO);

        ResponseEntity<AbsenResponseDTO> response = absenController.saveAbsen(absenValidationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(absenResponseDTO, response.getBody());
        verify(absenService).saveAbsen(absenValidationDTO);
    }

    // Test untuk mengambil semua data absen
    @Test
    void testGetAllAbsens() {
        List<AbsenResponseDTO> absens = Collections.singletonList(new AbsenResponseDTO());
        when(absenService.getAllAbsens()).thenReturn(absens);

        ResponseEntity<List<AbsenResponseDTO>> response = absenController.getAllAbsens();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(absens, response.getBody());
        verify(absenService).getAllAbsens();
    }

    // Test untuk membuat akses
    @Test
    void testCreateAkses() {
        AksesValidasiDTO aksesDTO = new AksesValidasiDTO();
        when(aksesService.createAkses(any(AksesValidasiDTO.class), any(HttpServletRequest.class)))
                .thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Object> response = aksesController.createAkses(aksesDTO, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(aksesService).createAkses(aksesDTO, request);
    }

    // Test untuk registrasi user
    @Test
    void testRegisterUser() {
        UserValidasiDTO userDTO = new UserValidasiDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("username");
        userDTO.setPassword("password");

        when(userService.existsByEmail("test@example.com")).thenReturn(false);
        when(userService.existsByUsername("username")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userService.save(any(Userz.class), any(HttpServletRequest.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        ResponseEntity<Object> response = authController.registerUser(userDTO, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userService).save(any(Userz.class), eq(request));
    }

    // Test untuk login user
    @Test
    void testLoginUser() {
        UserValidasiDTO userDTO = new UserValidasiDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        Userz user = new Userz();
        user.setPassword("encodedPassword");

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtility.generateToken(user)).thenReturn("jwtToken");

        ResponseEntity<Object> response = authController.loginUser(userDTO, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(GlobalFunction.customDataDitemukan(ConstantMessage.SUCCESS_LOGIN, "jwtToken", request).getBody(),
                response.getBody());
    }

    // Test untuk mengambil semua divisi
    @Test
    void testGetAllDivisi() {
        List<DivisiResponseDTO> divisiList = Collections.singletonList(new DivisiResponseDTO());
        Pageable pageable = PageRequest.of(0, 10);
        when(divisiService.getAllDivisi(pageable, request)).thenReturn(ResponseEntity.ok(divisiList));

        ResponseEntity<Object> response = divisiController.getAllDivisi(pageable, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(divisiService).getAllDivisi(pageable, request);
    }

    // Test untuk membuat data employee
    @Test
    void testCreateEmployee() {
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO();
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO();

        when(employeeService.createEmployee(employeeRequestDTO)).thenReturn(employeeResponseDTO);

        ResponseEntity<EmployeeResponseDTO> response = employeeController.createEmployee(employeeRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employeeResponseDTO, response.getBody());
        verify(employeeService).createEmployee(employeeRequestDTO);
    }

    // Test untuk membuat menu
    @Test
    void testCreateMenu() {
        MenuValidasiDTO menuDTO = new MenuValidasiDTO();
        MenuResponseDTO menuResponseDTO = new MenuResponseDTO();
        when(menuService.createMenu(any(MenuValidasiDTO.class))).thenReturn(menuResponseDTO);

        ResponseEntity<MenuResponseDTO> response = menuController.createMenu(menuDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(menuResponseDTO, response.getBody());
        verify(menuService).createMenu(menuDTO);
    }

    // Test untuk mendapatkan user berdasarkan ID
    @Test
    void testGetUserById() {
        Userz user = new Userz();
        when(userService.findById(anyLong(), any(HttpServletRequest.class)))
                .thenReturn(ResponseEntity.ok(user));

        ResponseEntity<Object> response = userController.getUserById(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).findById(1L, request);
    }

    @Test
    void testCreateUser() {
        when(modelMapper.map(any(UserValidasiDTO.class), eq(Userz.class))).thenReturn(user);
        when(userService.save(any(Userz.class), any(HttpServletRequest.class)))
                .thenReturn(new ResponseEntity<>(user, HttpStatus.CREATED));

        // Mock BindingResult untuk menghindari NullPointerException
        BindingResult mockBindingResult = mock(BindingResult.class);
        when(mockBindingResult.hasErrors()).thenReturn(false);  // Pastikan untuk mengembalikan false agar tidak gagal karena validasi

        org.springframework.ui.Model mockModel = mock(org.springframework.ui.Model.class);
        ResponseEntity<Object> response = adminController.createUser(userDTO, request, mockBindingResult, mockModel);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userService).save(any(Userz.class), eq(request));
    }


    @Test
    void testGetAllUsers() {
        Page<Userz> users = new PageImpl<>(Collections.singletonList(user));
        when(userService.findAll(any(Pageable.class), any(HttpServletRequest.class)))
                .thenReturn(new ResponseEntity<>(users, HttpStatus.OK));

        ResponseEntity<Object> response = adminController.getAllUsers(0, 10, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).findAll(any(Pageable.class), eq(request));
    }

    @Test
    void testUpdateUser() {
        when(modelMapper.map(any(UserValidasiDTO.class), eq(Userz.class))).thenReturn(user);
        when(userService.update(anyLong(), any(Userz.class), any(HttpServletRequest.class)))
                .thenReturn(new ResponseEntity<>(user, HttpStatus.OK));

        // Mock BindingResult untuk menghindari NullPointerException
        BindingResult mockBindingResult = mock(BindingResult.class);
        when(mockBindingResult.hasErrors()).thenReturn(false);  // Pastikan untuk mengembalikan false agar tidak gagal karena validasi

        org.springframework.ui.Model mockModel = mock(org.springframework.ui.Model.class);
        ResponseEntity<Object> response = adminController.updateUser(1L, userDTO, request, mockBindingResult, mockModel);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).update(eq(1L), any(Userz.class), eq(request));
    }


    @Test
    void testDeleteUser() {
        when(userService.delete(anyLong(), any(HttpServletRequest.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<Object> response = adminController.deleteUser(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).delete(1L, request);
    }

    @Test
    void testSearchUsers() {
        Page<Userz> users = new PageImpl<>(Collections.singletonList(user));
        when(userService.findByParam(any(Pageable.class), eq("email"), eq("test@example.com"), any(HttpServletRequest.class)))
                .thenReturn(new ResponseEntity<>(users, HttpStatus.OK));

        ResponseEntity<Object> response = adminController.searchUsers("email", "test@example.com", 0, 10, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).findByParam(any(Pageable.class), eq("email"), eq("test@example.com"), eq(request));
    }
}
