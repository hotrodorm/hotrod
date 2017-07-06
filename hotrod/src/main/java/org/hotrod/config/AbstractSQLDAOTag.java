package org.hotrod.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;

import org.apache.log4j.Logger;
import org.hotrod.config.sql.AbstractSQLSection;
import org.hotrod.config.sql.SQLComplementSection;
import org.hotrod.config.sql.SQLFoundationSection;
import org.hotrod.config.sql.SQLParameter;
import org.hotrod.config.tags.ColumnTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.SUtils;

public abstract class AbstractSQLDAOTag extends AbstractDAOTag {

  // Constants

  private static final Logger log = Logger.getLogger(AbstractSQLDAOTag.class);

  public static final String DEFAULT_COMPLEMENT_START = "{*";
  public static final String DEFAULT_COMPLEMENT_END = "*}";

  public static final String COMPLEMENT_START_ATT = "complement-start";
  public static final String COMPLEMENT_END_ATT = "complement-end";

  // Properties

  protected String complementStart = null;
  protected String complementEnd = null;

  @XmlMixed
  @XmlElementRefs({ @XmlElementRef(type = ColumnTag.class) })
  private List<Object> content = new ArrayList<Object>();

  // Simple parsing of the content
  private List<ColumnTag> columns = new ArrayList<ColumnTag>();
  private String text = null;

  private boolean alreadyValidated = false;
  protected List<AbstractSQLSection> sections = new ArrayList<AbstractSQLSection>();

  protected String hashingName;

  // Constructor

  protected AbstractSQLDAOTag(final String tagName) {
    super(tagName);
  }

  // JAXB Setters

  @XmlAttribute(name = "complement-start")
  public void setComplementStart(final String complementStart) {
    this.complementStart = complementStart;
  }

  @XmlAttribute(name = "complement-end")
  public void setComplementEnd(final String complementEnd) {
    this.complementEnd = complementEnd;
  }

  // Validation

  public void validateCore(final String attName, final String name) throws InvalidConfigurationFileException {

    if (this.alreadyValidated) {

      return;

    } else {

      this.hashingName = name;

      // complement-start

      if (this.complementStart == null) {
        this.complementStart = DEFAULT_COMPLEMENT_START;
      } else {
        if (SUtils.isEmpty(this.complementStart)) {
          throw new InvalidConfigurationFileException("Attribute '" + COMPLEMENT_START_ATT + "' of tag <" + getTagName()
              + "> with " + attName + " '" + name + "' cannot be empty.");
        }
      }

      // complement-end

      if (this.complementEnd == null) {
        this.complementEnd = DEFAULT_COMPLEMENT_END;
      } else {
        if (SUtils.isEmpty(this.complementEnd)) {
          throw new InvalidConfigurationFileException("Attribute '" + COMPLEMENT_END_ATT + "' of tag <" + getTagName()
              + "> with " + attName + " '" + name + "' cannot be empty.");
        }
      }

      // content

      StringBuilder sb = new StringBuilder();
      for (Object obj : this.content) {
        try {
          String s = (String) obj;
          sb.append(s);
        } catch (ClassCastException e1) {
          try {
            ColumnTag col = (ColumnTag) obj;
            this.columns.add(col);
          } catch (ClassCastException e2) {
            throw new InvalidConfigurationFileException("The body of the tag <" + super.getTagName() + "> with "
                + attName + " '" + name + "' has an invalid tag (" + obj.getClass().getName() + ").");
          }
        }
      }
      this.text = sb.toString();

      if (SUtils.isEmpty(this.text)) {
        throw new InvalidConfigurationFileException(
            "The body of the tag <" + getTagName() + "> with " + attName + " '" + name + "' cannot be empty. "
                + "Please add a SQL statement in the body of the <" + getTagName() + "> tag.");
      }
      parseBody(this.text, attName, name);
    }

    this.alreadyValidated = true;

  }

