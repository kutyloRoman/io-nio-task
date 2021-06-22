package com.kutylo.task1.sevice;

import com.kutylo.task1.model.Item;

public interface SerializationUtils {
  <T extends Item> void serialize(T item);

  <T extends Item> T deserialize();
}
