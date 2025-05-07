package com.localzero.api.repository;

import com.localzero.api.entity.Initiative;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InitiativeRepository extends JpaRepository<Initiative,Long> {
    List<Initiative> findByCreatedByEmail(String email);
}
