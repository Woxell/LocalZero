package com.localzero.api.controller;

import com.localzero.api.entity.Notification;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping("/sse")
public class SSEController {

    private final ConcurrentMap<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> emitters2 = new ConcurrentHashMap<>();

    @GetMapping("/messages/{email}")
    public SseEmitter streamMessages(@PathVariable String email) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(email, emitter);
        emitter.onCompletion(() -> emitters.remove(email));
        emitter.onTimeout(() -> emitters.remove(email));
        return emitter;
    }

    // SSEProxy calls this method when a new message is authorized to be sent
    public void sendMessageToReceiver(String receiverEmail, Object message) {
        System.out.println("Sending message to receiver: " + receiverEmail);
        SseEmitter emitter = emitters.get(receiverEmail);
        if (emitter != null) {
            try {
                emitter.send(message);
            } catch (Exception e) {
                emitters.remove(receiverEmail);
            }
        }
    }
}