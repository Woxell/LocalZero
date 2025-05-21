package com.localzero.api.template;

import com.localzero.api.entity.Initiative;
import com.localzero.api.entity.Person;
import com.localzero.api.repository.InitiativeRepository;
import com.localzero.api.service.PersonService;
import org.springframework.stereotype.Service;

@Service
public class InitiativeCreator extends AbstractInitiativeCreator {

    private final InitiativeRepository ir;
    private final PersonService ps;

    public InitiativeCreator(InitiativeRepository ir, PersonService ps){
        this.ir = ir;
        this.ps = ps;
    }

    @Override
    protected Person loadUser(String email) {
        return ps.findByEmail(email);
    }

    @Override
    protected void initiativedetailer(Person user, Initiative initiative) {
        initiative.setCreator(user);
        initiative.setCommunity(user.getCommunity());
        initiative.setCommunityMember(user);
    }

    @Override
    protected Initiative save(Initiative initiative) {
        return ir.save(initiative);
    }
}
