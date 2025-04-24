package com.thesys.titan.common.valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ValidHandle {
    public List<Map<String, Object>> validateHandling(BindingResult bindingResult) {
        List<Map<String, Object>> invalidFields = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            Map<String, Object> invalidField = new HashMap<>();
            invalidField.put("errFld", fieldError.getField());
            String errMsg = fieldError.getDefaultMessage();
            if (errMsg.indexOf("{0}") > -1) {
                errMsg = errMsg.replace("{0}", fieldError.getField());
            }
            invalidField.put("errMsg", errMsg);
            invalidFields.add(invalidField);
        });
        return invalidFields;
    }
}