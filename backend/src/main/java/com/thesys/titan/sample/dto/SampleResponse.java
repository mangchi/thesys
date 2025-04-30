package com.thesys.titan.sample.dto;

import com.thesys.titan.sample.entity.Sample;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SampleResponse {

        private Long id;
        private String description;
        private String contents;
        private Boolean useYn;
        private Long createdBy;
        private Long updatedBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static SampleResponse from(Sample sample) {
                return SampleResponse.builder()
                                .id(sample.getId())
                                .description(sample.getDescription())
                                .contents(sample.getContents())
                                .useYn(sample.isUseYn())
                                .createdBy(sample.getCreatedBy())
                                .updatedBy(sample.getUpdatedBy())
                                .createdAt(sample.getCreatedAt())
                                .updatedAt(sample.getUpdatedAt())
                                .build();
        }
}