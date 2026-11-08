package org.hotrod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.LiveSQLFunctionTemplateParser.InvalidLiveSQLFunctionTemplateException;
import org.hotrod.config.LiveSQLFunctionTemplateParser.Section;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;

@XmlRootElement(name = "function")
public class LiveSQLFunctionTag extends AbstractConfigurationTag {

  private static final String PROPERTY_PATTERN = "[a-zA-Z][a-zA-Z0-9_]*+";

  private static final Logger log = Logger.getLogger(LiveSQLFunctionTag.class.getName());

  private String name;
  private String template;

  private List<Section> templateSections = new ArrayList<>();

  public LiveSQLFunctionTag() {
    super("function");
  }

  @XmlAttribute
  public void setName(final String name) {
    this.name = name;
  }

  @XmlAttribute
  public void setTemplate(final String template) {
    this.template = template;
  }

  public void validate(File basedir, final File parentDir, final DatabaseAdapter adapter, final CatalogSchema currentCS)
      throws InvalidConfigurationFileException {
    if (this.name == null || this.name.trim().length() == 0) {
      throw new InvalidConfigurationFileException(this, "The 'name' attribute of the <function> tag is required.");
    }
    if (!this.name.matches(PROPERTY_PATTERN)) {
      throw new InvalidConfigurationFileException(this,
          "The 'name' attribute of the <function> tag must start with a letter and continue with letters, digits, "
              + "and/or underscore symbols; however, '" + this.name + "' was specified.");
    }
    if (this.template == null || this.template.trim().length() == 0) {
      throw new InvalidConfigurationFileException(this, "The 'template' attribute of the <function> tag is required.");
    }
    try {
      LiveSQLFunctionTemplateParser parser = new LiveSQLFunctionTemplateParser();
      this.templateSections = parser.parse(this.template);
    } catch (InvalidLiveSQLFunctionTemplateException e) {
      throw new InvalidConfigurationFileException(this,
          "The 'template' attribute of the <function> tag is invalid: " + e.getMessage());
    }
  }

  public String getName() {
    return this.name;
  }

  public List<Section> getTemplateSections() {
    return this.templateSections;
  }

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.name;
  }

}
