package com.krislee.hackingspringboot.reactive;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface ItemByExampleRepository extends ReactiveQueryByExampleExecutor<Item> {
}
