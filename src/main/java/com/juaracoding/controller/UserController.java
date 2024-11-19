package com.juaracoding.controller;

import com.juaracoding.config.OtherConfig;
import com.juaracoding.dto.validasi.AksesValidasiDTO;
import com.juaracoding.dto.validasi.DivisiValidasiDTO;
import com.juaracoding.dto.validasi.UserValidasiDTO;
import com.juaracoding.dto.validasi.VerifikasiForgotPasswordDTO;
import com.juaracoding.model.Userz;
import com.juaracoding.service.AksesService;
import com.juaracoding.service.UserService;
import com.juaracoding.utils.ConstantMessage;
import com.juaracoding.utils.MappingAttribute;
import com.juaracoding.utils.PdfGenaratorUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/authz")
public class UserController {

    private final UserService userService;
    private final AksesService aksesService;

    @Autowired
    private ModelMapper modelMapper;

    private Map<String, Object> objectMapper = new HashMap<>();
    private final Map<String, String> mapSorting = new HashMap<>();
    private final String[] strExceptionArr = new String[2];
    private final MappingAttribute mappingAttribute = new MappingAttribute();

    @Autowired
    PdfGenaratorUtil pdfGenaratorUtil;

    private final StringBuilder sBuild = new StringBuilder();

    @Autowired
    public UserController(UserService userService, AksesService aksesService) {
        this.userService = userService;
        this.aksesService = aksesService;
        strExceptionArr[0] = "UserController";
        mapSorting();
    }

    private void mapSorting() {
        mapSorting.put("id", "idUser");
        mapSorting.put("nama", "namaLengkap");
        mapSorting.put("uname", "username");
        mapSorting.put("email", "email");
        mapSorting.put("akses", "namaAkses");
    }

    @GetMapping("/v1/userman/new")
    public String createUser(Model model, HttpServletRequest request) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, (WebRequest) request);
            if (request.getAttribute("USR_ID") == null) {
                return "redirect:/api/check/logout";
            }
        }
        UserValidasiDTO userDTO = new UserValidasiDTO();
        userDTO.setPassword("Default@123");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("idAkses"));
        model.addAttribute("usr", userDTO);
        model.addAttribute("listAkses", aksesService.getAllAkses(pageable, request).getBody());
        return "user_management/create_user";
    }

    @GetMapping("/v1/userman/edit/{id}")
    public String editUser(Model model, HttpServletRequest request, @PathVariable("id") Long id) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, (WebRequest) request);
            if (request.getAttribute("USR_ID") == null) {
                return "redirect:/api/check/logout";
            }
        }
        objectMapper = (Map<String, Object>) userService.findById(id, request);
        UserValidasiDTO userDTO = (objectMapper.get("data") == null ? null : (UserValidasiDTO) objectMapper.get("data"));
        if ((Boolean) objectMapper.get("success")) {
            model.addAttribute("usr", userDTO);
            Pageable pageable = PageRequest.of(0, 10, Sort.by("idAkses"));
            model.addAttribute("listAkses", aksesService.getAllAkses(pageable, request).getBody());
            AksesValidasiDTO aksesDTO = userDTO.getAkses();
            String strSelected = aksesDTO == null ? "null" : aksesDTO.getIdAkses().toString();
            model.addAttribute("selectedValues", strSelected);
            return "user_management/edit_user";
        } else {
            model.addAttribute("usr", new UserValidasiDTO());
            return "redirect:/api/authz/v1/userman/default";
        }
    }

    @PostMapping("/v1/userman/new")
    public String newUser(@ModelAttribute("usr") @Valid UserValidasiDTO userDTO,
                          BindingResult bindingResult,
                          Model model,
                          HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("idAkses"));
            model.addAttribute("usr", userDTO);
            model.addAttribute("status", "error");
            model.addAttribute("listAkses", aksesService.getAllAkses(pageable, request).getBody());
            return "user_management/create_user";
        }

        Userz userz = modelMapper.map(userDTO, Userz.class);
        objectMapper = userService.saveUser(userz, (WebRequest) request);
        if ((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model, objectMapper);
            model.addAttribute("usr", new UserValidasiDTO());
            return "redirect:/api/authz/v1/userman/default";
        } else {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            Pageable pageable = PageRequest.of(0, 10, Sort.by("idAkses"));
            model.addAttribute("listAkses", aksesService.getAllAkses(pageable, request).getBody());
            model.addAttribute("usr", new UserValidasiDTO());
            return "user_management/create_user";
        }
    }

    @PostMapping("/v1/userman/edit/{id}")
    public String editUser(@ModelAttribute("usr") @Valid UserValidasiDTO userDTO,
                           BindingResult bindingResult,
                           Model model,
                           HttpServletRequest request,
                           @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            Pageable pageable = PageRequest.of(0, 10, Sort.by("idAkses"));
            model.addAttribute("usr", userDTO);
            model.addAttribute("listAkses", aksesService.getAllAkses(pageable, request).getBody());
            model.addAttribute("selectedValues", "");
            return "user_management/edit_user";
        }

        userDTO.setIdUser(id);
        Userz userz = modelMapper.map(userDTO, Userz.class);
        objectMapper = userService.updateUser(id, userz, (WebRequest) request);

        if ((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model, objectMapper);
            model.addAttribute("usr", new UserValidasiDTO());
            return "redirect:/api/authz/v1/userman/default";
        } else {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            Pageable pageable = PageRequest.of(0, 10, Sort.by("idAkses"));
            model.addAttribute("listAkses", aksesService.getAllAkses(pageable, request).getBody());
            model.addAttribute("usr", new UserValidasiDTO());
            return "user_management/edit_user";
        }
    }
}


