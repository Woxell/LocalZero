package com.localzero.api.repository;

import com.localzero.api.entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community,Long> {

}
