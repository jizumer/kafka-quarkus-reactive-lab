package com.jizumer;

import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class MyReactiveMessagingApplicationTest {

    @Inject
    MyReactiveMessagingApplication application;

    @Test
    void test() {
        assertEquals("HELLO", application.retryForeverWithExponentialBackoff(Message.of("Hello")).getPayload());
        assertEquals("BONJOUR", application.retryForeverWithExponentialBackoff(Message.of("bonjour")).getPayload());
    }
}