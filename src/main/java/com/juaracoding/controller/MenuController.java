package com.juaracoding.controller;

import com.juaracoding.dto.response.MenuResponseDTO;
import com.juaracoding.dto.validasi.MenuValidasiDTO;
import com.juaracoding.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public ResponseEntity<MenuResponseDTO> createMenu(@RequestBody MenuValidasiDTO menuDTO) {
        MenuResponseDTO createdMenu = menuService.createMenu(menuDTO);
        return ResponseEntity.ok(createdMenu);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponseDTO>> getAllMenus() {
        List<MenuResponseDTO> menus = menuService.getAllMenus();
        return ResponseEntity.ok(menus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> getMenuById(@PathVariable Long id) {
        Optional<MenuResponseDTO> menu = menuService.getMenuById(id);
        return menu.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> updateMenu(@PathVariable Long id, @RequestBody MenuValidasiDTO menuDTO) {
        MenuResponseDTO updatedMenu = menuService.updateMenu(id, menuDTO);
        return ResponseEntity.ok(updatedMenu);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        if (menuService.deleteMenu(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
