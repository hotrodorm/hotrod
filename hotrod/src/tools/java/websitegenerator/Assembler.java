package websitegenerator;

import java.io.File;

import websitegenerator.ArticleAssembler.CouldNotLoadTemplateException;
import websitegenerator.ArticleAssembler.CouldNotSaveArticleException;
import websitegenerator.Chapter.DuplicateArticleException;

public class Assembler {

  public static void main(final String[] args) {

    File sourceDir = new File("docs/web-site/src");
    File destDir = new File("dist");
    File templateFile = new File("docs/web-site/templates/template.html");

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
      display("Dest dir '" + sourceDir.getAbsolutePath() + "' does not exist.");
      System.exit(1);
      return;
    }
    if (!destDir.isDirectory()) {
      display("Dest dir '" + sourceDir.getAbsolutePath() + "' must be a directory.");
      System.exit(1);
      return;
    }
    String[] files = destDir.list();
    if (files != null && files.length > 0) {
      display("Dest dir '" + sourceDir.getAbsolutePath() + "' must be empty.");
      System.exit(1);
      return;
    }

    // Check the template file

    if (!templateFile.exists()) {
      display("Template file  '" + sourceDir.getAbsolutePath() + "' does not exist.");
      System.exit(1);
      return;
    }
    if (!templateFile.isFile()) {
      display("Template file '" + sourceDir.getAbsolutePath() + "' must be a normal file.");
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

  private static void display(final String txt) {
    System.out.println(txt);
  }

}
