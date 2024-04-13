package com.fnsi.fnsi_cache.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fnsi.fnsi_cache.dao.DictionaryRepository;
import com.fnsi.fnsi_cache.entity.Dictionary;
import com.fnsi.fnsi_cache.entity.Mapping;
import com.fnsi.fnsi_cache.entity.Passport;
import com.fnsi.fnsi_cache.exception.FNSIException;
import com.fnsi.fnsi_cache.exception.FNSIParsingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.Optional;


@Service
public class DictionaryServiceImpl implements DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final MappingService mappingService;
    private final PassportServiceImpl passportService;
    private final RestTemplate restTemplate;


    @Value("${user.key}")
    private String userKey;
    @Value("${list.size}")
    private int size = 100;


    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository, MappingService mappingService, PassportServiceImpl passportService, RestTemplate restTemplate) {
        this.dictionaryRepository = dictionaryRepository;
        this.mappingService = mappingService;
        this.passportService = passportService;
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
        String jsonToString = restTemplate.getForObject(url, String.class);
        String display = null;
        JsonNode jsonNode;
        try {
            jsonNode = new ObjectMapper().readTree(jsonToString);
        } catch (IOException e) {
            throw new FNSIParsingException("Запрашиваемая информация о справочнике с системой " + system + " версии" + version + " и кодом " + code + "не найдена");
        }
        if (!jsonNode.get("result").asText().equals("OK")) {
            throw new FNSIException(jsonNode.get("resultText").asText());
        }
        if (jsonNode.withArray("list").isEmpty()) {
            throw new FNSIException("Запрашиваемый справочник с системой " + system + " версии" + version + " и кодом " + code + "не найден");
        }
        for (JsonNode list : jsonNode.withArray("list").get(0)) {
            if (list.get("column").asText().equals(displayMapping)) {
                display = list.get("value").asText();
            }
        }
        if (display == null) {
            throw new FNSIException("Запрашиваемый справочник с системой " + system + " версии" + version + " и кодом " + code + "не найден");
        }
        Dictionary dictionary = new Dictionary(null, system, version, code, display);
        return dictionaryRepository.save(dictionary);
    }
    @Override
    @Transactional
    public Dictionary updateDictionary(Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }
    @Scheduled(cron = "0 0 0 * * *")
    @Override
    public void updateDictionaryList() {
        for (Passport passport : passportService.getPassportList()) {
            String system = passport.getSystem();
            String version = passport.getVersion();
            Long rowsCountDB = dictionaryRepository.getDictionariesCount(system, version);
            JsonNode jsonNode;
            try {
                jsonNode = new ObjectMapper().readTree(passport.getData());
            } catch (IOException e) {
                throw new FNSIParsingException("Не удалось получить паспорт для справочника с системой " + system + " версии" + version + "не найден");
            }

            if (jsonNode.get("rowsCount").asLong() == rowsCountDB) {
                continue;
            }
            Mapping mapping = mappingService.getMapping(system, version);
            String codeMapping = mapping.getCode();
            String displayMapping = mapping.getDisplay();
            Long pageCount = jsonNode.get("rowsCount").asLong() / size + 1;
            for (int page = 1; page <= pageCount; page++) {
                String url = "http://nsi.rosminzdrav.ru/port/rest/data?userKey=" + userKey +
                        "&identifier=" + system +
                        "&version=" + version +
                        "&page=" + page +
                        "&size=" + size +
                        "&columns=" + codeMapping + "," + displayMapping +
                        "&sorting=" + codeMapping;
                String jsonToString = restTemplate.getForObject(url, String.class);
                try {
                    jsonNode = new ObjectMapper().readTree(jsonToString);
                } catch (IOException e) {
                    throw new FNSIParsingException("Не удалось получить маппинг полей для справочника с системой " + system + " версии" + version + "не найден");
                }
                if (!jsonNode.get("result").asText().equals("OK")) {
                    throw new FNSIException(jsonNode.get("resultText").asText());
                }
                String code = null;
                String display = null;
                for (JsonNode jsonList : jsonNode.withArray("list")) {
                    for (JsonNode json : jsonList) {
                        if (json.get("column").asText().equals(codeMapping)) {
                            code = json.get("value").asText();
                        }
                        if (json.get("column").asText().equals(displayMapping)) {
                            display = json.get("value").asText();
                        }
                    }
                    dictionaryRepository.save(dictionaryRepository.getDictionary(system, version, code).orElse(new Dictionary(null, system, version, code, display)));
                }
            }
        }
    }

    @Override
    @Transactional
    @CacheEvict(cacheManager = "cacheManager", value = "dictionaries", key = "{#system + #version + #code}")
    public void deleteDictionary(String system, String version, String code) {
        dictionaryRepository.delete(dictionaryRepository.getDictionary(system, version, code)
                .orElseThrow(() -> new FNSIException("Запрашиваемый справочник с системой " + system + " версии " + version + " и кодом " + code + " не найден")));
    }
}
