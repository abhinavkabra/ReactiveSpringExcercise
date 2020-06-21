package com.learnreactivespring.controller;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class FluxAndMonoController {

	@GetMapping(value = "/flux", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Flux<Integer> getFlux(){
		return  Flux.just(1,2,3,4);
	}
	
	@GetMapping(value = "/flux-stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Long> getFluxStream(){
		return  Flux.interval(Duration.ofSeconds(1L));
	}
	
}
