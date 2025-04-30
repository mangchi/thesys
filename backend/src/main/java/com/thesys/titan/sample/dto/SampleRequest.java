package com.thesys.titan.sample.dto;

import com.thesys.titan.common.dto.CommonRequest;
import com.thesys.titan.common.valid.ValidGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(description = "SampleRequest DTO")
@Getter
@Setter
@ToString
public class SampleRequest extends CommonRequest {

        @NotNull(groups = { ValidGroups.SelectValid.class, ValidGroups.UpdateValid.class,
                        ValidGroups.DeleteValid.class }, message = "{error.notNull}")
        @Schema(description = "Sample key Id: create null 데이터(자동 생성), select,update,delete 경우 필수 데이터")
        private Long id;

        @Schema(description = "샘플 설명", defaultValue = "샘플 제목", nullable = true)
        @NotBlank(groups = { ValidGroups.CreateValid.class, ValidGroups.UpdatePwValid.class })
        @Size(max = 100)
        private String description;

        @Schema(description = "내용", defaultValue = "샘플 내용", nullable = true)
        @NotBlank(groups = { ValidGroups.CreateValid.class, ValidGroups.UpdatePwValid.class })
        @Size(max = 255)
        private String contents;

        @NotNull(groups = { ValidGroups.CreateValid.class, ValidGroups.DeleteValid.class }, message = "{error.notNull}")
        @Schema(description = "사용 여부", defaultValue = "true", nullable = false)
        private Boolean useYn;

}