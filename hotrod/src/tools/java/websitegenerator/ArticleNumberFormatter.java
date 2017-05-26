package websitegenerator;

import java.text.DecimalFormat;

public class ArticleNumberFormatter {

  private DecimalFormat df;

  public ArticleNumberFormatter(final Integer max) {
    String sn = "" + max;
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < sn.length(); i++) {
      sb.append("0");
    }
    this.df = new DecimalFormat(sb.toString());
  }

  public String format(final Integer n) {
    return this.df.format(n);
  }

}
