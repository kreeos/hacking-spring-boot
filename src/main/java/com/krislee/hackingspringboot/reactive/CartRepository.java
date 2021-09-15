package com.krislee.hackingspringboot.reactive;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface CartRepository extends ReactiveCrudRepository<Cart, String> {
    
}
