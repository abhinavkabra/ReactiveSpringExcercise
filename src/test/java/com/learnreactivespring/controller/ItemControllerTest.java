package com.learnreactivespring.controller;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ListBodySpec;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
public class ItemControllerTest {

	@Autowired
	WebTestClient webTestClient;

	@Autowired
	ItemReactiveRepository itemReactiveRepository;

	@Before
	public void setUp() {
		List<Item> itemList = Arrays.asList(new Item(null, "Samsung TV", 400.0), new Item(null, "LG TV", 420.0),
				new Item(null, "Apple Watch", 299.99), new Item(null, "Beats Headphones", 149.99));
		itemReactiveRepository.deleteAll().thenMany(Flux.fromIterable(itemList)).flatMap(itemReactiveRepository::save).thenMany(itemReactiveRepository.findAll())
				.subscribe((item) -> {
					System.out.println("Inserted from commandLine " + item.getId());
				});
	}

	@Test
	public void findAllItemTest() {
		ListBodySpec<Item> expectBodyList = webTestClient.get().uri("/v1/items").accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange().expectStatus().isOk().expectBodyList(Item.class);
		expectBodyList.hasSize(4);
	}

}
