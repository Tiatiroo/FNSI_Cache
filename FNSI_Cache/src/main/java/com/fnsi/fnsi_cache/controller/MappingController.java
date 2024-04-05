package com.fnsi.fnsi_cache.controller;

import com.fnsi.fnsi_cache.service.MappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
//        Выбрать подходящий сервис и разработать логику выбора\создания\получения полей маппинга справочника.
//        Брать данные для маппинга из бд паспортов справочников(столбец data). Преобразовывать строку в json и получать секцию keys.
//        Логика соотношения полей:
//        Если поле имеет type PRIMARY - то значение field будет соответствовать code в нашей системе.
//        Если поле имеет type VALUE - то значение field будет соответствовать вшыздфн в нашей системе.
//
//        Если в ключах нет параметров нужных типов - выкидывать исключение.
@RestController
public class MappingController {
    private final MappingService mappingService;
    @Autowired
    public MappingController(MappingService mappingService) {
        this.mappingService = mappingService;
    }


}
