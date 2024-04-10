package com.fnsi.fnsi_cache.dao;

import com.fnsi.fnsi_cache.entity.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Long> {
   @Query(value = "select p from Passport p where p.system = :system and p.version = :version")
   Optional<Passport> getPassport(@Param("system") String system, @Param("version") String version);

}
