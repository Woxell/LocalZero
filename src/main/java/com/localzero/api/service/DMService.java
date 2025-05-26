package com.localzero.api.service;

import com.localzero.api.Logger;
import com.localzero.api.entity.DirectMessage;
import com.localzero.api.repository.DirectMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DMService {

    @Autowired
    private DirectMessageRepository dmRepository;
    private final Logger logger = Logger.getInstance();

    public DirectMessage save(DirectMessage dm) {
        DirectMessage saved = null;
        try {
            saved = dmRepository.save(dm);
            logger.log(dm.getSenderEmail() + " --> " + dm.getReceiverEmail() + ": " + dm.getContent());
        } catch (Exception e) {
            logger.logError("Error saving DM: " + e.getMessage());
        }
        return saved;
    }

    public List<DirectMessage> findByReceiverEmail(String receiverEmail) {
        return dmRepository.findByReceiverEmail(receiverEmail);
    }

    public List<DirectMessage> findBySenderEmail(String senderEmail) {
        return dmRepository.findBySenderEmail(senderEmail);
    }

    public DirectMessage findById(Long id) {
        return dmRepository.findById(id).orElseThrow(() ->
                new RuntimeException(logger.logError("Direct message not found with id: " + id)));
    }

    public List<DirectMessage> findConversationBetween(String user1, String user2) {
        return dmRepository.findConversationBetween(user1, user2);
    }
}
