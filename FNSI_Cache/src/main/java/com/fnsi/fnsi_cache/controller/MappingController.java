package com.fnsi.fnsi_cache.controller;

import com.fnsi.fnsi_cache.entity.Mapping;
import com.fnsi.fnsi_cache.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/mappings")
@RestController
public class MappingController {
    private final MappingService mappingService;

    @Autowired
    public MappingController(MappingService mappingService) {
        this.mappingService = mappingService;
    }

    @GetMapping("/")
    public List<Mapping> getAllMap() {
        return mappingService.getAllMap();
    }

    @GetMapping("/{system}/{version}")
    public Mapping getMapping(@PathVariable(name = "system") String system,
                              @PathVariable(name = "version") String version) {
        return mappingService.getMapping(system, version);
    }

    @PutMapping("/")
    public Mapping updateMapping(@RequestBody Mapping mapping) {
        return mappingService.updateMapping(mapping);
    }

    @PostMapping("/")
    public Mapping addMapping(@RequestBody Mapping mapping) {
        return mappingService.addMapping(mapping);
    }

    @DeleteMapping("/{system}/{version}")
    public void deleteMapping(@PathVariable(name = "system") String system,
                              @PathVariable(name = "version") String version) {
        mappingService.deleteMapping(system, version);
    }
}
