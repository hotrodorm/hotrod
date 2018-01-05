package plugin001view3activator.views.config;

import java.util.ArrayList;
import java.util.List;

import plugin001view3activator.views.tree.DAOElement;
import plugin001view3activator.views.tree.FragmentConfigElement;
import plugin001view3activator.views.tree.HotRodViewContentProvider;
import plugin001view3activator.views.tree.MainConfigElement;
import plugin001view3activator.views.tree.TableElement;
import plugin001view3activator.views.tree.ViewElement;

public class ConfigProducer {

  // Properties

  private List<MainConfigElement> mainConfigs = null;

  // Constructor

  public ConfigProducer(final HotRodViewContentProvider provider) {
    FragmentConfigElement f1 = new FragmentConfigElement("hotrod-fragment-1.xml");
    f1.addChild(new TableElement("product"));
    f1.addChild(new ViewElement("hot_product"));
    f1.addChild(new DAOElement("AccountingDAO"));

    FragmentConfigElement f2 = new FragmentConfigElement("hotrod-fragment-2.xml");
    f2.addChild(new TableElement("client"));

    MainConfigElement c1 = new MainConfigElement("hotrod-1.xml", provider);
    c1.addChild(new TableElement("customer"));
    c1.addChild(new ViewElement("new_supplier"));
    c1.addChild(new DAOElement("OrdersDAO"));
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
