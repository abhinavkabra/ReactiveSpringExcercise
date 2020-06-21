package com.learnreactivespring;

import java.time.Duration;

import org.junit.Test;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoTests {

	@Test
	public void testMonoAndFlux() {
		Flux<String> just = Flux.just("Abhinav", "Shreya", "Shrenav").delayElements(Duration.ofSeconds(2L));

		StepVerifier.create(just).expectSubscription().expectNext("Abhinav", "Shreya", "Shrenav").verifyComplete();

		try {
			Thread.sleep(20000L);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

	}

}
