package com.abhishek.samplekafkaproducer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WikimediaStreamService {

    private static final Logger logger = LoggerFactory.getLogger(WikimediaStreamService.class);

    private final WebClient webClient;
    private final KafkaProducerService kafkaProducerService;

    public WikimediaStreamService(WebClient.Builder webClientBuilder, KafkaProducerService kafkaProducerService,  @Value("${app.wikimediaStreamUrl}") String wikimediaStreamUrl) {
        this.webClient = webClientBuilder.baseUrl(wikimediaStreamUrl).build();
        this.kafkaProducerService = kafkaProducerService;
    }

    public void startStreaming() {
        logger.info("Starting to stream data from Wikimedia...");

        webClient.get()
                .uri("/v2/stream/recentchange")
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(kafkaProducerService::sendMessage)
                .doOnError(error -> logger.error("Error occurred: {}", error.getMessage()))
                .subscribe();
    }
}
