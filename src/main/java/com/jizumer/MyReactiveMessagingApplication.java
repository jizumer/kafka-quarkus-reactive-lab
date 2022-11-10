package com.jizumer;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.faulttolerance.api.ExponentialBackoff;
import io.smallrye.reactive.messaging.annotations.Blocking;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.*;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.stream.Stream;

import static io.smallrye.reactive.messaging.kafka.i18n.KafkaLogging.log;

@ApplicationScoped
public class MyReactiveMessagingApplication {

    @ConfigProperty(name = "fail-on-message", defaultValue = "false")
    private boolean failOnMessage;

    @Inject
    @Channel("words-out")
    Emitter<String> emitter;


    /**
     * Consume the message from the "words-in" channel, uppercase it and send it to the uppercase channel.
     * Messages come from the broker.
     **/
    @Incoming("words-in")
    @Outgoing("uppercase")
    @Retry(
            maxRetries = -1,
            delay = 10
    )
    @ExponentialBackoff
    @Blocking
    public Message<String> toUpperCase(Message<String> message) {
        if (failOnMessage) {
            log.log(Logger.Level.ERROR, "Simulated error");
            throw new RuntimeException("Simulated error");
        }
        return message.withPayload(message.getPayload().toUpperCase());
    }

    /**
     * Consume the uppercase channel (in-memory) and print the messages.
     **/
    @Incoming("uppercase")
    public void sink(String word) {
        System.out.println(">> " + word);
    }
}
