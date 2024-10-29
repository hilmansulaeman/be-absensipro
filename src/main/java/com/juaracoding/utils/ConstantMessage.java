package com.juaracoding.utils;

public class ConstantMessage {

    /*
    Memperbolehkan nilai numerik dari 0 hingga 9.
    Memperbolehkan Huruf besar dan huruf kecil dari a sampai z.
    Yang diperbolehkan hanya garis bawah “_”, tanda hubung “-“ dan titik “.”
    Titik tidak diperbolehkan di awal dan akhir local part (sebelum tanda @).
    Titik berurutan tidak diperbolehkan.
    Local part, maksimal 64 karakter.
     */
//    public final static String REGEX_EMAIL_STRICT = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$";

    /*CONTENT TYPE*/
    public final static String CONTENT_TYPE_CSV = "text/csv";
    public final static String CONTENT_TYPE_XLS = "application/vnd.ms-excel";
    public final static String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /*REGEX*/
    public final static String REGEX_PHONE = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$";

    /*
    * Tidak memperbolehkan tanda | (pipa) dan ' (petik 1)
    */
    public final static String REGEX_EMAIL_STANDARD_RFC5322  = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public final static String REGEX_DATE_YYYYMMDD  = "^([0-9][0-9])?[0-9][0-9]-(0[0-9]||1[0-2])-([0-2][0-9]||3[0-1])$";

    public final static String REGEX_DATE_DDMMYYYY  = "^([0-2][0-9]||3[0-1])-(0[0-9]||1[0-2])-([0-9][0-9])?[0-9][0-9]$";

    /*Global*/

    public final static String SUCCESS_SEND_EMAIL = "SILAHKAN CEK EMAIL YANG TELAH ANDA DAFTARKAN";

    /*
        AUTH
     */

    public final static String ERROR_TOKEN_INVALID = "TOKEN TIDAK SESUAI";
    public final static String SUCCESS_LOGIN = "LOGIN BERHASIL";
    public final static String ERROR_EMAIL_ISEXIST = "REGISTRASI GAGAL! EMAIL SUDAH TERDAFTAR";
    public final static String ERROR_USERNAME_ISEXIST = "REGISTRASI GAGAL! USERNAME SUDAH TERDAFTAR";
    public final static String ERROR_EMAIL_MAX_MIN_LENGTH = "PANJANG EMAIL MIN 15 MAKS 50 !!";
    public final static String ERROR_EMAIL_IS_NULL = "EMAIL TIDAK BOLEH NULL!!";
    public final static String ERROR_EMAIL_IS_EMPTY = "EMAIL TIDAK BOLEH EMPTY!!";
    public final static String ERROR_LOGIN_FAILED = "USERNAME DAN PASSWORD SALAH !!";
    public final static String ERROR_NAMALENGKAP_IS_NULL = "NAMA LENGKAP TIDAK BOLEH NULL!!";
    public final static String ERROR_NAMALENGKAP_IS_EMPTY = "NAMA LENGKAP TIDAK BOLEH KOSONG!!";

    public final static String ERROR_PASSWORD_IS_SAME = "PASSWORD BARU TIDAK BOLEH SAMA DENGAN PASSWORD LAMA !!";

    public static final String ERROR_GENDER_CONFIRM_LENGTH = "ERROR GENDER";
    public static final String ERROR_MARTIALSTATUS_CONFIRM_LENGTH = "ERROR MARTIAL STATUS" ;
    public static final String ERROR_EMAIL_INVALID = "EMAIL INVALID";
    public static final Object SUCCESS_SAVE_USER = "SIMPAN USER BERHASIL" ;
}
