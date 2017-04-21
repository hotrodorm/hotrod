package websitegenerator;

public class Article {

  private String title;
  private String filename;

  public Article(final String title, final String filename) {
    this.title = title;
    this.filename = filename;
  }

  public String getTitle() {
    return title;
  }

  public String getFilename() {
    return filename;
  }

}
