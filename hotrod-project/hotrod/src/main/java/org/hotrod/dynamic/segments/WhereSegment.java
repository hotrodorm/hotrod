package org.hotrod.dynamic.segments;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hotrod.dynamic.DynamicExpressionException;
import org.hotrod.dynamic.DynamicExpressionFactory;
import org.hotrod.dynamic.ParameterContext;

public class WhereSegment extends DynamicSegment {

  private static final Logger log = Logger.getLogger(WhereSegment.class.getName());

  private String delimiter;
  private List<IfSegment> ifSegments;
  private String initial = "\nWHERE";
  private String separator = "\n  ";

  public WhereSegment(List<IfSegment> ifSegments, DynamicExpressionFactory factory) {
    this.delimiter = null;
    this.ifSegments = ifSegments;
  }

  public WhereSegment(String delimiter, List<IfSegment> ifSegments, DynamicExpressionFactory factory) {
    this.delimiter = delimiter;
    this.ifSegments = ifSegments;
  }

  @Override
  public void prepare(StaticSegmentConsumer sc, ParameterContext context) throws DynamicExpressionException {
    WhereSegmentConsumer wc = new WhereSegmentConsumer(sc, this.delimiter, this.initial, this.separator);
    for (IfSegment s : this.ifSegments) {
      wc.startNextEntry();
      s.prepare(wc, context);
    }
  }

  private static class WhereSegmentConsumer implements StaticSegmentConsumer {

    private StaticSegmentConsumer parentConsumer;
    private String delimiter;
    private boolean first;

    private String initial;
    private String separator;
    private boolean nextEntry;

    public WhereSegmentConsumer(StaticSegmentConsumer parentConsumer, String delimiter, String initial,
        String separator) {
      this.parentConsumer = parentConsumer;
      this.delimiter = delimiter;
      this.first = true;
      this.initial = initial;
      this.separator = separator;
      this.nextEntry = false;
    }

    public void startNextEntry() {
      this.nextEntry = true;
    }

    private static final Pattern AND_OR_PREFIX_PATTERN = Pattern.compile("^(\\s*(?:and|or))");

    @Override
    public void consume(String literal) {
      if (this.first) {
        this.first = false;
        this.parentConsumer.consume(this.initial);
        String ll = literal.toLowerCase();

        Matcher matcher = AND_OR_PREFIX_PATTERN.matcher(ll);
        boolean find = matcher.find();
        if (find) {
          String remove = matcher.group(1);
          this.parentConsumer.consume(literal.substring(remove.length()));
        } else {
          this.parentConsumer.consume(literal);
        }
        this.nextEntry = false;
      } else {
        if (this.nextEntry) {
          this.parentConsumer.consume(this.separator + literal);
          this.nextEntry = false;
        } else {
          this.parentConsumer.consume(literal);
        }
      }
    }

    @Override
    public void consume(ParameterSegment s) {
      this.first = false;
      this.parentConsumer.consume(s);
    }

  }

}
