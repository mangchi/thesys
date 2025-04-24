package com.thesys.titan.sample.dto;

import com.thesys.titan.common.valid.ValidGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

import com.thesys.titan.common.vo.CommonDto;

@Schema(description = "Sample DTO")
@Getter
@Setter
@ToString
public class SampleDto extends CommonDto {

        @NotNull(groups = { ValidGroups.SelectValid.class, ValidGroups.UpdateValid.class,
                        ValidGroups.DeleteValid.class }, message = "{error.notNull}")
        @Schema(description = "Sample key Id: create null 데이터(자동 생성), select,update,delete 경우 필수 데이터", nullable = true, defaultValue = "null")
        private BigInteger samId;

        @Schema(description = "상세 섷명", defaultValue = "샘플 설명", nullable = true)
        private String description;

        @NotNull(groups = { ValidGroups.CreateValid.class, ValidGroups.DeleteValid.class }, message = "{error.notNull}")
        @Pattern(// groups = {ValidGroups.CreateValid.class,ValidGroups.SelectValid.class,
                 // ValidGroups.UpdateValid.class, ValidGroups.DeleteValid.class},
                        regexp = "[0-1]", message = "{error.useYn.invalid}")
        @Schema(description = "사용 여부", defaultValue = "null", allowableValues = { "0", "1" }, nullable = true)
        private Integer useYn;

}