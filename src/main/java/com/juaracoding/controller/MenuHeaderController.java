package com.juaracoding.controller;

import com.juaracoding.dto.response.MenuHeaderResponseDTO;
import com.juaracoding.dto.validasi.MenuHeaderValidasiDTO;
import com.juaracoding.service.MenuHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu-headers")
public class MenuHeaderController {

    private final MenuHeaderService menuHeaderService;

    @Autowired
    public MenuHeaderController(MenuHeaderService menuHeaderService) {
        this.menuHeaderService = menuHeaderService;
    }

    @PostMapping
    public ResponseEntity<MenuHeaderResponseDTO> createMenuHeader(@RequestBody MenuHeaderValidasiDTO headerDTO) {
        MenuHeaderResponseDTO createdHeader = menuHeaderService.createMenuHeader(headerDTO);
        return ResponseEntity.ok(createdHeader);
    }

    @GetMapping
    public ResponseEntity<List<MenuHeaderResponseDTO>> getAllMenuHeaders() {
        List<MenuHeaderResponseDTO> headers = menuHeaderService.getAllMenuHeaders();
        return ResponseEntity.ok(headers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuHeaderResponseDTO> getMenuHeaderById(@PathVariable Long id) {
        Optional<MenuHeaderResponseDTO> header = menuHeaderService.getMenuHeaderById(id);
        return header.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuHeaderResponseDTO> updateMenuHeader(@PathVariable Long id, @RequestBody MenuHeaderValidasiDTO headerDTO) {
        MenuHeaderResponseDTO updatedHeader = menuHeaderService.updateMenuHeader(id, headerDTO);
        return ResponseEntity.ok(updatedHeader);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuHeader(@PathVariable Long id) {
        if (menuHeaderService.deleteMenuHeader(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
