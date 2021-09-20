package com.krislee.hackingspringboot.reactive;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class InventoryService {
    private ItemRepository itemRepository;
    private CartRepository cartRepository;
    private ItemByExampleRepository exampleRepository;

    InventoryService(ItemRepository itemRepository, CartRepository cartRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
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


    Mono<Cart> addItemToCart(String cartId, String id) {
        return this.cartRepository.findById(cartId)
                .log("foundCart")
                .defaultIfEmpty(new Cart(cartId))
                .log("emptyCart")
                .flatMap(cart -> cart.getCartItems().stream()
                        .filter(cartItem -> cartItem.getItem()
                                .getId().equals(id))
                        .findAny()
                        .map(cartItem -> {
                            cartItem.increment();
                            return Mono.just(cart).log("newCartItem");
                        })
                        .orElseGet(() ->
                                this.itemRepository.findById(id)
                                        .log("fetchItem")
                                        .map(CartItem::new)
                                        .log("cartItem")
                                        .doOnNext(cartItem ->
                                                cart.getCartItems().add(cartItem))
                                        .map(cartItem -> cart)).log("addedCartItem"))
                .log("cartWithAnotherItem")
                .flatMap(this.cartRepository::save)
                .log("savedCart");
    }
}
