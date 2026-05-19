package app;

public class Search1Filter {

  private boolean filterActive;
  private String firstName;
  private String lastName;

  public final boolean isFilterActive() {
    return filterActive;
  }

  public final void setFilterActive(boolean filterActive) {
    this.filterActive = filterActive;
  }

  public final String getFirstName() {
    return firstName;
  }

  public final void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public final String getLastName() {
    return lastName;
  }

  public final void setLastName(String lastName) {
    this.lastName = lastName;
  }

}
