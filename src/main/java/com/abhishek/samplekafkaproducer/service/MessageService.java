package com.abhishek.samplekafkaproducer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Value("${app.kafka.topic}")
    private String topic;
    private final KafkaTemplate<String, String> kafkaTemplate;


    public MessageService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(topic, message);
    }
}
