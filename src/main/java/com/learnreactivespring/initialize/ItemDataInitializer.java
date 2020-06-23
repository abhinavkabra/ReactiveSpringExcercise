package com.learnreactivespring.initialize;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import com.learnreactivespring.document.ItemCapped;
import com.learnreactivespring.repository.ItemReactiveCappedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;

@Component
@Profile("dev")
public class ItemDataInitializer implements CommandLineRunner {

	@Autowired
	ItemReactiveRepository itemReactiveRepository;

	@Autowired
	ItemReactiveCappedRepository itemReactiveCappedRepository;

	@Autowired
	MongoOperations mongoOperations;
	
	@Override
	public void run(String... args) throws Exception {
		initializeData();
		createCappedCollection();
		dataSetUpforCappedCollection();
	}

	private void createCappedCollection() {
		mongoOperations.dropCollection(ItemCapped.class);
		mongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped());
	}

	private void initializeData() {
		List<Item> itemList = Arrays.asList(new Item(null, "Samsung TV", 400.0), new Item(null, "LG TV", 420.0),
				new Item(null, "Apple Watch", 299.99), new Item(null, "Beats Headphones", 149.99));
		itemReactiveRepository.deleteAll().thenMany(Flux.fromIterable(itemList)).flatMap(itemReactiveRepository::save).thenMany(itemReactiveRepository.findAll()).subscribe((item) -> {
			System.out.println("Inserted from commandLine " + item.getId());
		});
		
	}

	public void dataSetUpforCappedCollection(){
		Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofSeconds(1))
				.map(i -> new ItemCapped(null,"Random Item " + i, (100.00+i)));

		itemReactiveCappedRepository
				.insert(itemCappedFlux)
				.subscribe((itemCapped -> {
					System.out.println("Inserted Item is " + itemCapped);
				}));

	}
}
