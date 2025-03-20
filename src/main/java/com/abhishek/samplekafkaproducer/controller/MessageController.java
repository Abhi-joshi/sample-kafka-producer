package com.abhishek.samplekafkaproducer.controller;

import com.abhishek.samplekafkaproducer.service.MessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String message) {
        messageService.sendMessage(message);
        return "Message sent successfully to Kafka!";
    }

}
