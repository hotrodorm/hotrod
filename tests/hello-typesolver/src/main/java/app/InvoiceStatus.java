package app;

public enum InvoiceStatus {

  DRAFT(1), UNPAID(2), PAID(3);

  private int code;

  private InvoiceStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

}
