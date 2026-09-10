package com.localzero.api.template;

import com.localzero.api.entity.Person;

public abstract class AbstractCreator<T extends TimeStampEntry> {

    public final T create(String email, String content){
        Person user = loadUser(email);
        T entity = build(user,content);
        setTimestamps(entity);
        return persist(entity);
    }

    protected abstract Person loadUser(String email);
    protected abstract T build(Person user,String content);
    protected abstract void setTimestamps(T entity);
    protected abstract T persist(T entity);

}
