package com.juaracoding.service;

import com.juaracoding.dto.report.MenuHeaderReportDTO;
import com.juaracoding.dto.response.MenuHeaderResponseDTO;
import com.juaracoding.dto.validasi.MenuHeaderValidasiDTO;
import com.juaracoding.model.MenuHeader;
import com.juaracoding.repo.MenuHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuHeaderService {

    private final MenuHeaderRepository menuHeaderRepository;

    @Autowired
    public MenuHeaderService(MenuHeaderRepository menuHeaderRepository) {
        this.menuHeaderRepository = menuHeaderRepository;
    }

    public MenuHeaderResponseDTO createMenuHeader(MenuHeaderValidasiDTO menuHeaderValidasiDTO) {
        MenuHeader menuHeader = new MenuHeader();
        menuHeader.setNamaMenuHeader(menuHeaderValidasiDTO.getNamaMenuHeader());
        menuHeader.setDeskripsiMenuHeader(menuHeaderValidasiDTO.getDeskripsiMenuHeader());
        menuHeader.setCreatedDate(new Date());
        menuHeader.setCreatedBy(1); // Assuming a default user/admin ID
        menuHeader.setIsDelete((byte) 0);

        MenuHeader savedMenuHeader = menuHeaderRepository.save(menuHeader);
        return convertToResponseDTO(savedMenuHeader);
    }

    public List<MenuHeaderResponseDTO> getAllMenuHeaders() {
        return menuHeaderRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<MenuHeaderResponseDTO> getMenuHeaderById(Long id) {
        return menuHeaderRepository.findById(id).map(this::convertToResponseDTO);
    }

    public boolean deleteMenuHeader(Long id) {
        if (menuHeaderRepository.existsById(id)) {
            menuHeaderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public MenuHeaderResponseDTO updateMenuHeader(Long id, MenuHeaderValidasiDTO menuHeaderValidasiDTO) {
        Optional<MenuHeader> menuHeaderOptional = menuHeaderRepository.findById(id);
        if (menuHeaderOptional.isPresent()) {
            MenuHeader menuHeader = menuHeaderOptional.get();
            menuHeader.setNamaMenuHeader(menuHeaderValidasiDTO.getNamaMenuHeader());
            menuHeader.setDeskripsiMenuHeader(menuHeaderValidasiDTO.getDeskripsiMenuHeader());
            menuHeader.setModifiedDate(new Date());
            menuHeader.setModifiedBy(1); // Assuming updated user ID

            MenuHeader updatedMenuHeader = menuHeaderRepository.save(menuHeader);
            return convertToResponseDTO(updatedMenuHeader);
        } else {
            throw new IllegalArgumentException("MenuHeader not found with ID: " + id);
        }
    }

    // Helper methods for DTO conversions
    private MenuHeaderResponseDTO convertToResponseDTO(MenuHeader menuHeader) {
        MenuHeaderResponseDTO dto = new MenuHeaderResponseDTO();
        dto.setIdMenuHeader(menuHeader.getIdMenuHeader());
        dto.setNamaMenuHeader(menuHeader.getNamaMenuHeader());
        dto.setDeskripsiMenuHeader(menuHeader.getDeskripsiMenuHeader());
        return dto;
    }

    private MenuHeaderReportDTO convertToReportDTO(MenuHeader menuHeader) {
        MenuHeaderReportDTO dto = new MenuHeaderReportDTO();
        dto.setIdMenuHeader(menuHeader.getIdMenuHeader());
        dto.setNamaMenuHeader(menuHeader.getNamaMenuHeader());
        dto.setDeskripsiMenuHeader(menuHeader.getDeskripsiMenuHeader());
        dto.setCreatedDate(menuHeader.getCreatedDate());
        dto.setModifiedDate(menuHeader.getModifiedDate());
        dto.setIsDelete(menuHeader.getIsDelete());
        return dto;
    }
}
