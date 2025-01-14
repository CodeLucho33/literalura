package com.example.literalura.repository;

import com.example.literalura.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    List<AuthorEntity> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(Integer startYear, Integer endYear);
}
