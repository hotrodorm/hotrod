package org.hotrod.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hotrod.config.AbstractDAOTag;

public class DAONamespace {

  private List<AbstractDAOTag> registeredDAOs = new ArrayList<AbstractDAOTag>();

  public void registerDAOTag(final AbstractDAOTag t, final String type, final String name)
      throws DuplicateDAOClassException, DuplicateDAOClassMethodException {
    for (AbstractDAOTag r : this.registeredDAOs) {
      if (r.getJavaClassName().equals(t.getJavaClassName())) {
        throw new DuplicateDAOClassException(type, name, r.getJavaClassName());
      }
      Set<String> methods = new HashSet<String>();
      for (String m : t.getDeclaredMethodNames()) {
        if (methods.contains(m)) {
          throw new DuplicateDAOClassMethodException(type, name, r.getJavaClassName(), m);
        }
        methods.add(m);
      }
    }
    this.registeredDAOs.add(t);
  }

  // Exception class

  public static class DuplicateDAOClassException extends Exception {

    private static final long serialVersionUID = 1L;

    private String type;
    private String name;
    private String className;

    public DuplicateDAOClassException(final String type, final String name, final String className) {
      super();
      this.type = type;
      this.name = name;
      this.className = className;
    }

    public String getType() {
      return type;
    }

    public String getName() {
      return name;
    }

    public String getClassName() {
      return className;
    }

  }

  public static class DuplicateDAOClassMethodException extends Exception {

    private static final long serialVersionUID = 1L;

    private String type;
    private String name;
    private String className;
    private String methodName;

    public DuplicateDAOClassMethodException(final String type, final String name, final String className,
        final String methodName) {
      super();
      this.type = type;
      this.name = name;
      this.className = className;
      this.methodName = methodName;
    }

    public String getType() {
      return type;
    }

    public String getName() {
      return name;
    }

    public String getClassName() {
      return className;
    }

    public String getMethodName() {
      return methodName;
    }

  }

}
