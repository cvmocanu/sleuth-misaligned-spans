package net.mocanu.tracing_issue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableBinding(Sink.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl("http://127.0.0.1:28080") // we will run WireMock at this address (see ApplicationTests)
                .build();
    }

}
