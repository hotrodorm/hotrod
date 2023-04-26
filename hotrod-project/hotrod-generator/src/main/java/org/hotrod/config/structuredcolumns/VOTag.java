package org.hotrod.config.structuredcolumns;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.Patterns;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.config.TableTag;
import org.hotrod.config.ViewTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.exceptions.InvalidSQLException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.ColumnsRetriever;
import org.hotrod.generator.mybatisspring.DataSetLayout;
import org.hotrod.identifiers.Id;
import org.hotrod.identifiers.ObjectId;
import org.hotrod.metadata.ColumnMetadata;
import org.hotrod.metadata.Metadata;
import org.hotrod.metadata.StructuredColumnMetadata;
import org.hotrod.metadata.StructuredColumnMetadata.IdColumnNotFoundException;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.metadata.VOMetadata;
import org.hotrod.metadata.VORegistry;
import org.hotrod.utils.ColumnsMetadataRetriever;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.hotrod.utils.JdbcTypes;
import org.hotrodorm.hotrod.utils.SUtil;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

@XmlRootElement(name = "vo")
public class VOTag extends AbstractConfigurationTag implements ColumnsProvider {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(VOTag.class);

  // Properties

  // Properties - Primitive content parsing by JAXB

  // This property cannot be transient. JAXB fails when doing so with the
  // message: Transient field "content" cannot have any JAXB annotations.

  @XmlMixed
  @XmlElementRefs({ //
      @XmlElementRef(type = CollectionTag.class), //
      @XmlElementRef(type = AssociationTag.class), //
      @XmlElementRef(type = ExpressionTag.class) //
  })
  private List<Object> content = new ArrayList<Object>();

  private String catalog = null;
  private String schema = null;
  private String table = null;
  private String view = null;

  private ObjectId objectId = null;

  private String id = null;
  private String property = null;
  private String alias = null;
  private String extendedVO = null;
  private List<String> body = null;

  private Set<String> idNames;

  private TableDataSetMetadata tableMetadata;
  private TableDataSetMetadata viewMetadata;

  private List<CollectionTag> collections = new ArrayList<CollectionTag>();
  private List<AssociationTag> associations = new ArrayList<AssociationTag>();

  private Expressions expressions = new Expressions();

  private transient Metadata metadata;

  private String compiledBody;
  private boolean useAllColumns;

  private transient ColumnsMetadataRetriever cmr;
  private String aliasPrefix;

  private transient List<StructuredColumnMetadata> inheritedColumns;
  private transient List<StructuredColumnMetadata> declaredColumns;

  // Constructors

  public VOTag() {
    super("vo");
  }

  protected VOTag(final String name) {
    super(name);
  }

  // JAXB Setters

  @XmlAttribute
  public void setCatalog(final String catalog) {
    this.catalog = catalog;
  }

  @XmlAttribute
  public void setSchema(final String schema) {
    this.schema = schema;
  }

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

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final boolean singleVOResult, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    log.debug("validate");

    // Sort: content, collections, and associations

    this.body = new ArrayList<String>();

    for (Object obj : this.content) {
      try {
        String s = (String) obj; // content part
        if (!SUtil.isEmpty(s)) {
          this.body.add(s);
        }
      } catch (ClassCastException e1) {
        try {
          CollectionTag c = (CollectionTag) obj; // collection
          this.collections.add(c);
          super.addChild(c);
        } catch (ClassCastException e2) {
          try {
            AssociationTag a = (AssociationTag) obj; // association
            this.associations.add(a);
            super.addChild(a);
          } catch (ClassCastException e3) {
            try {
              ExpressionTag exp = (ExpressionTag) obj; // expressions
              this.expressions.addExpression(exp);
              super.addChild(exp);
            } catch (ClassCastException e4) {
              throw new InvalidConfigurationFileException(this, "The body of the tag <" + super.getTagName()
                  + "> has an invalid tag of class " + obj.getClass().getName() + ".");
            }
          }
        }
      }
    }

    this.compiledBody = this.compileBody();

    // table & view

    String label;
    String name;

    if (this.table == null && this.view == null) {
      throw new InvalidConfigurationFileException(this, "Invalid <" + super.getTagName()
          + "> tag. Must specify the 'table' or the 'view' attribute, but none was specified.");
    }
    if (this.table != null && this.view != null) {
      throw new InvalidConfigurationFileException(this,
          "Invalid <" + super.getTagName() + "> tag. Cannot specify both the 'table' and 'view' attributes.");
    }
    if (this.table != null) {
      if (SUtil.isEmpty(this.table)) {
        throw new InvalidConfigurationFileException(this, "Invalid 'table' attribute on the <" + super.getTagName()
            + "> tag. When specified this attribute cannot be empty.");
      }
      label = "table '" + this.table + "'";
      name = this.table;
    } else {
      if (SUtil.isEmpty(this.view)) {
        throw new InvalidConfigurationFileException(this, "Invalid 'view' attribute on the <" + super.getTagName()
            + "> tag. When specified this attribute cannot be empty.");
      }
      label = "view '" + this.view + "'";
      name = this.view;
    }

