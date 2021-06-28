package com.example.rsocketclient.controller;

import com.example.rsocketclient.model.GreetingRequest;
import com.example.rsocketclient.model.GreetingResponse;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
public class RSocketController {

    private final RSocketRequester rSocketRequester;
    private final Mono<RSocketRequester> rSocketRequesterMono;

    public RSocketController(@Autowired RSocketRequester.Builder builder, Mono<RSocketRequester> rSocketRequesterMono) {
        this.rSocketRequesterMono = rSocketRequesterMono;
        WebsocketClientTransport ws = WebsocketClientTransport.create(URI.create("ws://localhost:8082"));

        this.rSocketRequester = builder.transport(ws);
        //this.rSocketRequester = builder.transport(TcpClientTransport.create("localhost",8080));
    }

    @GetMapping("/test")
    public Mono<GreetingResponse> greetingRequestMono(){
        return rSocketRequester.route("my-request-response")
                .data(new GreetingRequest("client-seth"))
                .retrieveMono(GreetingResponse.class);
    }

    @GetMapping("/hello")
    public String getHello(){
        return "Hello";
    }

    @GetMapping(value = "/socket/{author}")
    public Flux<GreetingResponse> getByAuthorViaSocket(@PathVariable String author) {
        return rSocketRequester.route("tweets.by.author").data(new GreetingRequest(author)).retrieveFlux(GreetingResponse.class);
    }
}
