package websitegenerator;

public class TitleFinder {

  private static final String TITLE_INITIAL_DELIMITER = "<h1>";
  private static final String TITLE_END_DELIMITER = "</h1>";

  public static String findTitle(final String article, final String defaultValue) {
    if (article == null) {
      return defaultValue;
    }
    int initial = article.indexOf(TITLE_INITIAL_DELIMITER);
    if (initial == -1) {
      return defaultValue;
    }
    int end = article.indexOf(TITLE_END_DELIMITER, initial + TITLE_INITIAL_DELIMITER.length());
    if (end == -1) {
      return defaultValue;
    }
    return article.substring(initial + TITLE_INITIAL_DELIMITER.length(), end);
  }

}
