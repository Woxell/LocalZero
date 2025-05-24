package com.localzero.api.service;

import com.localzero.api.entity.PostComment;
import com.localzero.api.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private CommentRepository commentRepository;

    public PostComment save(PostComment comment) {
        return commentRepository.save(comment);
    }

    public List<PostComment> findByPostId(Long postId){
        return commentRepository.findByPostId(postId);
    }
}
