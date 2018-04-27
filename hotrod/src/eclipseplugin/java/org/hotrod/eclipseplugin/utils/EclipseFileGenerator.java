package org.hotrod.eclipseplugin.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.hotrod.eclipseplugin.RelativeProjectPath;
import org.hotrod.generator.FileGenerator;

public class EclipseFileGenerator implements FileGenerator {

  private static final Logger log = Logger.getLogger(EclipseFileGenerator.class);

  private IProject project;

  public EclipseFileGenerator(final IProject project) {
    this.project = project;
  }

  @Override
  public TextWriter createWriter(final File f) throws IOException {
    return new EclipseTextWriter(f);
  }

  public class EclipseTextWriter implements TextWriter {

    private Writer w;
    private IFile ifile;
    private ByteArrayOutputStream bos;

    private EclipseTextWriter(final File f) throws IOException {
      RelativeProjectPath rp = RelativeProjectPath.findRelativePath(project, f);
      log.debug("opening: " + f.getAbsolutePath());
      if (rp == null) {
        this.w = new BufferedWriter(new FileWriter(f));
        this.ifile = null;
      } else {
        this.w = null;
        this.ifile = project.getFile(rp.getRelativeFileName());
      }
      this.bos = new ByteArrayOutputStream();
    }

    @Override
    public void write(String txt) throws IOException {
      if (this.ifile != null) {
        this.bos.write(txt.getBytes());
      } else {
        this.w.write(txt);
      }
    }

    @Override
    public void close() throws IOException {
      log.debug("closing...");
      if (this.ifile != null) {
        try {
          ByteArrayInputStream bis = new ByteArrayInputStream(this.bos.toByteArray());
          if (this.ifile.exists()) {
            this.ifile.setContents(bis, true, false, null);
          } else {
            this.ifile.create(bis, true, null);
          }
        } catch (CoreException e) {
          throw new IOException(e);
        }
      } else {
        this.w.close();
      }
    }

  }

}
