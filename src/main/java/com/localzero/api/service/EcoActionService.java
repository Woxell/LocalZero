package com.localzero.api.service;

import com.localzero.api.entity.EcoAction;
import com.localzero.api.repository.EcoActionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EcoActionService {

    private final EcoActionRepository ecoRepo;

    public EcoActionService(EcoActionRepository ecoRepo) {
        this.ecoRepo = ecoRepo;
    }

    public List<EcoAction> getAllByUser(String email) {
        return ecoRepo.findByAuthorEmail(email);
    }

    public EcoAction save(EcoAction action) {
        return ecoRepo.save(action);
    }
}
