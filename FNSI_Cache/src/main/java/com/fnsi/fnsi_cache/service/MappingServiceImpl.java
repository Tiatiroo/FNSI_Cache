package com.fnsi.fnsi_cache.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnsi.fnsi_cache.dao.MappingRepository;
import com.fnsi.fnsi_cache.entity.Mapping;
import com.fnsi.fnsi_cache.entity.Passport;
import com.fnsi.fnsi_cache.exception.FNSIException;
import com.fnsi.fnsi_cache.exception.FNSIParsingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
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
        JsonNode node = null;

        Passport passport = passportService.getFromDatabase(system, version);
        try {
            node = new ObjectMapper().readTree(passport.getData());
        } catch (IOException e) {
            throw new FNSIParsingException("Не удалось получить информацию о маппинга полей паспорта с системой " + system + " и версией " + version);
        }
        for (JsonNode key : node.withArray("keys")) {
            if (key.get("type").asText().equals("PRIMARY")) {
                code = key.get("field").asText();
            }
            if (key.get("type").asText().equals("VALUE")) {
                display = key.get("field").asText() ;
            }
        }
        if (code == null || display == null){
            throw new FNSIParsingException("Не удалось получить поля code или display паспорта с системой " + system + " и версией " + version);
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
                new FNSIException("Не удалось удалить поля маппинга спрочника с параметрами: система " + system + " версия " + version)));

    }
}
