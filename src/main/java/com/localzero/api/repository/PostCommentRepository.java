package com.localzero.api.repository;

import com.localzero.api.entity.PostComment;
import com.localzero.api.entity.PostCommentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, PostCommentId> {
    List<PostComment> findByPostId(Long postId);
}
