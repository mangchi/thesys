package com.thesys.titan.common.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FieldErrors {

    private List<CustomFieldError> errors;

    @Getter
    @Setter
    @Builder
    public static class CustomFieldError {
        private String field;
        private String message;

        public CustomFieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}