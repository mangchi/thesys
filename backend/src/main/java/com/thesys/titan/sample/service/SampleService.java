package com.thesys.titan.sample.service;

import com.thesys.titan.sample.dto.SampleDto;

import java.util.List;
import java.util.Map;
import com.thesys.titan.exception.BizException;

public interface SampleService {

     List<SampleDto> getSampleList() throws BizException;

     List<SampleDto> getSampleListWhere(Map<String, Object> params) throws BizException;

     SampleDto getSample(Map<String, Object> params) throws BizException;

     Map<String, Object> getSampleMap(Map<String, Object> params) throws BizException;

     int createSample(SampleDto dto) throws BizException;

     int createSampleMap(Map<String, Object> params) throws BizException;

     int createSampleMaps(List<Map<String, Object>> params) throws BizException;

     int createSamples(List<SampleDto> list) throws BizException;

     int updateSample(SampleDto dto) throws BizException;

     int deleteSample(SampleDto dto) throws BizException;
}
