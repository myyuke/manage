package com.example.demo.model;

public class Success {
    private Integer code;
    private String message;

    public Success(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "DeleteSuccess{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
