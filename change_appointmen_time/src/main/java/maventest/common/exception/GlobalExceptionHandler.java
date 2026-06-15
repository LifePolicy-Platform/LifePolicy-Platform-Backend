package maventest.common.exception;

import maventest.common.ApiResponse;
import maventest.common.ResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiResponse.fail(e.getHttpStatus().value(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors().stream()
                .map(err -> err.getDefaultMessage())
                .findFirst()
                .orElse("參數驗證失敗");
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(ResultCode.PARAM_ERROR.getCode(), msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        return ResponseEntity.internalServerError()
                .body(ApiResponse.fail(ResultCode.SERVER_ERROR.getCode(), e.getMessage()));
    }
}
