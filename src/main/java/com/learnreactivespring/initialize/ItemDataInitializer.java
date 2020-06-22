package com.learnreactivespring.initialize;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;

@Component
@Profile("dev")
public class ItemDataInitializer implements CommandLineRunner {

	@Autowired
	ItemReactiveRepository itemReactiveRepository;
	
	@Override
	public void run(String... args) throws Exception {
		initializeData();
		
	}

	private void initializeData() {
		List<Item> itemList = Arrays.asList(new Item(null, "Samsung TV", 400.0), new Item(null, "LG TV", 420.0),
				new Item(null, "Apple Watch", 299.99), new Item(null, "Beats Headphones", 149.99));
		itemReactiveRepository.deleteAll().thenMany(Flux.fromIterable(itemList)).flatMap(itemReactiveRepository::save).thenMany(itemReactiveRepository.findAll()).subscribe((item) -> {
			System.out.println("Inserted from commandLine " + item.getId());
		});
		
	}

}
