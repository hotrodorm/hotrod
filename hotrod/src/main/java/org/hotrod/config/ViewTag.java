package org.hotrod.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hotrod.ant.Constants;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.ClassPackage;
import org.hotrod.utils.SUtils;
import org.hotrod.utils.identifiers.DataSetIdentifier;
import org.hotrod.utils.identifiers.DbIdentifier;
import org.nocrala.tools.database.tartarus.core.JdbcColumn;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public class ViewTag extends AbstractCompositeDAOTag {

  private static final Logger log = Logger.getLogger(ViewTag.class);

  static final String TAG_NAME = "view";

  private String name = null;
  private String javaClassName = null;
  private List<ColumnTag> columns = new ArrayList<ColumnTag>();

  @SuppressWarnings("unused")
  private List<SequenceTag> sequences = new ArrayList<SequenceTag>();
  @SuppressWarnings("unused")
  private List<QueryTag> updates = new ArrayList<QueryTag>();

  private DaosTag daosTag;
  private HotRodFragmentConfigTag fragmentConfig;
  private ClassPackage fragmentPackage;

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig) throws InvalidConfigurationFileException {
    log.debug("validate");

    this.daosTag = daosTag;
    this.fragmentConfig = fragmentConfig;
    this.fragmentPackage = this.fragmentConfig != null && this.fragmentConfig.getFragmentPackage() != null
        ? this.fragmentConfig.getFragmentPackage() : null;

    String nameTitle = "name";
    String nameValue = this.name;

    // name

    if (SUtils.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(
          "Attribute 'name' of tag <" + TAG_NAME + "> cannot be empty. " + "Must specify a database view name.");
    }

    // java-name

    if (this.javaClassName != null) {
      this.javaClassName = this.javaClassName.trim();
      if (SUtils.isEmpty(this.javaClassName)) {
        throw new InvalidConfigurationFileException("Invalid 'java-name' attribute value of tag <" + TAG_NAME
            + "> for the view '" + this.name + "'. When specified, the value cannot be empty.");
      }
      if (!this.javaClassName.matches(TableTag.VALID_JAVA_NAME_PATTERN)) {
        throw new InvalidConfigurationFileException(
            "Invalid 'java-name' attribute value '" + this.javaClassName + "' of tag <" + TAG_NAME + "> for the view '"
                + this.name + "'. When specified, the java-name must start with an upper case letter, "
                + "and continue with any combination of letters, digits, underscores, or dollar signs.");
      }
      nameTitle = "java-class-name";
      nameValue = this.javaClassName;
      // } else {
      // this.javaClassName = daosTag.generateDAOName(new
      // DbIdentifier(this.name));
    }

    // columns

    Set<ColumnTag> cols = new HashSet<ColumnTag>();
    for (ColumnTag c : this.columns) {
      c.validate(ViewTag.TAG_NAME, this.name, config);
      if (cols.contains(c)) {
        throw new InvalidConfigurationFileException(
            "Multiple <" + ColumnTag.TAG_NAME + "> tags with the same name on tag <" + TAG_NAME + "> for table '"
                + this.name + "'. You cannot specify the same column name " + "multiple times on a same view.");
      }
      cols.add(c);
    }

    // sequences and updates

    super.validate(TAG_NAME, nameTitle, nameValue);

  }

  public void validateAgainstDatabase(final JdbcDatabase db, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    JdbcTable v = this.findJdbcView(db, adapter);
    if (v == null) {
      throw new InvalidConfigurationFileException(
          "Could not find database view '" + this.name + "' as specified in the <view> tag of the configuration file. "
              + "\n\nPlease verify the specified database catalog and schema names are correct according to this database. "
              + "You can try leaving the catalog/schema values empty, so " + Constants.TOOL_NAME
              + " will list all available values.");
    }

    for (ColumnTag c : this.columns) {
      JdbcColumn jc = this.findJdbcColumn(c.getName(), v, adapter);
      if (jc == null) {
        throw new InvalidConfigurationFileException("Could not find column '" + c.getName() + "' on database view '"
            + this.name + "', as specified in the <column> tag of the configuration file. ");
      }
    }

  }

  // Search

  private JdbcTable findJdbcView(final JdbcDatabase db, final DatabaseAdapter adapter) {
    for (JdbcTable v : db.getViews()) {
      if (adapter.isTableIdentifier(v.getName(), this.name)) {
        return v;
      }
    }
    return null;
  }

  private JdbcColumn findJdbcColumn(final String name, final JdbcTable t, final DatabaseAdapter adapter) {
    for (JdbcColumn jc : t.getColumns()) {
      if (adapter.isColumnIdentifier(jc.getName(), name)) {
        return jc;
      }
    }
    return null;
  }

  public ColumnTag findColumnTag(final String columnName, final DatabaseAdapter adapter) {
    for (ColumnTag ct : this.columns) {
      if (ct.isName(columnName, adapter)) {
        return ct;
      }
    }
    return null;
  }

  // Setters (digester)

  public void setName(final String name) {
    this.name = name;
  }

  public void setJavaName(final String javaName) {
    this.javaClassName = javaName;
  }

  public void addColumn(final ColumnTag c) {
    this.columns.add(c);
  }

  // Getters

  public String getName() {
    return name;
  }

  public String getJavaName() {
    return javaClassName;
  }

  public List<ColumnTag> getColumns() {
    return columns;
  }

  public HotRodFragmentConfigTag getFragmentConfig() {
    return fragmentConfig;
  }

  // DAO Tag implementation

  @Override
  public ClassPackage getPackage() {
    return this.daosTag.getDaoPackage(this.fragmentPackage);
  }

  @Override
  public String getJavaClassName() {
    if (this.javaClassName == null) {
      return new DataSetIdentifier(this.name).getJavaClassIdentifier();
    } else {
      return new DataSetIdentifier(this.name, this.javaClassName).getJavaClassIdentifier();
    }
  }

}
