package org.hotrod.generator.mybatis;

import java.util.LinkedHashMap;

public class EntityDAORegistry {

  private LinkedHashMap<String, ObjectDAO> entityDAOs = new LinkedHashMap<String, ObjectDAO>();

  public void add(final String voFullClassName, final ObjectDAO dao) {
    this.entityDAOs.put(voFullClassName, dao);
  }

  public ObjectDAO findEntityDAO(final String voFullClassName) {
    return this.entityDAOs.get(voFullClassName);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("=== EntityDAORegistry (" + this.entityDAOs.size() + " entries) ===\n");
    for (String key : this.entityDAOs.keySet()) {
      sb.append(" * " + key + " / " + this.entityDAOs.get(key) + "\n");
    }
    return sb.toString();
  }

}
