package tests;

//<!-- Calling a stored procedure -->
//
//<resultMap id="values1" type="tests.ValuesVO">
//  <result property="name" column="NAME" />
//  <result property="type" column="TYPE" />
//  <result property="currentBalance" column="CURRENT_BALANCE" />
//  <result property="createdOn" column="CREATED_ON" />
//  <result property="rowVersion" column="ROW_VERSION" />
//</resultMap>
//
//<select id="callParams1" parameterType="tests.ValuesVO" statementType="CALLABLE">
//  {call params1(
//    #{accountName, jdbcType=VARCHAR, mode=IN},
//    #{balanceSum, jdbcType=INTEGER, mode=INOUT},
//    #{balanceOut, jdbcType=INTEGER, mode=OUT}
//    )}
//</select>    

public class ValuesVO {

  // account_name in varchar2(20),
  // balance_sum inout number(9),
  // balance out number(9)

  private String nameOut;

  private String accountName;
  private Integer balanceSum;
  private Integer balanceOut;

  public String getNameOut() {
    return nameOut;
  }

  public void setNameOut(String nameOut) {
    this.nameOut = nameOut;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public Integer getBalanceSum() {
    return balanceSum;
  }

  public void setBalanceSum(Integer balanceSum) {
    this.balanceSum = balanceSum;
  }

  public Integer getBalanceOut() {
    return balanceOut;
  }

  public void setBalanceOut(Integer balanceOut) {
    this.balanceOut = balanceOut;
  }

  @Override
  public String toString() {
    return "ValuesVO [" + (accountName != null ? "accountName=" + accountName + ", " : "")
        + (balanceSum != null ? "balanceSum=" + balanceSum + ", " : "")
        + (balanceOut != null ? "balanceOut=" + balanceOut : "") + "]";
  }

}
