package org.hotrod.livesql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.hotrod.livesql.queries.typesolver.TypeRule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class PersistenceLayerConfigFactory implements ApplicationContextAware {

  private static final Logger log = Logger.getLogger(PersistenceLayerConfigFactory .class.getName());

  private ApplicationContext applicationContext;

  public void setApplicationContext(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @PostConstruct
  private void initialize() {
    log.info("POST-CONSTRUCT");
    computeValidLayerConfigs();
  }

  Map<String, LayerConfigInterface> configs;
  private LayerConfigInterface defaultConfig;

  private void computeValidLayerConfigs() {
    log.info("POST-CONSTRUCT 2");
    Map<String, LayerConfigInterface> allConfigBeans = applicationContext
        .getBeansOfType(LayerConfigInterface.class);
    this.configs = new HashMap<>();
    this.defaultConfig = null;
    for (Entry<String, LayerConfigInterface> b : allConfigBeans.entrySet()) {
      Component[] ac = b.getValue().getClass().getAnnotationsByType(Component.class);
      if (ac != null && ac.length == 1) {
        this.configs.put(b.getKey(), b.getValue());
        this.defaultConfig = b.getValue();
      }
    }
  }

  public List<TypeRule> getCustomRules(final String layerQualifier) {
    log.info("POST-CONSTRUCT 3");
    if (layerQualifier == null) {

      if (this.configs.size() > 1) {
        throw new RuntimeException("Looking for a single/default LayerConfig bean but found " + this.configs.size()
            + ", with the following names: " + this.configs.keySet().stream().collect(Collectors.joining(", ")));
      }
      return defaultConfig.getTypeRules();

    } else {

      LayerConfigInterface c = this.configs.get(layerQualifier);
      if (c == null) {
        throw new RuntimeException("Could not find a persistence layer configuration for qualifier: " + layerQualifier);
      }
      return c.getTypeRules();

    }
  }

}
