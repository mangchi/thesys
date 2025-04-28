package com.thesys.titan.sample.controller;

import com.thesys.titan.common.valid.ValidGroups;
import com.thesys.titan.common.valid.ValidHandle;
import com.thesys.titan.constants.MessageConstants;
import com.thesys.titan.sample.dto.SampleDto;
import com.thesys.titan.sample.service.SampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Slf4j
@Tag(name = "Sample", description = "Sample 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/sample")
public class SampleController {

    private final SampleService sampleService;

    private final ValidHandle validHandle;

    private final MessageSourceAccessor messageSource;

    private static final String USE_YN = "useYn"; // 상수 정의

    /**
     * 샘플 조회 서비스
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
     * 샘플 조회 서비스 by param
     *
     * @param params Map<String,Object>
     * @return Map<String,Object>
     */
    @Operation(summary = "샘플 조회 서비스 by param", description = "파라메터를 받아서 조회하는 서배스")
    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "list", array = @ArraySchema(schema = @Schema(implementation = SampleDto.class))),
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "샘플서비스에 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @PostMapping("/listByParam")
    @Parameter(name = "description", description = "sample  설명, query string")
    @Parameter(name = "useYn", description = "사용 여부, query string")
    public ResponseEntity<Map<String, Object>> listByParam(@RequestParam Map<String, Object> params) {

        return processListRequest(params);
    }

    /**
     * 샘플 조회 서비스 by request body
     *
     * @param params Map<String,Object>
     * @return Map<String,Object>
     */
    @Operation(summary = "샘플 조회 서비스 by request body", description = "파라메터를 받아서 조회하는 서배스")
    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "list", array = @ArraySchema(schema = @Schema(implementation = SampleDto.class))),
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "샘플서비스에 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @PostMapping("/listByBody")
    public ResponseEntity<Map<String, Object>> listByBody(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = SampleDto.class))) @RequestBody Map<String, Object> params) {

        return processListRequest(params);
    }

    private ResponseEntity<Map<String, Object>> processListRequest(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("list", sampleService.getSampleListWhere(params));
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_SUCCESS));
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_FAIL));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 샘플 조회 서비스 by pathvariable
     *
     * @param samId Integer
     * @return Customize Toolbar...
     */

    @Operation(summary = "샘플 조회 서비스 by pathvariable", description = "pathvariable을 받아서 조회하는 서배스")
    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "info", schema = @Schema(implementation = SampleDto.class)),
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "샘플서비스에 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @GetMapping(value = "/info/{samId}")
    public ResponseEntity<Map<String, Object>> infoByPath(
            @Parameter(description = "sample 아이디", example = "7") @PathVariable(name = "samId") Integer samId) {
        Map<String, Object> params = new HashMap<>();
        params.put("samId", samId);
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("info", sampleService.getSample(params));
            result.put("info1", sampleService.getSample(params));
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_SUCCESS));
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_FAIL));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 샘플 조회 서비스 by querystring
     *
     * @param samId Integer
     * @param useYn Integer
     * @return Map<String, Object>
     */
    @Operation(summary = "샘플 조회 서비스 by querystring", description = "querystring을 받아서 조회하는 서배스")
    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "info", array = @ArraySchema(schema = @Schema(implementation = SampleDto.class))),
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "샘플서비스에 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @GetMapping(value = "/infoByQuery")
    public ResponseEntity<Map<String, Object>> infoByQuery(
            @Parameter(description = "Sample Id ex)3,4") @RequestParam(name = "samId", required = false, defaultValue = "3") Integer samId,
            @Parameter(description = "사용여부 ex)0, 1") @RequestParam(name = "useYn", required = false, defaultValue = "0") Integer useYn) {
        Map<String, Object> params = new HashMap<>();
        params.put("samId", samId);
        params.put(USE_YN, useYn);
        log.debug("params:{}", params);
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("info", sampleService.getSample(params));
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_SUCCESS));
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_FAIL));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 샘플Dto 반환하는 서비스
     *
     * @param params Map<String,Object>
     * @return Map<String,Object>
     */
    @Operation(summary = "샘플Dto 서비스", description = "샘플Dto 반환하는 서비스")
    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "info", schema = @Schema(type = "info", description = "sample info", implementation = SampleDto.class)),
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "샘플서비스에 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @PostMapping("/info")
    public ResponseEntity<Map<String, Object>> info(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = SampleDto.class))) @RequestBody Map<String, Object> params) {

        Map<String, Object> result = new HashMap<>();
        try {
            result.put("info", sampleService.getSample(params));
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_SUCCESS));
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_FAIL));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 샘플Map 반환하는 서비스
     *
     * @param params Map<String,Object>
     * @return Map<String,Object>
     */
    @Operation(summary = "샘플Map 서비스", description = "샘플Map 반환하는 서비스")
    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "info", schema = @Schema(type = "info", description = "sample info", implementation = SampleDto.class)),
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "샘플서비스에 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @PostMapping("/infoMap")
    public ResponseEntity<Map<String, Object>> infoMap(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = SampleDto.class))) @RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("info", sampleService.getSampleMap(params));
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_SUCCESS));
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_FAIL));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    /**
     * 샘플 등록 서비스
     *
     * @param dto           SampleDto
     * @param bindingResult BindingResult
     * @return Map<String,Object>
     */
    @Operation(summary = "샘플 등록 서비스", description = "샘플 등록 서비스")
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
            @RequestBody @Validated(ValidGroups.CreateValid.class) SampleDto dto,
            BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> invalidFields = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            log.error("createSample NOT VALID PARAMETERS");
            invalidFields.addAll(validHandle.validateHandling(bindingResult));
        }
        if (dto.getUseYn() > 1) {
            Map<String, Object> invalidField = new HashMap<>();
            invalidField.put("errFld", USE_YN);
            invalidField.put("errMsg", messageSource.getMessage("error.useYn.invalid"));
            invalidFields.add(invalidField);
        }
        if (!invalidFields.isEmpty()) {
            result.put("invalid_fields", invalidFields);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        try {
            int cnt = sampleService.createSample(dto);
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

    /**
     * 샘플리스트 등록 서비스 by Map
     *
     * @param params List<Map<String,Object>
     * @return Map<String,Object>
     */
    @Operation(summary = "샘플리스트 등록 서비스 by Map ", description = "샘플리스트 등록 서비스 by Map")

    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "샘플등록을 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "invalid_fields", array = @ArraySchema(schema = @Schema(type = "object", properties = {
                    @StringToClassMapItem(key = "errFld", value = Object.class),
                    @StringToClassMapItem(key = "errMsg", value = Object.class)
            }))),
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @PostMapping("/createSampleMap")
    public ResponseEntity<Map<String, Object>> createSampleMap(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = SampleDto.class))) @RequestBody Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        try {
            int cnt = sampleService.createSampleMap(params);
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

    /**
     * 샘플리스트 등록 서비스 by List<Map>
     *
     * @param params List<Map<String,Object>
     * @return Map<String,Object>
     */
    @Operation(summary = "샘플리스트 등록 서비스 by List<Map> ", description = "샘플리스트 등록 서비스 by List<Map>")
    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "샘플등록을 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "invalid_fields", array = @ArraySchema(schema = @Schema(type = "object", properties = {
                    @StringToClassMapItem(key = "errFld", value = Object.class),
                    @StringToClassMapItem(key = "errMsg", value = Object.class)
            }))),
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @PostMapping("/createSampleMaps")
    public ResponseEntity<Map<String, Object>> createSampleMaps(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = SampleDto.class))) @RequestBody List<Map<String, Object>> params) {
        Map<String, Object> result = new HashMap<>();

        try {
            int cnt = sampleService.createSampleMaps(params);
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

    /**
     * 샘플 list 등록 서비스
     *
     * @param list          List<SampleDto>
     * @param bindingResult BindingResultBindingResult
     * @return Map<String,Object>
     */
    @Operation(summary = "샘플 list 등록 서비스", description = "샘플 list 등록 서비스")
    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "샘플 list 등록을 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "invalid_fields", array = @ArraySchema(schema = @Schema(type = "object", properties = {
                    @StringToClassMapItem(key = "errIdx", value = Object.class),
                    @StringToClassMapItem(key = "errFld", value = Object.class),
                    @StringToClassMapItem(key = "errMsg", value = Object.class)
            }))),
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @PostMapping("/createSamples")
    public ResponseEntity<Map<String, Object>> createSamples(
            @RequestBody @Validated(ValidGroups.CreateValid.class) List<SampleDto> list,
            BindingResult bindingResult) {
        Map<String, Object> result = new HashMap<>();
        for (SampleDto dto : list) {
            log.debug("dto: {}", dto);
        }

        if (bindingResult.hasErrors()) {
            log.error("createSamples NOT VALID PARAMETERS");

        }

        try {
            int cnt = sampleService.createSamples(list);
            if (cnt > 0) {
                result.put("msg", messageSource.getMessage(MessageConstants.MSG_FAIL));
            }
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_FAIL));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

    }

    /**
     * 샘플 수정 서비스
     *
     * @param dto           SampleDto
     * @param bindingResult BindingResult
     * @return Map<String,Object>
     */
    @Operation(summary = "샘플 수정 서비스", description = "샘플 수정 서비스")
    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "샘플수정을 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "invalid_fields", array = @ArraySchema(schema = @Schema(type = "object", properties = {
                    @StringToClassMapItem(key = "errFld", value = Object.class),
                    @StringToClassMapItem(key = "errMsg", value = Object.class)
            }))),
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @PatchMapping("/updateSample")
    public ResponseEntity<Map<String, Object>> updateSample(
            @RequestBody @Validated(ValidGroups.UpdateValid.class) SampleDto dto,
            BindingResult bindingResult) {

        Map<String, Object> result = new HashMap<>();
        if (bindingResult.hasErrors()) {
            log.error("updateSample NOT VALID PARAMETERS");
            List<Map<String, Object>> invalidFields = validHandle.validateHandling(bindingResult);
            result.put("invalidFields", invalidFields);
            return ResponseEntity.ok().body(result);

        }
        try {
            int cnt = sampleService.updateSample(dto);
            if (cnt > 0) {
                result.put("msg", messageSource.getMessage(MessageConstants.MSG_SUCCESS));
            } else {
                result.put("msg", messageSource.getMessage("error.updateNotFound"));
            }
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_FAIL));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

    }

    /**
     * 샘플 삭제 서비스
     *
     * @param dto           SampleDto
     * @param bindingResult BindingResult
     * @return Map<String,Object>
     */
    @Operation(summary = "샘플 삭제 서비스", description = "샘플 삭제 서비스")
    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "샘플삭제 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "invalid_fields", array = @ArraySchema(schema = @Schema(type = "object", properties = {
                    @StringToClassMapItem(key = "errFld", value = Object.class),
                    @StringToClassMapItem(key = "errMsg", value = Object.class)
            }))),
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @DeleteMapping("/deleteSample")
    public ResponseEntity<Map<String, Object>> deleteSample(
            @RequestBody @Validated(ValidGroups.DeleteValid.class) SampleDto dto,
            BindingResult bindingResult) {

        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> invalidFields = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            log.error("deleteSample NOT VALID PARAMETERS");
            invalidFields = validHandle.validateHandling(bindingResult);
        }
        if (dto.getUseYn() > 0) {
            Map<String, Object> invalidField = new HashMap<>();
            invalidField.put("errFld", USE_YN);
            invalidField.put("errMsg", messageSource.getMessage("error.useYn.invalid"));
            invalidFields.add(invalidField);
        }
        if (!invalidFields.isEmpty()) {
            result.put("invalidFields", invalidFields);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        try {
            int cnt = sampleService.deleteSample(dto);
            if (cnt > 0) {
                result.put("msg", messageSource.getMessage(MessageConstants.MSG_SUCCESS));
            } else {
                result.put("msg", messageSource.getMessage("error.deleteNotFound"));
            }
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_FAIL));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

    }

    @Operation(summary = "서버 시간 서비스", description = "서버 시간 서비스")
    @ApiResponse(responseCode = "200", content = @Content(schemaProperties = {
            @SchemaProperty(name = "msg", schema = @Schema(type = "string", description = "메시지")) }))
    @ApiResponse(responseCode = "400", description = "서버 시간 실패하였습니다.", content = @Content(schemaProperties = {
            @SchemaProperty(name = "time", schema = @Schema(type = "string", description = "메시지")) }))
    @DeleteMapping("/serverTime")
    public ResponseEntity<Map<String, Object>> serverTime(HttpServletRequest request) {

        Map<String, Object> result = new HashMap<>();
        try {
            ZoneId zid = ZoneId.of("Asia/Seoul");
            ZonedDateTime seoulTime = ZonedDateTime.now().withZoneSameInstant(zid);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            result.put("time", seoulTime.format(formatter));
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.put("msg", messageSource.getMessage(MessageConstants.MSG_FAIL));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

}