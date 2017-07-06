package org.hotrod.generator.mybatis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.tags.HotRodConfigTag;
import org.hotrod.config.tags.MyBatisTag;
import org.hotrod.utils.SUtils;

public class MyBatisConfiguration {

  private static final String MAPPERS_INSERTION = "<mappers/>";

  private HotRodConfigTag config;
  private List<String> sourceFiles;
  private List<CustomDAOMapper> collectionMappers;
  private List<String> mapperCustom;

  private MyBatisTag mybatisTag;

  private Writer w;

  public MyBatisConfiguration(final HotRodConfigTag config) {
    this.config = config;
    this.mybatisTag = (MyBatisTag) this.config.getGenerators().getSelectedGeneratorTag();
    this.sourceFiles = new ArrayList<String>();
    this.collectionMappers = new ArrayList<CustomDAOMapper>();
    this.mapperCustom = new ArrayList<String>();
  }

  public void addSourceFile(final String sourceFile) {
    this.sourceFiles.add(sourceFile);
  }

  public void addMapperPrimitives(final CustomDAOMapper mapper) {
    this.collectionMappers.add(mapper);
  }

  private void gatherCustomMappers() {
    String[] found = this.mybatisTag.getMappers().getCustomDir().list(new FilenameFilter() {
      public boolean accept(final File dir, final String name) {
        return name != null && name.endsWith(".xml");
      }
    });
    if (found != null) {
      for (String f : found) {
        this.mapperCustom.add(f);
      }
    }
  }

  public void generate() throws UncontrolledException, ControlledException {

    File templateFile = this.mybatisTag.getTemplate().getFile();
    if (!templateFile.exists()) {
      throw new ControlledException(
          "Could not generate the MyBatis main configuration file: " + "the template file '" + templateFile.getPath()
              + "' mentioned in the <mybatis-configuration-template> tag configuration file does not exist.");
    }
    String template;
    try {
      template = SUtils.loadFileAsString(templateFile);
    } catch (IOException e) {
      throw new ControlledException("Could not generate the MyBatis main configuration file: "
          + "could not read the template file '" + templateFile.getPath() + "': " + e.getMessage());
    }

    int count = SUtils.countMatches(template, MAPPERS_INSERTION);

    if (count == 0) {
      throw new ControlledException(
          "Could not generate the MyBatis main configuration file: " + "didn't find the placeholder "
              + MAPPERS_INSERTION + " to insert the mappers in the template file '" + templateFile.getPath() + "'.");
    }
    if (count > 1) {
      throw new ControlledException("Could not generate the MyBatis main configuration file: "
          + "expected only one placeholder " + MAPPERS_INSERTION + " to insert the mappers in the template file '"
          + templateFile.getPath() + "', but found " + count + ".");
    }

    gatherCustomMappers();

    File mbconfig = new File(this.mybatisTag.getMappers().getPrimitivesDir(), "mybatis-configuration.xml");
    this.w = null;

    try {
      this.w = new BufferedWriter(new FileWriter(mbconfig));

      String mappers = prepareMappers();
      String result = template.replace(MAPPERS_INSERTION, mappers);
      this.w.write(result);

    } catch (IOException e) {
      throw new UncontrolledException("Could not generate the MyBatis main configuration file: "
          + "could not write to file '" + mbconfig.getName() + "'.", e);
    } finally {
      if (this.w != null) {
        try {
          this.w.close();
        } catch (IOException e) {
          throw new UncontrolledException("Could not generate the MyBatis main configuration file: "
              + "could not close the file '" + mbconfig.getName() + "'.", e);
        }
      }
    }

  }

  private String prepareMappers() {
    StringBuilder sb = new StringBuilder();
    sb.append("  <mappers>\n");

    for (String sourceFile : this.sourceFiles) {
      sb.append("    <mapper resource=\"" + sourceFile + "\" />\n");
    }

//    for (CustomDAOMapper m : this.collectionMappers) {
//      sb.append("    <mapper resource=\"" + this.mybatisTag.getMappers().getRelativePrimitivesDir() + "/"
//          + m.getSourceFileName() + "\" />\n");
//    }

    for (String m : this.mapperCustom) {
      sb.append("    <mapper resource=\"" + this.mybatisTag.getMappers().getRelativeCustomDir() + "/" + m + "\" />\n");
    }

    sb.append("  </mappers>\n");
    return sb.toString();
  }

}
