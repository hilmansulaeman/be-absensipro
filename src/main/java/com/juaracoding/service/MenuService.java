package com.juaracoding.service;

import com.juaracoding.config.OtherConfig;
import com.juaracoding.dto.validasi.MenuValidasiDTO;
import com.juaracoding.handler.ResourceNotFoundException;
import com.juaracoding.handler.ResponseHandler;
import com.juaracoding.model.Menu;
import com.juaracoding.model.MenuHeader;
import com.juaracoding.repo.MenuRepository;
import com.juaracoding.utils.ConstantMessage;
import com.juaracoding.utils.LoggingFile;
import com.juaracoding.utils.TransformToDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class MenuService {

    private MenuRepository menuRepository;

    private String[] strExceptionArr = new String[2];
    @Autowired
    private ModelMapper modelMapper;

    private Map<String, Object> objectMapper = new HashMap<String, Object>();

    private TransformToDTO transformToDTO = new TransformToDTO();

    private Map<String, String> mapColumnSearch = new HashMap<String, String>();
    private Map<Integer, Integer> mapItemPerPage = new HashMap<Integer, Integer>();
    private String[] strColumnSearch = new String[2];

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        mapColumn();
        strExceptionArr[0] = "MenuService";
        this.menuRepository = menuRepository;
    }

    private void mapColumn() {
        mapColumnSearch.put("id", "ID MENU");
        mapColumnSearch.put("nama", "NAMA MENU");
        mapColumnSearch.put("path", "PATH MENU");
        mapColumnSearch.put("point", "END POINT");
    }

    public Map<String, Object> saveMenu(MenuValidasiDTO menuDTO, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_SAVE;
        Object strUserIdz = request.getAttribute("USR_ID", 1);

        Menu menu;
        try {
            if (strUserIdz == null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE, null, "FV03001", request);
            }

            // Create new Menu instance and set its attributes
            menu = modelMapper.map(menuDTO, Menu.class);

            // Use the menuHeaderId to set the MenuHeader
            MenuHeader menuHeader = new MenuHeader();
            menuHeader.setIdMenuHeader(menuDTO.getMenuHeaderId()); // `menuHeaderId` berasal dari MenuValidasiDTO
            menu.setMenuHeader(menuHeader);

            menu.setCreatedBy(Integer.parseInt(strUserIdz.toString()));
            menu.setCreatedDate(new Date());
            menuRepository.save(menu);
        } catch (Exception e) {
            strExceptionArr[1] = "saveMenu(MenuValidasiDTO menuDTO, WebRequest request) --- LINE 67";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                    "FE03001", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataSave(objectMapper, menu.getIdMenu(), mapColumnSearch),
                null, request);
    }

    public Map<String, Object> updateMenu(Long idMenu, MenuValidasiDTO menuDTO, WebRequest request) {
        String strMessage = ConstantMessage.SUCCESS_UPDATE;
        Object strUserIdz = request.getAttribute("USR_ID", 1);

        try {
            Menu nextMenu = menuRepository.findById(idMenu).orElseThrow(() -> null);

            if (nextMenu == null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_MENU_NOT_EXISTS,
                        HttpStatus.NOT_ACCEPTABLE,
                        transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                        "FV03002", request);
            }
            if (strUserIdz == null) {
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                        HttpStatus.NOT_ACCEPTABLE,
                        null,
                        "FV03003", request);
            }

            nextMenu.setNamaMenu(menuDTO.getNamaMenu());
            nextMenu.setPathMenu(menuDTO.getPathMenu());
            nextMenu.setMenuHeader(menuDTO.getMenuHeader());
            nextMenu.setEndPoint(menuDTO.getEndPoint());
            nextMenu.setModifiedBy(Integer.parseInt(strUserIdz.toString()));
            nextMenu.setModifiedDate(new Date());

        } catch (Exception e) {
            strExceptionArr[1] = "updateMenu(Long idMenu, MenuValidasiDTO menuDTO, WebRequest request) --- LINE 92";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST,
                    transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                    "FE03002", request);
        }
        return new ResponseHandler().generateModelAttribut(strMessage,
                HttpStatus.CREATED,
                transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                null, request);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveUploadFileMenu(List<MenuValidasiDTO> listMenuDTO,
                                                  MultipartFile multipartFile,
                                                  WebRequest request) throws Exception {
        List<Menu> listMenuResult = null;
        String strMessage = ConstantMessage.SUCCESS_SAVE;

        try {
            List<Menu> listMenu = modelMapper.map(listMenuDTO, new TypeToken<List<Menu>>() {}.getType());
            listMenuResult = menuRepository.saveAll(listMenu);
            if (listMenuResult.size() == 0) {
                strExceptionArr[1] = "saveUploadFile(List<MenuValidasiDTO> listMenuDTO, MultipartFile multipartFile, WebRequest request) --- LINE 82";
                LoggingFile.exceptionStringz(strExceptionArr, new ResourceNotFoundException("FILE KOSONG"), OtherConfig.getFlagLogging());
                return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_EMPTY_FILE + " -- " + multipartFile.getOriginalFilename(),
                        HttpStatus.BAD_REQUEST, null, "FV03004", request);
            }
        } catch (Exception e) {
            strExceptionArr[1] = "saveUploadFile(List<MenuValidasiDTO> listMenuDTO, MultipartFile multipartFile, WebRequest request) --- LINE 88";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_SAVE_FAILED,
                    HttpStatus.BAD_REQUEST, null, "FE03002", request);
        }
        return new ResponseHandler().
                generateModelAttribut(strMessage,
                        HttpStatus.CREATED,
                        transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                        null,
                        request);
    }

    public Map<String, Object> findAllMenu(Pageable pageable, WebRequest request) {
        List<MenuValidasiDTO> listMenuDTO = null;
        Map<String, Object> mapResult = null;
        Page<Menu> pageMenu = null;
        List<Menu> listMenu = null;

        try {
            pageMenu = menuRepository.findByIsDelete(pageable, (byte) 1);
            listMenu = pageMenu.getContent();
            if (listMenu.size() == 0) {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper, pageable, mapColumnSearch), //HANDLE NILAI PENCARIAN
                                "FV03005",
                                request);
            }
            listMenuDTO = modelMapper.map(listMenu, new TypeToken<List<MenuValidasiDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper, listMenuDTO, pageMenu, mapColumnSearch);

        } catch (Exception e) {
            strExceptionArr[1] = "findAllMenu(Pageable pageable, WebRequest request) --- LINE 178";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_INTERNAL_SERVER,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper, pageable, mapColumnSearch), //HANDLE NILAI PENCARIAN
                    "FE03003", request);
        }

        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        null);
    }

    public Map<String, Object> findByPage(Pageable pageable, WebRequest request, String columFirst, String valueFirst) {
        Page<Menu> pageMenu = null;
        List<Menu> listMenu = null;
        List<MenuValidasiDTO> listMenuDTO = null;
        Map<String, Object> mapResult = null;

        try {
            if (columFirst.equals("id")) {
                try {
                    Long.parseLong(valueFirst);
                } catch (Exception e) {
                    strExceptionArr[1] = "findByPage(Pageable pageable, WebRequest request, String columFirst, String valueFirst) --- LINE 209";
                    LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
                    return new ResponseHandler().
                            generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                    HttpStatus.OK,
                                    transformToDTO.transformObjectDataEmpty(objectMapper, pageable, mapColumnSearch), //HANDLE NILAI PENCARIAN
                                    "FE03004",
                                    request);
                }
            }
            pageMenu = getDataByValue(pageable, columFirst, valueFirst);
            listMenu = pageMenu.getContent();
            if (listMenu.size() == 0) {
                return new ResponseHandler().
                        generateModelAttribut(ConstantMessage.WARNING_DATA_EMPTY,
                                HttpStatus.OK,
                                transformToDTO.transformObjectDataEmpty(objectMapper, pageable, mapColumnSearch), //HANDLE NILAI PENCARIAN EMPTY
                                "FV03006",
                                request);
            }
            listMenuDTO = modelMapper.map(listMenu, new TypeToken<List<MenuValidasiDTO>>() {}.getType());
            mapResult = transformToDTO.transformObject(objectMapper, listMenuDTO, pageMenu, mapColumnSearch);
        } catch (Exception e) {
            strExceptionArr[1] = "findByPage(Pageable pageable, WebRequest request, String columFirst, String valueFirst) --- LINE 237";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(ConstantMessage.ERROR_FLOW_INVALID,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    transformToDTO.transformObjectDataEmpty(objectMapper, pageable, mapColumnSearch),
                    "FE03005", request);
        }
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        mapResult,
                        null,
                        request);
    }

    public Map<String, Object> findById(Long id, WebRequest request) {
        Menu menu = menuRepository.findById(id).orElseThrow(() -> null);
        if (menu == null) {
            return new ResponseHandler().generateModelAttribut(ConstantMessage.WARNING_MENU_NOT_EXISTS,
                    HttpStatus.NOT_ACCEPTABLE,
                    transformToDTO.transformObjectDataEmpty(objectMapper, mapColumnSearch),
                    "FV03005", request);
        }
        MenuValidasiDTO menuDTO = modelMapper.map(menu, new TypeToken<MenuValidasiDTO>() {}.getType());
        return new ResponseHandler().
                generateModelAttribut(ConstantMessage.SUCCESS_FIND_BY,
                        HttpStatus.OK,
                        menuDTO,
                        null,
                        request);
    }

    public List<MenuValidasiDTO> getAllMenu() { // KHUSUS UNTUK FORM INPUT SAJA
        List<MenuValidasiDTO> listMenuDTO = null;
        List<Menu> listMenu = null;

        try {
            listMenu = menuRepository.findByIsDelete((byte) 1);
            if (listMenu.size() == 0) {
                return new ArrayList<MenuValidasiDTO>();
            }
            listMenuDTO = modelMapper.map(listMenu, new TypeToken<List<MenuValidasiDTO>>() {}.getType());
        } catch (Exception e) {
            strExceptionArr[1] = "findAllMenu() --- LINE 304";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return listMenuDTO;
        }
        return listMenuDTO;
    }

    private Page<Menu> getDataByValue(Pageable pageable, String paramColumn, String paramValue) {
        if (paramValue.equals("")) {
            return menuRepository.findByIsDelete(pageable, (byte) 1);
        }
        if (paramColumn.equals("id")) {
            return menuRepository.findByIsDeleteAndIdMenu(pageable, (byte) 1, Long.parseLong(paramValue));
        } else if (paramColumn.equals("nama")) {
            return menuRepository.findByIsDeleteAndNamaMenuContainsIgnoreCase(pageable, (byte) 1, paramValue);
        } else if (paramColumn.equals("path")) {
            return menuRepository.findByIsDeleteAndPathMenuContainsIgnoreCase(pageable, (byte) 1, paramValue);
        } else if (paramColumn.equals("point")) {
            return menuRepository.findByIsDeleteAndEndPointContainsIgnoreCase(pageable, (byte) 1, paramValue);
        }

        return menuRepository.findByIsDelete(pageable, (byte) 1);
    }
}
