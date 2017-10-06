package org.hotrod.config.sqlcolumns;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ColumnsMetadataRetriever.InvalidSQLException;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

@XmlRootElement(name = "expressions")
public class ExpressionsTag extends ColumnsProducerTag {

  // Constants

  private static final Logger log = Logger.getLogger(ExpressionsTag.class);

  // Properties

  // Properties - Primitive content parsing by JAXB

  @XmlMixed
  @XmlElementRefs({ //
  })
  private List<Object> content = new ArrayList<Object>();

  private String expressions = "";

  // Constructor

  public ExpressionsTag() {
    super("expressions");
  }

  // JAXB Setters

  // Behavior

  public void validate(final HotRodConfigTag config) throws InvalidConfigurationFileException {

    log.debug("validate");

    // Sort: expressions

    for (Object obj : this.content) {
      try {
        String s = (String) obj; // content part
        if (!SUtils.isEmpty(s)) {
          if (!this.expressions.isEmpty()) {
            this.expressions = this.expressions + " ";
          }
          this.expressions = this.expressions + s;
        }
      } catch (ClassCastException e1) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "The body of the tag <"
            + super.getTagName() + "> has an invalid tag (of class '" + obj.getClass().getName() + "').");
      }
    }

    // expressions in body

    if (SUtils.isEmpty(this.expressions)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Invalid empty <" + super.getTagName() + "> tag. " + "When specified this tag must not be empty.");
    }

  }

  // Meta data gathering

  @Override
  public void gatherMetadataPhase1(final SelectMethodTag selectTag, final DatabaseAdapter adapter,
      final JdbcDatabase db, final DatabaseLocation loc, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final Connection conn1) throws InvalidSQLException {
    super.prepareRetrieval(selectTag, adapter, db, loc, selectGenerationTag, columnsPrefixGenerator, conn1);
  }

  @Override
  public void gatherMetadataPhase2(final Connection conn2)
      throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException {
    super.retrieve(conn2);
  }

  // Getters

  public String getExpressions() {
    return expressions;
  }

}
