package org.hotrod.eclipseplugin.treeview;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.hotrod.eclipseplugin.Activator;

public class ImageCache {

  private static Map<String, Image> cache = new HashMap<String, Image>();

  public static Image getImage(final String imagePath) {
    if (cache.containsKey(imagePath)) {
      return cache.get(imagePath);
    } else {
      ImageDescriptor imgDesc = Activator.getImageDescriptor(imagePath);
      Image image = imgDesc != null ? imgDesc.createImage() : null;
      cache.put(imagePath, image);
      return image;
    }
  }

  public static void dispose() {
    for (Image image : cache.values()) {
      image.dispose();
    }
  }

}
