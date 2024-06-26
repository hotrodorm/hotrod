package app.test.ord;

public class RHelper {

  public static void ren(OrdTer ot) {
    try {
      Exp exp = (Exp) ot;
      exp.renderTo();
    } catch (ClassCastException e) {
      try {
        OrdExp oe = (OrdExp) ot;
        oe.renderTo();
      } catch (ClassCastException e2) {
        // not rendereable
      }
    }
  }

}
