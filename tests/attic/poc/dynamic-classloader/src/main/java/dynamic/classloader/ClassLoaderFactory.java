package dynamic.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ClassLoaderFactory {

  public static ClassLoader newClassLoader(final List<File> classPath) throws InvalidClassPathException {
    return createClassLoader(classPath, ClassLoaderFactory.class.getClassLoader());
  }

  public static ClassLoader createClassLoader(final List<File> classPath, final ClassLoader parent)
      throws InvalidClassPathException {

    // 1. Assemble URLs

    List<URL> urlList = new ArrayList<URL>();

    for (File f : classPath) {
      if (f != null) {
        if (!f.exists()) {
          throw new InvalidClassPathException("Class path entry not found: " + f);
        }
        if (f.isDirectory()) {
          // Add directory entry
          try {
            URL url = f.toURI().toURL();
            urlList.add(url);
          } catch (MalformedURLException e) {
            throw new InvalidClassPathException("Invalid jar class path entry: " + f, e);
          }
        } else if (f.isFile()) {
          if (!f.getName().toLowerCase().endsWith(".jar")) {
            throw new InvalidClassPathException("Invalid class path file: must have .jar extension: " + f);
          }
          // Add jar entry
          try {
            urlList.add(new URL("jar", "", "file:" + f.getPath() + "!/"));
          } catch (MalformedURLException e) {
            throw new InvalidClassPathException("Invalid jar class path entry: " + f, e);
          }
        } else {
          throw new InvalidClassPathException("Invalid class path entry: must be a jar file or a directory: " + f);
        }
      }
    }

    // 2. Create the class loader

    URL[] urls = urlList.toArray(new URL[0]);
    URLClassLoader cl = URLClassLoader.newInstance(urls, parent);

    // 3. Return the new class loader

    return cl;

  }

  public static class InvalidClassPathException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidClassPathException(final String message, final Throwable cause) {
      super(message, cause);
    }

    public InvalidClassPathException(final String message) {
      super(message);
    }

  }

}
