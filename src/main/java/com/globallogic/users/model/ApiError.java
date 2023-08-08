package com.globallogic.users.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Getter
@Setter
public class ApiError {

    private Integer code;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Timestamp timestamp;
    private String detail;

    private ApiError() {
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public ApiError(Integer code, String detail) {
        this();
        this.code = code;
        this.detail = detail;
    }
}
