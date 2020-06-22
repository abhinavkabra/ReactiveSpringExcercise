package com.learnreactivespring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ItemController {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @GetMapping("/v1/items")
    public Flux<Item> getAllItems() {
        return itemReactiveRepository.findAll();
    }

    @GetMapping("/v1/item/{id}")
    public Mono<ResponseEntity<Item>> getItemById(@PathVariable String id) {
        return itemReactiveRepository.findById(id).map((item) -> new ResponseEntity<>(item, HttpStatus.OK)).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/v1/item")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item){
        return itemReactiveRepository.save(item);
    }

    @PutMapping("/v1/item/{id}")
    public Mono<ResponseEntity> updateItem(@PathVariable String id, @RequestBody Item item) {
        return itemReactiveRepository.findById(id).flatMap(i ->{
            i.setDescription(item.getDescription());
            i.setPrice(item.getPrice());
            return itemReactiveRepository.save(i);
        }).map(updateItem -> new ResponseEntity(updateItem,HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/v1/item/{id}")
    public Mono<Void> deleteItemById(@PathVariable String id) {
        return itemReactiveRepository.deleteById(id);
    }

}
