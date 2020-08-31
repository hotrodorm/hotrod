package com.myapp1.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.myapp1.persistence.CityVO;
import com.myapp1.persistence.RegionVO;
import com.myapp1.persistence.primitives.CityDAO;
import com.myapp1.persistence.primitives.CityDAO.CityOrderBy;
import com.myapp1.persistence.primitives.RegionDAO;
import com.myapp1.persistence.primitives.RegionDAO.RegionOrderBy;

public class FKs {

  @Autowired
  private RegionDAO regionDAO;

  @Autowired
  private CityDAO cityDAO;

  public void test1() {

    RegionVO r1 = new RegionVO();

    // Common select children
    List<CityVO> cities = regionDAO.selectChildrenCityOf(r1).fromUnifiedCode().toCode1(CityOrderBy.CITY_NAME);

    // Reflexive select parent
    RegionVO r2 = regionDAO.selectParentRegionOf(r1).fromUnifiedCode().toRegionId();

    // Reflexive select children
    List<RegionVO> r3 = regionDAO.selectChildrenRegionOf(r1).fromRegionId().toUnifiedCode(RegionOrderBy.NAME);

    CityVO c3 = new CityVO();

    // Common select parent
    RegionVO r4 = cityDAO.selectParentRegionOf(c3).fromCityName().toName();
    RegionVO r5 = cityDAO.selectParentRegionOf(c3).fromCode1().toUnifiedCode();
    RegionVO r6 = cityDAO.selectParentRegionOf(c3).fromCode1().toRegionId();
    RegionVO r8 = cityDAO.selectParentRegionOf(c3).fromCode2().toUnifiedCode();
    RegionVO r9 = cityDAO.selectParentRegionOf(c3).fromCode2().toRegionId();

    List<RegionVO> r10 = cityDAO.selectChildrenRegionOf(c3).fromCityId().toRegionId();

  }

}
