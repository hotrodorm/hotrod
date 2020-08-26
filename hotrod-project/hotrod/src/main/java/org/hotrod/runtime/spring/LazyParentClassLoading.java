package org.hotrod.runtime.spring;

public interface LazyParentClassLoading {

  // superclass already loaded
  boolean isLoaded();

  void loadSuperclass();

  void unloadSuperclass();

}
