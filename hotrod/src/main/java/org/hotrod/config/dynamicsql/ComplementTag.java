package org.hotrod.config.dynamicsql;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import org.hotrod.config.SQLParameter;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.generator.ParameterRenderer;

@XmlRootElement(name = "complement")
public class ComplementTag extends DynamicSQLPart {

  // Constructor

  public ComplementTag() {
    super("complement");
  }

  // Behavior

  @Override
  public void validate(final String tagIdentification) throws InvalidConfigurationFileException {
    
    super.validateParts(tagIdentification);
    
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

  @Override
  public List<SQLParameter> getParameters() {
    return null;
  }

  // Rendering

  @Override
  public String renderSQLSentence(final ParameterRenderer parameterRenderer) {
    return super.renderTag(parameterRenderer);
  }

}