package com.localzero.api.service;

import com.localzero.api.entity.Initiative;
import com.localzero.api.repository.InitiativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InitiativeService {
    private final InitiativeRepository ir;

    public InitiativeService(InitiativeRepository ir){
        this.ir = ir;
    }

    public List<Initiative> getAll(){
        return ir.findAll();
    }

    public List<Initiative> getByParticipant(String email){
        return ir.findByParticipantEmail(email);
    }
    public Initiative getById(Long id){
        return ir.findById(id).orElseThrow();
    }
}
