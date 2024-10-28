package com.juaracoding.service;

import com.juaracoding.dto.report.MenuReportDTO;
import com.juaracoding.dto.response.MenuResponseDTO;
import com.juaracoding.dto.validasi.MenuValidasiDTO;
import com.juaracoding.model.Menu;
import com.juaracoding.model.MenuHeader;
import com.juaracoding.repo.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public MenuResponseDTO createMenu(MenuValidasiDTO menuValidasiDTO) {
        Menu menu = new Menu();
        menu.setNamaMenu(menuValidasiDTO.getNamaMenu());
        menu.setPathMenu(menuValidasiDTO.getPathMenu());
        menu.setEndPoint(menuValidasiDTO.getEndPoint());
        menu.setCreatedDate(new Date());
        menu.setCreatedBy(1); // Assuming default user/admin ID
        menu.setIsDelete((byte) 0);

        Menu savedMenu = menuRepository.save(menu);
        return convertToResponseDTO(savedMenu);
    }

    public List<MenuResponseDTO> getAllMenus() {
        return menuRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<MenuResponseDTO> getMenuById(Long id) {
        return menuRepository.findById(id).map(this::convertToResponseDTO);
    }

    public boolean deleteMenu(Long id) {
        if (menuRepository.existsById(id)) {
            menuRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public MenuResponseDTO updateMenu(Long id, MenuValidasiDTO menuValidasiDTO) {
        Optional<Menu> menuOptional = menuRepository.findById(id);
        if (menuOptional.isPresent()) {
            Menu menu = menuOptional.get();
            menu.setNamaMenu(menuValidasiDTO.getNamaMenu());
            menu.setPathMenu(menuValidasiDTO.getPathMenu());
            menu.setEndPoint(menuValidasiDTO.getEndPoint());
            menu.setModifiedDate(new Date());
            menu.setModifiedBy(1); // Assuming updated user/admin ID

            Menu updatedMenu = menuRepository.save(menu);
            return convertToResponseDTO(updatedMenu);
        } else {
            throw new IllegalArgumentException("Menu not found with ID: " + id);
        }
    }

    // Helper methods for DTO conversions
    private MenuResponseDTO convertToResponseDTO(Menu menu) {
        MenuResponseDTO dto = new MenuResponseDTO();
        dto.setIdMenu(menu.getIdMenu());
        dto.setNamaMenu(menu.getNamaMenu());
        dto.setPathMenu(menu.getPathMenu());
        dto.setEndPoint(menu.getEndPoint());

        // Set menuHeaderName from MenuHeader
        MenuHeader menuHeader = menu.getMenuHeader();
        dto.setMenuHeaderName(menuHeader != null ? menuHeader.getNamaMenuHeader() : null);

        return dto;
    }

    private MenuReportDTO convertToReportDTO(Menu menu) {
        MenuReportDTO dto = new MenuReportDTO();
        dto.setIdMenu(menu.getIdMenu());
        dto.setNamaMenu(menu.getNamaMenu());
        dto.setPathMenu(menu.getPathMenu());
        dto.setEndPoint(menu.getEndPoint());

        // Set menuHeaderName from MenuHeader
        MenuHeader menuHeader = menu.getMenuHeader();
        dto.setMenuHeaderName(menuHeader != null ? menuHeader.getNamaMenuHeader() : null);

        dto.setCreatedDate(menu.getCreatedDate());
        dto.setModifiedDate(menu.getModifiedDate());
        dto.setIsDelete(menu.getIsDelete());
        return dto;
    }
}
