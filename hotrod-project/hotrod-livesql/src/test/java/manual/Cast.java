package manual;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hotrod.livesql.util.CastUtil;

public class Cast {

  public static void main(String[] args) {
    new Cast().allChecks();
  }

  private int succeeded = 0;
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

    // PostgreSQL:
    // INTEGER[]
    check("INTEGER[]", true);
    check("integ\"er[]", true);

    // MySQL:
    // AT TIME ZONE '+00:00' AS DATETIME(2)
    // CHAR CHARACTER SET latin1
    check("CHAR CHARACTER SET latin1", true);
    // CHAR(50) CHARACTER SET latin1
    check("CHAR(50) CHARACTER SET latin1", true);

    // MariaDB:
    // CHAR CHARACTER SET utf8
    check("CHAR CHARACTER SET utf8", true);

    if (this.failed == 0) {
      log("SUCCESS -- " + this.succeeded + " succeeded, " + this.failed + " failed.");
    } else {
      log("==========");
      log("FAILURE -- " + this.succeeded + " succeeded, " + this.failed + " failed.");
      log("==========");
    }
  }

  private void check(String type, boolean expectedToBeValid) {
    try {
//      log("Will check '" + type + "' - expected=" + expectedToBeValid);
      CastUtil.validateCastType(type);
//      log("continued...");
      if (expectedToBeValid) {
        this.succeeded++;
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
        this.succeeded++;
      }
    }
  }

  private void log(String txt) {
    System.out.println(txt);
  }

}
