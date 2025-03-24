package com.abhishek.samplekafkaproducer.runner;

import com.abhishek.samplekafkaproducer.service.WikimediaStreamService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StreamRunner implements CommandLineRunner {

    private final WikimediaStreamService wikimediaStreamService;

    public StreamRunner(WikimediaStreamService wikimediaStreamService) {
        this.wikimediaStreamService = wikimediaStreamService;
    }

    @Override
    public void run(String... args) {
       wikimediaStreamService.startStreaming();
    }
}
