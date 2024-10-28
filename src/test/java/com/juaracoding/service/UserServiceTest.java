package com.juaracoding.service;

import com.juaracoding.model.Userz;
import com.juaracoding.repo.UserRepository;
import com.juaracoding.utils.GlobalFunction;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
        Userz user = new Userz();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        ResponseEntity<Object> response = userService.save(user, request);

        // Casting body menjadi Map atau objek yang sesuai dengan respons Anda
        Map<String, Object> expectedBody = (Map<String, Object>) GlobalFunction.dataBerhasilDisimpan(request).getBody();
        Map<String, Object> actualBody = (Map<String, Object>) response.getBody();

        // Bandingkan pesan, status, dan timestamp jika ada di respons
        assertEquals(expectedBody.get("message"), actualBody.get("message"));
        assertEquals(expectedBody.get("status"), actualBody.get("status"));
        assertEquals(expectedBody.get("success"), actualBody.get("success"));
    }


    @Test
    void testFindUserById() {
        Userz user = new Userz();
        user.setIdUser(1L);
        user.setEmail("test@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<Object> response = userService.findById(1L, request);
        assertEquals(GlobalFunction.dataByIdDitemukan(user, request), response);
    }

    @Test
    void testFindAllUsers() {
        Page<Userz> users = new PageImpl<>(Collections.singletonList(new Userz()));
        when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(users);

        ResponseEntity<Object> response = userService.findAll(PageRequest.of(0, 10), request);
        assertEquals(GlobalFunction.customDataDitemukan("Users fetched successfully", users.getContent(), request), response);
    }
}
