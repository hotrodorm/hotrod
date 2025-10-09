package org.hotrod.utils;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.hotrod.exceptions.InvalidPackageException;

public class ClassPackage {

  private static final Logger log = Logger.getLogger(ClassPackage.class.getName());

  private String pkg;
  private String[] names;

  private ClassPackage(final String[] names) {
    this.names = names;
    this.pkg = Arrays.stream(names).collect(Collectors.joining("."));
  }

  public static ClassPackage parse(final String pkg) throws InvalidPackageException {

    log.fine("init");

    if (pkg == null) {
      throw new InvalidPackageException("Package cannot be empty.");
    }

// package Identifier {. Identifier} ;
//    
//  Identifier:
//    IdentifierChars but not a Keyword or BooleanLiteral or NullLiteral
//
//  IdentifierChars:
//    JavaLetter {JavaLetterOrDigit}
//
//  JavaLetter:
//    any Unicode character that is a "Java letter": Character.isJavaIdentifierStart(int) returns true
//
//  JavaLetterOrDigit:
//    any Unicode character that is a "Java letter-or-digit": Character.isJavaIdentifierPart(int) returns true.    

    String[] names;
    if (pkg.isEmpty()) {
      names = new String[0];
    } else {
      names = pkg.split("\\.");
    }
    for (String name : names) {
      if (!isIdentifier(name)) {
        throw new InvalidPackageException("Invalid identifier '" + name + "' in the java package '" + pkg
            + "'. A java package must be a sequence of identifiers separated by periods; "
            + "each identifier starts with a java letter and continues with java letters or digits. "
            + "See https://docs.oracle.com/javase/specs/jls/se16/html/jls-7.html#jls-7.4 for details.");
      }
    }
    return new ClassPackage(names);
  }

  private static boolean isIdentifier(String name) {
//    log.info("name=" + name);
    if (name == null)
      return false;
    if (name.isEmpty())
      return false;
    for (int i = 0; i < name.length(); i++) {
      int c = name.charAt(i);
//      log.info("* i = " + i + " - c: " + c);
      if (i == 0) {
        if (!Character.isJavaIdentifierStart(c)) {
          return false;
        }
      } else {
//        log.info("Character.isJavaIdentifierPart(" + ((char) c) + ")=" + Character.isJavaIdentifierPart(c));
        if (!Character.isJavaIdentifierPart(c)) {
          return false;
        }
      }
    }
    return true;
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

  public String getFullClassName(final String baseClassName) {
    return this.pkg + "." + baseClassName;
  }

  public ClassPackage append(final ClassPackage p) {
    String[] allNames = AUtils.concat(this.names, p.names, new String[0]);
    return new ClassPackage(allNames);
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
