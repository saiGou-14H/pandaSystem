package com.saigou.util;

import lombok.Data;
import lombok.Getter;

@Data
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 4564124491192825748L;

    private int code;

    public CustomException() {
        super();
    }

    public CustomException(ResponseEnum data) {
        super(data.getMessage());
        this.setCode(data.getCode());
    }

    public void setCode(int code) {
        this.code = code;
    }
}

