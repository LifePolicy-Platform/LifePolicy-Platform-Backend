package maventest.common.exception;

import org.springframework.http.HttpStatus;

public class ErrorInputException extends ApiException {

    public ErrorInputException(String code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST);
    }
}