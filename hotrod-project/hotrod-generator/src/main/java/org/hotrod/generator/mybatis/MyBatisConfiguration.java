package org.hotrod.generator.mybatis;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.MyBatisTag;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.generator.GeneratableObject;
import org.hotrodorm.hotrod.utils.SUtil;

public class MyBatisConfiguration extends GeneratableObject {

  private static final Logger log = LogManager.getLogger(MyBatisConfiguration.class);

  private static final String MAPPERS_INSERTION_TOKEN = "<mappers/>";

  private HotRodConfigTag config;
  private List<String> sourceFiles;
  private List<String> mapperCustom;

  private MyBatisTag mybatisTag;

  private TextWriter w;

  public MyBatisConfiguration(final HotRodConfigTag config) {
    this.config = config;
    this.mybatisTag = (MyBatisTag) this.config.getGenerators().getSelectedGeneratorTag();
    this.config.getGenerators().addGeneratableObject(this);
    this.sourceFiles = new ArrayList<String>();
    this.mapperCustom = new ArrayList<String>();
  }

  public void addSourceFile(final String sourceFile) {
    this.sourceFiles.add(sourceFile);
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

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException, ControlledException {

    File templateFile = this.mybatisTag.getTemplate().getFile();
    if (!templateFile.exists()) {
      throw new ControlledException(
          "Could not generate the MyBatis main configuration file: " + "the template file '" + templateFile.getPath()
              + "' mentioned in the <mybatis-configuration-template> tag configuration file does not exist.");
    }
    String template;
    try {
      template = SUtil.loadFileAsString(templateFile);
    } catch (IOException e) {
      throw new ControlledException("Could not generate the MyBatis main configuration file: "
          + "could not read the template file '" + templateFile.getPath() + "': " + e.getMessage());
    }

    int count = SUtil.countMatches(template, MAPPERS_INSERTION_TOKEN);

    if (count == 0) {
      throw new ControlledException("Could not generate the MyBatis main configuration file: "
          + "didn't find the placeholder " + MAPPERS_INSERTION_TOKEN + " to insert the mappers in the template file '"
          + templateFile.getPath() + "'.");
    }
    if (count > 1) {
      throw new ControlledException("Could not generate the MyBatis main configuration file: "
          + "expected only one placeholder " + MAPPERS_INSERTION_TOKEN + " to insert the mappers in the template file '"
          + templateFile.getPath() + "', but found " + count + ".");
    }

    gatherCustomMappers();

    File mbconfig = new File(this.mybatisTag.getMappers().getPrimitivesDir(), "mybatis-configuration.xml");
    this.w = null;

    try {
      this.w = fileGenerator.createWriter(mbconfig);

      String mappers = prepareMappers();
      String result = template.replace(MAPPERS_INSERTION_TOKEN, mappers);
      this.w.write(result);

      super.markGenerated();

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
      sb.append("    <mapper resource=\"" + getFileURL(sourceFile) + "\" />\n");
    }

    for (String m : this.mapperCustom) {
      sb.append("    <mapper resource=\"" + getFileURL(this.mybatisTag.getMappers().getRelativeCustomDir() + "/" + m)
          + "\" />\n");
    }

    sb.append("  </mappers>\n");
    return sb.toString();
  }

  private String getFileURL(final String relativePath) {
    String adaptedURL = adaptURL(relativePath);
    log.debug("mapperURL=" + adaptedURL);
    return adaptedURL;
  }

  private String adaptURL(final String relativePath) {
    if (File.separatorChar == '\\') { // windows path?
      return relativePath.replace('\\', '/');
    }
    return relativePath;
  }

}