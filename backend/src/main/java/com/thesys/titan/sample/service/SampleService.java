package com.thesys.titan.sample.service;

import com.thesys.titan.sample.dto.SampleRequest;
import com.thesys.titan.sample.dto.SampleResponse;
import com.thesys.titan.sample.entity.Sample;
import com.thesys.titan.sample.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SampleService {

     private final SampleRepository sampleRepository;

     @Transactional
     public SampleResponse create(SampleRequest request) {
          Sample entity = Sample.of(request);
          return SampleResponse.from(sampleRepository.save(entity));
     }

     @Transactional
     public SampleResponse update(Long id, SampleRequest request) {
          Sample entity = sampleRepository.findById(id).orElseThrow();
          entity.update(request);
          return SampleResponse.from(entity);
     }

     @Transactional
     public void delete(Long id) {
          Sample entity = sampleRepository.findById(id).orElseThrow();
          sampleRepository.delete(entity);
     }

     @Transactional(readOnly = true)
     public List<SampleResponse> getAll() {
          return sampleRepository.findAll().stream()
                    .map(SampleResponse::from)
                    .toList();
     }

     @Transactional(readOnly = true)
     public SampleResponse getOne(Long id) {
          return sampleRepository.findById(id).map(SampleResponse::from)
                    .orElseThrow();
     }
}
