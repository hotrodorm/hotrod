package org.hotrod.runtime.livesql.dialects;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.runtime.livesql.exceptions.InvalidLiteralException;
import org.hotrod.runtime.livesql.exceptions.UnsupportedLiveSQLFeatureException;
import org.hotrod.runtime.livesql.expressions.Helper;
import org.hotrod.runtime.livesql.expressions.OrderingTerm;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeExpression;
import org.hotrod.runtime.livesql.expressions.datetime.DateTimeFieldExpression;
import org.hotrod.runtime.livesql.expressions.numbers.NumberExpression;
import org.hotrod.runtime.livesql.expressions.strings.StringExpression;
import org.hotrod.runtime.livesql.queries.QueryWriter;
import org.hotrod.runtime.livesql.queries.select.CrossJoin;
import org.hotrod.runtime.livesql.queries.select.FullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.InnerJoin;
import org.hotrod.runtime.livesql.queries.select.Join;
import org.hotrod.runtime.livesql.queries.select.JoinLateral;
import org.hotrod.runtime.livesql.queries.select.LeftJoinLateral;
import org.hotrod.runtime.livesql.queries.select.LeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalFullOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalInnerJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalLeftOuterJoin;
import org.hotrod.runtime.livesql.queries.select.NaturalRightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.RightOuterJoin;
import org.hotrod.runtime.livesql.queries.select.UnionJoin;
import org.hotrodorm.hotrod.utils.Separator;

public class SybaseASEDialect extends LiveSQLDialect {

  public SybaseASEDialect(final boolean discovered, final String productName, final String productVersion,
      final int majorVersion, final int minorVersion) {
    super(discovered, productName, productVersion, majorVersion, minorVersion);
  }

  // WITH rendering

  @Override
  public WithRenderer getWithRenderer() {
    throw new UnsupportedLiveSQLFeatureException(
        "LiveSQL does not support Common Table Expressions (CTEs) SAP ASE/Sybase.");
  }

  // DISTINCT ON rendering

  @Override
  public DistinctOnRenderer getDistinctOnRenderer() {
    throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not support the DISTINCT ON clause.");
  }

  // From rendering

  @Override
  public FromRenderer getFromRenderer() {
    return () -> "";
  }

  // Table Expression rendering

  @Override
  public TableExpressionRenderer getTableExpressionRenderer() {
    return (columns) -> " ("
        + Arrays.stream(columns).map(c -> this.canonicalToNatural(c)).collect(Collectors.joining(", ")) + ")";
  }

  // Join rendering

  @Override
  public JoinRenderer getJoinRenderer() {
    return new JoinRenderer() {

      @Override
      public String renderJoinKeywords(final Join join) throws UnsupportedLiveSQLFeatureException {
        if (join instanceof InnerJoin) {
          return "JOIN";
        } else if (join instanceof LeftOuterJoin) {
          return "LEFT OUTER JOIN";
        } else if (join instanceof RightOuterJoin) {
          return "RIGHT OUTER JOIN";
        } else if (join instanceof FullOuterJoin) {
          throw new UnsupportedLiveSQLFeatureException("Full outer joins are not supported in the Sybase ASE database");
        } else if (join instanceof CrossJoin) {
          throw new UnsupportedLiveSQLFeatureException("Cross joins are not supported in the Sybase ASE database");
        } else if (join instanceof NaturalInnerJoin) {
          throw new UnsupportedLiveSQLFeatureException("Natural joins are not supported in the Sybase ASE database");
        } else if (join instanceof NaturalLeftOuterJoin) {
          throw new UnsupportedLiveSQLFeatureException("Natural joins are not supported in the Sybase ASE database");
        } else if (join instanceof NaturalRightOuterJoin) {
          throw new UnsupportedLiveSQLFeatureException("Natural joins are not supported in the Sybase ASE database");
        } else if (join instanceof NaturalFullOuterJoin) {
          throw new UnsupportedLiveSQLFeatureException("Natural joins are not supported in the Sybase ASE database");
        } else if (join instanceof JoinLateral) {
          throw new UnsupportedLiveSQLFeatureException("Lateral joins are not supported in the Sybase ASE database");
        } else if (join instanceof LeftJoinLateral) {
          throw new UnsupportedLiveSQLFeatureException(
              "Lateral left joins are not supported in the Sybase ASE database");
        } else if (join instanceof UnionJoin) {
          throw new UnsupportedLiveSQLFeatureException("Union joins are not supported in Sybase ASE database");
        } else {
          throw new UnsupportedLiveSQLFeatureException(
              "Invalid join type (" + join.getClass().getSimpleName() + ") in Sybase ASE database");
        }
      }

    };
  }

