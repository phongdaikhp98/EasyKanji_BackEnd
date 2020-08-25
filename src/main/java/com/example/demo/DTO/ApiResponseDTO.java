package com.example.demo.DTO;

public class ApiResponseDTO {
    private int errorcode;
    private String message;
    private Object body;
    public static final String SUCCESSS_MESSAGE = "SUCCESS";
    public static final String FAIL_MESSAGE = "FAIL";

    public ApiResponseDTO(int errorcode, String message, Object body) {
        this.errorcode = errorcode;
        this.message = message;
        this.body = body;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public static ApiResponseDTO success(Object t, String message) {
        return new ApiResponseDTO(0,message,t);
    }

    public static ApiResponseDTO fail(Object t, String message) {
        return new ApiResponseDTO(1,message,t);
    }
}
