package org.hotrod.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.GeneratorNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.Compare;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@XmlRootElement(name = "hotrod")
public class HotRodConfigTag extends AbstractHotRodConfigTag {

  // Constants

  private static final Logger log = Logger.getLogger(HotRodConfigTag.class);

  // Properties

  private GeneratorsTag generatorsTag = null;

  private List<ConverterTag> converters = new ArrayList<ConverterTag>();
  private Map<String, ConverterTag> convertersByName = null;

  // Constructors

  public HotRodConfigTag() {
    super("hotrod");
    log.debug("init");
  }

  // JAXB Setters

  @XmlElement
  public void setGenerators(final GeneratorsTag generators) {
    this.generatorsTag = generators;
  }

  @XmlElement
  public void setConverter(final ConverterTag converter) {
    this.converters.add(converter);
  }

  // Getters

  public GeneratorsTag getGenerators() {
    return generatorsTag;
  }

  public List<ConverterTag> getConverters() {
    return this.converters;
  }

  public ConverterTag getConverterTagByName(final String name) {
    return this.convertersByName.get(name);
  }

  public void validate(final File basedir, final File parentDir, final String generatorName)
      throws InvalidConfigurationFileException, GeneratorNotFoundException {

    // Generators

    this.generatorsTag.validate(basedir, parentDir, generatorName);
    super.subTags.add(this.generatorsTag);

    // Converters

    this.convertersByName = new HashMap<String, ConverterTag>();

    for (ConverterTag c : this.converters) {
      c.validate();
      super.subTags.add(c);
      if (this.convertersByName.containsKey(c.getName())) {
        throw new InvalidConfigurationFileException(super.getSourceLocation(),
            "Duplicate converter name. There are multiple <converter> tags with the same name: '" + c.getName() + "'.");
      }
      this.convertersByName.put(c.getName(), c);
    }

  }

  // Entity Resolver

  public static class HotRodInternalEntityResolver implements EntityResolver {

    private static final Logger log = Logger.getLogger(HotRodInternalEntityResolver.class);

    private static final String DTD = "/hotrod.dtd";

    @Override
    public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
      log.debug("*** public: " + publicId + " system: " + systemId);
      InputStream is = getClass().getResourceAsStream(DTD);
      log.debug("*** is=" + is);
      if (is == null) {
        throw new IOException("Could not find DTD definition file/resource: " + DTD);
      }
      return new InputSource(is);
    }

  }

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      HotRodConfigTag f = (HotRodConfigTag) fresh;
      boolean different = !same(fresh);

      this.generatorsTag = f.generatorsTag;
      this.converters = f.converters;
      this.convertersByName = f.convertersByName;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    try {
      HotRodConfigTag f = (HotRodConfigTag) fresh;
      return //
      Compare.same(this.generatorsTag, f.generatorsTag) && //
          Compare.same(this.converters, f.converters) && //
          Compare.same(this.convertersByName, f.convertersByName) //
      ;
    } catch (ClassCastException e) {
      return false;
    }
  }

}
