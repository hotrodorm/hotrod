package tests;

public class SplitTest {

  public static void main(final String[] args) {
    String txt = "\ndef";
    String[] lines = txt.split("\n");
    System.out.println("--- Lines: " + (lines == null ? "null" : "" + lines.length) + " ---");
    if (lines != null) {
      for (String line : lines) {
        System.out.println("> line[" + line.length() + "]: '" + line + "'");
      }
    }
    System.out.println("--- end of lines ---");
  }

}
