package manual;

import org.hotrod.livesql.util.CastUtil;

public class CastTest {

  public static void main(String[] args) {
    new CastTest().allChecks();
  }

  private int pass = 0;
  private int failed = 0;

  private void allChecks() {

//  private static Pattern TYPE_PATTERN = Pattern
//  .compile("^[a-zA-Z][a-zA-Z0-9\\ ]*" + "(\\([0-9]+([\\ ]*,[0-9]+)?\\))?$");

//    Pattern p = Pattern.compile("^"
//        + "[a-zA-Z][a-zA-Z0-9\\ ]*"
//        + "(\\([0-9]+(,\\ *[0-9]+)?\\))?"
//        + "$");
//    Matcher m = p.matcher("a");
//    System.out.println("m.find()=" + m.find());
//
    check(null, false);
    check("", false);
    check(" ", false);
    check("a", true);
    check("A", true);

    check("[]", true);
    check(" []", false);
    check("[]  abc[]d  []d", true);
    check("a[]", true);
    check("[]b", true);
    check("a[]b", true);

    check("''", false);
    check("'+'", false);
    check("'+9'", true);
    check("'+9:'", false);
    check("'+9:9'", true);
    check("':0'", false);

    check("a':0'", false);
    check("a'+1:0'", true);
    check("a'+1:0'b", true);
    check("'+1:0'b", true);
    check("a '+1:0'", true);
    check("'+1:0' bb", true);
    check("':0'b", false);
    check("a':0'b", false);

//    // PostgreSQL:
//    // INTEGER[]
    check("INTEGER[]", true);
    check("integ\"er[]", false);
//
//    // MySQL:
//    // AT TIME ZONE '+00:00' AS DATETIME(2)
    check("AT TIME ZONE '+00:00' AS DATETIME(2)", true);
    check("AT TIME ZONE ':0' AS DATETIME(2)", false);
    check("AT TIME ZONE '0:0' AS DATETIME(2)", false);
    check("AT TIME ZONE '+0:0' AS DATETIME(2)", true);
//    // CHAR CHARACTER SET latin1
    check("CHAR CHARACTER SET latin1", true);
//    // CHAR(50) CHARACTER SET latin1
    check("CHAR(50) CHARACTER SET latin1", true);
//
//    // MariaDB:
//    // CHAR CHARACTER SET utf8
    check("CHAR CHARACTER SET utf8", true);
    check(" CHAR CHARACTER SET utf8", false);

    if (this.failed == 0) {
      log("SUCCESS -- " + this.pass + " passed, " + (this.failed == 0 ? "no failures" : this.failed + " failed."));
    } else {
      log("==========");
      log("FAILURE -- " + this.pass + " passed, " + (this.failed == 0 ? "no failures" : this.failed + " failed."));
      log("==========");
    }
  }

  private void check(String type, boolean expectedToBeValid) {
    try {
//      log("Will check '" + type + "' - expected=" + expectedToBeValid);
      CastUtil.validateCastType(type);
//      log("continued...");
      if (expectedToBeValid) {
        this.pass++;
      } else {
        this.failed++;
        log("Type '" + type + "' should not be valid but succeeded.");
      }
    } catch (RuntimeException e) {
//      log("exception...");
      if (expectedToBeValid) {
        this.failed++;
        log("Type '" + type + "' should be valid but failed.");
      } else {
        this.pass++;
      }
    }
  }

  private void log(String txt) {
    System.out.println(txt);
  }

}
