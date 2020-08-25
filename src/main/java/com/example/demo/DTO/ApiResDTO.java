package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class ApiResDTO {
    private int errorcode;
    private String message;
    private Object body;

    public static ApiResDTO success(Object t, String message) {
        return new ApiResDTO(0,message,t);
    }

    public static ApiResDTO fail(Object t, String message) {
        return new ApiResDTO(1,message,t);
    }

}
