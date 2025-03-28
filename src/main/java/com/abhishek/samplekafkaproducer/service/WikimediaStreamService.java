package com.abhishek.samplekafkaproducer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;

@Service
public class WikimediaStreamService {

    private static final Logger logger = LoggerFactory.getLogger(WikimediaStreamService.class);

    private final WebClient webClient;
    private final KafkaProducerService kafkaProducerService;

    public WikimediaStreamService(WebClient.Builder webClientBuilder, KafkaProducerService kafkaProducerService,  @Value("${app.wikimediaStreamUrl}") String wikimediaStreamUrl) {
        this.webClient = webClientBuilder.baseUrl(wikimediaStreamUrl)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .keepAlive(true)
                        .responseTimeout(Duration.ofMinutes(5))))
                .build();
        this.kafkaProducerService = kafkaProducerService;
    }

    public void startStreaming() {
        logger.info("Starting to stream data from Wikimedia...");

        webClient.get()
                .uri("/v2/stream/recentchange")
                .retrieve()
                .bodyToFlux(String.class)
                .onBackpressureBuffer(10000)
                .flatMap(event -> Mono.fromRunnable(() -> kafkaProducerService.sendMessage(event))
                        .onErrorResume(ex -> {
                            logger.error("Failed to send message to Kafka: {}", ex.getMessage());
                            return Mono.empty();
                        }))
                .doOnError(error -> logger.error("Stream error: {}", error.getMessage()))
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(5)).filter(Objects::nonNull))
                .onErrorResume(error -> {
                    logger.error("Stream failed permanently, applying fallback: {}", error.getMessage());
                    return Flux.empty();
                })
                .subscribe();
    }
}
