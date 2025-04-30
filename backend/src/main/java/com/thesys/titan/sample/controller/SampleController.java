package com.thesys.titan.sample.controller;

import com.thesys.titan.common.dto.CommonResponse;
import com.thesys.titan.common.valid.ValidGroups;
import com.thesys.titan.common.vo.FieldErrors;
import com.thesys.titan.sample.dto.SampleRequest;
import com.thesys.titan.sample.dto.SampleResponse;
import com.thesys.titan.sample.service.SampleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.thesys.titan.common.valid.ValidHandle;

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

    @PostMapping
    @Operation(summary = "샘플 등록", description = "새로운 샘플 데이터를 생성합니다.")
    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = SampleResponse.class)))
    @ApiResponse(responseCode = "400", description = "잘못된 요청")
    @ApiResponse(responseCode = "500", description = "서버 오류")
    public ResponseEntity<CommonResponse<SampleResponse>> create(
            @Validated(ValidGroups.CreateValid.class) @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "샘플 생성 요청") SampleRequest request,
            BindingResult bindingResult) {
        // Validate the request
        FieldErrors invalidFields = validHandle.toFieldErrors(bindingResult);
        if (invalidFields.getErrors() != null && !invalidFields.getErrors().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(CommonResponse.of(null, "E400", "", invalidFields));

        }
        return ResponseEntity.ok(CommonResponse.ok(sampleService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "샘플 수정", description = "ID로 기존 샘플을 수정합니다.")
    public ResponseEntity<SampleResponse> update(
            @Parameter(description = "샘플 ID", required = true) @PathVariable Long id,
            @Validated(ValidGroups.UpdateValid.class) @RequestBody SampleRequest request) {
        return ResponseEntity.ok(sampleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "샘플 삭제", description = "ID로 샘플 데이터를 삭제합니다.")
    public ResponseEntity<Void> delete(
            @Parameter(description = "샘플 ID", required = true) @PathVariable Long id) {
        sampleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "샘플 전체 조회", description = "전체 샘플 리스트를 조회합니다.")
    public ResponseEntity<List<SampleResponse>> getAll() {
        return ResponseEntity.ok(sampleService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "샘플 단건 조회", description = "ID로 단일 샘플 데이터를 조회합니다.")
    public ResponseEntity<SampleResponse> getOne(
            @Parameter(description = "샘플 ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(sampleService.getOne(id));
    }
}