package research.optimizer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class CustomersPopulator {

  private static final int TOTAL = 30000;

  // 200 first names.

  private static final String[] FIRST_NAME = { "JAMES", "JOHN", "ROBERT", "MICHAEL", "WILLIAM", "DAVID", "RICHARD",
      "JOSEPH", "THOMAS", "CHARLES", "CHRISTOPHER", "DANIEL", "MATTHEW", "ANTHONY", "DONALD", "MARK", "PAUL", "STEVEN",
      "ANDREW", "KENNETH", "GEORGE", "JOSHUA", "KEVIN", "BRIAN", "EDWARD", "RONALD", "TIMOTHY", "JASON", "JEFFREY",
      "RYAN", "GARY", "JACOB", "NICHOLAS", "ERIC", "STEPHEN", "JONATHAN", "LARRY", "JUSTIN", "SCOTT", "FRANK",
      "BRANDON", "RAYMOND", "GREGORY", "BENJAMIN", "SAMUEL", "PATRICK", "ALEXANDER", "JACK", "DENNIS", "JERRY", "TYLER",
      "AARON", "HENRY", "DOUGLAS", "JOSE", "PETER", "ADAM", "ZACHARY", "NATHAN", "WALTER", "HAROLD", "KYLE", "CARL",
      "ARTHUR", "GERALD", "ROGER", "KEITH", "JEREMY", "TERRY", "LAWRENCE", "SEAN", "CHRISTIAN", "ALBERT", "JOE",
      "ETHAN", "AUSTIN", "JESSE", "WILLIE", "BILLY", "BRYAN", "BRUCE", "JORDAN", "RALPH", "ROY", "NOAH", "DYLAN",
      "EUGENE", "WAYNE", "ALAN", "JUAN", "LOUIS", "RUSSELL", "GABRIEL", "RANDY", "PHILIP", "HARRY", "VINCENT", "BOBBY",
      "JOHNNY", "LOGAN", "MARY", "PATRICIA", "JENNIFER", "ELIZABETH", "LINDA", "BARBARA", "SUSAN", "JESSICA",
      "MARGARET", "SARAH", "KAREN", "NANCY", "BETTY", "LISA", "DOROTHY", "SANDRA", "ASHLEY", "KIMBERLY", "DONNA",
      "CAROL", "MICHELLE", "EMILY", "AMANDA", "HELEN", "MELISSA", "DEBORAH", "STEPHANIE", "LAURA", "REBECCA", "SHARON",
      "CYNTHIA", "KATHLEEN", "AMY", "SHIRLEY", "ANNA", "ANGELA", "RUTH", "BRENDA", "PAMELA", "NICOLE", "KATHERINE",
      "VIRGINIA", "CATHERINE", "CHRISTINE", "SAMANTHA", "DEBRA", "JANET", "RACHEL", "CAROLYN", "EMMA", "MARIA",
      "HEATHER", "DIANE", "JULIE", "JOYCE", "EVELYN", "FRANCES", "JOAN", "CHRISTINA", "KELLY", "VICTORIA", "LAUREN",
      "MARTHA", "JUDITH", "CHERYL", "MEGAN", "ANDREA", "ANN", "ALICE", "JEAN", "DORIS", "JACQUELINE", "KATHRYN",
      "HANNAH", "OLIVIA", "GLORIA", "MARIE", "TERESA", "SARA", "JANICE", "JULIA", "GRACE", "JUDY", "THERESA", "ROSE",
      "BEVERLY", "DENISE", "MARILYN", "AMBER", "MADISON", "DANIELLE", "BRITTANY", "DIANA", "ABIGAIL", "JANE", "NATALIE",
      "LORI", "TIFFANY", "ALEXIS", "KAYLA" };

  // 100 last names.

  private static final String[] LAST_NAME = { "SMITH", "JOHNSON", "WILLIAMS", "JONES", "BROWN", "DAVIS", "MILLER",
      "WILSON", "MOORE", "TAYLOR", "ANDERSON", "THOMAS", "JACKSON", "WHITE", "HARRIS", "MARTIN", "THOMPSON", "GARCIA",
      "MARTINEZ", "ROBINSON", "CLARK", "RODRIGUEZ", "LEWIS", "LEE", "WALKER", "HALL", "ALLEN", "YOUNG", "HERNANDEZ",
      "KING", "WRIGHT", "LOPEZ", "HILL", "SCOTT", "GREEN", "ADAMS", "BAKER", "GONZALEZ", "NELSON", "CARTER", "MITCHELL",
      "PEREZ", "ROBERTS", "TURNER", "PHILLIPS", "CAMPBELL", "PARKER", "EVANS", "EDWARDS", "COLLINS", "STEWART",
      "SANCHEZ", "MORRIS", "ROGERS", "REED", "COOK", "MORGAN", "BELL", "MURPHY", "BAILEY", "RIVERA", "COOPER",
      "RICHARDSON", "COX", "HOWARD", "WARD", "TORRES", "PETERSON", "GRAY", "RAMIREZ", "JAMES", "WATSON", "BROOKS",
      "KELLY", "SANDERS", "PRICE", "BENNETT", "WOOD", "BARNES", "ROSS", "HENDERSON", "COLEMAN", "JENKINS", "PERRY",
      "POWELL", "LONG", "PATTERSON", "HUGHES", "FLORES", "WASHINGTON", "BUTLER", "SIMMONS", "FOSTER", "GONZALES",
      "BRYANT", "ALEXANDER", "RUSSELL", "GRIFFIN", "DIAZ", "HAYES" };

  private Random random;

  public CustomersPopulator(final Random random) {
    this.random = random;
  }

  public void truncate() throws SQLException {
    ConsoleProgress cp = new ConsoleProgress("Deleting Customers", 1);
    SQLExecutor.executeUpdate("delete from customer");
    cp.complete();
  }

  public void populate(final AddressesPopulator addressesPopulator) throws SQLException {
    PreparedStatement st = null;
    ConsoleProgress cp = new ConsoleProgress("Adding Customers", TOTAL);
    try {
      String sql = "insert into customer (id, first_name, last_name, phone_number, address_id) values (?, ?, ?, ?, ?)";
      st = SQLExecutor.getConnection().prepareStatement(sql);

      for (int id = 0; id < TOTAL; id++) {

        String firstName = FIRST_NAME[random.nextInt(FIRST_NAME.length)];
        String lastName = LAST_NAME[random.nextInt(LAST_NAME.length)];
        int phoneNumber = 2025550000 + random.nextInt(10000);
        int addressId = addressesPopulator.getRandomId();

        int col = 1;
        st.setInt(col++, id);
        st.setString(col++, firstName);
        st.setString(col++, lastName);
        st.setString(col++, "" + phoneNumber);
        st.setInt(col++, addressId);

        st.execute();

        cp.update(id);
      }

      cp.complete();

    } finally {
      if (st != null) {
        st.close();
      }
    }

  }

  public int getRandomId() {
    return this.random.nextInt(TOTAL);
  }

}
