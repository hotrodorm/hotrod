package websitegenerator;

import java.io.File;

import websitegenerator.ArticleAssembler.CouldNotLoadTemplateException;
import websitegenerator.ArticleAssembler.CouldNotSaveArticleException;
import websitegenerator.Chapter.DuplicateArticleException;

public class Assembler {

  public static void main(final String[] args) {

    if (args.length != 3) {
      usage();
      System.exit(1);
      return;
    }

    File sourceDir = new File(args[0]);
    File destDir = new File(args[1]);
    File templateFile = new File(args[2]);

    display("Source dir: " + sourceDir.getPath());
    display("Dest dir: " + destDir.getPath());
    display("Template file: " + templateFile.getPath());

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

    // Check the template file

    if (!templateFile.exists()) {
      display("Template file  '" + templateFile.getAbsolutePath() + "' does not exist.");
      System.exit(1);
      return;
    }
    if (!templateFile.isFile()) {
      display("Template file '" + templateFile.getAbsolutePath() + "' must be a normal file.");
      System.exit(1);
      return;
    }

    // Run the assembler

    try {
      ArticleAssembler aa = new ArticleAssembler(sourceDir, destDir, templateFile);
      aa.assembleArticles();
    } catch (DuplicateArticleException e) {
      display(e.getMessage());
      System.exit(1);
      return;
    } catch (CouldNotLoadTemplateException e) {
      display(e.getMessage());
      System.exit(1);
      return;
    } catch (CouldNotSaveArticleException e) {
      display(e.getMessage());
      System.exit(1);
      return;
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
      return;
    }

    display("Assembling completed successfuly.");

  }

  private static void usage() {
    display("Usage: assembler <source-dir> <dest-dir> <template-file>");
  }

  private static void display(final String txt) {
    System.out.println(txt);
  }

}
