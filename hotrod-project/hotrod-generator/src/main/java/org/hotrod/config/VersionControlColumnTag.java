package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.metadata.Metadata;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcKeyColumn;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

@XmlRootElement(name = "version-control-column")
public class VersionControlColumnTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(VersionControlColumnTag.class);

  // Properties

  private String name = null;

  private JdbcTable jdbcTable = null;
  private JdbcColumn jdbcColumn = null;

  // Constructor

  public VersionControlColumnTag() {
    super("version-control-column");
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    log.debug("validate");

    // name

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'name' of tag <" + super.getTagName()
          + "> cannot be empty. " + "Must specify the version control column name. A column of type numeric.");
    }

  }

  public void validateAgainstDatabase(final Metadata metadata, final String canonicalSQLName, final JdbcTable t)
      throws InvalidConfigurationFileException {

    this.jdbcTable = t;

    // Check the optimistic locking column exists

    this.jdbcColumn = metadata.findJdbcColumn(this.jdbcTable, this.name);
    if (this.jdbcColumn == null) {
      throw new InvalidConfigurationFileException(this, "Could not find column '" + this.name + "' for table '"
          + canonicalSQLName + "' as specified in the attribute 'column' of the tag <" + super.getTagName() + ">.");
    }

    // Check the optimistic locking column is integer-like

    // log.info("this.jdbcColumn=" + this.jdbcColumn.getDataType() + " '" +
    // this.jdbcColumn.getTypeName() + "'");
    // log.info("generator.getAdapter().isSerial(this.jdbcColumn)=" +
    // generator.getAdapter().isSerial(this.jdbcColumn));

    if (!metadata.getAdapter().isSerial(this.jdbcColumn)) {
      throw new InvalidConfigurationFileException(this, "Invalid type for version control columm '" + this.name
          + "' on table '" + canonicalSQLName + ". ' A version control column must be of an integer-like number type.");
    }

    // Check the table has a PK

    if (t.getPk() == null) {
      throw new InvalidConfigurationFileException(this, "Cannot use optimistic locking (row-version-control) on table '"
          + canonicalSQLName + "' since it does not have a primary key.");
    }

    // Check the optimistic locking column is not part of the PK

    for (JdbcKeyColumn kc : t.getPk().getKeyColumns()) {
      JdbcColumn c = kc.getColumn();
      if (this.jdbcColumn.getName().equals(c.getName())) {
        throw new InvalidConfigurationFileException(this,
            "Cannot use optimistic locking (row-version-control) on table '" + canonicalSQLName
                + "'. The specified version control column '" + this.name
                + "' cannot be part of the primary key of the table.");
      }
    }

  }

  // Getters

  public String getName() {
    return name;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}
