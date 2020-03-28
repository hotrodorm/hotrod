package websitegenerator;

import java.io.File;

public class Renumber {

  public static void main(final String[] args) {

    if (args.length != 2) {
      usage();
      System.exit(1);
      return;
    }

    File sourceDir = new File(args[0]);
    File destDir = new File(args[1]);

    display("Source dir: " + sourceDir.getPath());
    display("Source dir: " + destDir.getPath());

    // Check source dir

    if (!sourceDir.exists()) {
      display("Source dir '" + sourceDir.getAbsolutePath() + "' does not exist.");
      System.exit(1);
      return;
    }
    if (!sourceDir.isDirectory()) {
      display("Source dir '" + sourceDir.getAbsolutePath() + "' must be a directory.");
      System.exit(1);
      return;
    }

    // Check dest dir

    if (!destDir.exists()) {
      display("Dest dir '" + destDir.getAbsolutePath() + "' does not exist.");
      System.exit(1);
      return;
    }
    if (!destDir.isDirectory()) {
      display("Dest dir '" + destDir.getAbsolutePath() + "' must be a directory.");
      System.exit(1);
      return;
    }
    String[] files = destDir.list();
    if (files != null && files.length > 0) {
      display("Dest dir '" + destDir.getAbsolutePath() + "' must be empty.");
      System.exit(1);
      return;
    }

    // Renumber

    try {
      ArticleRenumberer ar = new ArticleRenumberer(sourceDir, destDir);
      ar.renumber();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
      return;
    }

    display("Renumbering completed successfuly.");

  }

  private static void usage() {
    display("Usage: renumber <source-dir> <dest-dir>");
  }

  private static void display(final String txt) {
    System.out.println(txt);
  }

}
