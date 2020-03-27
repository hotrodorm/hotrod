package org.hotrod.runtime.dynamicsql.expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hotrod.runtime.dynamicsql.DynamicSQLEvaluationException;
import org.hotrod.runtime.dynamicsql.DynamicSQLParameters;
import org.hotrod.runtime.dynamicsql.EvaluationFeedback;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class TrimExpression extends DynamicExpression {

  private String prefix;
  private LinkedHashSet<String> prefixOverrides;
  private String suffix;
  private LinkedHashSet<String> suffixOverrides;
  protected DynamicExpression[] expressions;

  public TrimExpression(final String prefix, final String prefixOverrides, final String suffix,
      final String suffixOverrides, final DynamicExpression... expressions) {
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
    for (DynamicExpression expr : this.expressions) {
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
      if (SUtil.startsWithIgnoreCase(txt, p)) {
        return p;
      }
    }
    return null;
  }

  private String searchForSuffix(final String txt) {
    for (String p : this.suffixOverrides) {
      if (SUtil.endsWithIgnoreCase(txt, p)) {
        return p;
      }
    }
    return null;
  }

  @Override
  public List<Object> getConstructorParameters() {
    List<Object> params = new ArrayList<Object>();
    List<String> stringParams = new ArrayList<String>();
    stringParams.add(this.prefix);
    stringParams.add(ListWriter.render(this.prefixOverrides, "|"));
    stringParams.add(this.suffix);
    stringParams.add(ListWriter.render(this.suffixOverrides, "|"));
    params.add(stringParams);
    params.addAll(Arrays.asList(this.expressions));
    return params;
  }

}
