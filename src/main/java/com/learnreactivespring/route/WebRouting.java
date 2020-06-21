package com.learnreactivespring.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.learnreactivespring.handler.FunctionHandler;

@Configuration
public class WebRouting {

	@Bean
	public RouterFunction<ServerResponse> routingfunction(FunctionHandler functionHandler) {
		return RouterFunctions
				.route(GET("/flux-function").and(accept(MediaType.APPLICATION_JSON_UTF8)),
						functionHandler::getFluxThroughFunction)
				.andRoute(GET("/mono-function").and(accept(MediaType.APPLICATION_JSON_UTF8)),
						functionHandler::getMonoThroughFunction);
	}

}
