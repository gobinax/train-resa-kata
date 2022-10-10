package org.train.reservation.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate bookingReferenceClient(final RestTemplateBuilder restTemplateBuilder,
                                               @Value("${booking-reference.url}") String bookingReferenceUrl) {
        return restTemplateBuilder
                .rootUri(bookingReferenceUrl)
                .setReadTimeout(Duration.ofSeconds(30))
                .setConnectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Bean
    public RestTemplate trainDataClient(final RestTemplateBuilder restTemplateBuilder,
                                        @Value("${train-data.url}") String bookingReferenceUrl) {
        return restTemplateBuilder
                .rootUri(bookingReferenceUrl)
                .setReadTimeout(Duration.ofSeconds(30))
                .setConnectTimeout(Duration.ofSeconds(10))
                .build();
    }
}
