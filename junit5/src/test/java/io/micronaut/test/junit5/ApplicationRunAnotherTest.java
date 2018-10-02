package io.micronaut.test.junit5;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MicronautTest
class ApplicationRunAnotherTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    TestService testService;

    @Test
    void testPingServer() {
        when(testService.doStuff()).thenReturn("mocked by " + ApplicationRunAnotherTest.class.getName());
        Assertions.assertEquals(
                "mocked by " +ApplicationRunAnotherTest.class.getName(),
                client.toBlocking().retrieve(HttpRequest.GET("/test"), String.class)
        );
        verify(testService).doStuff();
    }

    @Test
    void testPingServerAgain() {
        when(testService.doStuff()).thenReturn("changed by " + ApplicationRunAnotherTest.class.getName());
        Assertions.assertEquals(
                "changed by " +ApplicationRunAnotherTest.class.getName(),
                client.toBlocking().retrieve(HttpRequest.GET("/test"), String.class)
        );
        verify(testService).doStuff();
    }

    @MockBean(DefaultTestService.class)
    TestService testService() {
        return mock(TestService.class);
    }
}
