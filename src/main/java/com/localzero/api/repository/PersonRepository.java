package com.localzero.api.repository;
import com.localzero.api.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person,String> {
    Optional<Person> findByEmail(String email);
    List<Person> findAll();
    List<Person> findByEmailNot(String email);
}
