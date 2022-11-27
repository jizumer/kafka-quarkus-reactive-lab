package com.jizumer;

import io.smallrye.faulttolerance.api.ExponentialBackoff;
import io.smallrye.reactive.messaging.annotations.Blocking;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

import static io.smallrye.reactive.messaging.kafka.i18n.KafkaLogging.log;

@ApplicationScoped
public class MyReactiveMessagingApplication {

    private boolean simulateErrorOnMessage;

    public void setSimulateErrorOnMessage(boolean simulateErrorOnMessage) {
        this.simulateErrorOnMessage = simulateErrorOnMessage;
    }


    @Incoming("retry-forever-exponential-backoff")
    @Outgoing("print-message")
    @Retry(
            maxRetries = -1,
            delay = 2000
    )
    @ExponentialBackoff
    @Blocking
    public Message<String> retryForeverWithExponentialBackoff(Message<String> message) {
        simulateError();
        return message.withPayload(message.getPayload().toUpperCase());
    }

    private void simulateError() {
        if (simulateErrorOnMessage) {
            log.log(Logger.Level.ERROR, "Simulated error");
            throw new RuntimeException("Simulated error");
        }
    }

    @Incoming("print-message")
    public void sink(String message) {
        System.out.println(">> " + message);
    }
}
