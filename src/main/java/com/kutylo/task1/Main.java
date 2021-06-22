package com.kutylo.task1;

import com.kutylo.task1.model.FoodItem;
import com.kutylo.task1.model.Item;
import com.kutylo.task1.sevice.IOSerializationUtils;
import com.kutylo.task1.sevice.SerializationUtils;

import java.time.LocalDateTime;

public class Main {
  public static void main(String[] args) {
    SerializationUtils serializationUtils = new IOSerializationUtils();
    Item item = new FoodItem(1, "Chicken", "Hot chicken", 22.2, LocalDateTime.now(), 1.2);
    serializationUtils.serialize(item);

    FoodItem foodItem = serializationUtils.deserialize();
    System.out.println(foodItem);
  }
}
