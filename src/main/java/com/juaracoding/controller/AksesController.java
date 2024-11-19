package com.juaracoding.controller;

import com.juaracoding.config.OtherConfig;
import com.juaracoding.dto.response.AksesResponseDTO;
import com.juaracoding.dto.validasi.AksesValidasiDTO;
import com.juaracoding.dto.response.MenuResponseDTO;
import com.juaracoding.model.Akses;
import com.juaracoding.service.AksesService;
import com.juaracoding.service.DivisiService;
import com.juaracoding.service.MenuService;
import com.juaracoding.utils.MappingAttribute;
import com.juaracoding.utils.PdfGenaratorUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/usrmgmnt")
public class AksesController {

    private final AksesService aksesService;
    private final MenuService menuService;
    private final DivisiService divisiService;
    private final ModelMapper modelMapper;

    private final Map<String, Object> objectMapper = new HashMap<>();
    private final Map<String, String> mapSorting = new HashMap<>();
    private final MappingAttribute mappingAttribute = new MappingAttribute();

    private final PdfGenaratorUtil pdfGenaratorUtil;
    private final StringBuilder sBuild = new StringBuilder();

    @Autowired
    public AksesController(AksesService aksesService, MenuService menuService, DivisiService divisiService,
                           ModelMapper modelMapper, PdfGenaratorUtil pdfGenaratorUtil) {
        this.aksesService = aksesService;
        this.menuService = menuService;
        this.divisiService = divisiService;
        this.modelMapper = modelMapper;
        this.pdfGenaratorUtil = pdfGenaratorUtil;
        mapSorting();
    }

    private void mapSorting() {
        mapSorting.put("id", "idAkses");
        mapSorting.put("nama", "namaAkses");
    }

    @GetMapping("/v1/akses/new")
    public String createAkses(Model model, WebRequest request) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, request);
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        model.addAttribute("akses", new AksesValidasiDTO());
        model.addAttribute("listDivisi", divisiService.findAllDivisi(PageRequest.of(0, 5), request).get("data"));
        model.addAttribute("listMenu", menuService.getAllMenu());
        return "akses/create_akses";
    }

    @GetMapping("/v1/akses/edit/{id}")
    public String editAkses(Model model, WebRequest request, @PathVariable("id") Long id) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model, request);
        objectMapper.putAll(aksesService.findById(id, request));

        if ((Boolean) objectMapper.get("success")) {
            AksesResponseDTO aksesResponse = (AksesResponseDTO) objectMapper.get("data");
            model.addAttribute("akses", aksesResponse);
            model.addAttribute("listDivisi", divisiService.findAllDivisi(PageRequest.of(0, 5), request).get("data"));
            model.addAttribute("listMenu", menuService.getAllMenu());
            model.addAttribute("menuSelected", aksesResponse.getListMenuNames());
            if (aksesResponse.getDivisiName() != null) {
                model.addAttribute("selectedValues", aksesResponse.getDivisiName());
            }
            return "akses/edit_akses";
        } else {
            return "redirect:/api/usrmgmnt/v1/akses/default";
        }
    }

    @PostMapping("/v1/akses/new")
    public String newAkses(@ModelAttribute("akses") @Valid AksesValidasiDTO aksesValidasiDTO,
                           BindingResult bindingResult, Model model, WebRequest request) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model, request);

        if (bindingResult.hasErrors()) {
            model.addAttribute("akses", aksesValidasiDTO);
            model.addAttribute("status", "error");
            model.addAttribute("listDivisi", divisiService.findAllDivisi(PageRequest.of(0, 5), request).get("data"));
            model.addAttribute("listMenu", menuService.getAllMenu());
            return "akses/create_akses";
        }

        Akses akses = modelMapper.map(aksesValidasiDTO, Akses.class);
        objectMapper.putAll(aksesService.saveAkses(akses, request));

        if ((Boolean) objectMapper.get("success")) {
            Long idDataSave = objectMapper.get("idDataSave") == null
                    ? 1
                    : Long.parseLong(objectMapper.get("idDataSave").toString());
            return "redirect:/api/usrmgmnt/v1/akses/fbpsb/0/asc/id?columnFirst=id&valueFirst=" + idDataSave + "&sizeComponent=5";
        } else {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            model.addAttribute("listDivisi", divisiService.findAllDivisi(PageRequest.of(0, 5), request).get("data"));
            model.addAttribute("listMenu", menuService.getAllMenu());
            model.addAttribute("akses", new AksesValidasiDTO());
            model.addAttribute("status", "error");
            return "akses/create_akses";
        }
    }

    @PostMapping("/v1/akses/edit/{id}")
    public String editAkses(@ModelAttribute("akses") @Valid AksesValidasiDTO aksesValidasiDTO,
                            BindingResult bindingResult, Model model, WebRequest request, @PathVariable("id") Long id) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model, request);

        if (bindingResult.hasErrors()) {
            model.addAttribute("akses", aksesValidasiDTO);
            model.addAttribute("listDivisi", divisiService.findAllDivisi(PageRequest.of(0, 5), request).get("data"));
            model.addAttribute("listMenu", menuService.getAllMenu());
            return "akses/edit_akses";
        }

        Akses akses = modelMapper.map(aksesValidasiDTO, Akses.class);
        objectMapper.putAll(aksesService.updateAkses(id, akses, request));

        if ((Boolean) objectMapper.get("success")) {
            return "redirect:/api/usrmgmnt/v1/akses/fbpsb/0/asc/id?columnFirst=id&valueFirst=" + id + "&sizeComponent=5";
        } else {
            mappingAttribute.setErrorMessage(bindingResult, objectMapper.get("message").toString());
            model.addAttribute("akses", new AksesValidasiDTO());
            model.addAttribute("listDivisi", divisiService.findAllDivisi(PageRequest.of(0, 5), request).get("data"));
            model.addAttribute("listMenu", menuService.getAllMenu());
            return "akses/edit_akses";
        }
    }

    @GetMapping("/v1/akses/default")
    public String getDefaultData(Model model, WebRequest request) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        mappingAttribute.setAttribute(model, request);
        Pageable pageable = PageRequest.of(0, 5, Sort.by("idAkses"));
        objectMapper.putAll(aksesService.findAllAkses(pageable, request));
        mappingAttribute.setAttribute(model, objectMapper);

        model.addAttribute("akses", new AksesValidasiDTO());
        model.addAttribute("sortBy", "id");
        model.addAttribute("currentPage", 1);
        model.addAttribute("asc", "asc");
        model.addAttribute("columnFirst", "");
        model.addAttribute("valueFirst", "");
        model.addAttribute("sizeComponent", 5);
        return "/akses/akses";
    }
}

