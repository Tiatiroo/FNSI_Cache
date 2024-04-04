package com.fnsi.fnsi_cache.service;

import com.fnsi.fnsi_cache.dao.MappingRepository;
import com.fnsi.fnsi_cache.entity.Mapping;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class MappingServiceImpl implements MappingService {

    final MappingRepository mappingRepository;

    public MappingServiceImpl(MappingRepository mappingRepository) {
        this.mappingRepository = mappingRepository;
    }

    @Override
    @Transactional
    public Mapping getMapping(String system, String version) {
        return mappingRepository.getMapping(system, version).orElseThrow(() ->
                new EntityNotFoundException("Не удалось получить поля маппинга спрочника c параметрами: система " + system  +" версия " + version));
    }

    @Override
    @Transactional
    public Mapping addMapping(Mapping mapping) {
        return mappingRepository.save(mapping);
    }

    @Override
    @Transactional
    public Mapping updateMapping(Mapping mapping) {
        return mappingRepository.save(mapping);
    }

    @Override
    @Transactional
    public void deleteMapping(String system, String version) {
        mappingRepository.delete(mappingRepository.getMapping(system, version).orElseThrow(() ->
                new EntityNotFoundException("Не удалось удалить поля маппинга спрочника с параметрами: система " + system  +" версия " + version)));

    }
}
