package com.kutylo.task2;

import java.util.Map;
import java.util.Scanner;

public class InputUtils {

  private final DiskAnalyzer diskAnalyzer = new DiskAnalyzer();
  private final Scanner scanner = new Scanner(System.in);
  private String resultFile;

  public void inputParameters() {
    System.out.println("Enter path to output file:");
    resultFile = scanner.nextLine() + "/result.txt";
    System.out.println("Enter number of function:");
    System.out.println("1 - Search for the file name with the maximum number of letter\n" +
        "2 -Print largest files (by size in bytes).\n" +
        "3 -The average file size in the specified directory\n" +
        "4 -The number of files and folders,begin with input letter).\n");
    int functionNumber = scanner.nextInt();
    callFunction(functionNumber);
  }

  private void callFunction(int number) {
    switch (number) {
      case 1:
        callFirstFunction();
        break;
      case 2:
        callSecondFunction();
        break;
      case 3:
        callThirdFunction();
        break;
      case 4:
        callFourthFunction();
        break;
      default:
        System.out.println("Incorrect number!");
    }
  }

  private void callFirstFunction() {
    System.out.println("Enter letter:");
    char letter = scanner.next().charAt(0);
    String result = diskAnalyzer.getFileWithMaximumNumberOfLetter(letter);
    System.out.println(result);
    FileUtils.writeToFile(result, String.format("File with maximum number of letter: %s - %s", letter, result));
  }

  private void callSecondFunction() {
    System.out.println("Enter amount of files:");
    int amount = scanner.nextInt();
    Map<String, Long> result = diskAnalyzer.getLargestFilesInSystem(amount);
    System.out.println(result);
    FileUtils.writeToFile(resultFile, result);
  }

  private void callThirdFunction() {
    System.out.println("Enter directory of files:");
    String directory = scanner.nextLine();
    double result = diskAnalyzer.getAverageFileSizeInDirectory(directory);
    System.out.println(result);
    FileUtils.writeToFile(resultFile, String.format("Average size in directory: %s - %s", directory, result));
  }

  private void callFourthFunction() {
    System.out.println("Enter letter:");
    char letter = scanner.next().charAt(0);
    Map<String, Long> result = diskAnalyzer.getFilesAndFoldersNumberByLetter(letter);
    System.out.println(result);
    FileUtils.writeToFile(resultFile, result);
  }
}
