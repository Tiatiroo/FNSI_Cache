package com.fnsi.fnsi_cache.service;

import com.fnsi.fnsi_cache.dao.PassportRepository;
import com.fnsi.fnsi_cache.entity.Passport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Service
public class ServicePassportsImplement implements PassportsService {
    private final PassportRepository passportRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public ServicePassportsImplement(PassportRepository passportRepository, RestTemplate restTemplate) {
        this.passportRepository = passportRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public Passport getFromDatabase(String system, String version) {
        Optional<Passport> optional = passportRepository.getPassport(system, version);
        if (optional.isPresent() && optional.get().getData() != null) {
            return optional.get();
        }
        String url = "http://nsi.rosminzdrav.ru/port/rest/passport?" +
                "userKey=2b6a3146-9b41-4d0a-a3b0-51d294cf2e03&" +
                "identifier=" + system + "&version=" + version;
        String json = restTemplate.getForObject(url, String.class);
        JsonNode node;
        try {
            node = new ObjectMapper().readTree(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!node.get("result").asText().equals("OK")) {
            throw new RuntimeException(node.get("resultText").asText());
        }
        Passport passport = passportRepository.getPassport(system, version).orElse(new Passport(null, system, version, json));
        passport.setData(json);
        return passportRepository.save(passport);
    }
    @Override
    @Transactional
    public void deleteFromDatabase(String system, String version) {
        passportRepository.delete(passportRepository.getPassport(system, version)
                .orElseThrow(() ->
                        new EntityNotFoundException("Паспорта c системой " + system + " и версией " + version + " не существует")));
    }
    @Override
    @Transactional
    public Passport addPassport(Passport passport) {
        return passportRepository.save(passport);
    }

    @Override
    @Transactional
    public Passport updatePassport(Passport passport) {
        return passportRepository.save(passport);
    }

}
