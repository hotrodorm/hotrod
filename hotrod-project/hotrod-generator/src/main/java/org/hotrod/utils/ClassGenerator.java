package org.hotrod.utils;

import java.io.File;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassGenerator {

  private File basedir;
  private ClassPackage classPackage;
  private String name;

  private Writer w;

  private Set<String> imports;

  public ClassGenerator(final File basedir, final ClassPackage classPackage, final String name) {
    this.basedir = basedir;
    this.classPackage = classPackage;
    this.name = name;

    this.imports = new HashSet<String>();
  }

  public void registerClass(final String className) {
    this.imports.add(className);
  }

  public void registerClass(final Class<?> c) {
    this.imports.add(c.getName());
  }

  private String renderImports() {
    return this.imports.stream().map(i -> "import " + i + ";").collect(Collectors.joining("\n"));
  }
  
  

}
