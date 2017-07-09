package org.hotrod.runtime.dynamicsql.expressions;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;
import org.hotrod.runtime.util.SUtils;

public class TrimExpression extends DynamicSQLExpression {

  private String prefix;
  private LinkedHashSet<String> prefixOverrides;
  private String suffix;
  private LinkedHashSet<String> suffixOverrides;
  private DynamicSQLExpression[] expressions;

  public TrimExpression(final String prefix, final String prefixOverrides, final String suffix,
      final String suffixOverrides, final DynamicSQLExpression... expressions) {
    this.prefix = prefix;
    this.prefixOverrides = new LinkedHashSet<String>();
    if (prefixOverrides != null) {
      parseOverrides(prefixOverrides, this.prefixOverrides);
    }
    this.suffix = suffix;
    this.suffixOverrides = new LinkedHashSet<String>();
    if (suffixOverrides != null) {
      parseOverrides(suffixOverrides, this.suffixOverrides);
    }
    this.expressions = expressions;

    // for (String po : this.prefixOverrides) {
    // System.out.println("prefixOverrides='" + po + "'");
    // }
    // for (String so : this.suffixOverrides) {
    // System.out.println("suffixOverrides='" + so + "'");
    // }
  }

  private void parseOverrides(final String overrides, final Set<String> set) {
    if (overrides != null) {
      String[] parts = overrides.split("\\|");
      if (parts != null) {
        for (String chunk : parts) {
          if (chunk != null && !chunk.isEmpty()) {
            set.add(chunk);
          }
        }
      }
    }
  }

  @Override
  public EvaluationFeedback evaluate(final StringBuilder out, final DynamicSQLParameters variables)
      throws DynamicSQLEvaluationException {

    // Render the body

    StringBuilder sb = new StringBuilder();
    boolean contentRendered = false;
    for (DynamicSQLExpression expr : this.expressions) {
      EvaluationFeedback feedback = expr.evaluate(sb, variables);
      contentRendered = contentRendered || feedback.wasContentRendered();
    }
    String rendered = sb.toString().trim();

    if (contentRendered) {

      // Remove a prefix if found

      // System.out.println("rendered=" + rendered);
      String p = this.searchForPrefix(rendered);
      // System.out.println("p=" + p);
      if (p != null) {
        rendered = rendered.substring(p.length());
      }

      // Remove a suffix if found

      String s = this.searchForSuffix(rendered);
      if (s != null) {
        rendered = rendered.substring(0, rendered.length() - s.length());
      }

      // Build the content, using the prefix and suffix if necessary

      String content = (this.prefix != null ? this.prefix : "") + rendered + (this.suffix != null ? this.suffix : "");

      // Add the content to the evaluation

      out.append(content);

    }

    return new EvaluationFeedback(contentRendered);

  }

  private String searchForPrefix(final String txt) {
    for (String p : this.prefixOverrides) {
      if (SUtils.startsWithIgnoreCase(txt, p)) {
        return p;
      }
    }
    return null;
  }

  private String searchForSuffix(final String txt) {
    for (String p : this.suffixOverrides) {
      if (SUtils.endsWithIgnoreCase(txt, p)) {
        return p;
      }
    }
    return null;
  }

}
