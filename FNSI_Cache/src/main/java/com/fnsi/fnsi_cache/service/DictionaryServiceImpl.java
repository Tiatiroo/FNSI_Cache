package com.fnsi.fnsi_cache.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnsi.fnsi_cache.dao.DictionaryRepository;
import com.fnsi.fnsi_cache.entity.Dictionary;
import com.fnsi.fnsi_cache.entity.Mapping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;


@Service
public class DictionaryServiceImpl implements DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final MappingService mappingService;
    private final RestTemplate restTemplate;

    @Value("${user.key}")
    private String userKey;


    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository, MappingService mappingService, RestTemplate restTemplate) {
        this.dictionaryRepository = dictionaryRepository;
        this.mappingService = mappingService;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public Dictionary addDictionary(Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    @Override
    @Transactional
    @Cacheable(cacheManager = "cacheManager", value = "dictionaries", key = "{#system + #version + #code}")
    public Dictionary getDictionary(String system, String version, String code) {

        Optional<Dictionary> optionalDictionary = dictionaryRepository.getDictionary(system, version, code);
        if (optionalDictionary.isPresent()) {
            return optionalDictionary.get();
        }
        Mapping mapping = mappingService.getMapping(system, version);
        String displayMapping = mapping.getDisplay();
        String codeMapping = mapping.getCode();
        String url = "http://nsi.rosminzdrav.ru/port/rest/data?userKey=" + userKey +
                "&identifier=" + system +
                "&version=" + version +
                "&filters=" + codeMapping + "|" + code +
                "&columns=" + displayMapping;
        String jsonString = restTemplate.getForObject(url, String.class);
        String display = null;
        JsonNode jsonNode;
        try {
            jsonNode = new ObjectMapper().readTree(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!jsonNode.get("result").asText().equals("OK")) {
            throw new RuntimeException(jsonNode.get("resultText").asText());
        }
        if (jsonNode.withArray("list").isEmpty()){
            throw new RuntimeException("Запрашиваемый справочник с системой " + system + " версии" + version + " и кодом " + code + "не найден");
        }
        for (JsonNode list : jsonNode.withArray("list").get(0)) {
            if (list.get("column").asText().equals(displayMapping)) {
                display = list.get("value").asText();
            }
        }
        if (display == null) {
            throw new RuntimeException("Запрашиваемый справочник с системой " + system + " версии" + version + " и кодом " + code + "не найден");
        }
        Dictionary dictionary = new Dictionary(null, system, version, code, display);
        return dictionaryRepository.save(dictionary);
    }

    @Override
    @Transactional
    public Dictionary updateDictionary(Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    @Override
    @Transactional
    @CacheEvict(cacheManager = "cacheManager", value = "dictionaries", key = "{#system + #version + #code}")
    public void deleteDictionary(String system, String version, String code) {
        dictionaryRepository.delete(dictionaryRepository.getDictionary(system, version, code)
                .orElseThrow(() -> new EntityNotFoundException("Запрашиваемый справочник с системой " + system + " версии " + version + " и кодом " + code + " не найден")));
    }
}
