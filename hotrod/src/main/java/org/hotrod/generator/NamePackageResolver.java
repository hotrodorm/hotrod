package org.hotrod.generator;

import org.hotrod.utils.ClassPackage;

public interface NamePackageResolver {

  String generateAbstractVOName(String name);

  ClassPackage getPrimitivesVOPackage(ClassPackage cp);

}
