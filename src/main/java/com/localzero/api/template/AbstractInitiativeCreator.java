package com.localzero.api.template;

import com.localzero.api.entity.Initiative;
import com.localzero.api.entity.Person;

import java.time.LocalDateTime;

public abstract class AbstractInitiativeCreator {
    public final Initiative create(String email, Initiative initiative){
        Person user = loadUser(email);
        initiativedetailer(user,initiative);
        initiative.setCreationDatetime(LocalDateTime.now());
        return save(initiative);
    }

    protected abstract Person loadUser(String email);
    protected abstract  void initiativedetailer(Person user, Initiative initiative);
    protected abstract Initiative save(Initiative initiative);
}
