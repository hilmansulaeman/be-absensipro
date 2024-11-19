package com.juaracoding.service;

import com.juaracoding.config.OtherConfig;
import com.juaracoding.dto.report.MenuHeaderReportDTO;
import com.juaracoding.dto.response.MenuHeaderResponseDTO;
import com.juaracoding.dto.validasi.MenuHeaderValidasiDTO;
import com.juaracoding.handler.ResponseHandler;
import com.juaracoding.model.MenuHeader;
import com.juaracoding.repo.MenuHeaderRepository;
import com.juaracoding.utils.ConstantMessage;
import com.juaracoding.utils.LoggingFile;
import com.juaracoding.utils.TransformToDTO;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuHeaderService {

    private MenuHeaderRepository menuHeaderRepository;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String, Object> objectMapper = new HashMap<>();
    private List<MenuHeader> lsCPUpload = new ArrayList<>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String, String> mapColumnSearch = new HashMap<>();
    private String[] strColumnSearch = new String[2];

    @Autowired
    public MenuHeaderService(MenuHeaderRepository menuHeaderRepository) {
        strExceptionArr[0] = "MenuHeaderService";
        mapColumn();
        this.menuHeaderRepository = menuHeaderRepository;
    }

    public Map<String, Object> saveMenuHeader(MenuHeaderValidasiDTO menuHeaderValidasiDTO, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID", 1);

        MenuHeader menuHeader;
        try {
            if (strUserIdz == null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE, null, "FV03001", request);
            }

            // Convert MenuHeaderValidasiDTO to MenuHeader
            menuHeader = modelMapper.map(menuHeaderValidasiDTO, MenuHeader.class);
            menuHeader.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            menuHeader.setCreatedDate(new Date());
            menuHeaderRepository.save(menuHeader);
        } catch (Exception e) {
            strExceptionArr[1] = "saveMenu(MenuHeaderValidasiDTO menuHeaderValidasiDTO, WebRequest request) --- LINE 67";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                    "FE03001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, menuHeader.getIdMenuHeader(), mapColumnSearch),
                null, request);
    }

    public Map<String, Object> updateMenuHeader(Long idMenuHeader, MenuHeaderValidasiDTO menuHeaderValidasiDTO, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_UPDATE;
        Object strUserIdz = request.getAttribute("USR_ID", 1);

        try {
            MenuHeader nextMenu = menuHeaderRepository.findById(idMenuHeader).orElseThrow(() -> null);

            if (nextMenu == null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_MENU_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE, transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                        "FV03002", request);
            }
            if (strUserIdz == null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE, null, "FV03003", request);
            }

            // Update fields using MenuHeaderValidasiDTO data
            nextMenu.setNamaMenuHeader(menuHeaderValidasiDTO.getNamaMenuHeader());
            nextMenu.setDeskripsiMenuHeader(menuHeaderValidasiDTO.getDeskripsiMenuHeader());
            nextMenu.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextMenu.setModifiedDate(new Date());

            menuHeaderRepository.save(nextMenu);
        } catch (Exception e) {
            strExceptionArr[1] = "updateMenuHeader(Long idMenuHeader, MenuHeaderValidasiDTO menuHeaderValidasiDTO, WebRequest request) --- LINE 92";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                    "FE03002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                null, request);
    }

    public Map<String, Object> findAllMenuHeader(Pageable pageable, WebRequest request) {
        List<MenuHeaderValidasiDTO> listMenuHeaderDTO = null;
        Map<String, Object> mapResult = null;
        Page<MenuHeader> pageMenuHeader = null;
        List<MenuHeader> listMenuHeader = null;

        try {
            pageMenuHeader = menuHeaderRepository.findByIsDelete(pageable, (byte) 1);
            listMenuHeader = pageMenuHeader.getContent();
            if (listMenuHeader.size() == 0) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                        HttpStatus.OK,
                        transformToDTO.transformObjectDataEmpty(objectMapper, pageable, mapColumnSearch),
                        "FV05005", request);
            }
            // Mapping from entity to DTO
            listMenuHeaderDTO = modelMapper.map(listMenuHeader, new TypeToken<List<MenuHeaderValidasiDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper, listMenuHeaderDTO, pageMenuHeader, mapColumnSearch);
        } catch (Exception e) {
            strExceptionArr[1] = "findAllMenuHeader(Pageable pageable, WebRequest request) --- LINE 177";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper, pageable, mapColumnSearch),
                    "FE05003", request);
        }

        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                HttpStatus.OK, mapResult, null, null);
    }

    public Map<String, Object> findById(Long id, WebRequest request) {
        MenuHeader menuHeader = menuHeaderRepository.findById(id).orElseThrow(() -> null);
        if (menuHeader == null) {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_MENU_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE, transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                    "FV03004", request);
        }

        // Convert the MenuHeader entity to MenuHeaderValidasiDTO
        MenuHeaderValidasiDTO menuHeaderDTO = modelMapper.map(menuHeader, MenuHeaderValidasiDTO.class);
        return new ResponseHandler().generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                HttpStatus.OK, menuHeaderDTO, null, request);
    }

    public List<MenuHeaderValidasiDTO> getAllMenuHeader() {
        List<MenuHeaderValidasiDTO> listMenuHeaderDTO = null;
        List<MenuHeader> listMenuHeader = null;

        try {
            listMenuHeader = menuHeaderRepository.findByIsDelete((byte) 1);
            if (listMenuHeader.size() == 0) {
                return new ArrayList<MenuHeaderValidasiDTO>();
            }
            listMenuHeaderDTO = modelMapper.map(listMenuHeader, new TypeToken<List<MenuHeaderValidasiDTO>>() {}.getType());
        } catch (Exception e) {
            strExceptionArr[1] = "getAllMenuHeader() --- LINE 223";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return listMenuHeaderDTO;
        }
        return listMenuHeaderDTO;
    }

    public Map<String, Object> deleteMenuHeader(Long idMenuHeader, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_DELETE;
        Object strUserIdz = request.getAttribute("USR_ID", 1);
        MenuHeader nextMenuHeader = null;
        try {
            nextMenuHeader = menuHeaderRepository.findById(idMenuHeader).orElseThrow(() -> null);

            if (nextMenuHeader == null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_DEMO_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE, transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                        "FV05006", request);
            }
            if (strUserIdz == null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE, null, "FV05007", request);
            }

            nextMenuHeader.setIsDelete((byte) 0);
            nextMenuHeader.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextMenuHeader.setModifiedDate(new Date());
            menuHeaderRepository.save(nextMenuHeader);
        } catch (Exception e) {
            strExceptionArr[1] = "deleteMenuHeader(Long idMenuHeader, WebRequest request) --- LINE 344";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                    "FE05007", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage, HttpStatus.OK,
                transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch), null, request);
    }

    // Mapping columns for search purposes
    public Map<String, String> mapColumn() {
        mapColumnSearch.put("id", "ID");
        mapColumnSearch.put("nama", "NAMA");

        mapColumnSearch.put("deskripsi", "DESKRIPSI");
        return mapColumnSearch;
    }

    public Page<MenuHeader> getDataByValue(Pageable pageable, String paramColumn, String paramValue) {
        if (paramValue.equals("")) {
            return menuHeaderRepository.findByIsDelete(pageable, (byte) 1);
        }

        if (paramColumn.equals("id")) {
            return menuHeaderRepository.findByIsDeleteAndIdMenuHeader(pageable, (byte) 1, Long.parseLong(paramValue));
        } else if (paramColumn.equals("nama")) {
            return menuHeaderRepository.findByIsDeleteAndNamaMenuHeader(pageable, (byte) 1, paramValue);
        } else if (paramColumn.equals("deskripsi")) {
            return menuHeaderRepository.findByIsDeleteAndDeskripsiMenuHeader(pageable, (byte) 1, paramValue);
        }

        return menuHeaderRepository.findByIsDelete(pageable, (byte) 1);
    }

    public Map<String, Object> findByPage(Pageable pageable, WebRequest request, String columnFirst, String valueFirst) {
        return null;
    }
}
