package com.localzero.api.entity;

/**
 * @author Emil
 */

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
public class PostComment {

    @EmbeddedId
    private PostCommentId id = new PostCommentId();

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post;

    @ManyToOne
    @MapsId("memberEmail")
    @JoinColumn(name = "author_email", referencedColumnName = "email", nullable = false)
    private Person author;

    private String content;

    @Column(nullable = false)
    private LocalDateTime creationDatetime;

}

@AllArgsConstructor
@NoArgsConstructor
@Data
class PostCommentId implements Serializable {
    private Long postId;
    private String memberEmail;
}