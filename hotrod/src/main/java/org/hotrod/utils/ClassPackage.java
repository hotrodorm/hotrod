package org.hotrod.utils;

import java.io.File;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.hotrod.exceptions.InvalidPackageException;

public class ClassPackage {

  private static final Logger log = Logger.getLogger(ClassPackage.class);

  private static final String CHUNK_PATTERN = "[a-zA-Z][a-zA-Z$_0-9]*";
  private static final String PACKAGE_PATTERN = CHUNK_PATTERN + "(\\." + CHUNK_PATTERN + ")*";

  private String pkg;
  private String[] names;

  private ClassPackage(final String pkg, final String[] names) {
    this.pkg = pkg;
    this.names = names;
  }

  public ClassPackage(final String pkg) throws InvalidPackageException {

    log.debug("init");

    if (pkg == null) {
      throw new InvalidPackageException("Package cannot be empty.");
    }

    this.pkg = pkg;
    this.names = pkg.split("\\.");

    if (!pkg.matches(PACKAGE_PATTERN)) {
      throw new InvalidPackageException(
          "Package '" + this.pkg + "' is not valid. " + "Must contain alphanumeric or underscore characters only.");
    }

    for (String name : this.names) {
      if (!name.matches(CHUNK_PATTERN)) {
        throw new InvalidPackageException("Package section '" + name + "' of the package '" + this.pkg
            + "' is not valid. " + "Must contain alphanumeric or underscore characters only.");
      }
    }
  }

  public File getPackageDir(final File baseDir) {
    File f = baseDir;
    for (String name : this.names) {
      f = new File(f, name);
    }
    return f;
  }

  public String getPackage() {
    return this.pkg;
  }

  public String getFullClassName(final String className) {
    return this.pkg + "." + className;
  }

  public ClassPackage append(final String name) throws InvalidPackageException {
    return new ClassPackage(this.pkg + "." + name);
  }

  public ClassPackage append(final ClassPackage p) {
    String[] allNames = AUtils.concat(this.names, p.names, new String[0]);
    return new ClassPackage(this.pkg + "." + p.pkg, allNames);
  }

  // toString

  public String toString() {
    return "{package:" + this.pkg + "}";
  }

  // Indexable

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(names);
    result = prime * result + ((pkg == null) ? 0 : pkg.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ClassPackage other = (ClassPackage) obj;
    if (!Arrays.equals(names, other.names))
      return false;
    if (pkg == null) {
      if (other.pkg != null)
        return false;
    } else if (!pkg.equals(other.pkg))
      return false;
    return true;
  }

}
