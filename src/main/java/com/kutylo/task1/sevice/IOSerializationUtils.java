package com.kutylo.task1.sevice;

import com.kutylo.task1.model.Item;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IOSerializationUtils implements SerializationUtils {
  private final String pathToFile = "D:\\Mentorship\\io-test.txt";

  @Override
  public <T extends Item> void serialize(T item) {
    try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(pathToFile))) {
      outputStream.writeObject(item);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public <T extends Item> T deserialize() {
    try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(pathToFile))) {
      return (T) inputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }
}
