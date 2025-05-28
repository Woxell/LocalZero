package com.localzero.api.repository;

import com.localzero.api.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community,Long> {

    Optional<Community> findByMemberEmail(String email);
}
