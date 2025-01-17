package com.fnsi.fnsi_cache.dao;

import com.fnsi.fnsi_cache.entity.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MappingRepository extends JpaRepository<Mapping,Long> {

        @Query(value = "select m from Mapping m" +
                " where m.system =:system " +
                "and (m.version = :version " +
                "or (m.version is null and " +
                "NOT EXISTS(select m2 from Mapping m2 " +
                "where m2.system =:system and m2.version =:version)))")

        Optional<Mapping> getMapping(@Param("system") String system, @Param("version") String version);

    }





