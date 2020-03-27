package org.hotrod.utils;

import java.util.HashSet;
import java.util.Set;

public class ImportsRenderer {

  private Set<String> imports = new HashSet<String>();
  private StringBuilder sb = new StringBuilder();

  public void add(final String imp) {
    if (!this.imports.contains(imp)) {
      this.imports.add(imp);
      this.sb.append("import " + imp + ";\n");
    }
  }

  public void add(final Class<?> c) {
    add(c.getName());
  }

  public void newLine() {
    this.sb.append("\n");
  }

  public void comment(final String comment) {
    this.sb.append("// " + comment + "\n");
  }

  public String render() {
    return sb.toString();
  }

}
