package com.kutylo.task3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FastFileMoverWithBuffer implements FastFileMover {
  @Override
  public void moveFile(String path1, String path2) {
    File originalFile = new File(path1);
    File fileToMove = new File(path2);

    try (FileInputStream inputStream = new FileInputStream(originalFile);
         FileOutputStream fileOutputStream = new FileOutputStream(fileToMove)) {
      byte[] buffer = new byte[1024];
      int length;
      while ((length = inputStream.read(buffer)) != -1) {
        fileOutputStream.write(buffer, 0, length);
      }
      if (originalFile.delete()) {
        System.out.println("File deleted successfully");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
