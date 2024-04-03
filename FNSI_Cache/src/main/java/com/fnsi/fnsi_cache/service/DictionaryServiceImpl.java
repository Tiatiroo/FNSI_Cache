package com.fnsi.fnsi_cache.service;

import com.fnsi.fnsi_cache.dao.DictionaryRepository;
import com.fnsi.fnsi_cache.entity.Dictionary;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
@Service
public class DictionaryServiceImpl implements DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    @Override
    @Transactional
    public Dictionary addDictionary(Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    @Override
    @Transactional
    public Dictionary getDictionary(String system, String version, String code) {
        return dictionaryRepository.getDictionary(system,version,code)
                .orElseThrow(()-> new EntityNotFoundException("Запрашиваемый справочник с системой " + system + " версии " + version + " и кодом " + code + " не найден"));
    }

    @Override
    @Transactional
    public Dictionary updateDictionary(Dictionary dictionary) {
        return dictionaryRepository.save(dictionary);
    }

    @Override
    @Transactional
    public void deleteDictionary(String system, String version, String code) {
        dictionaryRepository.delete(dictionaryRepository.getDictionary(system,version,code)
                .orElseThrow(()-> new EntityNotFoundException("Запрашиваемый справочник с системой " + system + " версии " + version + " и кодом " + code + " не найден")));
    }
}
