package org.hotrod.runtime.livesql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.hotrod.runtime.livesql.queries.typesolver.TypeRule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class PersistenceLayerConfigFactory implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  public void setApplicationContext(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @PostConstruct
  private void initialize() {
    computeValidLayerConfigs();
  }

  Map<String, LayerConfig> configs;
  private LayerConfig defaultConfig;

  private void computeValidLayerConfigs() {
    Map<String, LayerConfig> allConfigBeans = applicationContext
        .getBeansOfType(LayerConfig.class);
    this.configs = new HashMap<>();
    this.defaultConfig = null;
    for (Entry<String, LayerConfig> b : allConfigBeans.entrySet()) {
      Component[] ac = b.getValue().getClass().getAnnotationsByType(Component.class);
      if (ac != null && ac.length == 1) {
        this.configs.put(b.getKey(), b.getValue());
        this.defaultConfig = b.getValue();
      }
    }
  }

  public List<TypeRule> getCustomRules(final String layerQualifier) {
    if (layerQualifier == null) {

      if (this.configs.size() > 1) {
        throw new RuntimeException("Looking for a single/default LayerConfig bean but found " + this.configs.size()
            + ", with the following names: " + this.configs.keySet().stream().collect(Collectors.joining(", ")));
      }
      return defaultConfig.getTypeRules();

    } else {

      LayerConfig c = this.configs.get(layerQualifier);
      if (c == null) {
        throw new RuntimeException("Could not find a persistence layer configuration for qualifier: " + layerQualifier);
      }
      return c.getTypeRules();

    }
  }

}
