package com.fnsi.fnsi_cache.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnsi.fnsi_cache.dao.MappingRepository;
import com.fnsi.fnsi_cache.entity.Mapping;
import com.fnsi.fnsi_cache.entity.Passport;
import com.fnsi.fnsi_cache.exception.DataNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class MappingServiceImpl implements MappingService {
    private final MappingRepository mappingRepository;
    private final PassportService passportService;

    public MappingServiceImpl(MappingRepository mappingRepository, PassportService passportService) {
        this.mappingRepository = mappingRepository;
        this.passportService = passportService;
    }

    @Override
    @Transactional
    public List<Mapping> getAllMap() {
        return mappingRepository.findAll();
    }

    @Override
    @Transactional
    public Mapping getMapping(String system, String version) {
        Optional<Mapping> optional = mappingRepository.getMapping(system, version);
        if (optional.isPresent()) {
            return optional.get();
        }
        String code = null;
        String display = null;

        Passport passport = passportService.getFromDatabase(system, version);
        try {
            JsonNode node = new ObjectMapper().readTree(passport.getData());
            for (JsonNode key : node.withArray("keys")) {
                if (key.get("type").asText().equals("PRIMARY")) {
                    code = key.get("field").asText();
                }
                if (key.get("type").asText().equals("VALUE")) {
                    display = key.get("field").asText() ;
                }
            }
            if (code == null || display == null)
                throw new DataNotFoundException("Не удалось получить поля code или display паспорта с системой " + system + " и версией " + version);
        } catch (JsonProcessingException e) {
            throw new DataNotFoundException("не удалось получить данные из таблицы паспортов, для паспорта с системой " + system + " и версией " + version);
        }
        Mapping mapping = new Mapping(null, system, version, code, display);
        return mappingRepository.save(mapping);

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
                new EntityNotFoundException("Не удалось удалить поля маппинга спрочника с параметрами: система " + system + " версия " + version)));

    }
}
