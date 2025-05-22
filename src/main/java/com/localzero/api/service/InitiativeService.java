package com.localzero.api.service;

import com.localzero.api.entity.Community;
import com.localzero.api.entity.Initiative;
import com.localzero.api.entity.Person;
import com.localzero.api.repository.InitiativeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class InitiativeService {
    private final InitiativeRepository ir;
    private final PersonService personService;

    public InitiativeService(InitiativeRepository ir, PersonService personService){
        this.ir = ir;
        this.personService = personService;
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

    public List<Initiative> getVisibleForUser(Person person) {
        Set<Community> communities = person.getCommunities();
        String email = person.getEmail();

        Set<Initiative> all = new HashSet<>();
        all.addAll(ir.findByIsPublicTrueOrCommunitiesIn(communities));
        all.addAll(ir.findByCreatorEmail(email));

        return new ArrayList<>(all);
    }

    public void addParticipant(Long initiativeId, String email) {
        Initiative initiative = ir.findById(initiativeId).orElseThrow();
        Person person = personService.findByEmail(email);
        initiative.getParticipants().add(person);
        ir.save(initiative);
    }

}
