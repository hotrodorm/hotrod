package manualtests;

import org.hotrod.utils.identifiers.Identifier;
import org.hotrod.utils.identifiers.JavaIdentifier;

public class JavaIdentifierTests {

  public static void main(final String[] args) {
    print("AbcDef");
    print("Abc1Def");
    print("abcdef");
    print("ABCDEF");
    print("AbcDEf");
  }

  private static void print(String v1) {
    Identifier id;
    id = new JavaIdentifier(v1, "int");
    System.out.println("As java Class -> " + id.getJavaClassIdentifier());
  }

}
