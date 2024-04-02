package com.fnsi.fnsi_cache.dao;

import com.fnsi.fnsi_cache.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary,Long> {

    @Query(value = "select n from Dictionary n where n.system = :system and n.version = :version and n.code = :code")
    Optional<Dictionary> getDictionary(@Param("system") String system, @Param("version") String version, @Param("code") String code);

    @Query(value = "select count(n) from Dictionary n where n.system = :system and n.version = :version")
    Long getDictionariesCount(@Param("system") String system, @Param("version") String version);

}
