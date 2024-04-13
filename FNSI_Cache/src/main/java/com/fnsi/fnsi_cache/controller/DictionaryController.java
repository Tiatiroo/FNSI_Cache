package com.fnsi.fnsi_cache.controller;

import com.fnsi.fnsi_cache.entity.Dictionary;
import com.fnsi.fnsi_cache.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/dictionaries")
@RestController
public class DictionaryController {
    private final DictionaryService dictionaryService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/{system}/{version}/{code}")
    public Dictionary getDictionary(@PathVariable(name = "system") String system,
                                    @PathVariable(name = "version") String version,
                                    @PathVariable(name = "code") String code) {
        return dictionaryService.getDictionary(system, version, code);
    }

    @PutMapping("/")
    public Dictionary updateDictionary(@RequestBody Dictionary dictionary) {
        return dictionaryService.updateDictionary(dictionary);
    }

    @PostMapping("/")
    public Dictionary addDictionary(@RequestBody Dictionary dictionary) {
        return dictionaryService.addDictionary(dictionary);
    }

    @DeleteMapping("/{system}/{version}/{code}")
    public void deleteDictionary(@PathVariable(name = "system") String system,
                                 @PathVariable(name = "version") String version,
                                 @PathVariable(name = "code") String code) {
        dictionaryService.deleteDictionary(system, version, code);
    }
}
