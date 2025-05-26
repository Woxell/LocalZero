package com.localzero.api.entity;

/**
 * @author: Adrian , Emil
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class DirectMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "sender_email", nullable = false)
    private String senderEmail;


    @Column(name = "receiver_email", nullable = false)
    private String receiverEmail;

    @ManyToOne
    @JoinColumn(name = "sender_email", referencedColumnName = "email", insertable = false, updatable = false)
    @JsonIgnore
    private Person sender;

    @ManyToOne
    @JoinColumn(name = "receiver_email", referencedColumnName = "email", insertable = false, updatable = false)
    @JsonIgnore
    private Person receiver;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "creation_datetime", nullable = false)
    private LocalDateTime creationDatetime;

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;
}
/*
@Data
class DirectMessageId implements Serializable {
    private String senderEmail;
    private String receiverEmail;
}

 */
