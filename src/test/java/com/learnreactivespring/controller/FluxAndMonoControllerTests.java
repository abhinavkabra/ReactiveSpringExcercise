package com.learnreactivespring.controller;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest
public class FluxAndMonoControllerTests {

	@Autowired
	WebTestClient webTestClient;

	@Test
	public void textFluxStreamUsingStepVerifier() {
		Flux<Integer> fluxBody = webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isOk().returnResult(Integer.class).getResponseBody();
		StepVerifier.create(fluxBody).expectSubscription().expectNext(1).expectNext(2).expectNext(3).expectNext(4)
				.verifyComplete();
	}

	@Test
	public void textFluxStreamUsingExpectBodyList() {
		webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).expectBodyList(Integer.class)
				.hasSize(4);
	}

	@Test
	public void textFluxStreamUsingResponseBody() {
		List<Integer> integerList = Arrays.asList(1, 2, 3, 4);
		EntityExchangeResult<List<Integer>> returnResult = webTestClient.get().uri("/flux")
				.accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectHeader()
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).expectBodyList(Integer.class).returnResult();
		List<Integer> responseBody = returnResult.getResponseBody();
		assertEquals(responseBody, integerList);
	}

	@Test
	public void textFluxStreamUsingConsumeExchangeResultEntity() {
		List<Integer> integerList = Arrays.asList(1, 2, 3, 4);
		webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).expectBodyList(Integer.class)
				.consumeWith((response) -> {
					List<Integer> responseBody = response.getResponseBody();
					assertEquals(responseBody, integerList);
				});
	}

	@Test
	public void textFluxInfiniteStreamUsingStepVerifier() {
		Flux<Long> fluxBody = webTestClient.get().uri("/flux-stream").accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange().expectStatus().isOk().returnResult(Long.class).getResponseBody();
		StepVerifier.create(fluxBody).expectSubscription().expectNext(0L).expectNext(1L).expectNext(2L).expectNext(3L)
				.expectNext(4L).thenCancel().verify();
	}
	
	@Test
	public void textMonoUsingStepVerifier() {
							  webTestClient.get().uri("/mono").accept(MediaType.APPLICATION_JSON_UTF8).exchange()
				.expectStatus().isOk().expectBody(String.class).value(s -> {
					s.equalsIgnoreCase("Abhinav");
				});
	
	
	
		/*StepVerifier.create(monoBody).expectSubscription().expectNext(1).expectNext(2).expectNext(3).expectNext(4)
				.verifyComplete();*/
	}

}
