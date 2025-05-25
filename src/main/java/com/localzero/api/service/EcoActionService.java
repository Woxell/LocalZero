package com.localzero.api.service;

import com.localzero.api.Logger;
import com.localzero.api.entity.EcoAction;
import com.localzero.api.repository.EcoActionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EcoActionService {

    private EcoActionRepository ecoRepo;
    private final Logger logger = Logger.getInstance();

    public List<EcoAction> getAllEcoactionsBy(String email) {
        return ecoRepo.findByAuthorEmail(email);
    }

    public EcoAction save(EcoAction action) {
        logger.log("EcoAction saved by " + action.getAuthorEmail() + ": " + action.getContent());
        return ecoRepo.save(action);
    }
}