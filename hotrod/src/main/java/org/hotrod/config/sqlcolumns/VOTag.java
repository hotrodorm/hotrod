package org.hotrod.config.sqlcolumns;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.DaosTag;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.HotRodFragmentConfigTag;
import org.hotrod.config.Patterns;
import org.hotrod.config.SelectGenerationTag;
import org.hotrod.config.SelectMethodTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UnresolvableDataTypeException;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.metadata.TableDataSetMetadata;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.ColumnsMetadataRetriever;
import org.hotrod.utils.ColumnsMetadataRetriever.InvalidSQLException;
import org.hotrod.utils.ColumnsPrefixGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

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
  private String property = null;
  private String alias = null;
  private String extendedVO = null;
  private List<String> body = null;

  private TableDataSetMetadata tableMetadata;
  private TableDataSetMetadata viewMetadata;

  private List<ExpressionsTag> expressions = new ArrayList<ExpressionsTag>();
  private List<CollectionTag> collections = new ArrayList<CollectionTag>();
  private List<AssociationTag> associations = new ArrayList<AssociationTag>();

  private boolean existingVO;
  private String voClass;

  private ColumnsMetadataRetriever cmr;

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

    if (this.alias != null) {
      if (SUtils.isEmpty(this.alias)) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "When specified, the 'alias' attribute should not be empty.");
      }
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

    // body

    if (this.body.isEmpty()) {
      if (this.alias == null) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "When a <" + this.getTagName()
            + "> tag does not include a list of columns in its body, it must specify the 'alias' attribute. ");
      }
    } else {
      if (this.alias != null) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "When a <" + this.getTagName()
                + "> tag includes a list of columns in its body, it cannot specify an 'alias' attribute. "
                + "Remove either the body of the <" + this.getTagName() + "> tag or the 'alias' attribute.");
      }
    }

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
    if (this.table != null) {
      this.tableMetadata = generator.findTableMetadata(this.table);
      if (this.tableMetadata == null) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "Could not find table '" + this.table
            + "', specified by the 'table' attribute on the <" + super.getTagName() + "> tag.");
      }
      this.viewMetadata = null;
    } else {
      this.viewMetadata = generator.findViewMetadata(this.view);
      if (this.viewMetadata == null) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(), "Could not find view '" + this.table
            + "', specified by the 'view' attribute on the <" + super.getTagName() + "> tag.");
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
  public void gatherMetadataPhase1(final SelectMethodTag selectTag, final DatabaseAdapter adapter,
      final JdbcDatabase db, final DatabaseLocation loc, final SelectGenerationTag selectGenerationTag,
      final ColumnsPrefixGenerator columnsPrefixGenerator, final Connection conn1) throws InvalidSQLException {

    if (this.expressions.isEmpty() && this.collections.isEmpty() && this.associations.isEmpty()) {

      this.existingVO = true;
      this.voClass = this.table != null ? this.tableMetadata.getIdentifier().getJavaClassIdentifier()
          : this.viewMetadata.getIdentifier().getJavaClassIdentifier();

      String b = this.compileBody();
      if (this.body.isEmpty() || b.equals("*")) {
        // all columns from the dataset

      } else {
        // specific columns
        this.cmr = new ColumnsMetadataRetriever(selectTag, adapter, db, loc, selectGenerationTag, this,
            columnsPrefixGenerator);
      }

    } else {

      this.existingVO = false;
      this.voClass = this.extendedVO;

      for (ExpressionsTag exp : this.expressions) {
        exp.gatherMetadataPhase1(selectTag, adapter, db, loc, selectGenerationTag, columnsPrefixGenerator, conn1);
      }

      for (CollectionTag c : this.collections) {
        c.gatherMetadataPhase1(selectTag, adapter, db, loc, selectGenerationTag, columnsPrefixGenerator, conn1);
      }

      for (AssociationTag a : this.associations) {
        a.gatherMetadataPhase1(selectTag, adapter, db, loc, selectGenerationTag, columnsPrefixGenerator, conn1);
      }

    }

  }

  @Override
  public void gatherMetadataPhase2(final Connection conn2)
      throws InvalidSQLException, UncontrolledException, UnresolvableDataTypeException {

    for (ExpressionsTag exp : this.expressions) {
      exp.gatherMetadataPhase2(conn2);
    }

    for (CollectionTag c : this.collections) {
      c.gatherMetadataPhase2(conn2);
    }

    for (AssociationTag a : this.associations) {
      a.gatherMetadataPhase2(conn2);
    }

  }

  // Utilities

  private String compileBody() {
    if (this.body == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    boolean endedWithComma = false;
    boolean first = true;
    for (String s : this.body) {
      String t = s.trim();
      if (!t.isEmpty()) {
        if (first) {
          first = false;
        } else {
          if (!t.startsWith(",") && !endedWithComma) {
            sb.append(", ");
          }
          sb.append(t);
        }
        endedWithComma = t.endsWith(",");
      }
    }
    return sb.toString();
  }

  // Getters

  public String getTable() {
    return table;
  }

  public String getView() {
    return view;
  }

  public String getProperty() {
    return property;
  }

  public String getAlias() {
    return alias;
  }

  public String getExtendedVOClass() {
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

}