  // Pagination rendering

  public PaginationRenderer getPaginationRenderer() {
    return new PaginationRenderer() {

      @Override
      public PaginationType getPaginationType(final boolean orderedSelect, final Integer offset, final Integer limit) {
        if (offset != null) {
          throw new UnsupportedLiveSQLFeatureException("OFFSET is not supported Sybase ASE");
        }
        return PaginationType.TOP;
      }

      @Override
      public void renderTopPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        if (offset == null) {
          w.write(" top " + limit);
        } else {
          throw new UnsupportedLiveSQLFeatureException("OFFSET is not supported Sybase ASE");
        }
      }

      @Override
      public void renderBottomPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Pagination cannot be rendered at the bottom in Sybase ASE");
      }

      @Override
      public void renderBeginEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException(
            "Pagination cannot be rendered in enclosing fashion in Sybase ASE");
      }

      @Override
      public void renderEndEnclosingPagination(final Integer offset, final Integer limit, final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException(
            "Pagination cannot be rendered in enclosing fashion in Sybase ASE");
      }

    };
  }

  // For Update rendering

  @Override
  public LockingRenderer getLockingRenderer() {
    throw new UnsupportedLiveSQLFeatureException(
        "Sybase ASE does not support locking rows for plain SELECTs outside cursors and procedures");
  }

  // Set operation rendering

  @Override
  public SetOperatorRenderer getSetOperationRenderer() {
    return new SetOperatorRenderer() {

      @Override
      public void renderUnion(final QueryWriter w) {
        w.write("UNION");
      }

      @Override
      public void renderUnionAll(final QueryWriter w) {
        w.write("UNION ALL");
      }

      @Override
      public void renderExcept(final QueryWriter w) {
        w.write("MINUS");
      }

      @Override
      public void renderExceptAll(final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Sybase/SAP ASE does not support the EXCEPT ALL set operator. "
            + "Nevertheless, this operator can be simulated using an anti join");
      }

      @Override
      public void renderIntersect(final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Sybase/SAP ASE does not support the INTERSECT set operator. "
            + "Nevertheless, this operator can be simulated using a semi join");
      }

      @Override
      public void renderIntersectAll(final QueryWriter w) {
        throw new UnsupportedLiveSQLFeatureException("Sybase/SAP ASE does not support the INTERSECT ALL set operator. "
            + "Nevertheless, this operator can be simulated using a semi join");
      }

    };
  }

  // Function rendering

  @Override
  public FunctionRenderer getFunctionRenderer() {
    return new FunctionRenderer() {

      // General purpose functions

      @Override
      public void groupConcat(final QueryWriter w, final boolean distinct, final StringExpression value,
          final List<OrderingTerm> ordering, final StringExpression separator) {
        throw new UnsupportedLiveSQLFeatureException("GROUP_CONCAT() is not supported in Sybase ASE");
      }

      // Arithmetic functions

      @Override
      public void logarithm(final QueryWriter w, final NumberExpression x, final NumberExpression base) {
        if (base == null) {
          this.write(w, "log", x);
        } else {
          w.write("(");
          this.write(w, "log", x);
          w.write(" / ");
          this.write(w, "log", base);
          w.write(")");
        }
      }

      @Override
      public void round(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
        if (places == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "Sybase ASE requires the number of decimal places to be specified on the ROUND() function");
        }
        this.write(w, "round", x, places);
      }

      @Override
      public void trunc(final QueryWriter w, final NumberExpression x, final NumberExpression places) {
        throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not support the TRUNC()function");
      }

      // String functions

      @Override
      public void concat(final QueryWriter w, final List<StringExpression> strings) {
        w.write("(");
        Separator sep = new Separator(" || ");
        for (StringExpression s : strings) {
          w.write(sep.render());
          Helper.renderTo(s, w);
        }
        w.write(")");
      }

      @Override
      public void length(final QueryWriter w, final StringExpression string) {
        this.write(w, "char_length", string);
      }

      @Override
      public void locate(final QueryWriter w, final StringExpression substring, final StringExpression string,
          final NumberExpression from) {
        if (from == null) {
          this.write(w, "charindex", substring, string);
        } else {
          this.write(w, "charindex", substring, string, from);
        }
      }

      @Override
      public void substr(final QueryWriter w, final StringExpression string, final NumberExpression from,
          final NumberExpression length) {
        if (length == null) {
          throw new UnsupportedLiveSQLFeatureException(
              "Sybase ASE requires the length parameter to be be specified on the SUBSTR() function");
        }
        this.write(w, "substring", string, from, length);
      }

      // Date/Time functions

      @Override
      public void currentDate(final QueryWriter w) {
        w.write("current_date()");
      }

      @Override
      public void currentTime(final QueryWriter w) {
        w.write("current_time");
      }

      @Override
      public void currentDateTime(final QueryWriter w) {
        w.write("getdate()");
      }

      @Override
      public void date(final QueryWriter w, final DateTimeExpression datetime) {
        throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not suppor the DATE() function");
      }

      @Override
      public void time(final QueryWriter w, final DateTimeExpression datetime) {
        throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not suppor the TIME() function");
      }

      @Override
      public void dateTime(final QueryWriter w, final DateTimeExpression date, final DateTimeExpression time) {
        throw new UnsupportedLiveSQLFeatureException("Sybase ASE does not suppor the DATETIME() function");
      }

      @Override
      public void extract(final QueryWriter w, final DateTimeExpression datetime, final DateTimeFieldExpression field) {
        w.write("datepart(");
        Helper.renderTo(field, w);
        w.write(", ");
        Helper.renderTo(datetime, w);
        w.write(")");
      }

    };
  }

  // New SQL Identifier rendering

  private final String UNQUOTED_NATURAL = "[A-Za-z][A-Za-z0-9_]*";
  private final String UNQUOTED_CANONICAL = "[A-Za-z][A-Za-z0-9_]*";

  @Override
  public String naturalToCanonical(final String natural) {
    if (natural == null) {
      return null;
    }
    if (natural.matches(UNQUOTED_NATURAL)) {
      return natural;
    }
    return natural;
  }

  @Override
  public String canonicalToNatural(final String canonical) {
    if (canonical == null)
      return null;
    if (canonical.matches(UNQUOTED_CANONICAL)) {
      return canonical.toLowerCase();
    } else {
      return this.quoteIdentifier(canonical);
    }
  }

  @Override
  public String quoteIdentifier(final String verbatim) {
    return "\"" + verbatim.replace("\"", "\"\"").replace("'", "''").replace("]", "]]") + "\"";
  }

  @Override
  public DateTimeLiteralRenderer getDateTimeLiteralRenderer() {
    return new DateTimeLiteralRenderer() {

      @Override
      public String renderDate(final String isoDate) {
        return "cast('" + isoDate + "' as DATE)";
      }

      @Override
      public String renderTime(final String isoTime, final int precision) {
        if (precision > 3) {
          throw new InvalidLiteralException(
              "Sybase ASE's TIME literals accept a maximum precision of 3, but " + precision + " was specified");
        }
        return "cast('" + isoTime + "' as TIME)";
      }

      @Override
      public String renderTimestamp(final String isoTimestamp, final int precision) {
        if (precision > 6) {
          throw new InvalidLiteralException(
              "Sybase ASE's TIME literals accept a maximum precision of 6, but " + precision + " was specified");
        }
        return "cast('" + isoTimestamp + "' as BIGDATETIME)";
      }

      @Override
      public String renderOffsetTime(final String isoTime, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("Sybase ASE does not implement the TIME WITH TIME ZONE data type.");
      }

      @Override
      public String renderOffsetTimestamp(final String isoTimestamp, final String isoOffset, final int precision) {
        throw new InvalidLiteralException("Sybase ASE does not implement the TIMESTAMP WITH TIME ZONE data type.");
      }

    };
  }

  @Override
  public BooleanLiteralRenderer getBooleanLiteralRenderer() {
    return new BooleanLiteralRenderer() {

      @Override
      public void renderTrue(final QueryWriter w) {
        w.write("1 = 1");
      }

      @Override
      public void renderFalse(final QueryWriter w) {
        w.write("1 = 0");
      }

    };
  }

  @Override
  public boolean mandatoryColumnNamesInRecursiveCTEs() {
    return false;
  }

  // Update rendering

  @Override
  public UpdateRenderer getUpdateRenderer() {
    return new UpdateRenderer() {

      @Override
      public boolean removeMainTableAlias() {
        return true;
      }

    };
  }

}
