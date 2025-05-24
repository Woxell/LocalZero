package com.localzero.api.entity;

/**
 * @author Emil
 * @author Mahyar
 * @André
 */
import jakarta.persistence.*;
import lombok.Data;
import com.localzero.api.template.TimeStampEntry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "post")
public class Post implements TimeStampEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "initiative_id")
    private Initiative initiative;

    @ManyToOne
    @JoinColumn(name = "author_email", referencedColumnName = "email")
    private Person author;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "image", columnDefinition = "bytea")
    private byte[] image;

    @Column(name = "likes_count")
    private int likesCount;

    @Column(name = "creation_datetime")
    private LocalDateTime creationDatetime;

    @PrePersist
    private void onCreate() {
        creationDatetime = LocalDateTime.now();
        likesCount = 0;
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostComment> comments = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "eco_action_id")
    private EcoAction ecoAction;
}
