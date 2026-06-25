package maventest.common.exception;

import maventest.code.ApiCode;
import maventest.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiResponse.fail(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors().stream()
                .map(err -> err.getDefaultMessage())
                .findFirst()
                .orElse(ApiCode.INPUT_INVALID.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(ApiCode.INPUT_INVALID.getCode(), msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        return ResponseEntity.internalServerError()
                .body(ApiResponse.fail(ApiCode.SYSTEM_ERROR.getCode(), ApiCode.SYSTEM_ERROR.getMessage()));
    }
}
