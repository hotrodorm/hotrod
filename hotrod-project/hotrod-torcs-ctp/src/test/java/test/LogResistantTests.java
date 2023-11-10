package test;

import java.io.File;
import java.io.IOException;

import org.hotrod.torcs.ctp.LogResistantFormatter;

import test.FileUtils.CouldNotReadFromFileException;

public class LogResistantTests {

  public static void main(final String[] args) throws IOException, CouldNotReadFromFileException {

    String plan = new String(FileUtils.readFromFile(new File("docs/plan3.txt")));
    System.out.println("--- Plan (orig) ---\n" + plan + "\n\n");

    LogResistantFormatter f = new LogResistantFormatter();
    String[] lines = f.format(plan);

    System.out.println("--- Plan (" + lines.length + " lines) ---");
    for (String l : lines) {
      System.out.println(l);
    }
    System.out.println("--- end of plan ---");

  }

}
