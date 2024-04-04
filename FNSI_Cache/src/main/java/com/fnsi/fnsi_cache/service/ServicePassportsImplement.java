package com.fnsi.fnsi_cache.service;

import com.fnsi.fnsi_cache.dao.PassportRepository;
import com.fnsi.fnsi_cache.entity.Passport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class ServicePassportsImplement implements PassportsService {


    private final PassportRepository passportRepository;
    @Autowired
    public ServicePassportsImplement(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    @Override
    @Transactional
    public Passport getFromDatabase(String system, String version) {
        return passportRepository.getPassport(system, version)
                .orElseThrow(() ->
                        new EntityNotFoundException("Паспорт c системой " + system + " и версией " + version + " не найден!"));
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
