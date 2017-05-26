package websitegenerator;

public class Article {

  private String filenname;
  private String title;

  private Integer chapterNumber;
  private Integer articleNumber;
  private String tail;

  public Article(final String filename, final String title) {

    this.filenname = filename;
    this.title = title;

    if (filenname == null) {
      this.chapterNumber = 0;
      this.articleNumber = 0;
      this.tail = null;
      return;
    }

    int dash = filenname.indexOf('-');
    if (dash == -1) {
      this.chapterNumber = 0;
      this.articleNumber = 0;
      this.tail = null;
      return;
    }
    this.tail = filenname.substring(dash + 1);

    String numbers = filenname.substring(0, dash);
    int dot = numbers.indexOf('.');
    if (dot == -1) {
      this.chapterNumber = lenientParse(numbers);
      this.articleNumber = 0;
    } else {
      this.chapterNumber = lenientParse(numbers.substring(0, dot));
      this.articleNumber = lenientParse(numbers.substring(dot + 1));
    }
  }

  private Integer lenientParse(final String txt) {
    if (txt == null) {
      return 0;
    }
    try {
      return new Integer(txt);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

  // Indexing

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((articleNumber == null) ? 0 : articleNumber.hashCode());
    result = prime * result + ((chapterNumber == null) ? 0 : chapterNumber.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Article other = (Article) obj;
    if (articleNumber == null) {
      if (other.articleNumber != null)
        return false;
    } else if (!articleNumber.equals(other.articleNumber))
      return false;
    if (chapterNumber == null) {
      if (other.chapterNumber != null)
        return false;
    } else if (!chapterNumber.equals(other.chapterNumber))
      return false;
    return true;
  }

  public String produceFileName(final ArticleNumberFormatter cf, final ArticleNumberFormatter af,
      final boolean hasMultipleArticles) {
    return cf.format(this.chapterNumber)
        + (this.articleNumber.equals(0) && !hasMultipleArticles ? "" : "-" + af.format(this.articleNumber)) + "-"
        + this.tail;
  }

  // Setters

  public void setChapterNumber(Integer chapterNumber) {
    this.chapterNumber = chapterNumber;
  }

  void setArticleNumber(final Integer n) {
    this.articleNumber = n;
  }

  // Getters

  public String getFileName() {
    return filenname;
  }

  public String getTitle() {
    return title;
  }

  public Integer getChapterNumber() {
    return chapterNumber;
  }

  public Integer getArticleNumber() {
    return articleNumber;
  }

  public String getTail() {
    return tail;
  }

}
