package com.localzero.api.service;

import com.localzero.api.entity.Community;
import com.localzero.api.entity.Initiative;
import com.localzero.api.repository.InitiativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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



    public List<Initiative> getAllPublic() {
        return ir.findByIsPublicTrue();
    }

    public List<Initiative> getPublicOrByCommunity(Community community) {
        return ir.findByIsPublicTrueOrCommunitiesContaining(community);
    }

    public List<Initiative> getPublicOrByCommunities(Set<Community> communities) {
        return ir.findByIsPublicTrueOrCommunitiesIn(communities);
    }


}
