package com.thesys.titan.sample.entity;

import com.thesys.titan.common.entity.BaseEntity;
import com.thesys.titan.sample.dto.SampleRequest;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sample")
public class Sample extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description", length = 200, nullable = true)
    private String description;

    @Column(name = "contents", length = 2000, nullable = true)
    private String contents;

    @Column(name = "use_yn", length = 1, nullable = false)
    private boolean useYn;

    public static Sample of(SampleRequest request) {
        return Sample.builder()
                .description(request.getDescription())
                .contents(request.getContents())
                .useYn(request.getUseYn())
                .build();
    }

    public void update(SampleRequest request) {
        this.description = request.getDescription();
        this.contents = request.getContents();
        this.useYn = request.getUseYn();
    }
}
