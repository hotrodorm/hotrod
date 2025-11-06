package org.hotrod.generator.jdbc;

import java.sql.Types;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.ComplementTag;
import org.hotrod.config.EnhancedSQLPart;
import org.hotrod.config.ParameterTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.SequenceOfParts;
import org.hotrod.config.VerbatimTextPart;
import org.hotrod.config.dynamicsql.BindTag;
import org.hotrod.config.dynamicsql.ChooseTag;
import org.hotrod.config.dynamicsql.CollectionOfPartsTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart;
import org.hotrod.config.dynamicsql.ForEachTag;
import org.hotrod.config.dynamicsql.IfTag;
import org.hotrod.config.dynamicsql.LiteralTextPart;
import org.hotrod.config.dynamicsql.OtherwiseTag;
import org.hotrod.config.dynamicsql.ParameterInjection;
import org.hotrod.config.dynamicsql.ParameterisableTextPart;
import org.hotrod.config.dynamicsql.SQLSegment;
import org.hotrod.config.dynamicsql.SetTag;
import org.hotrod.config.dynamicsql.TrimTag;
import org.hotrod.config.dynamicsql.VariableOccurrence;
import org.hotrod.config.dynamicsql.WhenTag;
import org.hotrod.config.dynamicsql.WhereTag;
import org.hotrod.config.structuredcolumns.ColumnsTag;
import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.utils.ClassWriter;
import org.hotrod.utils.SUtil;

public class NitroRenderer {

  private static final Logger log = Logger.getLogger(NitroRenderer.class.getName());

  public void render(List<DynamicSQLPart> parts, ClassWriter w) throws ErrorMessageException {
    log.fine("[0] render --- List ---");
    this.render(parts, w, 0, RENDER_ALL);
  }

  public void renderSelect(List<EnhancedSQLPart> parts, ClassWriter w) throws ErrorMessageException {
    log.fine("[0] render --- List ---");
    this.renderSelect(parts, w, 0, ERENDER_ALL);
  }

  interface PartFilter {
    boolean accepts(DynamicSQLPart p);
  }

  interface EnhancedPartFilter {
    boolean accepts(EnhancedSQLPart p);
  }

  private static final PartFilter RENDER_ALL = c -> true;
  private static final EnhancedPartFilter ERENDER_ALL = c -> true;

  private void render(List<DynamicSQLPart> parts, ClassWriter w, int level, PartFilter filter)
      throws ErrorMessageException {
    log.fine("[" + level + "] render --- List ---");
    for (DynamicSQLPart p : parts) {
      if (filter.accepts(p)) {
        this.renderDynamicPart(p, w, level);
      }
    }
  }

  private void renderSelect(List<EnhancedSQLPart> parts, ClassWriter w, int level, EnhancedPartFilter filter)
      throws ErrorMessageException {
    log.fine("[" + level + "] render --- List ---");
    for (EnhancedSQLPart p : parts) {
      if (filter.accepts(p)) {
        this.renderEnhancedPart(p, w, level);
      }
    }
  }

//  LiteralTextPart

//  IfTag
//  WhereTag
//  SetTag
//  ChooseTag
//  WhenTag
//  OtherwiseTag
//  TrimTag
//  BindTag
//  ForEachTag

//  ParameterisableTextPart
//  CollectionOfPartsTag
//  ComplementDAOTag

