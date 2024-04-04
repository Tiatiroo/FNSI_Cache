package com.fnsi.fnsi_cache.service;

import com.fnsi.fnsi_cache.dao.PassportRepository;
import com.fnsi.fnsi_cache.entity.Passport;
import liquibase.pro.packaged.P;
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
                .<RuntimeException>orElseThrow(() -> 
                        new RuntimeException("Паспорт не найден!"));
    }

    @Override
    @Transactional
    public void deleteFromDatabase(String system, String version) {
        passportRepository.delete(passportRepository.getPassport(system, version)
                .<EntityNotFoundException>orElseThrow(() -> 
                        new EntityNotFoundException("Паспорта не существует")));
    }

    @Override
    public Passport addPassport(Passport passport) {
        Passport save = passportRepository.save(passport);
        return save;
    }

    @Override
    public Passport updatePassport(Passport passport) {
        return passportRepository.save(passport);
    }

}
