package org.hotrod.generator;

import org.hotrod.utils.ClassPackage;

public interface NamePackageResolver {

  String generateAbstractVOName(String name);

  String generateVOName(String name);

  ClassPackage getPrimitivesVOPackage(ClassPackage cp);

}
