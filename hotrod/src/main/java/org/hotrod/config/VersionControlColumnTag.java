package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.Compare;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcKeyColumn;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

@XmlRootElement(name = "version-control-column")
public class VersionControlColumnTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(VersionControlColumnTag.class);

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

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Attribute 'name' of tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify the version control column name. A column of type numeric.");
    }

  }

  public void validateAgainstDatabase(final HotRodGenerator generator, final String name, final JdbcTable t)
      throws InvalidConfigurationFileException {

    this.jdbcTable = t;

    // Check the optimistic locking column exists

    this.jdbcColumn = generator.findJdbcColumn(this.jdbcTable, this.name);
    if (this.jdbcColumn == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Could not find column '" + this.name + "' for table '" + name
              + "' as specified in the attribute 'column' of the tag <" + super.getTagName() + ">.");
    }

    // Check the optimistic locking column is integer-like

    if (generator.getAdapter().isSerial(this.jdbcColumn)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid type for version control columm '"
          + this.name + "' on table '" + name + ". ' A version control column must be of an integer-like number type.");
    }

    // Check the table has a PK

    if (t.getPk() == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Cannot use optimistic locking (row-version-control) on table '" + name
              + "' since it does not have a primary key.");
    }

    // Check the optimistic locking column is not part of the PK

    for (JdbcKeyColumn kc : t.getPk().getKeyColumns()) {
      JdbcColumn c = kc.getColumn();
      if (this.jdbcColumn.getName().equals(c.getName())) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Cannot use optimistic locking (row-version-control) on table '" + name
                + "'. The specified version control column ('" + this.name
                + "') cannot be part of the primary key of the table.");
      }
    }

  }

  // Getters

  public String getName() {
    return name;
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      VersionControlColumnTag f = (VersionControlColumnTag) fresh;
      return this.name.equals(f.name);
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      VersionControlColumnTag f = (VersionControlColumnTag) fresh;
      boolean different = !same(fresh);

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      VersionControlColumnTag f = (VersionControlColumnTag) fresh;
      return Compare.same(this.name, f.name);
    } catch (ClassCastException e) {
      return false;
    }
  }

}
