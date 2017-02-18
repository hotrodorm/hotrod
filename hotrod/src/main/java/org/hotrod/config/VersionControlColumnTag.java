package org.hotrod.config;

import org.apache.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.DbUtils;
import org.hotrod.utils.SUtils;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcKeyColumn;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public class VersionControlColumnTag {

  private static final Logger log = Logger.getLogger(VersionControlColumnTag.class);

  private static final String TAG_NAME = "version-control-column";

  private String name = null;

  private JdbcTable jdbcTable = null;
  private JdbcColumn jdbcColumn = null;

  // Validation

  public void validate() throws InvalidConfigurationFileException {

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException("Attribute 'name' of tag <" + TAG_NAME + "> cannot be empty. "
          + "Must specify the version control column name. A column of type numeric.");
    }

  }

  public void validateAgainstDatabase(final JdbcDatabase db, final DatabaseAdapter adapter, final String name,
      final JdbcTable t) throws InvalidConfigurationFileException {

    this.jdbcTable = t;

    // Check the optimistic locking column exists

    this.jdbcColumn = DbUtils.findColumn(this.jdbcTable, this.name, adapter);
    if (this.jdbcColumn == null) {
      throw new InvalidConfigurationFileException("Could not find column '" + this.name + "' for table '" + name
          + "' as specified in the attribute 'column' of the tag <" + TAG_NAME + ">.");
    }

    // Check the optimistic locking column is integer-like

    if (!adapter.isSerial(this.jdbcColumn)) {
      throw new InvalidConfigurationFileException("Invalid type for version control columm '" + this.name
          + "' on table '" + name + ". ' A version control column must be of an integer-like number type.");
    }

    // Check the table has a PK

    if (t.getPk() == null) {
      throw new InvalidConfigurationFileException("Cannot use optimistic locking (row-version-control) on table '"
          + name + "' since it does not have a primary key.");
    }

    // Check the optimistic locking column is not part of the PK

    for (JdbcKeyColumn kc : t.getPk().getKeyColumns()) {
      JdbcColumn c = kc.getColumn();
      if (this.jdbcColumn.getName().equals(c.getName())) {
        throw new InvalidConfigurationFileException("Cannot use optimistic locking (row-version-control) on table '"
            + name + "'. The specified version control column ('" + this.name
            + "') cannot be part of the primary key of the table.");
      }
    }

  }

  // Setters (digester)

  public void setName(String name) {
    this.name = name;
  }

  // Getters

  public String getName() {
    return name;
  }

}
