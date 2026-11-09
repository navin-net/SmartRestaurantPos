package com.example.smartrestaurant.controller;

import com.example.smartrestaurant.exception.ApiException;
import com.example.smartrestaurant.model.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<BaseResponse<Void>> handleApiException(ApiException exception) {
        return ResponseEntity.status(exception.getStatus()).body(BaseResponse.error(
                String.valueOf(exception.getStatus().value()),
                exception.getMessage(),
                exception.getMessageKh()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        var message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .sorted().distinct()
                .collect(Collectors.joining("; "));
        return new ResponseEntity<>(BaseResponse.error("400", message,
                "ទិន្នន័យដែលបានបញ្ចូលមិនត្រឹមត្រូវ"), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception, Object body, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return super.handleExceptionInternal(exception,
                BaseResponse.error(String.valueOf(status.value()),
                        status.is5xxServerError() ? "An unexpected error occurred" : "Invalid request",
                        "សំណើមិនអាចដំណើរការបាន"), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException exception, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(BaseResponse.error("415",
                "Content-Type '" + exception.getContentType()
                        + "' is not supported. Use application/json or multipart/form-data "
                        + "with POST or PUT /api/v1/products",
                "ប្រភេទឯកសារមិនគាំទ្រ សូមប្រើ application/json ឬ multipart/form-data សម្រាប់ផលិតផល"),
                headers, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(
            ConversionNotSupportedException exception, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(BaseResponse.error("400",
                "Form field '" + exception.getPropertyName()
                        + "' has the wrong type. Use field 'file' (type File) for the main picture, "
                        + "'subImageFiles' (type File) for gallery pictures, and 'image' only for a text URL",
                "ទម្រង់ទិន្នន័យមិនត្រឹមត្រូវ សូមប្រើ field 'file' សម្រាប់រូបភាព"),
                headers, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException exception, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>(BaseResponse.error("413",
                "File is too large. Maximum size is 5MB",
                "ឯកសារធំពេក អតិបរមា 5MB"),
                headers, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleUnexpectedException(Exception exception) {
        log.error("Unexpected API error", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BaseResponse.error("500",
                        "An unexpected error occurred",
                        "មានកំហុសមិនរំពឹងទុកបានកើតឡើង"));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<BaseResponse<Void>> handleDisabled(DisabledException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BaseResponse.error(
                "403",
                "This account is not allowed to log in",
                "គណនីនេះមិនត្រូវបានអនុញ្ញាតឱ្យចូលប្រើទេ"));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BaseResponse<Void>> handleBadCredentials(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponse.error(
                "401",
                "Invalid username or password",
                "ឈ្មោះអ្នកប្រើ ឬពាក្យសម្ងាត់មិនត្រឹមត្រូវ"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponse<Void>> handleAuthentication(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseResponse.error(
                "401",
                "Authentication failed",
                "ការផ្ទៀងផ្ទាត់អត្តសញ្ញាណបរាជ័យ"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponse<Void>> handleAccessDenied(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BaseResponse.error(
                "403",
                "You do not have permission to perform this action",
                "អ្នកគ្មានសិទ្ធិអនុវត្តសកម្មភាពនេះទេ"));
    }
}
