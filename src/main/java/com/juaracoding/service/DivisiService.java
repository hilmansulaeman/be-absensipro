package com.juaracoding.service;

import com.juaracoding.config.OtherConfig;
import com.juaracoding.dto.validasi.DivisiValidasiDTO;
import com.juaracoding.handler.ResponseHandler;
import com.juaracoding.model.Divisi;
import com.juaracoding.repo.DivisiRepository;
import com.juaracoding.utils.GlobalFunction;
import com.juaracoding.utils.LoggingFile;
import com.juaracoding.utils.TransformToDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class DivisiService {

    private final DivisiRepository divisiRepository;
    private final ModelMapper modelMapper;

    private final Map<String, Object> objectMapper = new HashMap<>();
    private final TransformToDTO transformToDTO = new TransformToDTO();
    private final String[] strExceptionArr = new String[2];
    private final Map<String, String> mapColumnSearch = new HashMap<>();

    @Autowired
    public DivisiService(DivisiRepository divisiRepository, ModelMapper modelMapper) {
        this.divisiRepository = divisiRepository;
        this.modelMapper = modelMapper;
        strExceptionArr[0] = "DivisiService";
        mapColumn();
    }

    private void mapColumn() {
        mapColumnSearch.put("id", "ID DIVISI");
        mapColumnSearch.put("nama", "NAMA DIVISI");
    }

    public Map<String, Object> createDivisi(DivisiValidasiDTO divisiDTO, WebRequest request) {
        try {
            if (divisiRepository.existsByNamaDivisi(divisiDTO.getNamaDivisi())) {
                return new ResponseHandler().generateModelAttribut(
                        "Nama divisi sudah ada",
                        HttpStatus.BAD_REQUEST,
                        null,
                        "DIVISI_DUPLICATE",
                        request
                );
            }

            if (divisiRepository.existsByKodeDivisi(divisiDTO.getKodeDivisi())) {
                return new ResponseHandler().generateModelAttribut(
                        "Kode divisi sudah ada",
                        HttpStatus.BAD_REQUEST,
                        null,
                        "KODE_DUPLICATE",
                        request
                );
            }

            Divisi divisi = modelMapper.map(divisiDTO, Divisi.class);
            divisi.setCreatedDate(new Date());
            divisi.setCreatedBy(getUserIdFromRequest(request));
            divisiRepository.save(divisi);

            return new ResponseHandler().generateModelAttribut(
                    "Data berhasil disimpan",
                    HttpStatus.CREATED,
                    transformToDTO.transformObjectDataSave(objectMapper, divisi.getIdDivisi(), mapColumnSearch),
                    null,
                    request
            );
        } catch (Exception e) {
            strExceptionArr[1] = "createDivisi(DivisiValidasiDTO divisiDTO, WebRequest request)";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(
                    "Data gagal disimpan",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE05001",
                    request
            );
        }
    }

    public Map<String, Object> updateDivisi(Long id, DivisiValidasiDTO divisiDTO, WebRequest request) {
        try {
            Divisi divisi = divisiRepository.findById(id).orElseThrow(() -> null);
            if (divisi == null) {
                return new ResponseHandler().generateModelAttribut(
                        "Data divisi tidak ditemukan",
                        HttpStatus.NOT_FOUND,
                        null,
                        "FV05002",
                        request
                );
            }

            divisi.setNamaDivisi(divisiDTO.getNamaDivisi());
            divisi.setKodeDivisi(divisiDTO.getKodeDivisi());
            divisi.setDeskripsiDivisi(divisiDTO.getDeskripsiDivisi());
            divisi.setModifiedDate(new Date());
            divisi.setModifiedBy(getUserIdFromRequest(request));

            divisiRepository.save(divisi);

            return new ResponseHandler().generateModelAttribut(
                    "Data berhasil diperbarui",
                    HttpStatus.OK,
                    null,
                    null,
                    request
            );
        } catch (Exception e) {
            strExceptionArr[1] = "updateDivisi(Long id, DivisiValidasiDTO divisiDTO, WebRequest request)";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(
                    "Data gagal diperbarui",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE05002",
                    request
            );
        }
    }

    public Map<String, Object> deleteDivisi(Long id, WebRequest request) {
        try {
            Divisi divisi = divisiRepository.findById(id).orElseThrow(() -> null);
            if (divisi == null) {
                return new ResponseHandler().generateModelAttribut(
                        "Data divisi tidak ditemukan",
                        HttpStatus.NOT_FOUND,
                        null,
                        "FV05003",
                        request
                );
            }

            divisi.setIsDelete((byte) 0);
            divisi.setModifiedDate(new Date());
            divisi.setModifiedBy(getUserIdFromRequest(request));

            divisiRepository.save(divisi);

            return new ResponseHandler().generateModelAttribut(
                    "Data berhasil dihapus",
                    HttpStatus.OK,
                    null,
                    null,
                    request
            );
        } catch (Exception e) {
            strExceptionArr[1] = "deleteDivisi(Long id, WebRequest request)";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(
                    "Data gagal dihapus",
                    HttpStatus.BAD_REQUEST,
                    null,
                    "FE05003",
                    request
            );
        }
    }

    public Map<String, Object> getDivisiById(Long id, WebRequest request) {
        try {
            Divisi divisi = divisiRepository.findById(id).orElseThrow(() -> null);
            if (divisi == null) {
                return new ResponseHandler().generateModelAttribut(
                        "Data divisi tidak ditemukan",
                        HttpStatus.NOT_FOUND,
                        null,
                        "FV05004",
                        request
                );
            }

            DivisiValidasiDTO divisiDTO = modelMapper.map(divisi, DivisiValidasiDTO.class);

            return new ResponseHandler().generateModelAttribut(
                    "Data berhasil ditemukan",
                    HttpStatus.OK,
                    divisiDTO,
                    null,
                    request
            );
        } catch (Exception e) {
            strExceptionArr[1] = "getDivisiById(Long id, WebRequest request)";
            LoggingFile.exceptionStringz(strExceptionArr, e, OtherConfig.getFlagLogging());
            return new ResponseHandler().generateModelAttribut(
                    "Terjadi kesalahan",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "FE05004",
                    request
            );
        }
    }

    private Integer getUserIdFromRequest(WebRequest request) {
        Object strUserIdz = request.getAttribute("USR_ID", 1);
        return strUserIdz != null ? Integer.parseInt(strUserIdz.toString()) : null;
    }

    public Map<String, Object> findAllDivisi(Pageable pageable, WebRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Page<Divisi> divisiPage = divisiRepository.findAll(pageable);
            if (divisiPage.isEmpty()) {
                response.put("message", "Data kosong");
                response.put("success", false);
                return response;
            }
            response.put("data", divisiPage.getContent());
            response.put("success", true);
            return response;
        } catch (Exception e) {
            response.put("message", "Terjadi kesalahan");
            response.put("success", false);
            return response;
        }
    }

}

