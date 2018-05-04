package org.hotrod.eclipseplugin.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.hotrod.eclipseplugin.RelativeProjectPath;
import org.hotrod.generator.FileGenerator;

public class XEclipseFileGenerator implements FileGenerator {

  private static final Logger log = Logger.getLogger(XEclipseFileGenerator.class);

  private IProject project;

  public XEclipseFileGenerator(final IProject project) {
    this.project = project;
  }

  @Override
  public TextWriter createWriter(final File f) throws IOException {
    return new EclipseTextWriter(f);
  }

  public class EclipseTextWriter implements TextWriter {

    private Writer w;

    private EclipseTextWriter(final File f) throws IOException {
      RelativeProjectPath rp = RelativeProjectPath.findRelativePath(project, f);
      if (rp == null) {
        this.w = new BufferedWriter(new FileWriter(f));
      } else {
        File wf = project.getLocation().append(rp.getRelativeFileName()).toFile();
        log.info("wf=" + wf);
        this.w = new BufferedWriter(new FileWriter(wf));
      }
    }

    @Override
    public void write(String txt) throws IOException {
      this.w.write(txt);
    }

    @Override
    public void close() throws IOException {
      log.debug("closing...");
      this.w.close();
    }

  }

}
