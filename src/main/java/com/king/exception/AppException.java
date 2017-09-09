package com.king.exception;

/**
 *
 * An exception which should be used for validation and similar purposes.
 * As a result of throwing this kind of exception, the user will be informed by the message.
 *
 * Created by moien on 09/08/17.
 */
public class AppException extends RuntimeException {

    public AppException(String message) {
        super(message);
    }
}
