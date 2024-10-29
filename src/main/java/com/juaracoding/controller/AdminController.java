package com.juaracoding.controller;

import com.juaracoding.dto.response.UserResponseDTO;
import com.juaracoding.dto.validasi.UserValidasiDTO;
import com.juaracoding.model.Userz;
import com.juaracoding.service.UserService;
import com.juaracoding.utils.ConstantMessage;
import com.juaracoding.utils.MappingAttribute;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MappingAttribute mappingAttribute;

    @Autowired
    public AdminController(UserService userService, ModelMapper modelMapper, MappingAttribute mappingAttribute) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.mappingAttribute = mappingAttribute;
    }

    // Endpoint untuk menambahkan user baru
    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserValidasiDTO userDTO, HttpServletRequest request, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(mappingAttribute.setErrorMessage(result, "Invalid input data"));
        }

        // Konversi DTO ke entitas Userz
        Userz user = modelMapper.map(userDTO, Userz.class);
        ResponseEntity<Object> response = userService.save(user, request);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", ConstantMessage.SUCCESS_SAVE_USER);
        responseMap.put("status", HttpStatus.CREATED);
        responseMap.put("data", response.getBody());

        mappingAttribute.setAttribute(model, responseMap);
        return response;
    }

    // Endpoint untuk mengambil semua data user dengan paginasi
    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.findAll(pageable, request);
    }

    // Endpoint untuk mencari user berdasarkan ID
    @GetMapping("/users/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id, HttpServletRequest request) {
        return userService.findById(id, request);
    }

    // Endpoint untuk mengupdate data user
    @PutMapping("/users/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id,
                                             @Valid @RequestBody UserValidasiDTO userDTO,
                                             HttpServletRequest request,
                                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(mappingAttribute.setErrorMessage(result, "Invalid input data"));
        }

        Userz user = modelMapper.map(userDTO, Userz.class);
        return userService.update(id, user, request);
    }

    // Endpoint untuk menghapus user berdasarkan ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        return userService.delete(id, request);
    }

    // Endpoint untuk mencari user berdasarkan parameter tertentu (email atau username)
    @GetMapping("/users/search")
    public ResponseEntity<Object> searchUsers(@RequestParam String columnName,
                                              @RequestParam String value,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.findByParam(pageable, columnName, value, request);
    }
}
