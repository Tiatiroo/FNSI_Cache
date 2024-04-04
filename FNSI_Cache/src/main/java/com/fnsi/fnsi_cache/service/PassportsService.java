package com.fnsi.fnsi_cache.service;

import com.fnsi.fnsi_cache.entity.Passport;

public interface PassportsService {
    Passport getFromDatabase(String system, String version);
    void deleteFromDatabase(String system, String version);
    Passport addPassport(Passport passport);
    Passport updatePassport(Passport passport);
}
