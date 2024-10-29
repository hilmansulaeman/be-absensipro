package com.juaracoding.handler;

import java.util.regex.Pattern;

public class FormatValidation {

    // Regular expressions for basic validation
    private static final Pattern PATH_PATTERN = Pattern.compile("^[a-zA-Z0-9/_-]{1,50}$");
    private static final Pattern ENDPOINT_PATTERN = Pattern.compile("^[a-zA-Z0-9/_-]{1,30}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L} .'-]{1,25}$"); // Unicode letters and spaces

    /**
     * Validates that a string is not null and within the specified length.
     *
     * @param value  the string to validate
     * @param maxLength maximum allowed length
     * @param fieldName name of the field for error message
     * @return true if valid, throws exception if not
     */
    public static boolean validateStringLength(String value, int maxLength, String fieldName) {
        if (value == null || value.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " must not exceed " + maxLength + " characters.");
        }
        return true;
    }

    /**
     * Validates a `pathMenu` format.
     *
     * @param pathMenu the pathMenu string to validate
     * @return true if valid, throws exception if not
     */
    public static boolean validatePathMenu(String pathMenu) {
        if (!PATH_PATTERN.matcher(pathMenu).matches()) {
            throw new IllegalArgumentException("PathMenu format is invalid. Allowed characters: a-z, A-Z, 0-9, /, _, -");
        }
        return true;
    }

    /**
     * Validates an `endPoint` format.
     *
     * @param endPoint the endPoint string to validate
     * @return true if valid, throws exception if not
     */
    public static boolean validateEndPoint(String endPoint) {
        if (!ENDPOINT_PATTERN.matcher(endPoint).matches()) {
            throw new IllegalArgumentException("EndPoint format is invalid. Allowed characters: a-z, A-Z, 0-9, /, _, -");
        }
        return true;
    }

    /**
     * Validates a `namaMenu` or `namaAkses` format.
     *
     * @param name the name string to validate
     * @param fieldName name of the field for error message
     * @return true if valid, throws exception if not
     */
    public static boolean validateName(String name, String fieldName) {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(fieldName + " contains invalid characters.");
        }
        return true;
    }

    /**
     * Validates if a field is non-null.
     *
     * @param value the object to validate
     * @param fieldName the name of the field for error message
     * @return true if valid, throws exception if not
     */
    public static boolean validateNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null.");
        }
        return true;
    }

    /**
     * Validates `isDelete` as either 0 or 1.
     *
     * @param isDelete the Byte value of isDelete
     * @return true if valid, throws exception if not
     */
    public static boolean validateIsDelete(Byte isDelete) {
        if (isDelete == null || (isDelete != 0 && isDelete != 1)) {
            throw new IllegalArgumentException("IsDelete must be either 0 or 1.");
        }
        return true;
    }
}
