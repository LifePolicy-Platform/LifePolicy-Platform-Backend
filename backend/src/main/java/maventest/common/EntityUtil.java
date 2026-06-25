package maventest.common;


import org.springframework.validation.BindingResult;

import maventest.common.exception.ErrorInputException;

import java.util.stream.Collectors;

public final class EntityUtil {

    private EntityUtil() {
    }

    public static void validDto(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return;
        }

        String errorMessage = bindingResult.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        throw new ErrorInputException(ApiCode.INPUT_INVALID.getCode(), errorMessage);
    }
}