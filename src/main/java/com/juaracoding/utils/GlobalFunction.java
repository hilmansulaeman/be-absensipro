package com.juaracoding.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.config.OtherConfig;
import com.juaracoding.handler.ResponseHandler;
import com.juaracoding.security.Crypto;
import com.juaracoding.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class GlobalFunction {

    public static ResponseEntity<Object> customDataDitemukan(String message, Object object, HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                message,
                HttpStatus.OK,
                object,
                null,
                request
        );
    }

    public static ResponseEntity<Object> dataGagalDisimpan(String errorCode, HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                "DATA GAGAL DISIMPAN",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,
                errorCode,
                request
        );
    }

    public static ResponseEntity<Object> dataGagalDihapus(String errorCode, HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                "DATA GAGAL DIHAPUS",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,
                errorCode,
                request
        );
    }

    public static ResponseEntity<Object> contentTypeWorkBook(String errorCode, HttpServletRequest request)
    {
        return new ResponseHandler().generateResponse("Ekstensi File harus .xlsx / Format Workbook bukan dari template yang diunduh dari Platform",
                HttpStatus.BAD_REQUEST,
                null,
                errorCode, request);
    }

    public static ResponseEntity<Object> dataWorkBookKosong(String errorCode, HttpServletRequest request)
    {
        return new ResponseHandler().generateResponse("Data Di File Excel Kosong / Tidak ada data yang dapat diproses",
                HttpStatus.BAD_REQUEST,
                null,
                errorCode, request);
    }



    public static ResponseEntity<Object> dataGagalDiubah(String errorCode, HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                "DATA GAGAL DIUBAH",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,
                errorCode,
                request
        );
    }

    public static ResponseEntity<Object> tidakDapatDiproses(String errorCode, HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                "PERMINTAAN TIDAK DAPAT DIPROSES",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,
                errorCode,
                request
        );
    }

    public static ResponseEntity<Object> dataTidakDitemukan(HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                "DATA TIDAK DITEMUKAN",
                HttpStatus.BAD_REQUEST,
                null,
                "X-01-002",
                request
        );
    }

    /**
     * Digunakan untuk functional download file dll
     * yang bersifat tidak mengembalikan data response
     */
    public static void manualResponse(HttpServletResponse response, ResponseEntity<Object> respObject){
        try {
            response.getWriter().write(convertObjectToJson(respObject.getBody()));
            response.setStatus(respObject.getStatusCodeValue());
        } catch (IOException e) {

        }
    }

    public static ResponseEntity<Object> dataByIdDitemukan(Object object, HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                "DATA DITEMUKAN",
                HttpStatus.OK,
                object,
                null,
                request
        );
    }

    public static ResponseEntity<Object> validasiGagal(String message, String errorCode, HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                message,
                HttpStatus.BAD_REQUEST,
                null,
                errorCode,
                request
        );
    }

    public static ResponseEntity<Object> dataBerhasilDisimpan(HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                "DATA BERHASIL DISIMPAN",
                HttpStatus.CREATED,
                null,
                null,
                request
        );
    }

    public static ResponseEntity<Object> dataBerhasilDiubah(HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                "DATA BERHASIL DIUBAH",
                HttpStatus.OK,
                null,
                null,
                request
        );
    }

    public static ResponseEntity<Object> dataBerhasilDihapus(HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                "DATA BERHASIL DIHAPUS",
                HttpStatus.OK,
                null,
                null,
                request
        );
    }

    public static ResponseEntity<Object> customReponse(String errorCode,String message,HttpServletRequest request){
        return new ResponseHandler().generateResponse(
                message,
                HttpStatus.BAD_REQUEST,
                null,
                errorCode,
                request
        );
    }

    /**
     * Mengconvert Object java ke JSON
     */
    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    public static Map<String,Object> convertClassToObject(Object object){
        Map<String, Object> map = new LinkedHashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
                LoggingFile.exceptionStringz("GlobalFunction","convertClassToObject",e, OtherConfig.getFlagLogging());
            }
        }
        return map;
    }

    public static Map<String,Object> convertClassToObject(Object object,String strNull){
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field: fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(object)==null?"-":field.get(object));
            } catch (IllegalAccessException e) {
                LoggingFile.exceptionStringz("GlobalFunction","convertClassToObject",e, OtherConfig.getFlagLogging());
            }
        }
        return map;
    }
    public static String camelToStandar(String str)
    {
        StringBuilder sb = new StringBuilder();
        char c = str.charAt(0);
        sb.append(Character.toLowerCase(c));
        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                sb.append(' ').append(Character.toLowerCase(ch));
            }
            else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    /** saya hardcode pattern nya karena kebutuhan report aja , jadi tidak perlu memasukkan pattern saat menggunakan functional ini */
    public static String formatingDateDDMMMMYYYY(){
        /** mengambil current time saat ini **/
        return new SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(new Date());
    }
    public static String formatingDateDDMMMMYYYY(LocalDate localDate){
        /** mengambil current time saat ini **/
        return new SimpleDateFormat("dd MMMM yyyy").
                format(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    public static String formatingDateDDMMMMYYYY(Date date){
        /** mengambil current time saat ini **/
        return new SimpleDateFormat("dd MMMM yyyy").format(date);
    }

    /** untuk mekanisme casting date beraneka ragam, tidak seperti functional yang dioverload sebelumnya
     * jadi parameter pattern harus diikutkan
     * y – Year (yyyy->1996; yy-> 96)
     * M – Month in year (MMMM->July; MMM->Jul; MM->07)
     * d – Day in month (dd--> 1-31)
     * E – Day name in week (EEE ---> Friday, EE --> Fri)
     * a – AM/PM marker (AM, PM)
     * H – Hour in day (HH--> 0-23)
     * h – Hour in AM/PM (hh--> 1-12)
     * m – Minute in hour (mm---> 0-60)
     * s – Second in minute (ss-->0-60)
     * S – millsecond (SSS--> 138)
     * */
    public static String formatingDateDDMMMMYYYY(String strDate,String pattern) throws ParseException {
        return new SimpleDateFormat("dd MMMM yyyy").
                format(new SimpleDateFormat(pattern, Locale.ENGLISH).parse(strDate));
    }
    public static String formatingDate(String newPattern){
        return new SimpleDateFormat(newPattern).format(new Date());
    }
    public static String formatingDate(LocalDate localDate,String newPattern){
        return new SimpleDateFormat(newPattern).
                format(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    public static String formatingDate(Date date,String newPattern){
        return new SimpleDateFormat(newPattern).format(date);
    }
    public static String formatingDate(String strDate,String currentPattern,String newPattern) throws ParseException {
        return new SimpleDateFormat(newPattern).
                format(new SimpleDateFormat(currentPattern, Locale.ENGLISH).parse(strDate));
    }

    public static Map<String,Object> claimsTokenBody(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        return new JwtUtility().mappingBodyToken(Crypto.performDecrypt(token),new HashMap<>());
    }

    public static String extractMessage(ResponseEntity<Object> response) {
        if (response.getBody() instanceof Map) {
            Map<String, Object> body = (Map<String, Object>) response.getBody();
            String message = (String) body.get("message");
            return message != null ? message.toLowerCase() : null; // Mengembalikan dalam huruf kecil
        }
        return null;
    }




}