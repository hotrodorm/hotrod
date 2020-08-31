package org.hotrodorm.pocs.sp.model;

import java.util.List;

public class Result {

  public enum ResultType {
    UPDATE_COUNT, RESULT_SET
  }

  private ResultType resultType;
  private List<String> resultSetColumnTypes;

}
