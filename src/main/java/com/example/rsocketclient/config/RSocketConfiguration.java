package com.example.rsocketclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
public class RSocketConfiguration {

    @Bean
    public Mono<RSocketRequester> rSocketRequester(
            RSocketStrategies rSocketStrategies
    ) {
        return RSocketRequester.builder()
                .rsocketStrategies(rSocketStrategies)
                .connectWebSocket(getURI());
    }

    private URI getURI() {
        return URI.create("ws://localhost:8080/rsocket");
    }
}
