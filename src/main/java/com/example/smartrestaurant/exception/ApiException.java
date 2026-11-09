package com.example.smartrestaurant.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

  private final HttpStatus status;
  private final String messageKh;

  public ApiException(HttpStatus status, String message, String messageKh) {
    super(message);
    this.status = status;
    this.messageKh = messageKh;
  }

  public static ApiException notFound(String resource, String resourceKh, Object id) {
    return new ApiException(HttpStatus.NOT_FOUND,
            resource + " not found with id: " + id,
            "រកមិនឃើញ" + resourceKh);
  }

  public static ApiException duplicate(String message, String messageKh) {
    return new ApiException(HttpStatus.CONFLICT, message, messageKh);
  }

  public static ApiException badRequest(String message, String messageKh) {
    return new ApiException(HttpStatus.BAD_REQUEST, message, messageKh);
  }

  public static ApiException forbidden(String message, String messageKh) {
    return new ApiException(HttpStatus.FORBIDDEN, message, messageKh);
  }
}