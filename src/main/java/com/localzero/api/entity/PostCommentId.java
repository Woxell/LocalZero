package com.localzero.api.entity;

import lombok.Data;

import java.io.Serializable;

/*
@AllArgsConstructor
@NoArgsConstructor

 */
@Data
public class PostCommentId implements Serializable {
    private Long postId;
    private String memberEmail;
}
