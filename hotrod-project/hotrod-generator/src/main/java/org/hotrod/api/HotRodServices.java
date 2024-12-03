package org.hotrod.api;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hotrod.config.Constants;
import org.hotrod.config.DisplayMode;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.Generator;
import org.hotrod.generator.HotRodContext;
import org.hotrod.generator.LiveGenerator;
import org.hotrod.runtime.BuildInformation;
import org.hotrod.utils.LocalFileGenerator;
import org.nocrala.tools.database.tartarus.utils.XUtil;

public class HotRodServices {

  private static final Logger log = Logger.getLogger(HotRodServices.class.getName());

  private File baseDir;
  private String jdbcdriverclass;
  private String jdbcurl;
  private String jdbcusername;
  private String jdbcpassword;
  private String jdbccatalog;
  private String jdbcschema;
  private File configFile;
  private DisplayMode displayMode;
  private LinkedHashSet<String> facetNames;

  public HotRodServices(File baseDir, String jdbcdriverclass, String jdbcurl, String jdbcusername, String jdbcpassword,
      String jdbccatalog, String jdbcschema, File configFile, DisplayMode displayMode,
      LinkedHashSet<String> facetNames) {
    super();
    this.baseDir = baseDir;
    this.jdbcdriverclass = jdbcdriverclass;
    this.jdbcurl = jdbcurl;
    this.jdbcusername = jdbcusername;
    this.jdbcpassword = jdbcpassword;
    this.jdbccatalog = jdbccatalog;
    this.jdbcschema = jdbcschema;
    this.configFile = configFile;
    this.displayMode = displayMode;
    this.facetNames = facetNames;
  }

  public void generate(final Feedback feedback) throws Exception {
    log.fine("init");

    feedback.info(Constants.TOOL_NAME + " Generator version " + BuildInformation.VERSION + " (build " + BuildInformation.BUILD_ID
        + ") - Generate");

    try {

      HotRodContext hc = new HotRodContext(configFile, jdbcdriverclass, jdbcurl, jdbcusername, jdbcpassword,
          jdbccatalog, jdbcschema, baseDir, facetNames, feedback);

      // Generate

      Generator g = hc.getConfig().getGenerators().getSelectedGeneratorTag().instantiateGenerator(hc, null,
          this.displayMode, false, feedback);
      log.fine("Generator instantiated.");

      try {

        LiveGenerator liveGenerator = (LiveGenerator) g;

        // a live generator
        log.fine("live generator");

        g.prepareGeneration();
        FileGenerator fg = new LocalFileGenerator();
        liveGenerator.generate(fg);

      } catch (ClassCastException e) {

        // a batch generator
        log.fine("batch generator");

        g.prepareGeneration();
        g.generate();
      }

      log.fine("Generation complete.");

    } catch (ControlledException e) {
      if (e.getLocation() == null) {
        throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code:\n" + e.getMessage());
      } else {
        throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code. Invalid configuration in "
            + e.getLocation().render() + ":\n" + e.getMessage());
      }
    } catch (UncontrolledException e) {
      feedback.error("Technical error found: " + XUtil.abridge(e));
      throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code.");
    } catch (InvalidConfigurationFileException e) {
      throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code. Invalid configuration in "
          + e.getTag().getSourceLocation().render() + ":\n" + e.getMessage());
    } catch (Throwable e) {
      log.log(Level.SEVERE, "Could not generate persistence layer.", e);
      throw new Exception(Constants.TOOL_NAME + " could not generate the persistence code.", e);
    }

  }

}
