package enumerations.phase1;

public enum EmployeeState {

  ENROLLED(1, "Enrolled"), //
  ACCEPTED(1, "Accepted"), //
  PENDING(1, "Pending"), //
  REJECTED(1, "Rejected"), //
  NONE(1, "NONE"), //
  DISMISSED(1, "DISMISSED");

  // Properties (table columns)

  private Integer value;
  private String name;

  // Constructor

  private EmployeeState(final Integer value, final String name) {
    this.value = value;
    this.name = name;
  }

  // Getters

  public Integer getValue() {
    return value;
  }

  public String getName() {
    return name;
  }

}
