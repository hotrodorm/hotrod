package org.hotrod.dynamicsql.assembler;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.hotrod.dynamicsql.parameters.ParameterInstance;
import org.hotrod.dynamicsql.segments.StaticSegmentConsumer;

public class ListFormatterConsumer implements StaticSegmentConsumer, AutoCloseable {

  private StaticSegmentConsumer parentConsumer;
  private String prefix;
  private String separator;
  private String suffix;
  private String[] removePrefixes;

  private boolean isFirstSegment;
  private boolean atStartOfEntry;
  private Pattern removePattern;

  public ListFormatterConsumer(StaticSegmentConsumer parentConsumer, ListProcessor processor) {
    this.parentConsumer = parentConsumer;
    this.prefix = processor.getHeader();
    this.separator = processor.getSeparator();
    this.suffix = processor.getTail();
    this.removePrefixes = processor.getRemovePrefixes();

    this.isFirstSegment = true;
    this.atStartOfEntry = true;
    this.removePattern = (this.removePrefixes == null || this.removePrefixes.length == 0) ? null
        : Pattern.compile("^(\\s*(?:" + Arrays.stream(this.removePrefixes).filter(s -> s != null)
            .map(s -> s.toLowerCase()).collect(Collectors.joining("|")) + "))");
  }

  @Override
  public void startNextEntry() {
    this.atStartOfEntry = true;
  }

  @Override
  public void consume(String literal) {
    if (this.isFirstSegment) {
      this.isFirstSegment = false;
      if (this.prefix != null) {
        this.parentConsumer.consume(this.prefix);
      }
      if (this.removePattern == null) {
        this.parentConsumer.consume(literal);
      } else {
        Matcher matcher = this.removePattern.matcher(literal.toLowerCase());
        boolean find = matcher.find();
        if (find) {
          String remove = matcher.group(1);
          this.parentConsumer.consume(literal.substring(remove.length()));
        } else {
          this.parentConsumer.consume(literal);
        }
      }
    } else {
      if (this.atStartOfEntry) {
        if (this.separator != null) {
          this.parentConsumer.consume(this.separator);
        }
        this.parentConsumer.consume(literal);
      } else {
        this.parentConsumer.consume(literal);
      }
    }
    this.atStartOfEntry = false;
  }

  @Override
  public void consume(ParameterInstance s) {
    this.isFirstSegment = false;
    this.parentConsumer.consume(s);
  }

  @Override
  public void close() {
    if (this.suffix != null) {
      this.parentConsumer.consume(this.suffix);
    }
  }

}
