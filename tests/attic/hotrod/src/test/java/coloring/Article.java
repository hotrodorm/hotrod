package coloring;

/**
 * JavaDoc comment
 * 
 * @author myuser
 */
public class Article {

  // Simple inline comment

  private static final String DEFAULT_NAME = "New Article";

  private String filenname;
  private String title;
  private Integer chapterNumber;

  public Article(final String filename, final String title) {

    this.filenname = filename;
    this.title = title;

    if (filenname == null) {
      this.chapterNumber = 0;
      return;
    }

  }

  public String getFileName() {
    return filenname;
  }

}
