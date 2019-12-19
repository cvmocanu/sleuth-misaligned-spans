package net.mocanu.tracing_issue;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

    private static WireMockServer wireMockServer;

    @Autowired
    private Sink sink;

    @BeforeClass
    public static void startMockServer() {
        wireMockServer = new WireMockServer(28_080);
        wireMockServer.start();
    }

    @AfterClass
    public static void stopMockServer() {
        wireMockServer.stop();
    }

    @Test
    public void test() {
        wireMockServer.stubFor(
                post("/")
                        .willReturn(
                                aResponse().withStatus(200)
                                           .withBody("Hello John")
                        )
        );

        SubscribableChannel input = sink.input();

        input.send(
                new GenericMessage<>("John")
        );
    }

}
