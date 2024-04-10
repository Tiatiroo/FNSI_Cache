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
    public MappingController(MappingService mappingService){
        this.mappingService = mappingService;
    }

    @GetMapping("/")
    public List<Mapping> getAllMap() {
        return mappingService.getAllMap();
    }

    @GetMapping("/{system}/{version}")
    public Mapping getMapping(@PathVariable(name = "system")String system,
                              @PathVariable(name = "version")String version ) {
        Mapping mapping = mappingService.getMapping(system, version);
        return mapping;
    }

    @PutMapping("/")
    public Mapping replaceMap(@RequestBody Mapping replaceMap){
        return mappingService.updateMapping(replaceMap);
    }

    @PostMapping("/")
    public Mapping newMapping(@RequestBody Mapping newMapping) {
        return mappingService.addMapping(newMapping);
    }

    @DeleteMapping("/{system}/{version}")
    public void deleteMapping (@PathVariable(name = "system")String system,
                               @PathVariable(name = "version")String version){
        mappingService.deleteMapping(system, version);
    }
}
