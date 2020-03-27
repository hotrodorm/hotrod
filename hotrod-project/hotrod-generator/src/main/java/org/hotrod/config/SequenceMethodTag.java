package org.hotrod.config;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.utils.Compare;
import org.hotrod.utils.identifiers.Id;
import org.hotrod.utils.identifiers.ObjectId;
import org.hotrodorm.hotrod.utils.SUtil;

@XmlRootElement(name = "sequence")
public class SequenceMethodTag extends AbstractMethodTag<SequenceMethodTag> {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(SequenceMethodTag.class);

  static final String TAG_NAME = "sequence";

  // Properties

  private String name = null;
  private String catalog = null;
  private String schema = null;

  private ObjectId sequenceId = null;

  // Constructor

  public SequenceMethodTag() {
    super("sequence");
    log.debug("init");
  }

  // Duplicate

  @Override
  public SequenceMethodTag duplicate() {
    SequenceMethodTag d = new SequenceMethodTag();

    d.copyCommon(this);

    d.name = this.name;

    return d;
  }

  // JAXB Setters

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute
  public void setCatalog(final String catalog) {
    this.catalog = catalog;
  }

  @XmlAttribute
  public void setSchema(final String schema) {
    this.schema = schema;
  }

  @XmlAttribute(name = "method")
  public void setMethod(final String method) {
    this.method = method;
  }

  // Behavior

  public void validate(final DaosTag daosTag, final HotRodConfigTag config,
      final HotRodFragmentConfigTag fragmentConfig, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    log.debug(">>> VALIDATING SEQUENCE.");

    super.validate(daosTag, config, fragmentConfig);

    // name

    if (SUtil.isEmpty(this.name)) {
      throw new InvalidConfigurationFileException(this, //
          "Attribute 'name' cannot be empty", //
          "Attribute 'name' of tag <" + TAG_NAME + "> cannot be empty. " + "You must specify a sequence name.");
    }

    // catalog

    Id catalogId;
    try {
      catalogId = this.catalog == null ? null : Id.fromTypedSQL(this.catalog, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid catalog name '" + this.catalog + "' on tag <" + super.getTagName() + "> for the table '"
          + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg, msg);
    }

    // schema

    Id schemaId;
    try {
      schemaId = this.schema == null ? null : Id.fromTypedSQL(this.schema, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid schema name '" + this.schema + "' on tag <" + super.getTagName() + "> for the table '"
          + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg, msg);
    }

    // Assemble object id

    Id nameId;
    try {
      nameId = Id.fromTypedSQL(this.name, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid table name '" + this.name + "': " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg, msg);
    }

    try {
      this.sequenceId = new ObjectId(catalogId, schemaId, nameId, adapter);
    } catch (InvalidIdentifierException e) {
      String msg = "Invalid table object name: " + e.getMessage();
      throw new InvalidConfigurationFileException(this, msg, msg);
    }

  }

  // Getters

  public String getName() {
    return this.name;
  }

  public String getMethod() {
    return super.method;
  }

  public ObjectId getSequenceId() {
    return sequenceId;
  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    try {
      SequenceMethodTag f = (SequenceMethodTag) fresh;
      return this.sequenceId.equals(f.getSequenceId());
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      SequenceMethodTag f = (SequenceMethodTag) fresh;
      boolean different = !same(fresh);

      this.method = f.method;
      this.catalog = f.catalog;
      this.schema = f.schema;
      this.sequenceId = f.sequenceId;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      SequenceMethodTag f = (SequenceMethodTag) fresh;
      return //
      this.sequenceId.equals(f.getSequenceId()) && //
          Compare.same(this.method, f.method);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}
