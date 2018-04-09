package org.hotrod.generator;

public abstract class GeneretableObject {

  private boolean generated = false;

  public void markGenerated() {
    this.generated = true;
  }

  public boolean isGenerated() {
    return this.generated;
  }

}
