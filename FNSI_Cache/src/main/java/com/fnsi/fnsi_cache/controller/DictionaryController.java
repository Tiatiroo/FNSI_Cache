package com.fnsi.fnsi_cache.controller;

import com.fnsi.fnsi_cache.entity.Dictionary;
import com.fnsi.fnsi_cache.entity.Mapping;
import com.fnsi.fnsi_cache.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/mappings")
@RestController
public class DictionaryController {
    private final DictionaryService dictionaryService;
    @Autowired
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }
    @GetMapping("/{system}/{version}/{code}")
    public Dictionary getDictionari(@PathVariable(name = "system")String system,
                                    @PathVariable(name = "version")String version,
                                    @PathVariable(name = "code")String code){
        Dictionary dictionary = dictionaryService.getDictionary(system, version, code);
        return dictionary;
    }
}
