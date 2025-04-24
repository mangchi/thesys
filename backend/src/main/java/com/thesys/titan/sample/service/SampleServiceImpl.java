package com.thesys.titan.sample.service;

import com.thesys.titan.dao.DAO;
import com.thesys.titan.sample.dto.SampleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.thesys.titan.exception.BizException; // Adjust the package path if necessary

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SampleServiceImpl implements SampleService {

    private final DAO dao;

    @Override
    public List<SampleDto> getSampleList() throws BizException {
        return dao.selectList("Sample.selectSampleList").stream()
                .map(SampleDto.class::cast)
                .toList();
    }

    @Override
    public List<SampleDto> getSampleListWhere(Map<String, Object> params) throws BizException {
        return dao.selectList("Sample.selectSampleListWhere", params).stream()
                .map(SampleDto.class::cast)
                .toList();
    }

    public SampleDto getSample(Map<String, Object> params) throws BizException {
        return (SampleDto) dao.selectOne("Sample.selectSampleDto", params);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getSampleMap(Map<String, Object> params) throws BizException {
        return (Map<String, Object>) dao.selectObject("Sample.selectSampleMap", params);
    }

    @Override
    public int createSample(SampleDto dto) throws BizException {
        return dao.update("Sample.insertSample", dto);
    }

    @Override
    public int createSampleMap(Map<String, Object> params) throws BizException {
        return dao.update("Sample.insertSampleMap", params);
    }

    @Override
    public int createSampleMaps(List<Map<String, Object>> params) throws BizException {
        Map<String, Object> map = new HashMap<>();
        map.put("list", params);
        return dao.update("Sample.insertSampleMaps", map);
    }

    @Override
    public int createSamples(List<SampleDto> list) throws BizException {
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        return dao.update("Sample.insertSamples", map);
    }

    @Override
    public int updateSample(SampleDto dto) throws BizException {
        return dao.update("Sample.updateSample", dto);
    }

    @Override
    public int deleteSample(SampleDto dto) throws BizException {
        return dao.update("Sample.deleteSample", dto);
    }

}
