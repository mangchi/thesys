package com.thesys.titan.sample.repository;

import com.thesys.titan.sample.entity.Sample;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, Long> {

    Page<Sample> findByDescriptionContainingIgnoreCase(String keyword, Pageable pageable);
}
