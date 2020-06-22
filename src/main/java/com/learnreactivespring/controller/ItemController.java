package com.learnreactivespring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;

@RestController
public class ItemController {

	@Autowired
	ItemReactiveRepository itemReactiveRepository;
	
	@GetMapping("/v1/items")
	public Flux<Item> getAllItem() {
		return itemReactiveRepository.findAll();
	}
	
}
