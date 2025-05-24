package com.localzero.api.service;

import com.localzero.api.entity.Post;
import com.localzero.api.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByAuthorEmail(String email) {
        return postRepository.findByAuthorEmailOrderByCreationDatetimeDesc(email);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<Post> getAllStandalonePosts() {
        return postRepository.findByInitiativeIsNullOrderByCreationDatetimeDesc();
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByInitiativeId(Long initiativeId) {
        return postRepository.findByInitiativeIdOrderByCreationDatetimeDesc(initiativeId);
    }

    public void incrementLikes(long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Post getById(long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new RuntimeException("Post not found with id: " + postId));
    }
}
