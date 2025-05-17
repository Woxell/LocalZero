package com.localzero.api.repository;

import com.localzero.api.entity.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DirectMessageRepository extends JpaRepository<DirectMessage, Long> {

    @Query("""
        SELECT dm FROM DirectMessage dm
        WHERE (dm.senderEmail = :user1 AND dm.receiverEmail = :user2)
           OR (dm.senderEmail = :user2 AND dm.receiverEmail = :user1) 
        ORDER BY dm.creationDatetime
    """)
    //OR för att se båda Conversations
    List<DirectMessage> findConversationBetween(String user1, String user2);
}
