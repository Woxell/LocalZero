package com.localzero.api.service;

import com.localzero.api.entity.Community;
import com.localzero.api.repository.CommunityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommunityService {

    private CommunityRepository communityRepository;

    public List<Community> findAll(){
        return communityRepository.findAll();
    }
    public List<Community> findAllById(List<Long> ids){
        return communityRepository.findAllById(ids);
    }

    public Community save(Community community){
        return communityRepository.save(community);
    }
}
