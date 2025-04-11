package com.example.cipherquest.exception;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException{

    private final ErrorDefine errorCode;
    private final String message;

    public CustomException(ErrorDefine errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public CustomException(ErrorDefine errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

}
