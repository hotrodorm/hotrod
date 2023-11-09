package org.hotrod.torcs.ctp.h2;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

//deallocate sql1;
//
//prepare sql1 (unknown) as 
//SELECT
//i.id, 
//i.amount, 
//i.branch_id
//FROM public.invoice i
//WHERE i.branch_id = $1
//
//explain (format json) execute sql1(null);  

@Mapper
public interface DummyH2PlanMapper {

// planName: plan1
// paramTypes: unknown x n
// sql: select ...
// values: null x n

  @Select("explain ${sql}")
  public String getEstimatedPlan(Map<String, Object> parameters);

  @Select("explain analyze ${sql}")
  public String getActualPlan(Map<String, Object> parameters);

}
