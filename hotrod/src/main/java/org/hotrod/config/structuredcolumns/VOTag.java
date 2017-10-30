package org.hotrod.config.structuredcolumns;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.Patterns;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.mybatis.DataSetLayout;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata.IdColumnNotFoundException;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.metadata.VOMetadata;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ColumnsMetadataRetriever;
import org.hotrod.utils.ColumnsMetadataRetriever.InvalidSQLException;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.hotrod.utils.JdbcTypes;

@XmlRootElement(name = "vo")
public class VOTag extends AbstractConfigurationTag implements ColumnsProvider {

  // Constants

  private static final Logger log = Logger.getLogger(VOTag.class);

  // Properties

  // Properties - Primitive content parsing by JAXB

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = CollectionTag.class), //
      @XmlElementRef(type = AssociationTag.class), //
      @XmlElementRef(type = ExpressionsTag.class) //
  })
  private List<Object> content = new ArrayList<Object>();

  private String table = null;
  private String view = null;
  private String id = null;
  private String property = null;
  private String alias = null;
  private String extendedVO = null;
  private List<String> body = null;

  private Set<String> idNames;

  private TableDataSetMetadata tableMetadata;
  private TableDataSetMetadata viewMetadata;

  private List<ExpressionsTag> expressions = new ArrayList<ExpressionsTag>();
  private List<CollectionTag> collections = new ArrayList<CollectionTag>();
  private List<AssociationTag> associations = new ArrayList<AssociationTag>();

  private HotRodGenerator generator;

  private String compiledBody;
  private boolean useAllColumns;

  private ColumnsMetadataRetriever cmr;
  private String aliasPrefix;

  private List<StructuredColumnMetadata> inheritedColumns;
  private List<StructuredColumnMetadata> declaredColumns;

  // Constructors

  public VOTag() {
    super("vo");
  }

  protected VOTag(final String name) {
    super(name);
  }

  // JAXB Setters

  @XmlAttribute
  public void setTable(final String table) {
    this.table = table;
  }

  @XmlAttribute
  public void setView(final String view) {
    this.view = view;
  }

  @XmlAttribute
  public void setId(final String id) {
    this.id = id;
  }

  @XmlAttribute
  public void setProperty(final String property) {
    this.property = property;
  }

  @XmlAttribute
  public void setAlias(final String alias) {
    this.alias = alias;
  }

  @XmlAttribute(name = "extended-vo")
  public void setExtendedVO(final String extendedVO) {
    this.extendedVO = extendedVO;
  }

  // Behavior

  @Override
  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final boolean singleVOResult)
      throws InvalidConfigurationFileException {

    log.debug("validate");

    // Sort: content, collections, and associations

    this.body = new ArrayList<String>();

    for (Object obj : this.content) {
      try {
        String s = (String) obj; // content part
        if (!SUtils.isEmpty(s)) {
          this.body.add(s);
        }
      } catch (ClassCastException e1) {
        try {
          CollectionTag c = (CollectionTag) obj; // collection
          this.collections.add(c);
        } catch (ClassCastException e2) {
          try {
            AssociationTag a = (AssociationTag) obj; // association
            this.associations.add(a);
          } catch (ClassCastException e3) {
            try {
              ExpressionsTag expr = (ExpressionsTag) obj; // expressions
              this.expressions.add(expr);
            } catch (ClassCastException e4) {
              throw new InvalidConfigurationFileException(super.getSourceLocation(), "The body of the tag <"
                  + super.getTagName() + "> has an invalid tag (of class '" + obj.getClass().getName() + "').");
            }
          }
        }
      }
    }

    // table & view

    if (this.table == null && this.view == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid <" + super.getTagName()
          + "> tag. Must specify the 'table' or the 'view' attribute, but none was specified.");
    }
    if (this.table != null && this.view != null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Invalid <" + super.getTagName() + "> tag. Cannot specify both the 'table' and 'view' attributes.");
    }
    if (this.table != null) {
      if (SUtils.isEmpty(this.table)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid 'table' attribute on the <"
            + super.getTagName() + "> tag. When specified this attribute must not be empty.");
      }
    } else {
      if (SUtils.isEmpty(this.view)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid 'view' attribute on the <"
            + super.getTagName() + "> tag. When specified this attribute must not be empty.");
      }
    }

    // id

    this.idNames = new HashSet<String>();
    if (this.id != null) {
      for (String idName : this.id.split(",")) {
        if (!idName.isEmpty()) {
          if (this.idNames.contains(idName)) {
            throw new InvalidConfigurationFileException(super.getSourceLocation(), "Duplicate column '" + idName
                + "' on the 'id' attribute. The comma-separated list of column names should not include the same column more than once.");
          }
          this.idNames.add(idName);
        }
      }
      if (this.idNames.isEmpty()) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "Invalid value '" + this.id
            + "' for 'id' attribute. " + "Must specify a comma-separated list of column names.");
      }
    }

    // property

    if (this.property == null) {
      if (!singleVOResult) {
        if (super.getTagName().equals("vo")) {
          throw new InvalidConfigurationFileException(super.getSourceLocation(), "Missing 'property' attribute on <"
              + super.getTagName() + "> tag. This attribute can only be omitted "
              + "when the <columns> tag includes a single <vo> tag and no <expressions>, <association>, or <collection> tags.");
        } else {
          throw new InvalidConfigurationFileException(super.getSourceLocation(),
              "Missing required 'property' attribute on <" + super.getTagName() + "> tag.");
        }
      }
    } else {
      if (singleVOResult) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "The 'property' attribute on the <"
            + super.getTagName() + "> tag should not be specified. This attribute should be omitted "
            + "when the <columns> tag includes a single <vo> tag and no <expressions>, <association>, or <collection> tags.");
      }
      if (!this.property.matches(Patterns.VALID_JAVA_PROPERTY)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid property value '" + this.property + "' on <" + super.getTagName()
                + "> tag. A Java property name must start with a lower case letter, "
                + "and continue with letters, digits, and/or underscores.");
      }
    }

    // alias

    if (this.alias == null) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "The 'alias' attribute must be specified. "
              + "It indicates the table or view alias as it appears in the SQL FROM statement "
              + "(for example, the 'p' when typing the column 'p.city_name').");
    }
    if (SUtils.isEmpty(this.alias)) {
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "The 'alias' attribute must be non-empty. "
              + "It indicates the table or view alias as it appears in the SQL FROM statement "
              + "(for example, the 'p' when typing the column 'p.city_name').");
    }

    // extended-vo

    if (this.extendedVO == null) {
      if (!this.associations.isEmpty() || !this.collections.isEmpty() || !this.expressions.isEmpty()) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "The 'extended-vo' attribute must be specified when the <" + super.getTagName()
                + "> tag includes one or more <association>, <colection>, and/or <expressions> tags.");
      }
    } else {
      if (this.associations.isEmpty() && this.collections.isEmpty() && this.expressions.isEmpty()) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "The 'extended-vo' attribute must not be specified when the <" + super.getTagName()
                + "> tag does not include any <association>, <colection>, or <expressions> tags.");
      }
      if (!this.extendedVO.matches(Patterns.VALID_JAVA_CLASS)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Invalid 'extended-vo' attribute value '" + this.extendedVO + "' on the <" + super.getTagName()
                + "> tag. A Java class name must start with an upper case letter, "
                + "and continue with letters, digits, and/or underscores.");
      }
    }

    // body - nothing to validate

    // expressions

    for (ExpressionsTag exp : this.expressions) {
      exp.validate(daosTag, config, fragmentConfig, false);
    }

    // associations

    for (AssociationTag a : this.associations) {
      a.validate(daosTag, config, fragmentConfig, false);
    }

    // collections

    for (CollectionTag c : this.collections) {
      c.validate(daosTag, config, fragmentConfig, false);
    }

  }

  public void validateAgainstDatabase(final HotRodGenerator generator) throws InvalidConfigurationFileException {
    this.generator = generator;
    if (this.table != null) {
      this.tableMetadata = generator.findTableMetadata(this.table);
      if (this.tableMetadata == null) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Could not find <" + new TableTag().getTagName() + "> tag in the configuration file for the table '"
                + this.table + "', specified by the 'table' attribute on the <" + super.getTagName() + "> tag.");
      }
      this.viewMetadata = null;
    } else {
      this.viewMetadata = generator.findViewMetadata(this.view);
      if (this.viewMetadata == null) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Could not find <" + new ViewTag().getTagName() + "> tag in the configuration file for the view '"
                + this.view + "', specified by the 'view' attribute on the <" + super.getTagName() + "> tag.");
      }
      this.tableMetadata = null;
    }

    // collections

    for (CollectionTag c : this.collections) {
      c.validateAgainstDatabase(generator);
    }

    // associations

    for (AssociationTag a : this.associations) {
      a.validateAgainstDatabase(generator);
    }

    // expressions

    for (ExpressionsTag exp : this.expressions) {
      exp.validateAgainstDatabase(generator);
    }

  }

  // Meta data gathering

  @Override
  public void gatherMetadataPhase1(final SelectMethodTag selectTag, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final Connection conn1) throws InvalidSQLException {

    // body

    this.compiledBody = this.compileBody();
    this.useAllColumns = this.body.isEmpty() || this.compiledBody.equals("*");
    log.debug("this.compiledBody=" + this.compiledBody + " this.useAllColumns=" + this.useAllColumns);

    if (this.useAllColumns) { // all columns
      this.cmr = null;
      this.aliasPrefix = columnsPrefixGenerator.next();
    } else { // specific columns
      this.cmr = new ColumnsMetadataRetriever(selectTag, this.generator.getAdapter(), this.generator.getJdbcDatabase(),
          this.generator.getLoc(), selectGenerationTag, this, this.alias, columnsPrefixGenerator);
      this.cmr.prepareRetrieval(conn1);
    }

    // expressions, collections, associations

    for (ExpressionsTag exp : this.expressions) {
      exp.gatherMetadataPhase1(selectTag, selectGenerationTag, columnsPrefixGenerator, conn1);
    }

    for (CollectionTag c : this.collections) {
      c.gatherMetadataPhase1(selectTag, selectGenerationTag, columnsPrefixGenerator, conn1);
    }

    for (AssociationTag a : this.associations) {
      a.gatherMetadataPhase1(selectTag, selectGenerationTag, columnsPrefixGenerator, conn1);
    }

  }

  private static final Set<Integer> VALID_ID_JDBC_TYPES = new LinkedHashSet<Integer>();
  static {
    VALID_ID_JDBC_TYPES.add(java.sql.Types.BIT);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.TINYINT);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.SMALLINT);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.INTEGER);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.BIGINT);

    VALID_ID_JDBC_TYPES.add(java.sql.Types.FLOAT);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.REAL);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.DOUBLE);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.NUMERIC);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.DECIMAL);

    VALID_ID_JDBC_TYPES.add(java.sql.Types.CHAR);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.VARCHAR);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.LONGVARCHAR);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.DATE);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.TIME);

    VALID_ID_JDBC_TYPES.add(java.sql.Types.TIMESTAMP);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.BINARY);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.VARBINARY);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.LONGVARBINARY);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.NULL);

    VALID_ID_JDBC_TYPES.add(java.sql.Types.OTHER);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.BLOB);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.CLOB);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.BOOLEAN);
    // VALID_ID_JDBC_TYPES.add(java.sql.Types.); // CURSOR

    // VALID_ID_JDBC_TYPES.add(java.sql.Types.); // UNDEFINED
    VALID_ID_JDBC_TYPES.add(java.sql.Types.NVARCHAR);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.NCHAR);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.NCLOB);
    VALID_ID_JDBC_TYPES.add(java.sql.Types.ARRAY);
  }

  @Override
  public void gatherMetadataPhase2(final Connection conn2) throws InvalidSQLException, UncontrolledException,
      UnresolvableDataTypeException, ControlledException, InvalidConfigurationFileException {

    boolean requiresIds = this.incorporatesCollections();

    if (this.useAllColumns) { // 1. All columns

      if (this.tableMetadata != null) { // 1.a Based on a table

        if (this.tableMetadata.getPK() != null) { // 1.a.1 Table with a PK

          if (!this.idNames.isEmpty()) {
            try {
              this.inheritedColumns = StructuredColumnMetadata.promote(this.alias, this.tableMetadata.getColumns(),
                  this.aliasPrefix, this.idNames);
            } catch (IdColumnNotFoundException e) {
              throw new InvalidConfigurationFileException(super.getSourceLocation(),
                  "Could not find column '" + e.getIdName() + "' on the table '"
                      + this.tableMetadata.getIdentifier().getSQLIdentifier()
                      + "' as specified on the 'id' attribute.");
            }
          } else {
            this.inheritedColumns = StructuredColumnMetadata.promote(this.alias, this.tableMetadata.getColumns(),
                this.aliasPrefix);
          }

        } else { // 1.a.2 Table without a PK

          if (requiresIds && this.idNames.isEmpty()) {
            throw new InvalidConfigurationFileException(super.getSourceLocation(), "Missing 'id' attribute on tag <"
                + this.getTagName() + ">.\n" + "When a <" + this.getTagName() + "> uses a table with no PK (the table "
                + this.tableMetadata.getIdentifier().getSQLIdentifier()
                + " in this case) and includes other <collection> tags, "
                + "the 'id' attribute must specify the row-identifying columns for the table (i.e. an acting unique key for the table).");
          }
          try {
            this.inheritedColumns = StructuredColumnMetadata.promote(this.alias, this.tableMetadata.getColumns(),
                this.aliasPrefix, this.idNames);
          } catch (IdColumnNotFoundException e) {
            throw new InvalidConfigurationFileException(super.getSourceLocation(),
                "Could not find column '" + e.getIdName() + "' on the table '"
                    + this.tableMetadata.getIdentifier().getSQLIdentifier() + "' as specified on the 'id' attribute.");
          }

        }

      } else { // 1.b Based on a view

        if (requiresIds && this.idNames.isEmpty()) {
          throw new InvalidConfigurationFileException(super.getSourceLocation(), "Missing 'id' attribute on tag <"
              + this.getTagName() + ">.\n" + "When a <" + this.getTagName()
              + "> uses a view and includes other <collection> tags, "
              + "the 'id' attribute must specify the row-identifying columns for the view (i.e. an acting unique key for the view).");
        }
        try {
          this.inheritedColumns = StructuredColumnMetadata.promote(this.alias, this.viewMetadata.getColumns(),
              this.aliasPrefix, this.idNames);
        } catch (IdColumnNotFoundException e) {
          throw new InvalidConfigurationFileException(super.getSourceLocation(),
              "Could not find column '" + e.getIdName() + "' on the view '"
                  + this.viewMetadata.getIdentifier().getSQLIdentifier() + "' as specified on the 'id' attribute.");
        }

      }

      for (StructuredColumnMetadata c : this.inheritedColumns) {
        if (c.isId()) {
          validateIdJDBCType(c);
        }
      }

    } else { // 2. Specific columns

      if (this.tableMetadata != null) { // 2.a Based on a table

        if (this.tableMetadata.getPK() != null) { // 2.a.1 Table with a PK

          this.inheritedColumns = retrieveSpecificColumns(conn2, this.tableMetadata, requiresIds);

        } else { // 2.a.2 Table without a PK

          this.inheritedColumns = retrieveSpecificColumns(conn2, this.tableMetadata, requiresIds);

        }

      } else { // 2.b Based on a view

        this.inheritedColumns = retrieveSpecificColumns(conn2, this.viewMetadata, requiresIds);

      }

    }

    // 3. Retrieve inner expressions, collections, and associations

    this.declaredColumns = new ArrayList<StructuredColumnMetadata>();

    for (ExpressionsTag exp : this.expressions) {
      exp.gatherMetadataPhase2(conn2);
      this.declaredColumns.addAll(exp.getColumnsMetadata());
    }

    for (CollectionTag c : this.collections) {
      c.gatherMetadataPhase2(conn2);
    }

    for (AssociationTag a : this.associations) {
      a.gatherMetadataPhase2(conn2);
    }

  }

  private List<StructuredColumnMetadata> retrieveSpecificColumns(final Connection conn2, final TableDataSetMetadata dm,
      final boolean requiresIds) throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException,
      ControlledException, InvalidConfigurationFileException {

    Map<String, ColumnMetadata> baseColumnsByName = new HashMap<String, ColumnMetadata>();
    for (ColumnMetadata cm : dm.getColumns()) {
      baseColumnsByName.put(cm.getColumnName(), cm);
    }

    if (requiresIds) {
      if (dm.getPK() == null && this.idNames.isEmpty()) {
        if (this.tableMetadata != null) {
          throw new InvalidConfigurationFileException(super.getSourceLocation(), "Missing 'id' attribute on tag <"
              + this.getTagName() + ">.\n" + "When a <" + this.getTagName() + "> uses a table with no PK (the table "
              + this.tableMetadata.getIdentifier().getSQLIdentifier()
              + " in this case) and includes other <collection> tags, "
              + "the 'id' attribute must specify the row-identifying columns for the table (i.e. an acting unique key for the table).");
        } else {
          throw new InvalidConfigurationFileException(super.getSourceLocation(), "Missing 'id' attribute on tag <"
              + this.getTagName() + ">.\n" + "When a <" + this.getTagName()
              + "> uses a view and includes other <collection> tags, "
              + "the 'id' attribute must specify the row-identifying columns for the view (i.e. an acting unique key for the view).");
        }
      }
    }

    if (!this.idNames.isEmpty()) {
      for (String idName : this.idNames) {
        if (!idIsColumn(dm.getColumns(), idName)) {
          throw new InvalidConfigurationFileException(super.getSourceLocation(),
              "Could not find column '" + idName + "' on the "
                  + (this.tableMetadata != null ? "table '" + this.tableMetadata.getIdentifier().getSQLIdentifier()
                      : "view '" + this.viewMetadata.getIdentifier().getSQLIdentifier())
                  + "' as specified on the 'id' attribute.");
        }
      }
    }

    List<StructuredColumnMetadata> metadata = new ArrayList<StructuredColumnMetadata>();
    List<StructuredColumnMetadata> retrievedColumns = this.cmr.retrieve(conn2);
    for (StructuredColumnMetadata r : retrievedColumns) {
      ColumnMetadata baseColumn = baseColumnsByName.get(r.getColumnName());
      if (baseColumn == null) {
        throw new ControlledException(
            "Invalid column '" + r.getColumnName() + "' in the body of the <" + this.getTagName() + "> tag at "
                + super.getSourceLocation().render() + ".\n" + "There's no column '" + r.getColumnName() + "' in the "
                + (this.tableMetadata != null ? "table" : "view") + " '" + (this.tableMetadata != null
                    ? this.tableMetadata.renderSQLIdentifier() : this.viewMetadata.renderSQLIdentifier())
                + "'.");
      }
      boolean isId = this.idNames.isEmpty() ? baseColumn.belongsToPK() : columnIsId(baseColumn);
      if (isId) {
        validateIdJDBCType(baseColumn);
      }
      StructuredColumnMetadata sc = new StructuredColumnMetadata(baseColumn, this.alias, r.getColumnAlias(), isId);
      metadata.add(sc);
    }
    return metadata;
  }

  private void validateIdJDBCType(final ColumnMetadata baseColumn) throws InvalidConfigurationFileException {
    Integer jdbcType = baseColumn.getDataType();
    if (!VALID_ID_JDBC_TYPES.contains(jdbcType)) {
      List<String> validJdbcTypes = new ArrayList<String>();
      for (Integer t : VALID_ID_JDBC_TYPES) {
        validJdbcTypes.add(SUtils.alignRight("" + t, 7) + " (" + JdbcTypes.codeToName(t) + ")");
      }
      throw new InvalidConfigurationFileException(super.getSourceLocation(),
          "Unsupported JDBC type " + jdbcType + " (" + JdbcTypes.codeToName(jdbcType) + ") on column '"
              + baseColumn.getColumnName() + "' of "
              + (this.tableMetadata != null ? "table '" + this.tableMetadata.getIdentifier().getSQLIdentifier() + "'"
                  : "view '" + this.viewMetadata.getIdentifier().getSQLIdentifier() + "'")
              + ". " + "A column of this type cannot be used as an id column.\n" + "Supported JDBC types are:\n"
              + ListWriter.render(validJdbcTypes, "  ", "", "\n"));
    }
  }

  private boolean columnIsId(final ColumnMetadata cm) {
    for (String idName : this.idNames) {
      if (cm.isConfigurationName(idName)) {
        return true;
      }
    }
    return false;
  }

  private boolean idIsColumn(final List<ColumnMetadata> cols, final String idName) {
    for (ColumnMetadata cm : cols) {
      if (cm.isConfigurationName(idName)) {
        return true;
      }
    }
    return false;
  }

  public boolean incorporatesCollections() {
    if (!this.collections.isEmpty()) {
      return true;
    }
    for (AssociationTag a : this.associations) {
      if (a.incorporatesCollections()) {
        return true;
      }
    }
    return false;
  }

  // toString

  public String toString() {
    return "{VOTag: " + (this.tableMetadata != null ? "table=" + this.table : "view=" + this.view) + ", property="
        + this.property + "}";
  }

  // Utilities

  private String compileBody() {
    if (this.body == null) {
      log.debug("[body is null] ");
      return null;
    }
    StringBuilder sb = new StringBuilder();
    boolean endedWithComma = false;
    boolean first = true;
    for (String s : this.body) {
      log.debug("[body part] " + s);
      String t = s.trim();
      if (!t.isEmpty()) {
        if (first) {
          first = false;
        } else {
          if (!t.startsWith(",") && !endedWithComma) {
            sb.append(", ");
          }
        }
        sb.append(t);
        endedWithComma = t.endsWith(",");
      }
    }
    return sb.toString();
  }

  // Getters

  public HotRodGenerator getGenerator() {
    return generator;
  }

  public String getTable() {
    return table;
  }

  public String getView() {
    return view;
  }

  public String getId() {
    return id;
  }

  public String getProperty() {
    return property;
  }

  public String getAlias() {
    return alias;
  }

  public String getExtendedVO() {
    return extendedVO;
  }

  public List<CollectionTag> getCollections() {
    return collections;
  }

  public List<AssociationTag> getAssociations() {
    return associations;
  }

  public List<ExpressionsTag> getExpressions() {
    return expressions;
  }

  public List<StructuredColumnMetadata> getInheritedColumns() {
    return inheritedColumns;
  }

  public List<StructuredColumnMetadata> getDeclaredColumns() {
    return declaredColumns;
  }

  // Meta data

  public TableDataSetMetadata getTableMetadata() {
    return tableMetadata;
  }

  public TableDataSetMetadata getViewMetadata() {
    return viewMetadata;
  }

  public VOMetadata getMetadata(final DataSetLayout layout, final HotRodFragmentConfigTag fragmentConfig,
      final DaosTag daosTag) throws InvalidConfigurationFileException {
    return new VOMetadata(this, layout, fragmentConfig, daosTag);
  }

  // Rendering

  public List<String> gelAliasedSQLColumns() {
    List<String> columns = new ArrayList<String>();
    for (StructuredColumnMetadata m : this.inheritedColumns) {
      columns.add(m.renderAliasedSQLColumn());
    }
    for (StructuredColumnMetadata m : this.declaredColumns) { // expressions
      columns.add(m.renderAliasedSQLColumn());
    }
    for (AssociationTag a : this.associations) {
      columns.addAll(a.gelAliasedSQLColumns());
    }
    for (CollectionTag a : this.collections) {
      columns.addAll(a.gelAliasedSQLColumns());
    }
    return columns;
  }

  // Angle rendering

  @Override
  public String renderColumns() {
    if (this.useAllColumns) { // all columns
      ListWriter w = new ListWriter(",\n  ");
      if (this.tableMetadata != null) {
        for (ColumnMetadata cm : this.tableMetadata.getColumns()) {
          w.add(cm.renderSQLIdentifier());
        }
      } else {
        for (ColumnMetadata cm : this.viewMetadata.getColumns()) {
          w.add(cm.renderSQLIdentifier());
        }
      }
      return w.toString();
    } else { // specified columns only
      return this.compiledBody;
    }
  }

}
