package enumerations.phase2;

import java.sql.Date;

  public enum EmployeeState {
  
    ENROLLED(1, "Enrolled", true, Date.valueOf("2012-11-29")), //
    ACCEPTED(1, "Accepted", true, Date.valueOf("2012-11-29")), //
    PENDING(1, "Pending", false, Date.valueOf("2013-08-05")), //
    REJECTED(1, "Rejected", true, Date.valueOf("2012-08-01")), //
    NONE(1, null, null, null), //
    DISMISSED(1, null, null, null);
  
    // Properties (table columns)
  
    private Integer id;
    private String title;
    private Boolean inUse;
    private Date since;
  
    // Constructor
  
    private EmployeeState(final Integer id, final String title, final Boolean inUse, final Date since) {
      this.id = id;
      this.title = title;
      this.inUse = inUse;
      this.since = since;
    }
  
    // Getters
  
    public Integer getId() {
      return id;
    }
  
    public String getTitle() {
      return title;
    }
  
    public boolean isInUse() {
      return inUse;
    }
  
    public Date getSince() {
      return since;
    }
  
  }
