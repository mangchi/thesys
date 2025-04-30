package com.thesys.titan.common.valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.thesys.titan.common.vo.FieldErrors;

import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Component
public class ValidHandle {
    private static MessageSource messageSource;

    // 의존성 주입 (Spring Config에서 설정)
    public static void setMessageSource(MessageSource source) {
        messageSource = source;
    }

    public List<String> getErrorMessages(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(this::formatMessage)
                .toList(); // Java 16 이상. Java 8이면 collect(Collectors.toList())

    }

    public String getFirstErrorMessage(BindingResult bindingResult) {
        FieldError error = bindingResult.getFieldError();
        return (error != null) ? formatMessage(error) : "유효성 오류가 발생했습니다.";
    }

    private String formatMessage(FieldError error) {
        String message = messageSource.getMessage(error, Locale.getDefault());
        return error.getField() + ": " + message;
    }

    public FieldErrors toFieldErrors(BindingResult bindingResult) {

        List<FieldErrors.CustomFieldError> errorList = bindingResult.getFieldErrors().stream()
                .map(e -> FieldErrors.CustomFieldError.builder()
                        .field(e.getField())
                        .message(e.getDefaultMessage())
                        .build())
                .toList(); // Java 16 이상. Java 8이면 collect(Collectors.toList())

        return FieldErrors.builder()
                .errors(errorList)
                .build();
    }
}