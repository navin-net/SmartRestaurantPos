package com.example.smartrestaurant.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {

    private String code;
    private String message;
    private String messageKh;
    private T data;

    public static <T> BaseResponse<T> success(T data, String message, String messageKh) {
        return success(HttpStatus.OK, data, message, messageKh);
    }

    public static <T> BaseResponse<T> success(HttpStatus status, T data, String message, String messageKh) {
        return new BaseResponse<>(String.valueOf(status.value()), message, messageKh, data);
    }

    public static <T> BaseResponse<T> error(String code, String message, String messageKh) {
        return new BaseResponse<>(code, message, messageKh, null);
    }
}