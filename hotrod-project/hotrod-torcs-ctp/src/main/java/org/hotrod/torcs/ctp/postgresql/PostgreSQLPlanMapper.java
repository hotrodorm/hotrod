package org.hotrod.torcs.ctp.postgresql;

import java.util.List;
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
public interface PostgreSQLPlanMapper {

// planName: plan1
// paramTypes: unknown x n
// sql: select ...
// values: null x n

  @Select("prepare ${planName} (${paramTypes}) as ${sql}; explain (format json) execute ${planName} (${paramValues}); deallocate ${planName};")
  public List<String> getEstimatedPlan(Map<String, Object> parameters);

  @Select("prepare ${planName} (${paramTypes}) as ${sql}; explain (format json, analyze) execute ${planName} (${paramValues}); deallocate ${planName};")
  public List<String> getActualPlan(Map<String, Object> parameters);

}
