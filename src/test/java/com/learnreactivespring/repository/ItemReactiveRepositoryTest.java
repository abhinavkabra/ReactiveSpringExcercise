package com.learnreactivespring.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.learnreactivespring.document.Item;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemReactiveRepositoryTest {

	@Autowired
	ItemReactiveRepository itemReactiveRepository;

	List<Item> itemList = Arrays.asList(new Item(null, "Samsung TV", 400.0), new Item(null, "LG TV", 420.0),
			new Item(null, "Apple Watch", 299.99), new Item(null, "Beats Headphones", 149.99),
			new Item("ABC", "Bose Headphones", 149.99));

	@Before
	public void setUp() {
		itemReactiveRepository.deleteAll().thenMany(Flux.fromIterable(itemList)).flatMap(itemReactiveRepository::save)
				.doOnNext(s -> {
					System.out.println(s.getId());
				}).blockLast();
	}

	@Test
	public void getAllItem() {
		Flux<Item> findAll = itemReactiveRepository.findAll();
		StepVerifier.create(findAll).expectSubscription().expectNextCount(5).verifyComplete();
	}

	@Test
	public void getItemById() {
		StepVerifier.create(itemReactiveRepository.findById("ABC")).expectSubscription()
				.expectNextMatches(item -> item.getDescription().equalsIgnoreCase("Bose Headphones")).verifyComplete();
	}

	@Test
	public void findItemByDescrition() {

		StepVerifier.create(itemReactiveRepository.findByDescription("Bose Headphones").log("findItemByDescrition : "))
				.expectSubscription().expectNextCount(1).verifyComplete();
	}

	@Test
	public void saveItem() {

		Item item = new Item(null, "Google Home Mini", 30.00);
		Mono<Item> savedItem = itemReactiveRepository.save(item);
		StepVerifier.create(savedItem.log("saveItem : ")).expectSubscription()
				.expectNextMatches(
						item1 -> (item1.getId() != null && item1.getDescription().equals("Google Home Mini")))
				.verifyComplete();

	}

	@Test
	public void updateItem() {
		Flux<Item> flatMap = itemReactiveRepository.findByDescription("Bose Headphones").map(i -> {
			i.setPrice(new Double(2000));
			return i;
		}).flatMap(itemReactiveRepository::save);
	
		StepVerifier.create(flatMap).expectSubscription().expectNextMatches(item -> item.getId()!=null && item.getPrice().equals(new Double(2000)));
	
	}
	
	@Test
    public void deleteItemById() {


        Mono<Void> deletedItem = itemReactiveRepository.findById("ABC") // Mono<Item>
                .map(Item::getId) // get Id -> Transform from one type to another type
                .flatMap((id) -> {
                    return itemReactiveRepository.deleteById(id);
                });

        StepVerifier.create(deletedItem.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll().log("The new Item List : "))
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();


    }

    @Test
    public void deleteItem() {


        Flux<Void> deletedItem = itemReactiveRepository.findByDescription("LG TV") // Mono<Item>
                .flatMap((item) -> {
                    return itemReactiveRepository.delete(item);
                });

        StepVerifier.create(deletedItem.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemReactiveRepository.findAll().log("The new Item List : "))
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();


    }
}
