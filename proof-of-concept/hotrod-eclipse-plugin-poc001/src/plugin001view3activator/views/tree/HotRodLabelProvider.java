package plugin001view3activator.views.tree;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class HotRodLabelProvider extends LabelProvider {

  @Override
  public String getText(final Object element) {
    if (element instanceof TreeElement) {
      return ((TreeElement) element).getLabel();
    }
    return "[not-a-TreeElement:" + element.toString() + "]";
  }

  @Override
  public Image getImage(final Object element) {
    if (element instanceof TreeElement) {
      return ((TreeElement) element).getImage();
    }

    Image image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
    return image;

  }

}
