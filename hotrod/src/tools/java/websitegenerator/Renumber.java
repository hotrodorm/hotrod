package websitegenerator;

import java.io.File;

public class Renumber {

  public static void main(final String[] args) {

    if (args.length != 1) {
      usage();
      System.exit(1);
      return;
    }

    File sourceDir = new File(args[0]);

    display("Source dir: " + sourceDir.getPath());

    // Check source dir

    if (!sourceDir.exists()) {
      display("Source dir '" + sourceDir.getAbsolutePath() + "' does not exist.");
      System.exit(1);
      return;
    }
    if (!sourceDir.isDirectory()) {
      display("Source dir '" + sourceDir.getAbsolutePath() + "' must be adirectory.");
      System.exit(1);
      return;
    }

    // Renumber

    try {
      ArticleRenumberer ar = new ArticleRenumberer(sourceDir);
      ar.renumber();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
      return;
    }

    display("Renumbering completed successfuly.");

  }

  private static void usage() {
    display("Usage: renumber <source-dir>");
  }

  private static void display(final String txt) {
    System.out.println(txt);
  }

}
