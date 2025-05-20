package com.localzero.api.entity;

/**
 * @author Emil
 * @author Mahyar
 */

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "initiative_id")
    private Initiative initiative;

    @ManyToOne
    @JoinColumn(name = "author_email", referencedColumnName = "email")
    private Person author;

    private String content;

    @Lob
    private byte[] image;

    private int likesCount;

    @Column(name = "creation_datetime")
    private LocalDateTime creationDatetime;

    @PrePersist
    private void onCreate() {
        creationDatetime = LocalDateTime.now();
        likesCount = 0;
    }
}
