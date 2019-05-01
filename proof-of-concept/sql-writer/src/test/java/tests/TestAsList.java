package tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestAsList {

  public static void main(final String[] args) {
    toList2("abc");
    toList2("def", "ghi");
    
  }

//  private static void toList(final String... params) {
//    List<String> names = Arrays.asList(params);
//    System.out.println("names[" + (names == null ? "null" : names.size()) + "]");
//  }

  private static void toList2(final String first, final String... rest) {
    List<String> names = new ArrayList<String>();
    names.add(first);
    names.addAll(Arrays.asList(rest));
    System.out.println("names[" + (names == null ? "null" : names.size()) + "]");
  }

}
