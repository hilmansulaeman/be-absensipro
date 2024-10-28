package com.juaracoding.core;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IService<T> {
    public ResponseEntity<Object> save(T t, HttpServletRequest request);//001-010
    public ResponseEntity<Object> update(Long id,T t, HttpServletRequest request);//011-020
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request);//021-030
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request);//031-040
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request);//041-050
    public ResponseEntity<Object> findByParam(Pageable pageable,String columnName, String value, HttpServletRequest request);//051-060
//    public ResponseEntity<Object> uploadDataExcel(MultipartFile multipartFile, HttpServletRequest request);//061-070
//    public void downloadReportExcel(String filterBy, String value, HttpServletRequest request, HttpServletResponse response);//071-080
}