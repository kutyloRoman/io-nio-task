package com.kutylo.task2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileUtils {

  public static void writeToFile(String filePath, String result) {
    try (FileWriter fileWriter = new FileWriter(filePath)) {
      fileWriter.write(result);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void writeToFile(String filePath, Map<String, Long> result) {
    try (FileWriter fileWriter = new FileWriter(filePath)) {
      result.forEach((key, value) -> {
        try {
          fileWriter.write(key + ":" + value);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
