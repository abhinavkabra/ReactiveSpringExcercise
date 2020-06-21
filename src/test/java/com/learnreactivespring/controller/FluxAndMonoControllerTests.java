package com.learnreactivespring.controller;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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

import reactor.core.publisher.Flux;
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
		webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).expectBodyList(Integer.class).hasSize(4);
	}

	@Test
	public void textFluxStreamUsingResponseBody() {
		List<Integer> integerList = Arrays.asList(1,2,3,4);
		EntityExchangeResult<List<Integer>> returnResult = webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).expectBodyList(Integer.class).returnResult();
		List<Integer> responseBody = returnResult.getResponseBody();
		assertEquals(responseBody,integerList);		
	}
	
	@Test
	public void textFluxStreamUsingConsumeExchangeResultEntity() {
		List<Integer> integerList = Arrays.asList(1,2,3,4);
		webTestClient.get().uri("/flux").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).expectBodyList(Integer.class).consumeWith((response)->{
					List<Integer> responseBody = response.getResponseBody();
					assertEquals(responseBody,integerList);		
				});
	}
	
}
