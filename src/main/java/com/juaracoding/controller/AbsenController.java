package com.juaracoding.controller;

import com.juaracoding.config.OtherConfig;
import com.juaracoding.dto.response.AbsenResponseDTO;
import com.juaracoding.dto.validasi.AbsenValidationDTO;
import com.juaracoding.service.AbsenService;
import com.juaracoding.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/usrmgmnt")
public class AbsenController {

    private final AbsenService absenService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    private final Map<String, String> mapSorting = new HashMap<>();

    @Autowired
    public AbsenController(AbsenService absenService, UserService userService, ModelMapper modelMapper) {
        this.absenService = absenService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        mapSorting();
    }

    /**
     * Mapping untuk sorting jika diperlukan di masa depan.
     */
    private void mapSorting() {
        mapSorting.put("idAbsen", "idAbsen");
        mapSorting.put("absenIn", "absenIn");
        mapSorting.put("absenOut", "absenOut");
    }

    /**
     * Validasi sesi pengguna.
     */
    private boolean isValidSession(WebRequest request) {
        return "y".equalsIgnoreCase(OtherConfig.getFlagSessionValidation())
                && request.getAttribute("USR_ID", WebRequest.SCOPE_SESSION) != null;
    }

    /**
     * Endpoint untuk membuat absen baru.
     */
    @PostMapping("/v1/absen/new")
    public String newAbsen(@ModelAttribute("absen") @Valid AbsenValidationDTO absenDTO,
                           BindingResult bindingResult,
                           Model model,
                           WebRequest request) {

        // Validasi sesi
        if (!isValidSession(request)) {
            return "redirect:/api/check/logout";
        }

        // Validasi input
        if (absenDTO.getUserId() == null) {
            bindingResult.rejectValue("userId", "error.absenDTO", "User harus dipilih!");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("absen", absenDTO);
            model.addAttribute("listUserAbsen", userService.getAllUser());
            return "absen/create_absen";
        }

        // Simpan absen
        AbsenResponseDTO responseDTO = absenService.saveAbsen(absenDTO);

        if (responseDTO == null) {
            model.addAttribute("errorMessage", "Error saat menyimpan absen.");
            model.addAttribute("absen", absenDTO);
            model.addAttribute("listUserAbsen", userService.getAllUser());
            return "absen/create_absen";
        }

        return "redirect:/api/usrmgmnt/v1/absen/default";
    }

    /**
     * Endpoint untuk mengedit absen.
     */
    @PostMapping("/v1/absen/edit/{id}")
    public String editAbsen(@ModelAttribute("absen") @Valid AbsenValidationDTO absenDTO,
                            BindingResult bindingResult,
                            Model model,
                            WebRequest request,
                            @PathVariable("id") Long id) {

        // Validasi sesi
        if (!isValidSession(request)) {
            return "redirect:/api/check/logout";
        }

        // Validasi input
        if (absenDTO.getUserId() == null) {
            bindingResult.rejectValue("userId", "error.absenDTO", "User harus dipilih!");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("absen", absenDTO);
            model.addAttribute("listUserAbsen", userService.getAllUser());
            return "absen/edit_absen";
        }

        // Update absen
        AbsenResponseDTO responseDTO = absenService.updateAbsen(id, absenDTO);

        if (responseDTO == null) {
            model.addAttribute("errorMessage", "Error saat memperbarui absen.");
            model.addAttribute("absen", absenDTO);
            model.addAttribute("listUserAbsen", userService.getAllUser());
            return "absen/edit_absen";
        }

        return "redirect:/api/usrmgmnt/v1/absen/default";
    }
}
