package com.learnreactivespring.handler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest

@RunWith(SpringRunner.class)

@AutoConfigureWebTestClient
public class FunctionHandlerTest {

	@Autowired
	WebTestClient webTestClient;

	@Test
	public void testFunctionFlux() {
		Flux<Integer> responseBody = webTestClient.get().uri("/flux-function").accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange().expectStatus().isOk().returnResult(Integer.class).getResponseBody();
		StepVerifier.create(responseBody).expectSubscription().expectNext(1, 2, 3).verifyComplete();
	}

	@Test
	public void testFunctionMono() {
		webTestClient.get().uri("/mono-function").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus()
				.isOk().expectBody(String.class).value(s -> s.equalsIgnoreCase("Abhinav"));

	}

}
