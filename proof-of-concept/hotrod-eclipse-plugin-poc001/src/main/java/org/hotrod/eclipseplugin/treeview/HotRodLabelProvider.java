package org.hotrod.eclipseplugin.treeview;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class HotRodLabelProvider extends StyledCellLabelProvider {

  @Override
  public void update(final ViewerCell cell) {
    Object obj = cell.getElement();

    // Icon

    try {
      AbstractFace face = (AbstractFace) obj; // any face
      cell.setImage(face.getImage());
    } catch (ClassCastException e) { // unknown
      Image image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
      cell.setImage(image);
    }

    // Label

    StyledString styledString;
    try {
      SettingsFace s = (SettingsFace) obj; // settings
      styledString = new StyledString(s.getLabel());
    } catch (ClassCastException e1) {
      try {
        MainConfigFace face = (MainConfigFace) obj; // main config
        styledString = new StyledString(face.getLabel());
        String relativePath = face.getRelativePath().isEmpty() ? "." : face.getRelativePath();
        styledString.append(" [" + relativePath + "]", StyledString.DECORATIONS_STYLER);
      } catch (ClassCastException e2) {
        try {
          FragmentConfigFace fragmentFace = (FragmentConfigFace) obj; // fragment
          styledString = new StyledString(fragmentFace.getLabel());
          String relativePath = fragmentFace.getRelativePath().isEmpty() ? "." : fragmentFace.getRelativePath();
          styledString.append("  [" + relativePath + "]", StyledString.DECORATIONS_STYLER);
        } catch (ClassCastException e3) {
          try {
            AbstractMethodFace face = (AbstractMethodFace) obj; // method
            styledString = new StyledString(face.getLabel() + "()");
            styledString.append(" " + face.getDecoration(), StyledString.DECORATIONS_STYLER);
          } catch (ClassCastException e4) { // unknown
            try {
              AbstractFace face = (AbstractFace) obj; // any face
              styledString = new StyledString(face.getLabel());
              styledString.append(" " + face.getDecoration(), StyledString.DECORATIONS_STYLER);
            } catch (ClassCastException e5) { // unknown
              styledString = new StyledString("[not-a-TreeElement:" + obj.toString() + "]");
            }
          }
        }
      }
    }

    // styledString.append(" counter", StyledString.COUNTER_STYLER);
    // styledString.append(" decoration", StyledString.DECORATIONS_STYLER);
    // styledString.append(" qualifier", StyledString.QUALIFIER_STYLER);

    cell.setText(styledString.toString());
    cell.setStyleRanges(styledString.getStyleRanges());

    super.update(cell);
  }

}
