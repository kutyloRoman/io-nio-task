package com.kutylo.task2;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DiskAnalyzer {

  public Map<String, Long> getLargestFilesInSystem(int amount) {
    Map<String, Long> filesMap = new HashMap<>();
    FileSystem fileSystem = FileSystems.getDefault();

    fileSystem.getRootDirectories().forEach(root -> {
      File rootDir = root.toFile();
      getFilesInFolder(rootDir, filesMap);
    });

    return getLargestFiles(filesMap, amount);
  }

  public double getAverageFileSizeInDirectory(String path) {
    Map<String, Long> filesMap = new HashMap<>();
    getFilesInFolder(new File(path), filesMap);
    return getAverageSize(filesMap);
  }

  public String getFileWithMaximumNumberOfLetter(Character character) {
    FileSystem fileSystem = FileSystems.getDefault();
    Map<File, Integer> filesMap = new HashMap<>();

    fileSystem.getRootDirectories().forEach(root -> {
      File rootDir = root.toFile();
      getLetterFrequencyInFIleName(rootDir, filesMap, character);
    });

    return getFileWithMaximumLetterInName(filesMap).getAbsolutePath();
  }

  public Map<String, Long> getFilesAndFoldersNumberByLetter(Character character) {
    Map<String, Long> filesMap = new HashMap<>();
    filesMap.put("files", 0l);
    filesMap.put("folders", 0l);

    FileSystem fileSystem = FileSystems.getDefault();

    fileSystem.getRootDirectories().forEach(root -> {
      File rootDir = root.toFile();
      getNumberByLetter(rootDir, filesMap, character);
    });
    return filesMap;
  }

  private double getAverageSize(Map<String, Long> filesMap) {
    return filesMap.entrySet()
        .stream()
        .map(Map.Entry::getValue)
        .mapToLong(Long::longValue)
        .average()
        .orElse(0.0);
  }


  private File getFileWithMaximumLetterInName(Map<File, Integer> filesMap) {
    return filesMap.entrySet().stream().max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElseThrow(CannotFindFileException::new);
  }

  private Map<String, Long> getLargestFiles(Map<String, Long> filesMap, int amount) {
    return filesMap.entrySet()
        .stream()
        .distinct()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
        .limit(amount)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
  }

  private void getFilesInFolder(File file, Map<String, Long> filesMap) {
    if (Objects.isNull(file.listFiles())) {
      filesMap.put(file.getName(), file.length());
      return;
    }

    Arrays.asList(file.listFiles())
        .forEach(folder -> getFilesInFolder(folder, filesMap));
  }

  private void getNumberByLetter(File file, Map<String, Long> filesMap, Character character) {
    if (Objects.isNull(file.listFiles())) {
      if (nameBeginWithLetter(file, character)) {
        long amount = filesMap.get("files");
        filesMap.put("files", amount + 1);
        return;
      }
      return;
    }

    if (nameBeginWithLetter(file, character)) {
      long foldersAmount = filesMap.get("folders");
      filesMap.put("folders", foldersAmount + 1);
    }

    Arrays.asList(file.listFiles())
        .forEach(folder -> getNumberByLetter(folder, filesMap, character));
  }

  private boolean nameBeginWithLetter(File file, Character character) {
    return file.getName().charAt(0) == character;
  }

  private void getLetterFrequencyInFIleName(File file, Map<File, Integer> filesMap, Character character) {
    if (Objects.isNull(file.listFiles())) {
      filesMap.put(file, getLetterFrequency(file, character));
      return;
    }

    Arrays.asList(file.listFiles())
        .forEach(folder -> getLetterFrequencyInFIleName(folder, filesMap, character));
  }

  private int getLetterFrequency(File file, Character character) {
    String fileName = file.getName();
    int count = 0;
    for (int i = 0; i < fileName.length(); i++) {
      Character ch = fileName.charAt(i);
      if (ch.equals(character)) {
        count++;
      }
    }
    return count;
  }

}
