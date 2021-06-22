package com.kutylo.task3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FastFileMoverWithNio2 implements FastFileMover {
  @Override
  public void moveFile(String path1, String path2) {
    Path originalFile = Path.of(path1);
    Path fileToMove = Path.of(path2);
    try {
      Files.move(originalFile, fileToMove, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
