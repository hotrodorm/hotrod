package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.utils.ClassWriter;
import org.hotrod.utils.SUtil;

//Arguments:
//arg: #{}
//vararg: #{prefix?suffix} or #{firstprefix?prefix?suffix}

//Examples:
//- localtimestamp -- no parenthesis.
//- random() -- no parameters.
//- sin(#{}) -- one parameter.
//- left(#{}, #{}) -- two parameters.
//- format(#{}#{, ?}) -- a single parameter, plus varargs prepended with comma-space and no suffix.
//- coalesce(#{?, ?}) -- vararg: first prefix is empty, main prefix is comma-space, and no suffix.
//- substring(#{} from #{} for #{}) -- three parameters with custom template.
//- (#{} || #{}) -- concatenation; parenthesis added to enforce precedence.
//- cast(#{} as numeric) -- custom template.
//- (#{}::numeric) -- cast; parenthesis added to enforce precedence.

public class LiveSQLFunctionTemplateParser {

  private static final String PLACEHOLDER_BEGIN = "#{";
  private static final String PLACEHOLDER_MARKER = "?";
  private static final String PLACEHOLDER_END = "}";

  private String pattern;

  public List<Section> parse(final String pattern) throws InvalidLiveSQLFunctionTemplateException {
    if (pattern == null) {
      throw new InvalidLiveSQLFunctionTemplateException("Function cannot have an empty pattern.");
    }
    if (pattern.isEmpty()) {
      throw new InvalidLiveSQLFunctionTemplateException("Function cannot have an empty pattern.");
    }
    this.pattern = pattern;
    List<Section> sections = new ArrayList<>();

    int current = 0;
    int begin;
    int end;
    while ((begin = pattern.indexOf(PLACEHOLDER_BEGIN, current)) != -1) {
      if (begin > current) {
        sections.add(new LiteralSection(pattern.substring(current, begin)));
      }
      int body = begin + PLACEHOLDER_BEGIN.length();
      end = pattern.indexOf(PLACEHOLDER_END, body);
      if (end == -1) {
        throw new InvalidLiveSQLFunctionTemplateException(
            "Malformed parameter found in pattern '" + this.pattern + "': a parameter start '" + PLACEHOLDER_BEGIN
                + "' was found, but not the corresponding parameter end '" + PLACEHOLDER_END + "'.");
      } else if (end == body) { // arg
        sections.add(new ArgSection());
        current = end + PLACEHOLDER_END.length();
      } else { // vararg
        int m1 = pattern.indexOf(PLACEHOLDER_MARKER, body);
        if (m1 == -1 || m1 >= end) {
          throw new InvalidLiveSQLFunctionTemplateException("Invalid parameter '"
              + pattern.substring(begin, end + PLACEHOLDER_END.length()) + "' found in pattern '" + this.pattern
              + "': must be either a single positional without body as in '" + PLACEHOLDER_BEGIN + PLACEHOLDER_END
              + "', or a vararg with body like '" + PLACEHOLDER_BEGIN + "body" + PLACEHOLDER_END
              + "'; the body must include an '" + PLACEHOLDER_MARKER + "' to denote the parameter(s) insertion point.");
        }
        int m2 = pattern.indexOf(PLACEHOLDER_MARKER, m1 + PLACEHOLDER_MARKER.length());
        int other = pattern.indexOf(PLACEHOLDER_BEGIN, end + PLACEHOLDER_END.length());
        if (other != -1) {
          throw new InvalidLiveSQLFunctionTemplateException("Invalid parameter found at position " + other
              + "; there can be no more parameters after a vararg parameter '" + PLACEHOLDER_BEGIN + "body"
              + PLACEHOLDER_END + "'.'");
        }
        String firstPrefix;
        String prefix;
        String suffix;
        if (m2 == -1 || m2 >= end) { // one marker
          firstPrefix = null;
          prefix = pattern.substring(body, m1);
          suffix = pattern.substring(m1 + PLACEHOLDER_MARKER.length(), end);
        } else { // two markers
          firstPrefix = pattern.substring(body, m1);
          prefix = pattern.substring(m1 + PLACEHOLDER_MARKER.length(), m2);
          suffix = pattern.substring(m2 + PLACEHOLDER_MARKER.length(), end);
        }
        sections.add(new VarArgSection(firstPrefix, prefix, suffix));
        current = end + PLACEHOLDER_END.length();
      }
    }
    if (current < pattern.length()) {
      sections.add(new LiteralSection(pattern.substring(current)));
    }
    return sections;
  }

  public static interface Section {
    void printTo(ClassWriter w);
  }

  public static class LiteralSection implements Section {

    private String literal;

    public LiteralSection(String literal) {
      this.literal = coalesce(literal);
    }

    public String getLiteral() {
      return literal;
    }

    @Override
    public void printTo(ClassWriter w) {
      w.print(".literal(\"" + SUtil.escapeJavaString(literal) + "\")");
    }

  }

  public static class ArgSection implements Section {

    @Override
    public void printTo(ClassWriter w) {
      w.print(".arg()");
    }

  }

  public static class VarArgSection implements Section {

    private String firstPrefix;
    private String prefix;
    private String suffix;

    public VarArgSection(String firstPrefix, String prefix, String suffix) {
      this.firstPrefix = coalesce(firstPrefix);
      this.prefix = coalesce(prefix);
      this.suffix = coalesce(suffix);
    }

    public String getFirstPrefix() {
      return firstPrefix;
    }

    public String getPrefix() {
      return prefix;
    }

    public String getSuffix() {
      return suffix;
    }

    @Override
    public void printTo(ClassWriter w) {
      w.print(".vararg()");
    }

  }

  public static class InvalidLiveSQLFunctionTemplateException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidLiveSQLFunctionTemplateException(String message) {
      super(message);
    }

  }

  private static String coalesce(String s) {
    return s == null ? "" : s;
  }

}