  private void renderDynamicPart(DynamicSQLPart p, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] --- selector ---");
    if (p instanceof LiteralTextPart) {
      render((LiteralTextPart) p, w, level);
    } else if (p instanceof IfTag) {
      render((IfTag) p, w, level);
    } else if (p instanceof WhereTag) {
      render((WhereTag) p, w, level);
    } else if (p instanceof SetTag) {
      render((SetTag) p, w, level);
    } else if (p instanceof ChooseTag) {
      render((ChooseTag) p, w, level);
    } else if (p instanceof WhenTag) {
      render((WhenTag) p, w, level);
    } else if (p instanceof OtherwiseTag) {
      render((OtherwiseTag) p, w, level);
    } else if (p instanceof TrimTag) {
      render((TrimTag) p, w, level);
    } else if (p instanceof BindTag) {
      render((BindTag) p, w, level);
    } else if (p instanceof ForEachTag) {
      render((ForEachTag) p, w, level);
    } else if (p instanceof ParameterisableTextPart) {
      render((ParameterisableTextPart) p, w, level);
    } else if (p instanceof CollectionOfPartsTag) {
      render((CollectionOfPartsTag) p, w, level);
    } else {
      throw new ErrorMessageException(
          "Could not render Nitro query: unrecognized Dynamic SQL part of type '" + p.getClass().getName() + "'");
    }
  }

  private void renderEnhancedPart(EnhancedSQLPart p, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] --- selector ---");
    if (p instanceof ColumnsTag) {
      render((ColumnsTag) p, w, level);
    } else if (p instanceof ComplementTag) {
      render((ComplementTag) p, w, level);
    } else if (p instanceof SequenceOfParts) {
      render((SequenceOfParts) p, w, level);
    } else if (p instanceof LiteralTextPart) {
      render((LiteralTextPart) p, w, level);
    } else if (p instanceof IfTag) {
      render((IfTag) p, w, level);
    } else if (p instanceof WhereTag) {
      render((WhereTag) p, w, level);
    } else if (p instanceof SetTag) {
      render((SetTag) p, w, level);
    } else if (p instanceof ChooseTag) {
      render((ChooseTag) p, w, level);
    } else if (p instanceof WhenTag) {
      render((WhenTag) p, w, level);
    } else if (p instanceof OtherwiseTag) {
      render((OtherwiseTag) p, w, level);
    } else if (p instanceof TrimTag) {
      render((TrimTag) p, w, level);
    } else if (p instanceof BindTag) {
      render((BindTag) p, w, level);
    } else if (p instanceof ForEachTag) {
      render((ForEachTag) p, w, level);
    } else if (p instanceof ParameterisableTextPart) {
      render((ParameterisableTextPart) p, w, level);
    } else if (p instanceof CollectionOfPartsTag) {
      render((CollectionOfPartsTag) p, w, level);
    } else {
      throw new ErrorMessageException(
          "Could not render Nitro query: unrecognized Dynamic SQL part of type '" + p.getClass().getName() + "'");
    }
  }

  private void render(ColumnsTag t, ClassWriter w, int level) throws ErrorMessageException {
    throw new ErrorMessageException("Could not render Nitro query: Graph queries are not yet supported");
  }

  private void render(ComplementTag t, ClassWriter w, int level) throws ErrorMessageException {
    render(t.getParts(), w, level, RENDER_ALL);
  }

  private void render(SequenceOfParts t, ClassWriter w, int level) throws ErrorMessageException {
    for (EnhancedSQLPart ep : t.getParts()) {
      this.renderEnhancedPart(ep, w, level);
    }
  }

  private void render(LiteralTextPart t, ClassWriter w, int level) {
    log.fine("[" + level + "] render(Literal) -- " + t.getText());
    w.println(indent(level) + ".literal(" + renderString(t.getText()) + ")");
  }

  private void render(IfTag t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(if) -- '" + t.getTest() + "'");
    w.println(indent(level) + ".if_(" + renderString(t.getTest()) + ")");
    render(t.getParts(), w, level + 1, RENDER_ALL);
    w.println(indent(level) + ".endif()");
  }

  private void render(WhereTag t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(where)");
    w.println(indent(level) + ".where(\"AND\")");
    render(t.getParts(), w, level + 1, p -> p instanceof IfTag);
    w.println(indent(level) + ".endwhere()");
  }

  private void render(SetTag t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(set)");
    w.println(indent(level) + ".set()");
    render(t.getParts(), w, level + 1, p -> p instanceof IfTag);
    w.println(indent(level) + ".endset()");
  }

  private void render(ChooseTag t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(choose)");
    w.println(indent(level) + ".choose()");
    render(t.getParts(), w, level + 1, p -> p instanceof WhenTag || p instanceof OtherwiseTag);
    w.println(indent(level) + ".endchoose()");
  }

  private void render(WhenTag t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(when)");
    w.println(indent(level) + ".when(" + renderString(t.getTest()) + ")");
    render(t.getParts(), w, level + 1, RENDER_ALL);
    w.println(indent(level) + ".endwhen()");
  }

  private void render(OtherwiseTag t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(otherwise)");
    w.println(indent(level) + ".otherwise()");
    render(t.getParts(), w, level + 1, RENDER_ALL);
    w.println(indent(level) + ".endotherwise()");
  }

  private String renderString(String s) {
    return s == null ? "null" : "\"" + SUtil.escapeJavaString(s) + "\"";
  }

  private void render(TrimTag t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(trim)");
    w.println(indent(level) + ".trim(" + renderString(t.getPrefix()) + ", " + renderString(t.getSeparator()) + ", "
        + renderString(t.getSuffix()) + ")");
    render(t.getParts(), w, level + 1, p -> p instanceof IfTag);
    w.println(indent(level) + ".endtrim()");
  }

  private void render(BindTag t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(bind)");
    w.println(indent(level) + ".bind(" + renderString(t.getName()) + ", " + renderString(t.getValue()) + ")");
  }

  private void render(ForEachTag t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(foreach)");
    w.println(indent(level) + ".foreach(" + renderString(t.getItem()) + ", " + renderString(t.getCollection()) + ", "
        + renderString(t.getOpen()) + ", " + renderString(t.getSeparator()) + ", " + renderString(t.getClose()) + ")");
    render(t.getParts(), w, level + 1, RENDER_ALL);
    w.println(indent(level) + ".endforeach()");
  }

  private void render(ParameterisableTextPart t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(ParameterisableText):");
    List<SQLSegment> segments = t.getSegments();
    renderSQLSegments(w, level, segments);
  }

  private void renderSQLSegments(ClassWriter w, int level, List<SQLSegment> segments) throws ErrorMessageException {
    for (SQLSegment s : segments) {
      if (s instanceof SQLParameter) {
        render((SQLParameter) s, w, level);
      } else if (s instanceof LiteralTextPart) {
        render((LiteralTextPart) s, w, level);
      } else if (s instanceof VariableOccurrence) {
        render((VariableOccurrence) s, w, level);
      } else if (s instanceof VerbatimTextPart) {
        render((VerbatimTextPart) s, w, level);
      } else if (s instanceof ParameterInjection) {
        render((ParameterInjection) s, w, level);
      } else {
        throw new ErrorMessageException(
            "Could not render Nitro query (2): unrecognized Dynamic SQL part of type '" + s.getClass().getName() + "'");
      }
    }
  }

  private void render(SQLParameter t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(SQLParameter) -- " + t.getName());
    ParameterTag pt = t.getDefinition();
//    log.info("pt=" + pt);
    if (pt.getJDBCType() == null) {
      w.println(indent(level) + ".parameter(" + renderString(t.getName()) + ")");
    } else {
      w.println(indent(level) + ".parameterNullable(" + renderString(t.getName()) + ", ", Types.class,
          "." + t.getJdbcType() + ")");
    }
  }

  private void render(VariableOccurrence t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(VariableOccurrence)");
    w.println(indent(level) + ".variable(" + renderString(t.getName()) + ")");
  }

  private void render(VerbatimTextPart t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(VerbatimTextPart)");
    w.println(indent(level) + ".literal(" + renderString(t.getContent()) + ")");
  }

  private void render(ParameterInjection t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(ParameterInjection)");
    w.println(indent(level) + ".parameterInjection(" + renderString(t.getName()) + ")");
  }

  private void render(CollectionOfPartsTag t, ClassWriter w, int level) throws ErrorMessageException {
    log.fine("[" + level + "] render(CollectionOfParts)");
  }

  // Utils

  private String indent(int level) {
    return SUtil.filler(' ', 6 + level * 2);
  }

}
