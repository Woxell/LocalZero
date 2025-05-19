package com.localzero.api.service;

import com.localzero.api.entity.Post;
import com.localzero.api.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository pr;

    public List<Post> getAllPosts(){
        return pr.findAll();
    }

}
