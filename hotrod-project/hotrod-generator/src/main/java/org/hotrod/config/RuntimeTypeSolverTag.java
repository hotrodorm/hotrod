package org.hotrod.config;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.database.PropertyType;
import org.hotrod.dynamicsql.DynamicExpressionException;
import org.hotrod.dynamicsql.DynamicExpressionFactory;
import org.hotrod.dynamicsql.DynamicExpressionFactoryConfig;
import org.hotrod.dynamicsql.Parameters;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.livesql.queries.typesolver.TypeSource;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.typesolver.UnresolvableDataTypeException;
import org.hotrod.utils.JDBCTypes;
import org.hotrod.utils.JDBCTypes.JDBCType;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;

@XmlRootElement(name = "runtime-type-solver")
public class RuntimeTypeSolverTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(RuntimeTypeSolverTag.class.getName());

  // Properties

  private List<TypeSolverWhenTag> whens = new ArrayList<>();
  private TreeSet<RetrievedColumn> retrievedColumns = new TreeSet<>();

  // Constructor

  public RuntimeTypeSolverTag() {
    super("runtime-type-solver");
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

  public PropertyType resolveStaticType(final ColumnMetadata cm, final JdbcColumn c, final JDBCType resultSetType)
      throws UnresolvableDataTypeException {

    DynamicExpressionFactory factory = DynamicExpressionFactoryConfig.getFactory();

    RetrievedColumn rc = new RetrievedColumn(cm, c);
    this.retrievedColumns.add(rc);

    Parameters context = factory.newObjectContext(rc);

    // Find the first matching rule

    int ruleNumber = 1;
    for (TypeSolverWhenTag w : this.whens) {
      if (w.getTest() != null) {
        Object result = null;
        String resultClassName = null;
        try {

          result = w.getTestExpression().evaluate(context);

          if (result == null) {
            throw new UnresolvableDataTypeException(cm, "Could not evaluate <when> tag's test expression '"
                + w.getTest() + "': must return a boolean value but returned null");
          } else {
            resultClassName = result.getClass().getName();
          }
          boolean test = (Boolean) result;
          if (test) {
            JDBCType jdbcTypeOnWrite = w.getJDBCTypeOnWrite();
            if (jdbcTypeOnWrite == null) {
              jdbcTypeOnWrite = (c != null ? JDBCTypes.codeToType(c.getDataType()) : resultSetType);
            }
            return new PropertyType(w.getJavaType(), jdbcTypeOnWrite, false, TypeSource.STATIC_TYPESOLVER_RULE,
                w.getConverterTag(), ruleNumber, true);
          }
        } catch (ClassCastException e) {
          throw new UnresolvableDataTypeException(cm, "Could not evaluate <when> tag's test expression '" + w.getTest()
              + "': must return a boolean value but returned a " + resultClassName);
        } catch (DynamicExpressionException e) {
          throw new UnresolvableDataTypeException(cm,
              "Could not evaluate <when> tag's test expression '" + w.getTest() + "': " + e.getMessage());
        } catch (RuntimeException e) {
          log.log(Level.SEVERE,
              "Failed to evaluate the test expression '" + w.getTest() + "' of the <type-solver> tag.", e);
          throw e;
        }
      }
      ruleNumber++;
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

  public List<TypeSolverWhenTag> getWhens() {
    return whens;
  }

}
