package org.hotrod.generator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.metadata.SelectMethodMetadata;
import org.nocrala.tools.lang.collector.listcollector.ListWriter;

public class SelectMetadataCache implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger log = LogManager.getLogger(SelectMetadataCache.class);

  // dao-name,
  // method-name,
  // metadata
  private Map<String, Map<String, SelectMethodMetadata>> cache = new HashMap<String, Map<String, SelectMethodMetadata>>();

  public void put(final String dao, final String method, final SelectMethodMetadata metaData) {
    log.debug("CACHE PUT: " + dao + "." + method + "()");
    Map<String, SelectMethodMetadata> daoCache = this.cache.get(dao);
    if (daoCache == null) {
      daoCache = new HashMap<String, SelectMethodMetadata>();
      this.cache.put(dao, daoCache);
    }
    daoCache.put(method, metaData);
  }

  public SelectMethodMetadata get(final String dao, final String method) {
    log.debug("CACHE GET: " + dao + "." + method + "()");
    Map<String, SelectMethodMetadata> daoCache = this.cache.get(dao);
    if (daoCache == null) {
      return null;
    }
    return daoCache.get(method);
  }

  public int size() {
    return this.cache.size();
  }

  public String listNames() {
    ListWriter w = new ListWriter(", ");
    for (String name : this.cache.keySet()) {
      w.add(name);
    }
    return w.toString();
  }

}
