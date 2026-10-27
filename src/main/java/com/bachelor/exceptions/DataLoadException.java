package com.bachelor.exceptions;

/** The data source as a whole cannot be used (file missing or invalid, input closed, ...). */
public class DataLoadException extends Exception {

    public DataLoadException(String message) {
        super(message);
    }

    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}