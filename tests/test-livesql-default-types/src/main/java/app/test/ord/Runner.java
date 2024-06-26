package app.test.ord;

import java.util.ArrayList;
import java.util.List;

public class Runner {

  public static void main(String[] args) {

    List<OrdTer> ts = new ArrayList<>();
    ts.add(new Exp());
    ts.add(new OrdExp());

    for (OrdTer ot : ts) {
      RHelper.ren(ot);
    }

  }

}
