package com.abhishek.samplekafkaproducer.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final KafkaProducerService kafkaProducerService;

    public MessageService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public void sendMessage(String message) {
        kafkaProducerService.sendMessage(message);
    }
}
