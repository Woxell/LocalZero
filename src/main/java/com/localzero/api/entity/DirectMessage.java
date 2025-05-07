package com.localzero.api.entity;

/**
 * @author: Adrian
 */

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@IdClass(DirectMessageId.class)
public class DirectMessage {

    @Id
    @Column(name = "sender_email", nullable = false)
    private String senderEmail;

    @Id
    @Column(name = "receiver_email", nullable = false)
    private String receiverEmail;

    @ManyToOne
    @JoinColumn(name = "sender_email", referencedColumnName = "email", insertable = false, updatable = false)
    private Person sender;

    @ManyToOne
    @JoinColumn(name = "receiver_email", referencedColumnName = "email", insertable = false, updatable = false)
    private Person receiver;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "creation_datetime", nullable = false)
    private LocalDateTime creationDatetime;
}

@Data
class DirectMessageId implements Serializable {
    private String senderEmail;
    private String receiverEmail;
}
