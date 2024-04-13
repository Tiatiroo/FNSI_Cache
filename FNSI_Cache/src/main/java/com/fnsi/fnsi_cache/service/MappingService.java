package com.fnsi.fnsi_cache.service;

import com.fnsi.fnsi_cache.entity.Mapping;

import java.util.List;

public interface MappingService {
    List<Mapping> getAllMap();
    
    Mapping getMapping(String system, String version);

    Mapping addMapping(Mapping mapping);

    Mapping updateMapping(Mapping mapping);

    void deleteMapping(String system, String version);

}

