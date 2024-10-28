package com.juaracoding.controller;

import com.juaracoding.model.Userz;
import com.juaracoding.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody Userz user, HttpServletRequest request) {
        return userService.save(user, request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody Userz user, HttpServletRequest request) {
        return userService.update(id, user, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        return userService.delete(id, request);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers(Pageable pageable, HttpServletRequest request) {
        return userService.findAll(pageable, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id, HttpServletRequest request) {
        return userService.findById(id, request);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchUser(@RequestParam String column, @RequestParam String value, Pageable pageable, HttpServletRequest request) {
        return userService.findByParam(pageable, column, value, request);
    }
}
