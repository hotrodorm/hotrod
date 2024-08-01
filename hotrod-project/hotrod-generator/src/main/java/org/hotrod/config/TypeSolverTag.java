package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.PropertyType;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.runtime.livesql.queries.typesolver.TypeRule;
import org.hotrod.runtime.typesolver.OGNLPublicMemberAccess;
import org.hotrod.runtime.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.JdbcTypes;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;

import ognl.OgnlContext;
import ognl.OgnlException;

@XmlRootElement(name = "type-solver")
public class TypeSolverTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(TypeSolverTag.class);

  // Properties

  private List<TypeSolverWhenTag> whens = new ArrayList<TypeSolverWhenTag>();

  private OgnlContext context;

  private TreeSet<RetrievedColumn> retrievedColumns = new TreeSet<>();

  // Constructor

  public TypeSolverTag() {
    super("type-solver");
  }

  // JAXB Setters

  @XmlElement(name = "when")
  public void setTypeSolverWhen(final TypeSolverWhenTag w) {
    this.whens.add(w);
  }

  // Behavior

  public void validate(final HotRodConfigTag config) throws InvalidConfigurationFileException {

    // whens

    for (TypeSolverWhenTag w : this.whens) {
      w.validate(config);
    }

  }

  public PropertyType resolveType(final ColumnMetadata cm, final JdbcColumn c, final JDBCType resultSetType)
      throws UnresolvableDataTypeException {

    this.context = new OgnlContext(null, null, new OGNLPublicMemberAccess());

    // Create a scope (aka "root object")

    RetrievedColumn rc = new RetrievedColumn(cm, c);
    if (c != null) {
      this.retrievedColumns.add(rc);
    }

    // Find the first matching rule

    for (TypeSolverWhenTag w : this.whens) {
      Object result = null;
      try {
        result = w.getTestExpression().getValue(this.context, rc);
        if (result == null) {
          throw new UnresolvableDataTypeException(cm, "Could not evaluate <when> tag's test expression '" + w.getTest()
              + "': must return a boolean value but returned null");
        }
        Boolean test = (Boolean) result;
        if (test) {
//          if ("price".equals(cm.getName()) && "product".equals(cm.getName())) {
//            log.debug("w.getJDBCTypeOnWrite()=" + w.getJDBCTypeOnWrite());
//            log.debug("resultSetType=" + resultSetType);
//            log.debug("this.context=" + this.context);
//            log.debug("cm=" + cm);
//            log.debug("c=" + c);
//          }
          JDBCType jdbcTypeOnWrite = w.getJDBCTypeOnWrite();
          if (jdbcTypeOnWrite == null) {
            jdbcTypeOnWrite = (c != null ? JdbcTypes.codeToType(c.getDataType()) : resultSetType);
          }
          log.debug("## 5 RULE MATCHES: w.getJavaType()=" + w.getJavaType() + " jdbcTypeOnWrite=" + jdbcTypeOnWrite);
          return new PropertyType(w.getJavaType(), jdbcTypeOnWrite, false);
        }
      } catch (ClassCastException e) {
        throw new UnresolvableDataTypeException(cm, "Could not evaluate <when> tag's test expression '" + w.getTest()
            + "': must return a boolean value but returned a " + result.getClass().getName());
      } catch (OgnlException e) {
        throw new UnresolvableDataTypeException(cm,
            "Could not evaluate <when> tag's test expression '" + w.getTest() + "': " + e.getMessage());
      } catch (RuntimeException e) {
        e.printStackTrace();
        throw e;
      }
    }

    // No rule matched

    return null;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

  // Column properties

  public TreeSet<RetrievedColumn> getRetrievedColumns() {
    return retrievedColumns;
  }

  public List<TypeRule> getRules() {
    List<TypeRule> rules = new ArrayList<>();
    for (TypeSolverWhenTag w : this.whens) {
//      if (w.getJavaType() != null)
      TypeRule.of(w.getTest(), w.getJavaType());
    }
    return rules;
  }

}
