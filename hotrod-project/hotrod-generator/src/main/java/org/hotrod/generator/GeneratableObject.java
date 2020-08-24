package org.hotrod.generator;

import org.hotrod.generator.mybatisspring.Bundle;

public abstract class GeneratableObject {

  private boolean generated = false;

  private Bundle bundle;

  public void markGenerated() {
    this.generated = true;
  }

  public boolean isGenerated() {
    return this.generated;
  }

  public final void setBundle(final Bundle bundle) {
    this.bundle = bundle;
  }

  public final Bundle getBundle() {
    return this.bundle;
  }

}
