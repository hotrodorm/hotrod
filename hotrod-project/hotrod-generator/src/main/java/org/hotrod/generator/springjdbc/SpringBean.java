package org.hotrod.generator.springjdbc;

import java.io.Writer;

import org.hotrod.exceptions.UncontrolledException;

public interface SpringBean {

  void writeBeanTag(Writer w) throws UncontrolledException;

  public String getBeanName();
  
  public String getBeanClassName();

}
