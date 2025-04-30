package com.thesys.titan.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.thesys.titan.common.vo.FieldErrors;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Schema(description = "공통 응답 DTO")
public class CommonResponse<T> {

    @Schema(description = "결과 데이터")
    private final T data;

    @Schema(description = "메시지 코드", example = "S001")
    private final String messageCode;

    @Schema(description = "메시지 내용", example = "정상 처리되었습니다.")
    private final String message;

    @Schema(description = "필드 에러", example = "필드 에러 내용")
    private final FieldErrors fieldErrors;

    @Schema(description = "메시지 리스트", example = "메시지 리스트 내용")
    private final List<String> messages;

    @Builder
    public CommonResponse(T data, String messageCode, String message, FieldErrors fieldErrors, List<String> messages) {
        this.data = data;
        this.messageCode = messageCode;
        this.message = message;
        this.fieldErrors = fieldErrors;
        this.messages = messages;
    }

    public static <T> CommonResponse<T> ok(T data) {
        return CommonResponse.<T>builder()
                .data(data)
                .messageCode("S001")
                .message("정상 처리되었습니다.")
                .build();
    }

    public static <T> CommonResponse<T> of(T data, String code, String msg) {
        return CommonResponse.<T>builder()
                .data(data)
                .messageCode(code)
                .message(msg)
                .build();
    }

    public static <T> CommonResponse<T> of(T data, String code, String msg, FieldErrors fieldErrors) {
        return CommonResponse.<T>builder()
                .data(data)
                .messageCode(code)
                .message(msg)
                .fieldErrors(fieldErrors)
                .build();
    }

    public static <T> CommonResponse<T> of(T data, String code, String msg, List<String> messages) {
        return CommonResponse.<T>builder()
                .data(data)
                .messageCode(code)
                .message(msg)
                .messages(messages)
                .build();
    }

}
// CommonResponse는 모든 응답 DTO의 부모 클래스입니다.
// 이 클래스는 공통적으로 사용되는 필드인 msg와 code를 포함하고 있습니다.
// msg는 응답 메시지를 나타내고, code는 응답 코드(예: 성공, 실패 등)를 나타냅니다.
// 이 클래스를 상속받는 DTO들은 msg와 code 필드를 사용할 수 있습니다.