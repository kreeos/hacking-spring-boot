package com.krislee.hackingspringboot.reactive;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomeController {
    private ItemRepository itemRepository;
    private CartRepository cartRepository;
    private CartService cartService;
    private InventoryService inventoryService;

    public HomeController(ItemRepository itemRepository, CartRepository cartRepository
        , CartService cartService, InventoryService inventoryService) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.inventoryService = inventoryService;
    }
    @GetMapping
    Mono<Rendering> home() { // <1>
        return Mono.just(Rendering.view("home.html") // <2>
                .modelAttribute("items", //
                        this.itemRepository.findAll()) // <3>
                .modelAttribute("cart", //
                        this.cartRepository.findById("My Cart") // <4>
                                .defaultIfEmpty(new Cart("My Cart")))
                .build());
    }
    // end::2[]

    @PostMapping("/add/{id}")
    Mono<String> addToCart(@PathVariable String id) {
        return this.cartService.addToCart("My Cart", id)
                .thenReturn("redirect:/");
    }

    @PostMapping
    Mono<String> createItem(@ModelAttribute Item newItem) {
        return this.itemRepository.save(newItem) //
                .thenReturn("redirect:/");
    }

    @DeleteMapping("/delete/{id}")
    Mono<String> deleteItem(@PathVariable String id) {
        return this.itemRepository.deleteById(id) //
                .thenReturn("redirect:/");
    }

    // tag::search[]
    @GetMapping("/search") // <1>
    Mono<Rendering> search( //
                            @RequestParam(required = false) String name, // <2>
                            @RequestParam(required = false) String description, //
                            @RequestParam boolean useAnd) {
        return Mono.just(Rendering.view("home.html") // <3>
                .modelAttribute("items", //
                        inventoryService.searchByExample(name, description, useAnd)) // <4>
                .modelAttribute("cart", //
                        this.cartRepository.findById("My Cart")
                                .defaultIfEmpty(new Cart("My Cart")))
                .build());
    }
}
