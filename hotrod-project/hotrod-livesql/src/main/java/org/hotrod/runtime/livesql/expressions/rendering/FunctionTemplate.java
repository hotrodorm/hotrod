package org.hotrod.runtime.livesql.expressions.rendering;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.runtime.livesql.exceptions.InvalidFunctionException;
import org.hotrod.runtime.livesql.expressions.ComparableExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;

// Parameters:
// single: #{}
// vararg: #{prefix?suffix} or #{firstprefix?prefix?suffix}

// Examples:
// - localtimestamp -- no parenthesis.
// - random() -- no parameters.
// - sin(#{}) -- one parameter.
// - left(#{}, #{}) -- two parameters.
// - format(#{}#{, ?}) -- a single parameter, plus varargs prepended with comma-space and no suffix.
// - coalesce(#{?, ?}) -- vararg: first prefix is empty, main prefix is comma-space, and no suffix.
// - substring(#{} from #{} for #{}) -- three parameters with custom template.
// - (#{} || #{}) -- concatenation; parenthesis added to enforce precedence.
// - cast(#{} as numeric) -- custom template.
// - (#{}::numeric) -- cast; parenthesis added to enforce precedence.

public class FunctionTemplate {

  private static final String PLACEHOLDER_BEGIN = "#{";
  private static final String PLACEHOLDER_MARKER = "?";
  private static final String PLACEHOLDER_END = "}";
  private String pattern;
  private List<TemplateSegment> segments;

  public FunctionTemplate(final String pattern, final ComparableExpression... parameters) {
    if (pattern == null) {
      throw new InvalidFunctionException("Function cannot have an empty pattern.");
    }
    this.pattern = pattern;
    this.segments = new ArrayList<TemplateSegment>();
    int i = 0;
    int current = 0;
    int begin;
    int end;
    while ((begin = pattern.indexOf(PLACEHOLDER_BEGIN, current)) != -1) {
      this.segments.add(new Verbatim(pattern.substring(current, begin)));
      int body = begin + PLACEHOLDER_BEGIN.length();
      end = pattern.indexOf(PLACEHOLDER_END, body);
      if (end == -1) {
        throw new InvalidFunctionException(
            "Malformed parameter found in pattern '" + this.pattern + "': a parameter start '" + PLACEHOLDER_BEGIN
                + "' was found, but not the corresponding parameter end '" + PLACEHOLDER_END + "'.");
      } else if (end == body) { // single parameter
        if (i < parameters.length) {
          this.segments.add(new Parameter(parameters[i++]));
        } else {
          throw new InvalidFunctionException(
              "Not enough parameters for function with pattern '" + this.pattern + "': only " + parameters.length
                  + " parameters were provided, but the pattern seem to have more than that.");
        }
        current = end + PLACEHOLDER_END.length();
      } else { // vararg
        int m1 = pattern.indexOf(PLACEHOLDER_MARKER, body);
        if (m1 == -1 || m1 >= end) {
          throw new InvalidFunctionException("Invalid parameter '"
              + pattern.substring(begin, end + PLACEHOLDER_END.length()) + "' found in pattern '" + this.pattern
              + "': must be either a single positional without body as in '" + PLACEHOLDER_BEGIN + PLACEHOLDER_END
              + "', or a vararg with body like '" + PLACEHOLDER_BEGIN + "body" + PLACEHOLDER_END
              + "'; the body must include an '" + PLACEHOLDER_MARKER + "' to denote the parameter(s) insertion point.");
        }
        int m2 = pattern.indexOf(PLACEHOLDER_MARKER, m1 + PLACEHOLDER_MARKER.length());
        int other = pattern.indexOf(PLACEHOLDER_BEGIN, end + PLACEHOLDER_END.length());
        if (other != -1) {
          throw new InvalidFunctionException("Invalid parameter found at position " + other
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
        boolean first = true;
        while (i < parameters.length) {
          String computedPrefix;
          if (first) {
            first = false;
            computedPrefix = firstPrefix != null ? firstPrefix : prefix;
          } else {
            computedPrefix = prefix;
          }
          this.segments.add(new VarArg(computedPrefix, parameters[i++], suffix));
        }
        current = end + PLACEHOLDER_END.length();
      }
    }
    this.segments.add(new Verbatim(pattern.substring(current)));
    if (i < parameters.length) {
      throw new InvalidFunctionException("Too many parameters for function with pattern '" + this.pattern + "': "
          + parameters.length + " parameters were provided, but the pattern has only " + i + ".");
    }
  }

  public String toString() {
    return pattern;
  }

  public void renderTo(final QueryWriter w) {
    this.segments.forEach(s -> s.renderTo(w));
  }

  private interface TemplateSegment {
    void renderTo(final QueryWriter w);
  }

  private class Verbatim implements TemplateSegment {
    private String text;

    public Verbatim(final String text) {
      this.text = text;
    }

    @Override
    public void renderTo(final QueryWriter w) {
      w.write(this.text);
    }

  }

  private class Parameter implements TemplateSegment {
    private ComparableExpression expression;

    public Parameter(final ComparableExpression expression) {
      this.expression = expression;
    }

    @Override
    public void renderTo(final QueryWriter w) {
      this.expression.renderTo(w);
    }

  }

  private class VarArg implements TemplateSegment {

    private Verbatim prefix;
    private Parameter parameter;
    private Verbatim suffix;

    public VarArg(final String prefix, final ComparableExpression parameter, final String suffix) {
      this.prefix = new Verbatim(prefix);
      this.parameter = new Parameter(parameter);
      this.suffix = new Verbatim(suffix);
    }

    @Override
    public void renderTo(final QueryWriter w) {
      this.prefix.renderTo(w);
      this.parameter.renderTo(w);
      this.suffix.renderTo(w);
    }

  }

}