    // catalog

    Id catalogId;
    try {
      catalogId = this.catalog == null ? null : Id.fromTypedSQL(this.catalog, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid catalog name '" + this.catalog + "' on tag <" + super.getTagName() + "> for the " + label
          + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    // schema

    Id schemaId;
    try {
      schemaId = this.schema == null ? null : Id.fromTypedSQL(this.schema, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid schema name '" + this.schema + "' on tag <" + super.getTagName() + "> for the " + label
          + ": " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    // Assemble object id

    Id nameId;
    try {
      nameId = Id.fromTypedSQL(name, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid " + label + ": " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    try {
      this.objectId = new ObjectId(catalogId, schemaId, nameId, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid table object name: " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg);
    }

    // id

    this.idNames = new HashSet<String>();
    if (this.id != null) {
      for (String idName : this.id.split(",")) {
        if (!idName.isEmpty()) {
          if (this.idNames.contains(idName)) {
            throw new InvalidConfigurationFileException(this,
                "Duplicate column '" + idName + "' on the 'id' attribute. "
                    + "The comma-separated list of column names should not include the same column more than once.");
          }
          this.idNames.add(idName);
        }
      }
      if (this.idNames.isEmpty()) {
        throw new InvalidConfigurationFileException(this, "Invalid value '" + this.id + "' for 'id' attribute. "
            + "Must specify a comma-separated list of column names.");
      }
    }

    // property

    if (this.property == null) {
      if (!singleVOResult) {
        if (super.getTagName().equals("vo")) {
          throw new InvalidConfigurationFileException(this, "Missing 'property' attribute on <" + super.getTagName()
              + "> tag. This attribute can only be omitted "
              + "when the <columns> tag includes a single <vo> tag and no <expressions>, <association>, or <collection> tags.");
        } else {
          throw new InvalidConfigurationFileException(this,
              "Missing 'property' attribute on <" + super.getTagName() + "> tag.");
        }
      }
    } else {
      if (singleVOResult) {
        throw new InvalidConfigurationFileException(this, "The 'property' attribute on the <" + super.getTagName()
            + "> tag should not be specified. This attribute should be omitted "
            + "when the <columns> tag includes a single <vo> tag and no <expressions>, <association>, or <collection> tags.");
      }
      if (!this.property.matches(Patterns.VALID_JAVA_PROPERTY)) {
        throw new InvalidConfigurationFileException(this,
            "Invalid property value '" + this.property + "' on <" + super.getTagName()
                + "> tag. A Java property name must start with a lower case letter, "
                + "and continue with letters, digits, and/or underscores.");
      }
    }

    // alias

    if (this.alias == null) {
      if (!this.compiledBody.trim().isEmpty()) {
        throw new InvalidConfigurationFileException(this,
            "The 'alias' attribute must be specified when columns are specified in the body of the <"
                + super.getTagName() + "> tag. "
                + "It indicates the table or view alias as it appears in the SQL FROM statement "
                + "(for example, the 'p' when typing the column 'p.city_name').");
      }
    }
    if (this.alias != null && SUtil.isEmpty(this.alias)) {
      throw new InvalidConfigurationFileException(this,
          "When specified, the 'alias' attribute must be non-empty. "
              + "It indicates the table or view alias as it appears in the SQL FROM statement "
              + "(for example, the 'p' when typing the column 'p.city_name').");
    }

    // extended-vo

    if (this.extendedVO == null) {
      if (!this.associations.isEmpty() || !this.collections.isEmpty() || !this.expressions.isEmpty()) {
        throw new InvalidConfigurationFileException(this, "The 'extended-vo' attribute must be specified when the <"
            + super.getTagName() + "> tag includes one or more <association>, <colection>, and/or <expressions> tags.");
      }
    } else {
      // if (this.associations.isEmpty() && this.collections.isEmpty() &&
      // this.expressions.isEmpty()) {
      // throw new InvalidConfigurationFileException(this, //
      // "The 'extended-vo' attribute cannot be specified when the <" +
      // super.getTagName()
      // + "> tag does not include any <association>, <colection>, or
      // <expressions> tags", //
      // "The 'extended-vo' attribute cannot be specified when the <" +
      // super.getTagName()
      // + "> tag does not include any <association>, <colection>, or
      // <expressions> tags.");
      // }
      if (!this.extendedVO.matches(Patterns.VALID_JAVA_CLASS)) {
        throw new InvalidConfigurationFileException(this,
            "Invalid 'extended-vo' attribute value '" + this.extendedVO + "' on the <" + super.getTagName()
                + "> tag. A Java class name must start with an upper case letter, "
                + "and continue with letters, digits, and/or underscores.");
      }
    }

    // body - nothing to validate

    // expressions

    this.expressions.validate(daosTag, config, fragmentConfig, singleVOResult, this.idNames);

    // associations

    for (AssociationTag a : this.associations) {
      a.validate(daosTag, config, fragmentConfig, false, adapter);
    }

    // collections

    for (CollectionTag c : this.collections) {
      c.validate(daosTag, config, fragmentConfig, false, adapter);
    }

    log.debug("Validation complete.");
  }

  public void validateAgainstDatabase(final Metadata metadata) throws InvalidConfigurationFileException {
    this.metadata = metadata;
    log.debug("*** this.table=" + this.table + " this.view=" + this.view);
    if (this.table != null) {
      this.tableMetadata = metadata.findTableMetadata(this.objectId);
      log.debug("this.tableMetadata=" + this.tableMetadata);
      if (this.tableMetadata == null) {
        throw new InvalidConfigurationFileException(this,
            "Could not find <" + new TableTag().getTagName() + "> tag in the configuration file for the table '"
                + this.table + "', specified by the 'table' attribute on the <" + super.getTagName() + "> tag.");
      }
      this.viewMetadata = null;
    } else {
      this.viewMetadata = metadata.findViewMetadata(this.objectId);
      if (this.viewMetadata == null) {
        throw new InvalidConfigurationFileException(this,
            "Could not find <" + new ViewTag().getTagName() + "> tag in the configuration file for the view '"
                + this.view + "', specified by the 'view' attribute on the <" + super.getTagName() + "> tag.");
      }
      this.tableMetadata = null;
    }

    // collections

    for (CollectionTag c : this.collections) {
      c.validateAgainstDatabase(metadata);
    }

    // associations

    for (AssociationTag a : this.associations) {
      a.validateAgainstDatabase(metadata);
    }

    // expressions

    this.expressions.validateAgainstDatabase(metadata, this.tableMetadata);

  }

  // Meta data gathering

  @Override
  public void gatherMetadataPhase1(final SelectMethodTag selectTag, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final ColumnsRetriever cr)
      throws InvalidSQLException, InvalidConfigurationFileException {

    // body

    this.useAllColumns = this.body.isEmpty() || this.compiledBody.equals("*");
    log.debug("this.compiledBody=" + this.compiledBody + " this.useAllColumns=" + this.useAllColumns);

    if (this.alias != null) { // includes entity columns (all or partial)
      if (this.useAllColumns) { // all columns
        this.cmr = null;
        this.aliasPrefix = columnsPrefixGenerator.next();
      } else { // specific columns
        log.debug("this.generator=" + this.metadata);
        this.cmr = new ColumnsMetadataRetriever(selectTag, this.metadata.getAdapter(), this.metadata.getJdbcDatabase(),
            this.metadata.getLoc(), selectGenerationTag, this, this.alias, columnsPrefixGenerator, cr);
        this.cmr.prepareRetrieval();
      }
    }

    // expressions, collections, associations

    this.expressions.gatherMetadataPhase1(selectTag, selectGenerationTag, columnsPrefixGenerator, cr);

    for (CollectionTag c : this.collections) {
      c.gatherMetadataPhase1(selectTag, selectGenerationTag, columnsPrefixGenerator, cr);
    }

    for (AssociationTag a : this.associations) {
      a.gatherMetadataPhase1(selectTag, selectGenerationTag, columnsPrefixGenerator, cr);
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
  public void gatherMetadataPhase2() throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException,
      InvalidConfigurationFileException {

    // log.info("===========");
    // log.info("=========== VOTag " + this.table);
    // log.info("=========== - stacktrace:" + LogUtil.renderStack());

    boolean requiresIds = this.incorporatesCollections();

    if (this.alias != null) {

      if (this.useAllColumns) { // 1. All columns

        if (this.tableMetadata != null) { // 1.a Based on a table

          if (this.tableMetadata.getPK() != null) { // 1.a.1 Table with a PK

            if (!this.idNames.isEmpty()) {
              try {
                this.inheritedColumns = StructuredColumnMetadata.promote(this.alias, this.tableMetadata.getColumns(),
                    this.aliasPrefix, this.idNames);
              } catch (IdColumnNotFoundException e) {
                throw new InvalidConfigurationFileException(this,
                    "Could not find column '" + e.getIdName() + "' on the table '"
                        + this.tableMetadata.getId().getCanonicalSQLName() + "' as specified on the 'id' attribute.");
              }
            } else {
              this.inheritedColumns = StructuredColumnMetadata.promote(this.alias, this.tableMetadata.getColumns(),
                  this.aliasPrefix);
            }

          } else { // 1.a.2 Table without a PK

            if (requiresIds && this.idNames.isEmpty()) {
              throw new InvalidConfigurationFileException(this, "Missing 'id' attribute on tag <" + this.getTagName()
                  + ">.\n" + "When a <" + this.getTagName() + "> uses a table with no PK (the table "
                  + this.tableMetadata.getId().getCanonicalSQLName()
                  + " in this case) and includes other <collection> tags, "
                  + "the 'id' attribute must specify the row-identifying columns for the table (i.e. an acting unique key for the table).");
            }
            try {
              this.inheritedColumns = StructuredColumnMetadata.promote(this.alias, this.tableMetadata.getColumns(),
                  this.aliasPrefix, this.idNames);
            } catch (IdColumnNotFoundException e) {
              throw new InvalidConfigurationFileException(this,
                  "Could not find column '" + e.getIdName() + "' on the table '"
                      + this.tableMetadata.getId().getCanonicalSQLName() + "' as specified on the 'id' attribute.");
            }

          }

        } else { // 1.b Based on a view

          log.debug("this.tableMetadata=" + this.tableMetadata);
          log.debug("this.viewMetadata=" + this.viewMetadata);

          if (requiresIds && this.idNames.isEmpty()) {
            throw new InvalidConfigurationFileException(this, "Missing 'id' attribute on tag <" + this.getTagName()
                + ">.\n" + "When a <" + this.getTagName() + "> uses a view and includes other <collection> tags, "
                + "the 'id' attribute must specify the row-identifying columns for the view (i.e. an acting unique key for the view).");
          }
          try {
            this.inheritedColumns = StructuredColumnMetadata.promote(this.alias, this.viewMetadata.getColumns(),
                this.aliasPrefix, this.idNames);
          } catch (IdColumnNotFoundException e) {
            throw new InvalidConfigurationFileException(this,
                "Could not find column '" + e.getIdName() + "' on the view '"
                    + this.viewMetadata.getId().getCanonicalSQLName() + "' as specified on the 'id' attribute.");
          }

        }

        for (StructuredColumnMetadata c : this.inheritedColumns) {
          if (c.isId()) {
            validateIdJDBCType(c);
          }
        }

      } else { // 2. Specific columns

        if (this.tableMetadata != null) { // 2.a Based on a table
          this.inheritedColumns = retrieveSpecificColumns(this.tableMetadata, requiresIds);
        } else { // 2.b Based on a view
          this.inheritedColumns = retrieveSpecificColumns(this.viewMetadata, requiresIds);
        }

      }

    }

    // 3. Retrieve inner expressions, collections, and associations

    this.expressions.gatherMetadataPhase2();
    this.declaredColumns = this.expressions.getMetadata();
    log.debug("DECLARED COLUMNS=" + this.declaredColumns.size());

    for (CollectionTag c : this.collections) {
      c.gatherMetadataPhase2();
    }

    for (AssociationTag a : this.associations) {
      a.gatherMetadataPhase2();
    }

  }

  private List<StructuredColumnMetadata> retrieveSpecificColumns(final TableDataSetMetadata dm,
      final boolean requiresIds) throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException,
      InvalidConfigurationFileException {

    Map<String, ColumnMetadata> baseColumnsByName = new HashMap<String, ColumnMetadata>();
    for (ColumnMetadata cm : dm.getColumns()) {
      baseColumnsByName.put(cm.getColumnName(), cm);
    }

    if (requiresIds) {
      if (dm.getPK() == null && this.idNames.isEmpty()) {
        if (this.tableMetadata != null) {
          throw new InvalidConfigurationFileException(this, "Missing 'id' attribute on tag <" + this.getTagName()
              + ">.\n" + "When a <" + this.getTagName() + "> uses a table with no PK (the table "
              + this.tableMetadata.getId().getCanonicalSQLName()
              + " in this case) and includes other <collection> tags, "
              + "the 'id' attribute must specify the row-identifying columns for the table (i.e. an acting unique key for the table).");
        } else {
          throw new InvalidConfigurationFileException(this, "Missing 'id' attribute on tag <" + this.getTagName()
              + ">.\n" + "When a <" + this.getTagName() + "> uses a view and includes other <collection> tags, "
              + "the 'id' attribute must specify the row-identifying columns for the view (i.e. an acting unique key for the view).");
        }
      }
    }

    if (!this.idNames.isEmpty()) {
      for (String idName : this.idNames) {
        if (!idIsColumn(dm.getColumns(), idName)) {
          throw new InvalidConfigurationFileException(this,
              "Could not find column '" + idName + "' on the "
                  + (this.tableMetadata != null ? "table '" + this.tableMetadata.getId().getCanonicalSQLName()
                      : "view '" + this.viewMetadata.getId().getCanonicalSQLName())
                  + "' as specified on the 'id' attribute.");
        }
      }
    }

    List<StructuredColumnMetadata> metadata = new ArrayList<StructuredColumnMetadata>();
    List<StructuredColumnMetadata> retrievedColumns = this.cmr.retrieve();
    for (StructuredColumnMetadata r : retrievedColumns) {
      ColumnMetadata baseColumn = baseColumnsByName.get(r.getColumnName());
      if (baseColumn == null) {
        String msg = "Invalid column '" + r.getColumnName() + "' in the body of the <" + this.getTagName() + "> tag at "
            + super.getSourceLocation().render() + ".\n" + "There's no column '" + r.getColumnName() + "' in the "
            + (this.tableMetadata != null ? "table" : "view") + " '"
            + (this.tableMetadata != null ? this.tableMetadata.getId().getRenderedSQLName()
                : this.viewMetadata.getId().getRenderedSQLName())
            + "'.";
        throw new InvalidConfigurationFileException(dm.getDaoTag(), msg);
      }
      boolean isId = this.idNames.isEmpty() ? baseColumn.belongsToPK() : columnIsId(baseColumn);
      if (isId) {
        validateIdJDBCType(baseColumn);
      }
      StructuredColumnMetadata sc = new StructuredColumnMetadata(baseColumn, this.alias, r.getColumnAlias(), isId,
          this);
      metadata.add(sc);
    }
    return metadata;
  }

  private void validateIdJDBCType(final ColumnMetadata baseColumn) throws InvalidConfigurationFileException {
    Integer jdbcType = baseColumn.getDataType();
    if (!VALID_ID_JDBC_TYPES.contains(jdbcType)) {
      List<String> validJdbcTypes = new ArrayList<String>();
      for (Integer t : VALID_ID_JDBC_TYPES) {
        validJdbcTypes.add(SUtil.alignRight("" + t, 7) + " (" + JdbcTypes.codeToName(t) + ")");
      }
      throw new InvalidConfigurationFileException(this,
          "Unsupported JDBC type " + jdbcType + " (" + JdbcTypes.codeToName(jdbcType) + ") on column '"
              + baseColumn.getColumnName() + "' of "
              + (this.tableMetadata != null ? "table '" + this.tableMetadata.getId().getCanonicalSQLName() + "'"
                  : "view '" + this.viewMetadata.getId().getCanonicalSQLName() + "'")
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

  public VORegistry getVORegistry() {
    return this.metadata.getVORegistry();
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

  public Expressions getExpressions() {
    return this.expressions;
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

  public Set<TableDataSetMetadata> getReferencedEntities() {
    Set<TableDataSetMetadata> entities = new HashSet<TableDataSetMetadata>();
    if (this.tableMetadata != null) {
      entities.add(this.tableMetadata);
    }
    if (this.viewMetadata != null) {
      entities.add(this.viewMetadata);
    }
    for (VOTag a : this.associations) {
      entities.addAll(a.getReferencedEntities());
    }
    for (VOTag a : this.collections) {
      entities.addAll(a.getReferencedEntities());
    }
    return entities;
  }

  public VOMetadata getMetadata(final DataSetLayout layout, final HotRodFragmentConfigTag fragmentConfig,
      final DaosTag daosTag) throws InvalidConfigurationFileException {
    return new VOMetadata(this, layout, fragmentConfig, daosTag);
  }

  // Rendering

  public List<String> gelAliasedSQLColumns() {
    List<String> columns = new ArrayList<String>();
    if (this.inheritedColumns != null) {
      for (StructuredColumnMetadata m : this.inheritedColumns) {
        columns.add(m.renderAliasedSQLColumn());
      }
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
          w.add(cm.getId().getRenderedSQLName());
        }
      } else {
        for (ColumnMetadata cm : this.viewMetadata.getColumns()) {
          w.add(cm.getId().getRenderedSQLName());
        }
      }
      return w.toString();
    } else { // specified columns only
      return this.compiledBody;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName();
  }

}
