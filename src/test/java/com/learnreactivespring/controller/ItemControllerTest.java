package com.learnreactivespring.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
import reactor.core.publisher.Mono;

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
				new Item(null, "Apple Watch", 299.99), new Item("ABC", "Beats Headphones", 149.99));
		itemReactiveRepository.deleteAll().thenMany(Flux.fromIterable(itemList)).flatMap(itemReactiveRepository::save).thenMany(itemReactiveRepository.findAll())
				.doOnNext((item) -> {
					System.out.println("Inserted from commandLine " + item.getId());
				}).blockLast();
	}

	@Test
	public void findAllItemsTest() {
		ListBodySpec<Item> expectBodyList = webTestClient.get().uri("/v1/items").accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange().expectStatus().isOk().expectBodyList(Item.class);
		expectBodyList.hasSize(4);
	}

	@Test
	public void findItemTest(){
		webTestClient.get().uri("/v1/item/{id}","ABC").exchange().expectStatus().isOk().expectBody().jsonPath("$.price",149.99);
	}

	@Test
	public void findItemTestWrongPath(){
		webTestClient.get().uri("/v1/item/{id}","ADC").exchange().expectStatus().isNotFound();
	}

	@Test
	public void createItem(){
		webTestClient.post().uri("/v1/item").contentType(MediaType.APPLICATION_JSON_UTF8)
				.body(Mono.just(new Item(null, "An new Item to be insterted", new Double(150))), Item.class)
				.exchange().expectStatus().isCreated().expectBody().jsonPath("$.id").isNotEmpty()
				.jsonPath("$.description").isEqualTo("An new Item to be insterted")
				.jsonPath("$.price").isEqualTo(new Double(150));
	}

	@Test
	public void updateItem() {
		Item item = new Item(null, "Abhinav", new Double(123));
		Mono<Item> monoItem = Mono.just(item);
		WebTestClient.BodyContentSpec bodyContentSpec = webTestClient.put().uri("/v1/item/{id}", "ABC")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.body(monoItem, Item.class).exchange()
				.expectStatus().isOk().expectBody().jsonPath("$.description").isEqualTo("Abhinav")
				.jsonPath("$.price").isEqualTo(new Double(123));
	}

	@Test
	public void deleteItem() {
		webTestClient.delete().uri("/v1/item/{id}","ABC").accept(MediaType.APPLICATION_JSON_UTF8).exchange().expectStatus().isOk().expectBody(Void.class);
	}

}
