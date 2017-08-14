package org.hotrod.generator.mybatis;

import java.util.LinkedHashSet;
import java.util.Set;

public class ImportedClasses {

  private Set<String> set = new LinkedHashSet<String>();

  public void add(final String c) {
    this.set.add(c);
  }

  public Set<String> getClasses() {
    return set;
  }

}
