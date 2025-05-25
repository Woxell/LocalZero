package com.localzero.api.repository;

import com.localzero.api.entity.EcoAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EcoActionRepository extends JpaRepository<EcoAction, Long> {
    List<EcoAction> findByAuthorEmail(String email);
}

