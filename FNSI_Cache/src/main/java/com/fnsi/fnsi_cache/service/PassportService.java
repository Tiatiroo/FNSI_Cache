package com.fnsi.fnsi_cache.service;

import com.fnsi.fnsi_cache.entity.Passport;

import java.util.List;

public interface PassportService {
    Passport getFromDatabase(String system, String version);
    List<Passport> getPassportList();
    void deleteFromDatabase(String system, String version);
    Passport addPassport(Passport passport);
    Passport updatePassport(Passport passport);
}
