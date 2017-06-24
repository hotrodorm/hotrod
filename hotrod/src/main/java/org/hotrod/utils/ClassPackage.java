package org.hotrod.utils;

import java.io.File;

import org.hotrod.exceptions.InvalidPackageException;

public class ClassPackage {

  private static final String PACKAGE_CHUNK_PATTERN = "[a-zA-Z][a-zA-Z$_0-9]*";

  private String pkg;
  private String[] names;

  private ClassPackage(final String pkg, final String[] names) {
    this.pkg = pkg;
    this.names = names;
  }

  public ClassPackage(final String pkg) throws InvalidPackageException {
    if (pkg == null) {
      throw new InvalidPackageException("Package cannot be empty.");
    }
    this.pkg = pkg;
    this.names = pkg.split("\\.");
    for (String name : this.names) {
      if (!name.matches(PACKAGE_CHUNK_PATTERN)) {
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

  public String toString() {
    return "{package:" + this.pkg + "}";
  }

}
