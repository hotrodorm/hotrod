package org.hotrod.utils;

public class ClassWriter extends AbstractClassWriter {

  public ClassWriter(ClassPackage classPackage, String... headerLines) {
    super(classPackage, headerLines);
  }

  // Using 1 parameter(s)

  public void print(String s0) {
    this.segments.add(s0);
  }

  public void println(String s0) {
    this.segments.add(s0);
    this.println();
  }

  public void print(Class<?> c0) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
  }

  public void println(Class<?> c0) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.println();
  }

  public void print(ExternalClass e0) {
    this.segments.add(registerClass(e0));
  }

  public void println(ExternalClass e0) {
    this.segments.add(registerClass(e0));
    this.println();
  }

  // Using 2 parameter(s)

  public void print(String s0, String s1) {
    this.segments.add(s0);
    this.segments.add(s1);
  }

  public void println(String s0, String s1) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.println();
  }

  public void print(String s0, Class<?> c1) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
  }

  public void println(String s0, Class<?> c1) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.println();
  }

  public void print(String s0, ExternalClass e1) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
  }

  public void println(String s0, ExternalClass e1) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.println();
  }

  public void print(Class<?> c0, String s1) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
  }

  public void println(Class<?> c0, String s1) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
  }

  public void println(Class<?> c0, Class<?> c1) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
  }

  public void println(Class<?> c0, ExternalClass e1) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.println();
  }

  public void print(ExternalClass e0, String s1) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
  }

  public void println(ExternalClass e0, String s1) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
  }

  public void println(ExternalClass e0, Class<?> c1) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
  }

  public void println(ExternalClass e0, ExternalClass e1) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.println();
  }

  // Using 3 parameter(s)

  public void print(String s0, String s1, String s2) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
  }

  public void println(String s0, String s1, String s2) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.println();
  }

  public void print(String s0, String s1, Class<?> c2) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
  }

  public void println(String s0, String s1, Class<?> c2) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.println();
  }

  public void print(String s0, String s1, ExternalClass e2) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
  }

  public void println(String s0, String s1, ExternalClass e2) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.println();
  }

  public void print(String s0, Class<?> c1, String s2) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
  }

  public void println(String s0, Class<?> c1, String s2) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.println();
  }

  public void print(String s0, Class<?> c1, Class<?> c2) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
  }

  public void println(String s0, Class<?> c1, Class<?> c2) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.println();
  }

  public void print(String s0, Class<?> c1, ExternalClass e2) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
  }

  public void println(String s0, Class<?> c1, ExternalClass e2) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.println();
  }

  public void print(String s0, ExternalClass e1, String s2) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
  }

  public void println(String s0, ExternalClass e1, String s2) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.println();
  }

  public void print(String s0, ExternalClass e1, Class<?> c2) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
  }

  public void println(String s0, ExternalClass e1, Class<?> c2) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.println();
  }

  public void print(String s0, ExternalClass e1, ExternalClass e2) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
  }

  public void println(String s0, ExternalClass e1, ExternalClass e2) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.println();
  }

  public void print(Class<?> c0, String s1, String s2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
  }

  public void println(Class<?> c0, String s1, String s2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.println();
  }

  public void print(Class<?> c0, String s1, Class<?> c2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
  }

  public void println(Class<?> c0, String s1, Class<?> c2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.println();
  }

  public void print(Class<?> c0, String s1, ExternalClass e2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
  }

  public void println(Class<?> c0, String s1, ExternalClass e2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, String s2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
  }

  public void println(Class<?> c0, Class<?> c1, String s2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, Class<?> c2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
  }

  public void println(Class<?> c0, Class<?> c1, Class<?> c2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, ExternalClass e2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
  }

  public void println(Class<?> c0, Class<?> c1, ExternalClass e2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, String s2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
  }

  public void println(Class<?> c0, ExternalClass e1, String s2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, Class<?> c2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
  }

  public void println(Class<?> c0, ExternalClass e1, Class<?> c2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, ExternalClass e2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
  }

  public void println(Class<?> c0, ExternalClass e1, ExternalClass e2) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.println();
  }

  public void print(ExternalClass e0, String s1, String s2) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
  }

  public void println(ExternalClass e0, String s1, String s2) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.println();
  }

  public void print(ExternalClass e0, String s1, Class<?> c2) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
  }

  public void println(ExternalClass e0, String s1, Class<?> c2) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.println();
  }

  public void print(ExternalClass e0, String s1, ExternalClass e2) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
  }

  public void println(ExternalClass e0, String s1, ExternalClass e2) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, String s2) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
  }

  public void println(ExternalClass e0, Class<?> c1, String s2) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, Class<?> c2) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
  }

  public void println(ExternalClass e0, Class<?> c1, Class<?> c2) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, ExternalClass e2) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
  }

  public void println(ExternalClass e0, Class<?> c1, ExternalClass e2) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, String s2) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
  }

  public void println(ExternalClass e0, ExternalClass e1, String s2) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, Class<?> c2) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
  }

  public void println(ExternalClass e0, ExternalClass e1, Class<?> c2) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, ExternalClass e2) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
  }

  public void println(ExternalClass e0, ExternalClass e1, ExternalClass e2) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.println();
  }

  // Using 4 parameter(s)

  public void print(String s0, String s1, String s2, String s3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
  }

  public void println(String s0, String s1, String s2, String s3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.println();
  }

  public void print(String s0, String s1, String s2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(String s0, String s1, String s2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(String s0, String s1, String s2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
  }

  public void println(String s0, String s1, String s2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(String s0, String s1, Class<?> c2, String s3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
  }

  public void println(String s0, String s1, Class<?> c2, String s3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.println();
  }

  public void print(String s0, String s1, Class<?> c2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(String s0, String s1, Class<?> c2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(String s0, String s1, Class<?> c2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
  }

  public void println(String s0, String s1, Class<?> c2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(String s0, String s1, ExternalClass e2, String s3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
  }

  public void println(String s0, String s1, ExternalClass e2, String s3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.println();
  }

  public void print(String s0, String s1, ExternalClass e2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(String s0, String s1, ExternalClass e2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(String s0, String s1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
  }

  public void println(String s0, String s1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(String s0, Class<?> c1, String s2, String s3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
  }

  public void println(String s0, Class<?> c1, String s2, String s3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.println();
  }

  public void print(String s0, Class<?> c1, String s2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(String s0, Class<?> c1, String s2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(String s0, Class<?> c1, String s2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
  }

  public void println(String s0, Class<?> c1, String s2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(String s0, Class<?> c1, Class<?> c2, String s3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
  }

  public void println(String s0, Class<?> c1, Class<?> c2, String s3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.println();
  }

  public void print(String s0, Class<?> c1, Class<?> c2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(String s0, Class<?> c1, Class<?> c2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(String s0, Class<?> c1, Class<?> c2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
  }

  public void println(String s0, Class<?> c1, Class<?> c2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(String s0, Class<?> c1, ExternalClass e2, String s3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
  }

  public void println(String s0, Class<?> c1, ExternalClass e2, String s3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.println();
  }

  public void print(String s0, Class<?> c1, ExternalClass e2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(String s0, Class<?> c1, ExternalClass e2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(String s0, Class<?> c1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
  }

  public void println(String s0, Class<?> c1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(String s0, ExternalClass e1, String s2, String s3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
  }

  public void println(String s0, ExternalClass e1, String s2, String s3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.println();
  }

  public void print(String s0, ExternalClass e1, String s2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(String s0, ExternalClass e1, String s2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(String s0, ExternalClass e1, String s2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
  }

  public void println(String s0, ExternalClass e1, String s2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(String s0, ExternalClass e1, Class<?> c2, String s3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
  }

  public void println(String s0, ExternalClass e1, Class<?> c2, String s3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.println();
  }

  public void print(String s0, ExternalClass e1, Class<?> c2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(String s0, ExternalClass e1, Class<?> c2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(String s0, ExternalClass e1, Class<?> c2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
  }

  public void println(String s0, ExternalClass e1, Class<?> c2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(String s0, ExternalClass e1, ExternalClass e2, String s3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
  }

  public void println(String s0, ExternalClass e1, ExternalClass e2, String s3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.println();
  }

  public void print(String s0, ExternalClass e1, ExternalClass e2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(String s0, ExternalClass e1, ExternalClass e2, Class<?> c3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(String s0, ExternalClass e1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
  }

  public void println(String s0, ExternalClass e1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(Class<?> c0, String s1, String s2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
  }

  public void println(Class<?> c0, String s1, String s2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.println();
  }

  public void print(Class<?> c0, String s1, String s2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(Class<?> c0, String s1, String s2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(Class<?> c0, String s1, String s2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
  }

  public void println(Class<?> c0, String s1, String s2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(Class<?> c0, String s1, Class<?> c2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
  }

  public void println(Class<?> c0, String s1, Class<?> c2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.println();
  }

  public void print(Class<?> c0, String s1, Class<?> c2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(Class<?> c0, String s1, Class<?> c2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(Class<?> c0, String s1, Class<?> c2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
  }

  public void println(Class<?> c0, String s1, Class<?> c2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(Class<?> c0, String s1, ExternalClass e2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
  }

  public void println(Class<?> c0, String s1, ExternalClass e2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.println();
  }

  public void print(Class<?> c0, String s1, ExternalClass e2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(Class<?> c0, String s1, ExternalClass e2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(Class<?> c0, String s1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
  }

  public void println(Class<?> c0, String s1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, String s2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
  }

  public void println(Class<?> c0, Class<?> c1, String s2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, String s2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(Class<?> c0, Class<?> c1, String s2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, String s2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
  }

  public void println(Class<?> c0, Class<?> c1, String s2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, Class<?> c2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
  }

  public void println(Class<?> c0, Class<?> c1, Class<?> c2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, Class<?> c2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(Class<?> c0, Class<?> c1, Class<?> c2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, Class<?> c2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
  }

  public void println(Class<?> c0, Class<?> c1, Class<?> c2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, ExternalClass e2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
  }

  public void println(Class<?> c0, Class<?> c1, ExternalClass e2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, ExternalClass e2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(Class<?> c0, Class<?> c1, ExternalClass e2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
  }

  public void println(Class<?> c0, Class<?> c1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, String s2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
  }

  public void println(Class<?> c0, ExternalClass e1, String s2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, String s2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(Class<?> c0, ExternalClass e1, String s2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, String s2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
  }

  public void println(Class<?> c0, ExternalClass e1, String s2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, Class<?> c2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
  }

  public void println(Class<?> c0, ExternalClass e1, Class<?> c2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, Class<?> c2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(Class<?> c0, ExternalClass e1, Class<?> c2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, Class<?> c2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
  }

  public void println(Class<?> c0, ExternalClass e1, Class<?> c2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, ExternalClass e2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
  }

  public void println(Class<?> c0, ExternalClass e1, ExternalClass e2, String s3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, ExternalClass e2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(Class<?> c0, ExternalClass e1, ExternalClass e2, Class<?> c3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
  }

  public void println(Class<?> c0, ExternalClass e1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(ExternalClass e0, String s1, String s2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
  }

  public void println(ExternalClass e0, String s1, String s2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.println();
  }

  public void print(ExternalClass e0, String s1, String s2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(ExternalClass e0, String s1, String s2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(ExternalClass e0, String s1, String s2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
  }

  public void println(ExternalClass e0, String s1, String s2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(ExternalClass e0, String s1, Class<?> c2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
  }

  public void println(ExternalClass e0, String s1, Class<?> c2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.println();
  }

  public void print(ExternalClass e0, String s1, Class<?> c2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(ExternalClass e0, String s1, Class<?> c2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(ExternalClass e0, String s1, Class<?> c2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
  }

  public void println(ExternalClass e0, String s1, Class<?> c2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(ExternalClass e0, String s1, ExternalClass e2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
  }

  public void println(ExternalClass e0, String s1, ExternalClass e2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.println();
  }

  public void print(ExternalClass e0, String s1, ExternalClass e2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(ExternalClass e0, String s1, ExternalClass e2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(ExternalClass e0, String s1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
  }

  public void println(ExternalClass e0, String s1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, String s2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
  }

  public void println(ExternalClass e0, Class<?> c1, String s2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, String s2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(ExternalClass e0, Class<?> c1, String s2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, String s2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
  }

  public void println(ExternalClass e0, Class<?> c1, String s2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, Class<?> c2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
  }

  public void println(ExternalClass e0, Class<?> c1, Class<?> c2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, Class<?> c2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(ExternalClass e0, Class<?> c1, Class<?> c2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, Class<?> c2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
  }

  public void println(ExternalClass e0, Class<?> c1, Class<?> c2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, ExternalClass e2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
  }

  public void println(ExternalClass e0, Class<?> c1, ExternalClass e2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, ExternalClass e2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(ExternalClass e0, Class<?> c1, ExternalClass e2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
  }

  public void println(ExternalClass e0, Class<?> c1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, String s2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
  }

  public void println(ExternalClass e0, ExternalClass e1, String s2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, String s2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(ExternalClass e0, ExternalClass e1, String s2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, String s2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
  }

  public void println(ExternalClass e0, ExternalClass e1, String s2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, Class<?> c2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
  }

  public void println(ExternalClass e0, ExternalClass e1, Class<?> c2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, Class<?> c2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(ExternalClass e0, ExternalClass e1, Class<?> c2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, Class<?> c2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
  }

  public void println(ExternalClass e0, ExternalClass e1, Class<?> c2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, ExternalClass e2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
  }

  public void println(ExternalClass e0, ExternalClass e1, ExternalClass e2, String s3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, ExternalClass e2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
  }

  public void println(ExternalClass e0, ExternalClass e1, ExternalClass e2, Class<?> c3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
  }

  public void println(ExternalClass e0, ExternalClass e1, ExternalClass e2, ExternalClass e3) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.println();
  }

  // Using 5 parameter(s)

  public void print(String s0, String s1, String s2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(String s0, String s1, String s2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, String s1, String s2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, String s1, String s2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, String s1, String s2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, String s1, String s2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, String s1, String s2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(String s0, String s1, String s2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, String s1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, String s1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, String s1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, String s1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, String s1, String s2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(String s0, String s1, String s2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, String s1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, String s1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, String s1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, String s1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, String s1, Class<?> c2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(String s0, String s1, Class<?> c2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, String s1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, String s1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, String s1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, String s1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, String s1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(String s0, String s1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, String s1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, String s1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, String s1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, String s1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, String s1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(String s0, String s1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, String s1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, String s1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, String s1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, String s1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, String s1, ExternalClass e2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(String s0, String s1, ExternalClass e2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, String s1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, String s1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, String s1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, String s1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, String s1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(String s0, String s1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, String s1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, String s1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, String s1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, String s1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, String s1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(String s0, String s1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, String s1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, String s1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, String s1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, String s1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, Class<?> c1, String s2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(String s0, Class<?> c1, String s2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, Class<?> c1, String s2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, Class<?> c1, String s2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, Class<?> c1, String s2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, Class<?> c1, String s2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, Class<?> c1, String s2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(String s0, Class<?> c1, String s2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, Class<?> c1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, Class<?> c1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, Class<?> c1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, Class<?> c1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, Class<?> c1, String s2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(String s0, Class<?> c1, String s2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, Class<?> c1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, Class<?> c1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, Class<?> c1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, Class<?> c1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, Class<?> c1, Class<?> c2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(String s0, Class<?> c1, Class<?> c2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, Class<?> c1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, Class<?> c1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, Class<?> c1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, Class<?> c1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, Class<?> c1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(String s0, Class<?> c1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, Class<?> c1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, Class<?> c1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, Class<?> c1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, Class<?> c1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, Class<?> c1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(String s0, Class<?> c1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, Class<?> c1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, Class<?> c1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, Class<?> c1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, Class<?> c1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, Class<?> c1, ExternalClass e2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(String s0, Class<?> c1, ExternalClass e2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, Class<?> c1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, Class<?> c1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, Class<?> c1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, Class<?> c1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, Class<?> c1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(String s0, Class<?> c1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, Class<?> c1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, Class<?> c1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, Class<?> c1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, Class<?> c1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, Class<?> c1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(String s0, Class<?> c1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, Class<?> c1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, Class<?> c1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, Class<?> c1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, Class<?> c1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, ExternalClass e1, String s2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(String s0, ExternalClass e1, String s2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, ExternalClass e1, String s2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, ExternalClass e1, String s2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, ExternalClass e1, String s2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, ExternalClass e1, String s2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, ExternalClass e1, String s2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(String s0, ExternalClass e1, String s2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, ExternalClass e1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, ExternalClass e1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, ExternalClass e1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, ExternalClass e1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, ExternalClass e1, String s2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(String s0, ExternalClass e1, String s2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, ExternalClass e1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, ExternalClass e1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, ExternalClass e1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, ExternalClass e1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, ExternalClass e1, Class<?> c2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(String s0, ExternalClass e1, Class<?> c2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, ExternalClass e1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, ExternalClass e1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, ExternalClass e1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, ExternalClass e1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, ExternalClass e1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(String s0, ExternalClass e1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, ExternalClass e1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, ExternalClass e1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, ExternalClass e1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, ExternalClass e1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, ExternalClass e1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(String s0, ExternalClass e1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, ExternalClass e1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, ExternalClass e1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, ExternalClass e1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, ExternalClass e1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, ExternalClass e1, ExternalClass e2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(String s0, ExternalClass e1, ExternalClass e2, String s3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, ExternalClass e1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, ExternalClass e1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, ExternalClass e1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, ExternalClass e1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, ExternalClass e1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(String s0, ExternalClass e1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, ExternalClass e1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, ExternalClass e1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, ExternalClass e1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, ExternalClass e1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(String s0, ExternalClass e1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(String s0, ExternalClass e1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(String s0, ExternalClass e1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(String s0, ExternalClass e1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(String s0, ExternalClass e1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(String s0, ExternalClass e1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(s0);
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, String s1, String s2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(Class<?> c0, String s1, String s2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, String s1, String s2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, String s1, String s2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, String s1, String s2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, String s1, String s2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, String s1, String s2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, String s1, String s2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, String s1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, String s1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, String s1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, String s1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, String s1, String s2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, String s1, String s2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, String s1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, String s1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, String s1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, String s1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, String s1, Class<?> c2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(Class<?> c0, String s1, Class<?> c2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, String s1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, String s1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, String s1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, String s1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, String s1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, String s1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, String s1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, String s1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, String s1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, String s1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, String s1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, String s1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, String s1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, String s1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, String s1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, String s1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, String s1, ExternalClass e2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(Class<?> c0, String s1, ExternalClass e2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, String s1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, String s1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, String s1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, String s1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, String s1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, String s1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, String s1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, String s1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, String s1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, String s1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, String s1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, String s1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, String s1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, String s1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, String s1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, String s1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, String s2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(Class<?> c0, Class<?> c1, String s2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, String s2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, Class<?> c1, String s2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, String s2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, Class<?> c1, String s2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, String s2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, Class<?> c1, String s2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, Class<?> c1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, Class<?> c1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, String s2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, Class<?> c1, String s2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, Class<?> c1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, Class<?> c1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, Class<?> c2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(Class<?> c0, Class<?> c1, Class<?> c2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, Class<?> c1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, Class<?> c1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, Class<?> c1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, Class<?> c1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, Class<?> c1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, Class<?> c1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, Class<?> c1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, Class<?> c1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, ExternalClass e2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(Class<?> c0, Class<?> c1, ExternalClass e2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, Class<?> c1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, Class<?> c1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, Class<?> c1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, Class<?> c1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, Class<?> c1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, Class<?> c1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, Class<?> c1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, Class<?> c1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, Class<?> c1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, String s2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(Class<?> c0, ExternalClass e1, String s2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, String s2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, ExternalClass e1, String s2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, String s2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, ExternalClass e1, String s2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, String s2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, ExternalClass e1, String s2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, ExternalClass e1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, ExternalClass e1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, String s2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, ExternalClass e1, String s2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, ExternalClass e1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, ExternalClass e1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, Class<?> c2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(Class<?> c0, ExternalClass e1, Class<?> c2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, ExternalClass e1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, ExternalClass e1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, ExternalClass e1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, ExternalClass e1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, ExternalClass e1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, ExternalClass e1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, ExternalClass e1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, ExternalClass e1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, ExternalClass e2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(Class<?> c0, ExternalClass e1, ExternalClass e2, String s3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, ExternalClass e1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, ExternalClass e1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, ExternalClass e1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, ExternalClass e1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, ExternalClass e1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(Class<?> c0, ExternalClass e1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(Class<?> c0, ExternalClass e1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(Class<?> c0, ExternalClass e1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(Class<?> c0, ExternalClass e1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(ExternalClass.of(c0)));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, String s1, String s2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, String s1, String s2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, String s1, String s2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, String s1, String s2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, String s1, String s2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, String s1, String s2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, String s1, String s2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, String s1, String s2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, String s1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, String s1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, String s1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, String s1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, String s1, String s2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, String s1, String s2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, String s1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, String s1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, String s1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, String s1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, String s1, Class<?> c2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, String s1, Class<?> c2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, String s1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, String s1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, String s1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, String s1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, String s1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, String s1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, String s1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, String s1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, String s1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, String s1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, String s1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, String s1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, String s1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, String s1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, String s1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, String s1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, String s1, ExternalClass e2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, String s1, ExternalClass e2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, String s1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, String s1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, String s1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, String s1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, String s1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, String s1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, String s1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, String s1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, String s1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, String s1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, String s1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, String s1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, String s1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, String s1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, String s1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, String s1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(s1);
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, String s2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, Class<?> c1, String s2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, String s2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, Class<?> c1, String s2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, String s2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, Class<?> c1, String s2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, String s2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, Class<?> c1, String s2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, Class<?> c1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, Class<?> c1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, String s2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, Class<?> c1, String s2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, Class<?> c1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, Class<?> c1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, Class<?> c2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, Class<?> c1, Class<?> c2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, Class<?> c1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, Class<?> c1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, Class<?> c1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, Class<?> c1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, Class<?> c1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, Class<?> c1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, Class<?> c1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, Class<?> c1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, ExternalClass e2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, Class<?> c1, ExternalClass e2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, Class<?> c1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, Class<?> c1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, Class<?> c1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, Class<?> c1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, Class<?> c1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, Class<?> c1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, Class<?> c1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, Class<?> c1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, Class<?> c1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(ExternalClass.of(c1)));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, String s2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, ExternalClass e1, String s2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, String s2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, ExternalClass e1, String s2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, String s2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, ExternalClass e1, String s2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, String s2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, ExternalClass e1, String s2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, ExternalClass e1, String s2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, ExternalClass e1, String s2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, String s2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, ExternalClass e1, String s2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, ExternalClass e1, String s2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, ExternalClass e1, String s2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(s2);
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, Class<?> c2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, ExternalClass e1, Class<?> c2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, ExternalClass e1, Class<?> c2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, ExternalClass e1, Class<?> c2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, ExternalClass e1, Class<?> c2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, ExternalClass e1, Class<?> c2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, ExternalClass e1, Class<?> c2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, ExternalClass e1, Class<?> c2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, ExternalClass e1, Class<?> c2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, ExternalClass e1, Class<?> c2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(ExternalClass.of(c2)));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, ExternalClass e2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, ExternalClass e1, ExternalClass e2, String s3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, ExternalClass e1, ExternalClass e2, String s3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, ExternalClass e1, ExternalClass e2, String s3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(s3);
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, ExternalClass e1, ExternalClass e2, Class<?> c3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, ExternalClass e1, ExternalClass e2, Class<?> c3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, ExternalClass e1, ExternalClass e2, Class<?> c3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(ExternalClass.of(c3)));
    this.segments.add(registerClass(e4));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
  }

  public void println(ExternalClass e0, ExternalClass e1, ExternalClass e2, ExternalClass e3, String s4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(s4);
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
  }

  public void println(ExternalClass e0, ExternalClass e1, ExternalClass e2, ExternalClass e3, Class<?> c4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(ExternalClass.of(c4)));
    this.println();
  }

  public void print(ExternalClass e0, ExternalClass e1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
  }

  public void println(ExternalClass e0, ExternalClass e1, ExternalClass e2, ExternalClass e3, ExternalClass e4) {
    this.segments.add(registerClass(e0));
    this.segments.add(registerClass(e1));
    this.segments.add(registerClass(e2));
    this.segments.add(registerClass(e3));
    this.segments.add(registerClass(e4));
    this.println();
  }

}
