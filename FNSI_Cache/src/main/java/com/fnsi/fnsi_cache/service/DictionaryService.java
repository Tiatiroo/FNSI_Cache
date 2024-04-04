package com.fnsi.fnsi_cache.service;
import com.fnsi.fnsi_cache.entity.Dictionary;

public interface DictionaryService {
    Dictionary addDictionary(Dictionary dictionary);
    Dictionary getDictionary(String system, String version, String code);
    Dictionary updateDictionary(Dictionary dictionary);
    void deleteDictionary(String system, String version, String code);
}

