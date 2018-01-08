package org.hotrod.eclipseplugin.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.hotrod.eclipseplugin.domain.MainConfigFile;
import org.hotrod.eclipseplugin.domain.loader.ConfigFileLoader;
import org.hotrod.eclipseplugin.domain.loader.FaultyConfigFileException;
import org.hotrod.eclipseplugin.domain.loader.UnreadableConfigFileException;
import org.hotrod.eclipseplugin.elements.DAOElement;
import org.hotrod.eclipseplugin.elements.FragmentConfigElement;
import org.hotrod.eclipseplugin.elements.HotRodViewContentProvider;
import org.hotrod.eclipseplugin.elements.MainConfigElement;
import org.hotrod.eclipseplugin.elements.QueryElement;
import org.hotrod.eclipseplugin.elements.SelectElement;
import org.hotrod.eclipseplugin.elements.SequenceElement;
import org.hotrod.eclipseplugin.elements.TableElement;
import org.hotrod.eclipseplugin.elements.ViewElement;

public class ConfigProducer {

  // Properties

  public static final boolean USE_INTERNAL_CONFIG = true;

  private List<MainConfigElement> mainConfigs = null;

  // Constructor

  public ConfigProducer(final HotRodViewContentProvider provider, final List<File> files) {
    if (USE_INTERNAL_CONFIG) {
      generateInternalConfig(provider);
    } else {
      this.mainConfigs = new ArrayList<MainConfigElement>();
      for (File f : files) {
        try {
          MainConfigFile config = ConfigFileLoader.loadMainConfigFile(f);
          // MainConfigElement configElement = new MainConfigElement();
          // this.mainConfigs.add(config);
        } catch (UnreadableConfigFileException e) {
          e.printStackTrace();
        } catch (FaultyConfigFileException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private void generateInternalConfig(final HotRodViewContentProvider provider) {
    FragmentConfigElement f1 = new FragmentConfigElement("hotrod-fragment-1.xml");

    f1.addChild(new TableElement("customer"));

    f1.addChild(new ViewElement("new_supplier"));

    DAOElement d1 = new DAOElement("AccountingDAO");
    f1.addChild(d1);
    d1.addChild(new SelectElement("retrieveRevenueByLine"));
    d1.addChild(new SelectElement("retrieveCostByRegion"));

    FragmentConfigElement f2 = new FragmentConfigElement("hotrod-fragment-2.xml");
    f2.addChild(new TableElement("client"));

    MainConfigElement c1 = new MainConfigElement("hotrod-1.xml", provider);

    TableElement t1 = new TableElement("product");
    c1.addChild(t1);
    t1.addChild(new SequenceElement("selectSequenceId"));
    t1.addChild(new QueryElement("applyPromotion74"));
    t1.addChild(new SelectElement("getVIPProducts"));
    t1.addChild(new SelectElement("getProductSummary"));
    t1.addChild(new QueryElement("applyPromotion75"));
    t1.addChild(new SelectElement("getCountryProductTree"));

    ViewElement v1 = new ViewElement("hot_product");
    c1.addChild(v1);
    v1.addChild(new SelectElement("getBranchHotProducts"));
    v1.addChild(new QueryElement("addHotProduct"));
    v1.addChild(new QueryElement("removeHotProduct"));
    v1.addChild(new SelectElement("getRegionHotProducts"));

    DAOElement d2 = new DAOElement("OrdersDAO");
    c1.addChild(d2);
    d2.addChild(new QueryElement("chargeOrder"));
    d2.addChild(new QueryElement("cancelOrder"));
    d2.addChild(new QueryElement("fulfillOrder"));
    d2.addChild(new QueryElement("closeOrder"));
    d2.addChild(new QueryElement("reopenOrder"));

    c1.addChild(f1);
    c1.addChild(f2);

    MainConfigElement c2 = new MainConfigElement("hotrod-2.xml", provider);
    c2.addChild(new TableElement("employee"));

    this.mainConfigs = new ArrayList<MainConfigElement>();
    this.mainConfigs.add(c1);
    this.mainConfigs.add(c2);

    setUnmodified();
  }

  public void setUnmodified() {
    for (MainConfigElement e : this.mainConfigs) {
      e.setUnmodified();
    }
  }

  public List<MainConfigElement> getConfigs() {
    return this.mainConfigs;
  }

}
