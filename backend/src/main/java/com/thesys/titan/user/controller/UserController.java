package com.thesys.titan.user.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.BindingResult;

import com.thesys.titan.common.valid.ValidHandle; // Ensure this is the correct package path
import com.thesys.titan.common.valid.ValidGroups; // Ensure this is the correct package path
import com.thesys.titan.constants.MessageConstants;
import jakarta.servlet.http.HttpServletRequest;
import com.thesys.titan.sample.dto.SampleDto;
import com.thesys.titan.user.dto.UserDto; // Ensure this is the correct package path for UserDto
import com.thesys.titan.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "User", description = "User 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    private final ValidHandle validHandle;

    private final MessageSourceAccessor messageSource;

    /**
     * 사용자 조회 서비스
     *
     * @param ignoredRequest HttpServletRequest
     * @return Map<String,Object>
     */
    @Operation(summary = "샘플 조회 서비스 without param", description = "파라메터 없이 조회하는 서배스")
    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "list", array = @ArraySchema(schema = @Schema(implementation = SampleDto.class))),
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "샘플서비스에 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(HttpServletRequest ignoredRequest) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_SUCCESS));
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_FAIL));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 사용자 등록 서비스
     *
     * @param dto           UserDto
     * @param bindingResult BindingResult
     * @return Map<String,Object>
     */
    @Operation(summary = "샘플 등록 서비스", description = "사용자 등록 서비스")
    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "샘플등록을 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "invalid_fields", array = @ArraySchema(schema = @Schema(type = "object", properties = {
                    @StringToClassMapItem(key = "errFld", value = Object.class),
                    @StringToClassMapItem(key = "errMsg", value = Object.class)
            }))),
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @PostMapping("/createSample")
    public ResponseEntity<Map<String, Object>> createSample(
            @RequestBody @Validated(ValidGroups.CreateValid.class) UserDto dto,
            BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> invalidFields = null;
        if (bindingResult.hasErrors()) {
            log.error("createSample NOT VALID PARAMETERS");
            invalidFields = validHandle.validateHandling(bindingResult);

        }

        if (!invalidFields.isEmpty()) {
            result.put("invalid_fields", invalidFields);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        try {
            int cnt = userService.createUser(dto);
            if (cnt > 0) {
                result.put("msg", messageSource.getMessage(MessageConstants.MSG_SUCCESS));
            } else {
                result.put("msg", messageSource.getMessage(MessageConstants.MSG_FAIL));
            }
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_FAIL));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

    }

}
