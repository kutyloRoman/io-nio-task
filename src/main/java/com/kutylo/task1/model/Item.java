package com.kutylo.task1.model;

import java.io.Serializable;

public class Item implements Serializable {
  private transient int id;
  private String name;
  private String description;
  private transient double price;

  public Item() {
  }

  public Item(int id, String name, String description, double price) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
  }

  @Override
  public String toString() {
    return "Item{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", description='" + description + '\'' +
        ", price=" + price +
        '}';
  }
}
