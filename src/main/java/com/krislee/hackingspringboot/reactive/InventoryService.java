package com.krislee.hackingspringboot.reactive;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class InventoryService {
    private ItemRepository itemRepository;
    private ItemByExampleRepository exampleRepository;

    InventoryService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    Flux<Item> getItems() {
        // imagine calling a remote service!
        return Flux.empty();
    }

    Flux<Item> search(String partialName, String partialDescription, boolean useAnd) {
        if (partialName != null) {
            if (partialDescription != null) {
                if (useAnd) {
                    return itemRepository
                            .findByNameContainingAndDescriptionContainingAllIgnoreCase(
                                    partialName, partialDescription);
                } else {
                    return itemRepository.findByNameContainingOrDescriptionContainingAllIgnoreCase(
                            partialName, partialDescription);
                }
            } else {
                return itemRepository.findByNameContaining(partialName);
            }
        } else {
            if (partialDescription != null) {
                return itemRepository.findByDescriptionContainingIgnoreCase(partialDescription);
            } else {
                return itemRepository.findAll();
            }
        }
    }

    Flux<Item> searchByExample(String name, String description, boolean useAnd) {
        Item item = new Item(name, description, 0.0);

        ExampleMatcher matcher = (useAnd // <2>
                ? ExampleMatcher.matchingAll() //
                : ExampleMatcher.matchingAny()) //
                    .withStringMatcher(StringMatcher.CONTAINING) // <3>
                    .withIgnoreCase() // <4>
                    .withIgnorePaths("price"); // <5>

        Example<Item> probe = Example.of(item, matcher);
        return itemRepository.findAll(probe);
    }
}
