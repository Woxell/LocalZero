package com.localzero.api.service;

import com.localzero.api.Logger;
import com.localzero.api.entity.PostComment;
import com.localzero.api.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private CommentRepository commentRepository;
    private final Logger logger = Logger.getInstance();

    public PostComment save(PostComment comment) {
        logger.log(comment.getAuthor().getName() + " commented on post: " + comment.getPost().getId() + ": " + comment.getContent());
        return commentRepository.save(comment);
    }

    public List<PostComment> findByPostId(Long postId){
        return commentRepository.findByPostId(postId);
    }
}