package net.mocanu.tracing_issue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    private final WebClient webClient;

    private KafkaConsumer(WebClient webClient) {
        this.webClient = webClient;
    }

    @StreamListener(Sink.INPUT)
    public void consume(Message<String> message) {
        String personName = message.getPayload();

        String response = webClient.post()
                .uri("/")
                .syncBody(personName)
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(String.class))
                .block();

        LOGGER.info("HTTP API RESPONSE: {}", response);
    }

}
