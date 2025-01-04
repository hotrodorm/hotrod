package org.hotrod.config;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.metadata.Metadata;
import org.hotrod.utils.SUtil;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcKeyColumn;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

@XmlRootElement(name = "optimistic-locking")
public class OptimisticLockingTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(OptimisticLockingTag.class.getName());

  // Properties

  private String sstrategy = null;
  private String column = null;

  public enum OptimisticLockingStrategy {
    VERSION_NUMBER("version-number", false), TIMESTAMP("timestamp", false), FULL_ROW_CHECK("full-row-check", true);

    private String attribute;
    private String title;
    private boolean usesAllColumns;

    private OptimisticLockingStrategy(String attribute, boolean usesAllColumns) {
      this.attribute = attribute;
      this.title = this.attribute.toUpperCase().replace('-', ' ');
      this.usesAllColumns = usesAllColumns;
    }

    public String getAttribute() {
      return attribute;
    }

    public String getTitle() {
      return title;
    }

    public boolean usesAllColumns() {
      return usesAllColumns;
    }

    public static OptimisticLockingStrategy parse(String title) {
      for (OptimisticLockingStrategy s : OptimisticLockingStrategy.values()) {
        if (s.attribute.equals(title)) {
          return s;
        }
      }
      return null;
    }

  };

  private OptimisticLockingStrategy strategy;
  private JdbcTable jdbcTable = null;
  private JdbcColumn jdbcColumn = null;

  // Constructor

  public OptimisticLockingTag() {
    super("optimistic-locking");
  }

  // JAXB Setters

  @XmlAttribute(name = "strategy")
  public void setSStrategy(final String strategy) {
//    log.info(">>>>>>>>>>>>>>>>>>>>>> strategy=" + strategy);
    this.sstrategy = strategy;
  }

  @XmlAttribute(name = "column")
  public void setSColumn(final String column) {
    this.column = column;
  }

  // Behavior

  public void validate() throws InvalidConfigurationFileException {

    // strategy

    if (SUtil.isEmpty(this.sstrategy)) {
      throw new InvalidConfigurationFileException(this,
          "The attribute 'strategy' of the tag <" + super.getTagName() + "> cannot be empty. "
              + "Must specify one these strategies: " + Arrays.stream(OptimisticLockingStrategy.values())
                  .map(n -> n.getAttribute()).collect(Collectors.joining(", ")));
    }
    this.strategy = OptimisticLockingStrategy.parse(this.sstrategy);
    if (this.strategy == null) {
      throw new InvalidConfigurationFileException(this,
          "Invalid value '" + this.sstrategy + "' for the attribute 'strategy' of the tag <" + super.getTagName()
              + ">. " + "Must specify one these strategies: " + Arrays.stream(OptimisticLockingStrategy.values())
                  .map(n -> n.getAttribute()).collect(Collectors.joining(", ")));
    }

    // column

    switch (this.strategy) {
    case VERSION_NUMBER:
      if (SUtil.isEmpty(this.column)) {
        throw new InvalidConfigurationFileException(this,
            "Attribute 'column' of tag <" + super.getTagName() + "> cannot be empty. "
                + "The version number column must be specified "
                + "when the Version Number optimistic locking strategy is selected.");
      }
      break;
    case TIMESTAMP:
      if (SUtil.isEmpty(this.column)) {
        throw new InvalidConfigurationFileException(this,
            "Attribute 'column' of tag <" + super.getTagName() + "> cannot be empty. "
                + "The timestamp column must be specified "
                + "when the Version Number optimistic locking strategy is selected.");
      }
      break;
    case FULL_ROW_CHECK:
      if (this.column != null) {
        throw new InvalidConfigurationFileException(this, "Attribute 'column' of tag <" + super.getTagName()
            + "> cannot be specified " + "when the Full Row Check optimistic locking strategy is selected.");
      }
      break;
    }

  }

  public void validateAgainstDatabase(final Metadata metadata, final String canonicalSQLName, final JdbcTable t)
      throws InvalidConfigurationFileException {

    this.jdbcTable = t;

    switch (this.strategy) {
    case VERSION_NUMBER:
      validateVersionNumberStrategy(metadata, canonicalSQLName, t);
      break;
    case TIMESTAMP:
      validateTimestampStrategy(metadata, canonicalSQLName, t);
      break;
    case FULL_ROW_CHECK:
      validateFullRowCheckStrategy(metadata, canonicalSQLName, t);
      break;
    }

  }

  private void validateVersionNumberStrategy(final Metadata metadata, final String canonicalSQLName, final JdbcTable t)
      throws InvalidConfigurationFileException {

    // Check the optimistic locking column exists

    this.jdbcColumn = metadata.findJdbcColumn(this.jdbcTable, this.column);
    if (this.jdbcColumn == null) {
      throw new InvalidConfigurationFileException(this, "Could not find column '" + this.column + "' for table '"
          + canonicalSQLName + "' as specified in the attribute 'column' of the tag <" + super.getTagName() + ">.");
    }

    // Check the optimistic locking column is integer-like

    if (!metadata.getAdapter().isSerial(this.jdbcColumn)) {
      throw new InvalidConfigurationFileException(this, "Invalid type for version number columm '" + this.column
          + "' on table '" + canonicalSQLName + ". ' A version number column must be of an integer-like number type.");
    }

    // Check the table has a PK

    if (t.getPk() == null) {
      throw new InvalidConfigurationFileException(this,
          "Cannot use optimistic locking on table '" + canonicalSQLName + "' since it does not have a primary key.");
    }

    // Check the optimistic locking column is not part of the PK

    for (JdbcKeyColumn kc : t.getPk().getKeyColumns()) {
      JdbcColumn c = kc.getColumn();
      if (this.jdbcColumn.getName().equals(c.getName())) {
        throw new InvalidConfigurationFileException(this,
            "Cannot use optimistic locking on table '" + canonicalSQLName + "'. The specified version number column '"
                + this.column + "' cannot be part of the primary key of the table.");
      }
    }
  }

  private void validateTimestampStrategy(final Metadata metadata, final String canonicalSQLName, final JdbcTable t)
      throws InvalidConfigurationFileException {

    // Check the optimistic locking column exists

    this.jdbcColumn = metadata.findJdbcColumn(this.jdbcTable, this.column);
    if (this.jdbcColumn == null) {
      throw new InvalidConfigurationFileException(this, "Could not find column '" + this.column + "' for table '"
          + canonicalSQLName + "' as specified in the attribute 'column' of the tag <" + super.getTagName() + ">.");
    }

    // Check the table has a PK

    if (t.getPk() == null) {
      throw new InvalidConfigurationFileException(this,
          "Cannot use optimistic locking on table '" + canonicalSQLName + "' since it does not have a primary key.");
    }

    // Check the optimistic locking column is not part of the PK

    for (JdbcKeyColumn kc : t.getPk().getKeyColumns()) {
      JdbcColumn c = kc.getColumn();
      if (this.jdbcColumn.getName().equals(c.getName())) {
        throw new InvalidConfigurationFileException(this,
            "Cannot use optimistic locking on table '" + canonicalSQLName + "'. The specified timestamp column '"
                + this.column + "' cannot be part of the primary key of the table.");
      }
    }
  }

  private void validateFullRowCheckStrategy(final Metadata metadata, final String canonicalSQLName, final JdbcTable t)
      throws InvalidConfigurationFileException {
  }

  // Getters

  public OptimisticLockingStrategy getStrategy() {
    return strategy;
  }

  public String getColumn() {
    return column;
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.column;
  }

}
