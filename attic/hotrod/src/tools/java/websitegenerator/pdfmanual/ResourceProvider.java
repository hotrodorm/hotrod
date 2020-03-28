package websitegenerator.pdfmanual;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceProvider {

  private File baseDir;

  public ResourceProvider(final File baseDir) {
    this.baseDir = baseDir;
  }

  public InputStream getResourceAsStream(final String resourcePath) {
    try {
      return new FileInputStream(new File(this.baseDir, resourcePath));
    } catch (FileNotFoundException e) {
      return null;
    }
  }

}
