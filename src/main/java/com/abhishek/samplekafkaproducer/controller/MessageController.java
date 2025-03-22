package com.abhishek.samplekafkaproducer.controller;

import com.abhishek.samplekafkaproducer.service.MessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public Mono<String> createProduct(@RequestParam String message) {
        messageService.sendMessage(message);
        return Mono.just("Message sent successfully");
    }

}
