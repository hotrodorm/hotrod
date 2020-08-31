package com.myapp1;

import java.sql.SQLException;
import java.util.List;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.myapp1.persistence.CityVO;
import com.myapp1.persistence.RegionVO;
import com.myapp1.persistence.primitives.CityDAO;
import com.myapp1.persistence.primitives.RegionDAO;

@Component("dataServices2")
public class DataServices2 {

  @Autowired
  private RegionDAO regionDAO;

  @Autowired
  private CityDAO cityDAO;

  @Autowired
  private LiveSQL sql;

  public void demoFKs() {

    // Get region by PK
    RegionVO r3 = regionDAO.selectByPK(3);
    System.out.println(" * Get region by PK - r3=" + r3.getName()); // Sierra

    // Get region parent by FK
    RegionVO rp = regionDAO.selectParentRegionOf(r3).fromUnifiedCode().toRegionId(); // Chicago
    System.out.println(" * Get region parent by FK - rp: " + rp.getName()); // Sierra

    // Get region children by FK
    List<RegionVO> rc = regionDAO.selectChildrenRegionOf(r3).fromRegionId().toUnifiedCode(); // Boston
    for (RegionVO c : rc) {
      System.out.println(" * Get region children by FK - rc: " + c.getName());
    }

    // Get city parent by FK

    CityVO c = regionDAO.selectParentCityOf(r3).fromRegionId().toCityId(); // Boston
    System.out.println(" * Get city parent by FK - c: " + c.getCityName());

    // Get city children by FK

    List<CityVO> cc = regionDAO.selectChildrenCityOf(r3).fromUnifiedCode().toCode2();
    for (CityVO c1 : cc) {
      System.out.println(" * Get city children by FK - c1: " + c1.getCityName()); // Chicago, Boston
    }

  }

  public List<CityVO> findCitiesOfRegion(final int regionId) throws SQLException {
    CityVO example = new CityVO();
    example.setCode1(regionId);
    return this.cityDAO.selectByExample(example);
  }

}
