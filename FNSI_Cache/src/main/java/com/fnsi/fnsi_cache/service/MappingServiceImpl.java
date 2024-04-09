package com.fnsi.fnsi_cache.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnsi.fnsi_cache.dao.MappingRepository;
import com.fnsi.fnsi_cache.entity.Mapping;
import com.fnsi.fnsi_cache.entity.Passport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class MappingServiceImpl implements MappingService {
    private final MappingRepository mappingRepository;
    private final PassportsService passportsService;

    public MappingServiceImpl(MappingRepository mappingRepository, PassportsService passportsService) {
        this.mappingRepository = mappingRepository;
        this.passportsService = passportsService;
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

        Passport passport = passportsService.getFromDatabase(system, version);
        try {
            JsonNode node = new ObjectMapper().readTree(passport.getData());
            for (JsonNode key : node.withArray("keys")) {
                if (key.get("type").asText().equals("PRIMARY")) {
                    code = key.get("field").asText();
                }
                if (key.get("type").asText().equals("VALUE")) {
                    display = key.get("field").asText();
                }
            }
            if (code == null || display == null)
                throw new RuntimeException("Не удалось получить поля code или display из таблицы паспортов.");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("не удалось получить данные из таблицы паспортов.");
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
