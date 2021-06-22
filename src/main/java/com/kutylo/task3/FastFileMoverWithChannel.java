package com.kutylo.task3;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class FastFileMoverWithChannel implements FastFileMover {
  @Override
  public void moveFile(String path1, String path2) {
    try (RandomAccessFile accessFile = new RandomAccessFile(path1, "rw");
         RandomAccessFile accessFileToMove = new RandomAccessFile(path2, "rw")) {

      FileChannel writeChannel = accessFileToMove.getChannel();
      ByteBuffer buffer = ByteBuffer.allocate(48);
      buffer.flip();

      while (buffer.hasRemaining()) {
        writeChannel.write(buffer);
      }
      buffer.flip();
    } catch (IOException e) {
      e.printStackTrace();
    }
    deleteFile(path1);
  }

  public void deleteFile(String path) {
    try {
      Files.delete(Path.of(path));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
