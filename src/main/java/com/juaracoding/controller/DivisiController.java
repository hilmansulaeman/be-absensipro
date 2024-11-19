package com.juaracoding.controller;

import com.juaracoding.config.OtherConfig;
import com.juaracoding.dto.response.DivisiResponseDTO;
import com.juaracoding.dto.validasi.DivisiValidasiDTO;
import com.juaracoding.service.DivisiService;
import com.juaracoding.utils.ConstantMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.juaracoding.dto.validasi.DivisiValidasiDTO;
import com.juaracoding.model.Divisi;
import com.juaracoding.service.DivisiService;
import com.juaracoding.utils.MappingAttribute;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/usrmgmnt")
public class DivisiController {

    private final DivisiService divisiService;

    @Autowired
    private ModelMapper modelMapper;

    private Map<String, Object> objectMapper = new HashMap<>();
    private final Map<String, String> mapSorting = new HashMap<>();
    private final String[] strExceptionArr = new String[2];
    private final MappingAttribute mappingAttribute = new MappingAttribute();

    @Autowired
    public DivisiController(DivisiService divisiService) {
        strExceptionArr[0] = "DivisiController";
        mapSorting();
        this.divisiService = divisiService;
    }

    private void mapSorting() {
        mapSorting.put("id", "idDivisi");
        mapSorting.put("nama", "namaDivisi");
        mapSorting.put("kode", "kodeDivisi");
    }

    @GetMapping("/v1/divisi/new")
    public String createDivisi(Model model, WebRequest request) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        model.addAttribute("divisi", new DivisiValidasiDTO());
        return "divisi/create_divisi";
    }

    @PostMapping("/v1/divisi/new")
    public String newDivisi(@ModelAttribute(value = "divisi") @Valid DivisiValidasiDTO divisiDTO,
                            BindingResult bindingResult, Model model, WebRequest request) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("divisi", divisiDTO);
            return "divisi/create_divisi";
        }

        objectMapper = divisiService.createDivisi(divisiDTO, request);

        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID)) {
            return "redirect:/api/check/logout";
        }

        if ((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model, objectMapper);
            model.addAttribute("message", "DATA BERHASIL DISIMPAN");
            Long idDataSave = objectMapper.get("idDataSave") == null ? 1 : Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/usrmgmnt/v1/divisi/fbpsb/0/asc/idDivisi?columnFirst=id&valueFirst=" + idDataSave + "&sizeComponent=5";
        } else {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            model.addAttribute("divisi", divisiDTO);
            return "divisi/create_divisi";
        }
    }

    @GetMapping("/v1/divisi/edit/{id}")
    public String editDivisi(Model model, WebRequest request, @PathVariable("id") Long id) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }

        objectMapper = divisiService.getDivisiById(id, request);

        if ((Boolean) objectMapper.get("success")) {
            DivisiValidasiDTO divisiDTO = (DivisiValidasiDTO) objectMapper.get("data");
            model.addAttribute("divisi", divisiDTO);
            return "divisi/edit_divisi";
        } else {
            return "redirect:/api/usrmgmnt/v1/divisi/default";
        }
    }

    @PostMapping("/v1/divisi/edit/{id}")
    public String updateDivisi(@ModelAttribute(value = "divisi") @Valid DivisiValidasiDTO divisiDTO,
                               BindingResult bindingResult, @PathVariable("id") Long id,
                               Model model, WebRequest request) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("divisi", divisiDTO);
            return "divisi/edit_divisi";
        }

        objectMapper = divisiService.updateDivisi(id, divisiDTO, request);

        if (objectMapper.get("message").toString().equals(ConstantMessage.ERROR_FLOW_INVALID)) {
            return "redirect:/api/check/logout";
        }

        if ((Boolean) objectMapper.get("success")) {
            mappingAttribute.setAttribute(model, objectMapper);
            return "redirect:/api/usrmgmnt/v1/divisi/default";
        } else {
            model.addAttribute("divisi", divisiDTO);
            return "divisi/edit_divisi";
        }
    }

    @GetMapping("/v1/divisi/default")
    public String getDefaultData(Model model, WebRequest request) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("idDivisi"));
        objectMapper = divisiService.findAllDivisi(pageable, request);
        mappingAttribute.setAttribute(model, objectMapper, request);

        model.addAttribute("divisi", new DivisiValidasiDTO());
        model.addAttribute("sortBy", "idDivisi");
        model.addAttribute("currentPage", 1);
        model.addAttribute("asc", "asc");
        model.addAttribute("columnFirst", "");
        model.addAttribute("valueFirst", "");
        model.addAttribute("sizeComponent", 5);
        return "/divisi/divisi";
    }

    @GetMapping("/v1/divisi/delete/{id}")
    public String deleteDivisi(Model model, WebRequest request, @PathVariable("id") Long id) {
        objectMapper = divisiService.deleteDivisi(id, request);
        mappingAttribute.setAttribute(model, objectMapper);
        model.addAttribute("divisi", new DivisiValidasiDTO());
        return "redirect:/api/usrmgmnt/v1/divisi/default";
    }
}


