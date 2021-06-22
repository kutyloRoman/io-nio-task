package com.kutylo.task1.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FoodItem extends Item implements Serializable {
  private LocalDateTime expiredDate;
  private transient double weight;

  public FoodItem(int id, String name, String description, double price, LocalDateTime expiredDate, double weight) {
    super(id, name, description, price);
    this.expiredDate = expiredDate;
    this.weight = weight;
  }

  @Override
  public String toString() {
    return "FoodItem{" +
        "expiredDate=" + expiredDate +
        ", weight=" + weight +
        "} " + super.toString();
  }
}
