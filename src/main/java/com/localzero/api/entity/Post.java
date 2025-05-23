package com.localzero.api.entity;

/**
 * @author Emil
 * @author Mahyar
 */

import jakarta.persistence.*;
import lombok.Data;
import com.localzero.api.template.TimeStampEntry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Post implements TimeStampEntry{

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> comments = new ArrayList<>();

    
}
