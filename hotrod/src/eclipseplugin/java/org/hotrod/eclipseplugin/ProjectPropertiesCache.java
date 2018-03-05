package org.hotrod.eclipseplugin;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.hotrod.eclipseplugin.ProjectProperties.CouldNotLoadPropertiesException;

public class ProjectPropertiesCache {

  private static Map<String, ProjectProperties> projectProperties = new HashMap<String, ProjectProperties>();

  public static ProjectProperties getProjectProperties(final IProject project) {
    String path = project.getLocation().toFile().getPath();
    ProjectProperties props = projectProperties.get(path);
    if (props == null) {
      try {
        props = ProjectProperties.load(project);
      } catch (CouldNotLoadPropertiesException e) {
        props = ProjectProperties.newEmptyProperties(project);
      }
      projectProperties.put(path, props);
    }
    return props;
  }

}
