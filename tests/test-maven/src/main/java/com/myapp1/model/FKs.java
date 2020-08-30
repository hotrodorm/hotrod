package com.myapp1.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.myapp1.persistence.CityVO;
import com.myapp1.persistence.RegionVO;
import com.myapp1.persistence.primitives.CityDAO;
import com.myapp1.persistence.primitives.CityDAO.CityOrderBy;
import com.myapp1.persistence.primitives.RegionDAO;

public class FKs {

  @Autowired
  private RegionDAO regionDAO;

  @Autowired
  private CityDAO cityDAO;

  public void test1() {

    RegionVO r1 = new RegionVO();
    List<CityVO> cities = regionDAO.selectChildrenCityOf(r1).fromUnifiedCode().toCode1(CityOrderBy.CITY_NAME);

    CityVO c3 = new CityVO();
    RegionVO r3 = cityDAO.selectParentRegionOf(c3).fromCityName().toName();

    cityDAO.selectParentRegionOf(c3).fromCode1().toUnifiedCode();
  }

}