  private void parseBody(final String body, final String attName, final String name)
      throws InvalidConfigurationFileException {

    int pos = 0;
    int prefix;
    int suffix;

    log.debug("");
    log.debug("body: " + body);
    log.debug(">> this.complementStart=" + this.complementStart);
    log.debug(">> this.complementEnd=" + this.complementEnd);

    while (pos < body.length() && (prefix = body.indexOf(this.complementStart, pos)) != -1) {
      log.debug("in 1: prefix=" + prefix);
      int paramStart = prefix + this.complementStart.length();
      suffix = body.indexOf(this.complementEnd, paramStart);
      if (suffix == -1) {
        throw new InvalidConfigurationFileException(
            "Invalid SQL statement as the body of the tag <" + getTagName() + "> with " + attName + " '" + name
                + "': unbalanced parameter delimiters. " + "A parameter section that starts with '"
                + this.complementStart + "' is not properly closed with '" + this.complementEnd + "'.");
      }
      log.debug("in 2");
      this.sections.add(new SQLFoundationSection(body.substring(pos, prefix), this, attName, name));
      this.sections.add(new SQLComplementSection(body.substring(paramStart, suffix), this, attName, name));
      pos = suffix + this.complementEnd.length();
    }
    log.debug("out // pos=" + pos);
    if (pos < body.length()) {
      this.sections.add(new SQLFoundationSection(body.substring(pos), this, attName, name));
    }
    log.debug("finished. sections = " + this.sections.size());

    Set<String> paramNames = new HashSet<String>();
    for (SQLParameter p : this.getParameterOccurrences()) {
      if (p.isDefinition()) {
        if (paramNames.contains(p.getName())) {
          throw new InvalidConfigurationFileException("The body of the tag <" + getTagName() + "> with " + attName
              + " '" + name + "' has multiple parameter definitions with the same name: " + p.getName() + ".\n"
              + "* If you want them to be different parameters, " + "please choose a different names for them;\n"
              + "* If you want to use the same parameter multiple times, "
              + "then the 'javaType' and/or 'jdbcType' can only be specified " + "on the first occurrence of it.");
        }
        paramNames.add(p.getName());
      } else {
        SQLParameter definition = findDefinition(p);
        if (definition != null) {
          p.setDefinition(definition);
        } else {
          throw new InvalidConfigurationFileException("The body of the tag <" + getTagName() + "> with " + attName
              + " '" + name + "' includes a parameter reference '" + p.getName()
              + "' but there's no parameter defined with that name yet.\n" + "The first time a parameter is specified, "
              + "it must be fully qualified with the 'javaType' and "
              + "'jdbcType' values (i.e. a parameter definition).");
        }
      }
    }

  }

  private SQLParameter findDefinition(final SQLParameter ref) {
    for (SQLParameter p : this.getParameterOccurrences()) {
      if (p.isDefinition() && ref.getName().equals(p.getName())) {
        return p;
      }
    }
    return null;
  }

  public String getAugmentedSQL() {
    StringBuilder sb = new StringBuilder();
    for (AbstractSQLSection s : this.sections) {
      sb.append(s.renderAugmentedSQL());
    }
    return sb.toString();
  }

  // Indexing methods

  @Override
  public int hashCode() {
    log.debug("********************* hashingName=" + hashingName);
    final int prime = 31;
    int result = 1;
    result = prime * result + ((hashingName == null) ? 0 : hashingName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AbstractSQLDAOTag other = (AbstractSQLDAOTag) obj;
    if (hashingName == null) {
      if (other.hashingName != null)
        return false;
    } else if (!hashingName.equals(other.hashingName))
      return false;
    return true;
  }

  // Getters

  public String unescapeXml(final String txt) {
    return txt.replaceAll("&amp;", "&") //
        .replaceAll("&lt;", "<") //
        .replaceAll("&gt;", ">") //
        .replaceAll("&apos;", "'") //
        .replaceAll("&quot;", "\"");
  }

  public List<SQLParameter> getParameterOccurrences() {
    List<SQLParameter> params = new ArrayList<SQLParameter>();
    for (AbstractSQLSection s : this.sections) {
      params.addAll(s.getParameterOccurrences());
    }
    return params;
  }

  public List<SQLParameter> getParameterDefinitions() {
    List<SQLParameter> params = new ArrayList<SQLParameter>();
    for (AbstractSQLSection s : this.sections) {
      params.addAll(s.getParameterDefinitions());
    }
    return params;
  }

}
