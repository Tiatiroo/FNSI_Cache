package com.fnsi.fnsi_cache.controller;

import com.fnsi.fnsi_cache.entity.Mapping;
import com.fnsi.fnsi_cache.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MappingController {
    private final MappingService mappingService;
    @Autowired
    public MappingController(MappingService mappingService){
        this.mappingService = mappingService;
    }

    @GetMapping(value = "/mapping/")
    public List<Mapping> getAllMap() {
        return mappingService.getAllMap();
    }

    @GetMapping(value = "/mapping/{system}/{version}")
    public Mapping getMapping(@PathVariable(name = "system")String system,
                              @PathVariable(name = "version")String version ) {
        Mapping mapping = mappingService.getMapping(system, version);
        return mapping;
    }

    @PutMapping(value = "/mapping/")
    public Mapping replaceMap(@RequestBody Mapping replaceMap){
        return mappingService.updateMapping(replaceMap);
    }

    @PostMapping("/mapping")
    public Mapping newMapping(@RequestBody Mapping newMapping) {
        return mappingService.addMapping(newMapping);
    }

    @DeleteMapping(value = "/mapping/{system}/{version}")
    public void deleteMapping (@PathVariable(name = "system")String system,
                               @PathVariable(name = "version")String version){
        mappingService.deleteMapping(system, version);
    }
}
