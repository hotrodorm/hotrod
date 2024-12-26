package org.hotrod.generator.jdbc;

import java.sql.Types;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.ComplementDAOTag;
import org.hotrod.config.SQLParameter;
import org.hotrod.config.VerbatimTextPart;
import org.hotrod.config.dynamicsql.BindTag;
import org.hotrod.config.dynamicsql.ChooseTag;
import org.hotrod.config.dynamicsql.CollectionOfPartsTag;
import org.hotrod.config.dynamicsql.DynamicSQLPart;
import org.hotrod.config.dynamicsql.ForEachTag;
import org.hotrod.config.dynamicsql.IfTag;
import org.hotrod.config.dynamicsql.LiteralTextPart;
import org.hotrod.config.dynamicsql.OtherwiseTag;
import org.hotrod.config.dynamicsql.ParameterisableTextPart;
import org.hotrod.config.dynamicsql.SQLSegment;
import org.hotrod.config.dynamicsql.SetTag;
import org.hotrod.config.dynamicsql.TrimTag;
import org.hotrod.config.dynamicsql.VariableOccurrence;
import org.hotrod.config.dynamicsql.WhenTag;
import org.hotrod.config.dynamicsql.WhereTag;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.utils.ClassWriter;
import org.hotrod.utils.SUtil;

public class NitroRenderer {

  private static final Logger log = Logger.getLogger(NitroRenderer.class.getName());

  public void render(List<DynamicSQLPart> parts, ClassWriter w) throws ControlledException {
    log.info("[0] render --- List ---");
    this.render(parts, w, 0);
  }

  private void render(List<DynamicSQLPart> parts, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render --- List ---");
    for (DynamicSQLPart p : parts) {
      this.renderDynamicPart(p, w, level);
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

  private void renderDynamicPart(DynamicSQLPart p, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] --- selector ---");
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
    } else if (p instanceof ComplementDAOTag) {
      render((ComplementDAOTag) p, w, level);
    } else {
      throw new ControlledException(
          "Could not render Nitro query: unrecognized Dynamic SQL part of type '" + p.getClass().getName() + "'");
    }
  }

  private void render(LiteralTextPart t, ClassWriter w, int level) {
    log.info("[" + level + "] render(Literal) -- " + t.getText());
    w.println(filler(level) + ".literal(\"" + SUtil.escapeJavaString(t.getText()) + "\")");
  }

  private void render(IfTag t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(if) -- '" + t.getTest() + "'");
    w.println(filler(level) + ".if_(\"" + SUtil.escapeJavaString(t.getTest()) + "\", assembler");
    render(t.getParts(), w, level + 1);
    w.println(filler(level) + "  .end()");
    w.println(filler(level) + ")");
  }

  private void render(WhereTag t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(where)");

  }

  private void render(SetTag t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(set)");

  }

  private void render(ChooseTag t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(choose)");

  }

  private void render(WhenTag t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(when)");

  }

  private void render(OtherwiseTag t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(otherwise)");

  }

  private void render(TrimTag t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(trim)");

  }

  private void render(BindTag t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(bind)");

  }

  private void render(ForEachTag t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(foreach)");

  }

  private void render(ParameterisableTextPart t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(ParameterisableText):");
    for (SQLSegment s : t.getSegments()) {
      if (s instanceof SQLParameter) {
        render((SQLParameter) s, w, level);
      } else if (s instanceof LiteralTextPart) {
        render((LiteralTextPart) s, w, level);
      } else if (s instanceof VariableOccurrence) {
        render((VariableOccurrence) s, w, level);
      } else if (s instanceof VerbatimTextPart) {
        render((VerbatimTextPart) s, w, level);
      } else {
        throw new ControlledException(
            "Could not render Nitro query (2): unrecognized Dynamic SQL part of type '" + s.getClass().getName() + "'");
      }
    }
  }

  private void render(SQLParameter t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(SQLParameter) -- " + t.getName());
    w.println(filler(level) + ".parameter(\"" + SUtil.escapeJavaString(t.getName()) + "\", ", Types.class,
        "." + t.getJdbcType() + ")");
  }

  private void render(VariableOccurrence t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(VariableOccurrence)");
    w.println(filler(level) + ".parameter(\"" + SUtil.escapeJavaString(t.getName()) + "\")");
  }

  private void render(VerbatimTextPart t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(VerbatimTextPart)");
    w.println(filler(level) + ".literal(\"" + SUtil.escapeJavaString(t.getContent()) + "\")");
  }

  private void render(CollectionOfPartsTag t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(CollectionOfParts)");

  }

  private void render(ComplementDAOTag t, ClassWriter w, int level) throws ControlledException {
    log.info("[" + level + "] render(ComplementDAO)");

  }

  // Utils

  private String filler(int level) {
    return SUtil.filler(' ', 4 + level * 2);
  }

}
