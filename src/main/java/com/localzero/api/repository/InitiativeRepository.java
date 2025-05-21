package com.localzero.api.repository;

import com.localzero.api.entity.Initiative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InitiativeRepository extends JpaRepository<Initiative,Long> {
    List<Initiative> findByCreatorEmail(String email);
    @Query("""
        SELECT i FROM Initiative i
        JOIN i.participants p
        WHERE p.email = :email
    """)
    List<Initiative> findByParticipantEmail(@Param("email") String email);

}
