package com.krislee.hackingspringboot.reactive;

import org.springframework.data.annotation.Id;

import java.awt.Point;
import java.util.Date;
import java.util.Objects;

public class Item {
    private @Id String id;
    private String name;
    private String description;
    private double price;
    private String distributorRegion;
    private Date releaseDate;
    private int availableUnits;
    private Point locaition;
    private boolean active;

    private Item() {}

    Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    Item(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    // Getter, Setter and hashcode() is skipped for now
}
