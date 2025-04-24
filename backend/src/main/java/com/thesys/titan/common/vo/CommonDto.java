package com.thesys.titan.common.vo;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonDto {

    @Schema(hidden = true)
    private LocalDateTime createdAt;
    @Schema(hidden = true)
    private String createdId;
    @Schema(hidden = true)
    private LocalDateTime updateAt;
    @Schema(hidden = true)
    private String updateId;
}