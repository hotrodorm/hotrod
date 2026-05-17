package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TypeParser {

  public static class Type {

    public final String base;
    public final List<Type> generics;
    public final int arrayDepth;

    public Type(String base, List<Type> generics, int arrayDepth) {
      this.base = expand(base);
      this.generics = Collections.unmodifiableList(generics);
      this.arrayDepth = arrayDepth;
    }

    public static String expand(String type) {
      if (type != null && type.indexOf(".") == -1) {
        return "java.lang." + type;
      }
      return type;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(base);
      if (!generics.isEmpty()) {
        sb.append("<");
        for (int i = 0; i < generics.size(); i++) {
          if (i > 0)
            sb.append(", ");
          sb.append(generics.get(i).toString());
        }
        sb.append(">");
      }
      for (int i = 0; i < arrayDepth; i++)
        sb.append("[]");
      return sb.toString();
    }

  }

  public static Type parse(String input) throws InvalidTypeException {
    Parser p = new Parser(input);
    Type t = p.parseType();
    p.skipWhitespace();
    if (!p.isAtEnd())
      throw new InvalidTypeException("Unexpected trailing characters at pos " + p.pos);
    return t;
  }

  private static final class Parser {
    final String s;
    int pos = 0;

    Parser(String s) {
      this.s = s;
    }

    Type parseType() throws InvalidTypeException {
      skipWhitespace();
      String base = parseIdentifier();
      skipWhitespace();
      List<Type> generics = Collections.emptyList();
      if (peek() == '<') {
        generics = parseGenerics();
      }
      int arrays = 0;
      skipWhitespace();
      while (peek() == '[') {
        // expect "[]"
        int save = pos;
        consume('[');
        skipWhitespace();
        if (peek() == ']') {
          consume(']');
          arrays++;
          skipWhitespace();
        } else {
          pos = save; // rollback
          break;
        }
      }
      return new Type(base, generics, arrays);
    }

    List<Type> parseGenerics() throws InvalidTypeException {
      consume('<');
      skipWhitespace();
      List<Type> args = new ArrayList<>();
      while (true) {
        args.add(parseType());
        skipWhitespace();
        char c = peek();
        if (c == ',') {
          consume(',');
          skipWhitespace();
          continue;
        } else if (c == '>') {
          consume('>');
          break;
        } else {
          throw new InvalidTypeException("Expected ',' or '>' in generics at pos " + pos + " got '" + c + "'");
        }
      }
      return args;
    }

    String parseIdentifier() throws InvalidTypeException {
      skipWhitespace();
      int start = pos;
      while (!isAtEnd()) {
        char c = peek();
        // allow Java identifiers, dots for package/class, $ for inner classes
        if (Character.isJavaIdentifierPart(c) || c == '.' || c == '$') {
          pos++;
        } else
          break;
      }
      if (pos == start)
        throw new InvalidTypeException("Expected identifier at pos " + pos);
      return s.substring(start, pos).trim();
    }

    void skipWhitespace() {
      while (!isAtEnd() && Character.isWhitespace(peek()))
        pos++;
    }

    char peek() {
      return isAtEnd() ? '\0' : s.charAt(pos);
    }

    boolean isAtEnd() {
      return pos >= s.length();
    }

    void consume(char expected) throws InvalidTypeException {
      if (isAtEnd() || s.charAt(pos) != expected)
        throw new InvalidTypeException("Expected '" + expected + "' at pos " + pos);
      pos++;
    }

  }

  public static void main(String[] args) {
    String[] examples = { "java.util.Map<java.lang.String, java.util.List<int[]>>[]", "List<String[]>", "Map< K , V >",
        "int[][]", "Outer.Inner<Inner2<String>, Map<Integer, List<Double[]>>>[]", //
        "  a< b[] [][],c<d[],e<f,g[][][][]>[][][]>[],  h, i > " //
    };
    for (String ex : examples) {
      try {
        Type t = parse(ex);
        System.out.println(ex + " -> " + t);
      } catch (Exception e) {
        System.out.println(ex + " -> ERROR: " + e.getMessage());
      }
    }
  }

  public static class InvalidTypeException extends Exception {

    private static final long serialVersionUID = 1L;

    private InvalidTypeException(String message) {
      super(message);
    }

  }

}
