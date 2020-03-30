package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.PropertyType;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.utils.JdbcTypes;
import org.hotrod.utils.JdbcTypes.JDBCType;
import org.hotrod.utils.OGNLPublicMemberAccess;
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

  public PropertyType resolveType(final ColumnMetadata cm, final JdbcColumn c) throws UnresolvableDataTypeException {

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
        log.info("w=" + w);
        log.info("w.getTestExpression()=" + w.getTestExpression());
        log.info("this.context=" + this.context);
        log.info("cm=" + cm);
        log.info("c=" + c);
        result = w.getTestExpression().getValue(this.context, rc);
        Boolean test = (Boolean) result;
        if (test == null) {
          throw new UnresolvableDataTypeException(cm, "Could not evaluate <when> tag's test expression '" + w.getTest()
              + "': must return a boolean value but returned null");
        }
        if (test) {
          JDBCType jdbcType = JdbcTypes.nameToType(w.getJdbcType());
          if (jdbcType == null) {
            throw new UnresolvableDataTypeException(cm,
                "Unrecognized jdbc-type '" + w.getJdbcType() + "' in <when> tag with test expression '" + w.getTest()
                    + "': must be one of the following values:" + Stream.of(JDBCType.values())
                        .map(t -> t.getShortTypeName()).collect(Collectors.joining(",\n * ", "\n * ", "\n")));
          }
          log.info("## 5 RULE MATCHES: w.getJavaType()=" + w.getJavaType() + " jdbcType=" + jdbcType);
          return new PropertyType(w.getJavaType(), jdbcType, false);
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

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    // TODO: needs to be properly implemented
    return false;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    // TODO: needs to be properly implemented
    return true;
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    // TODO: needs to be properly implemented
    return true;
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

}
