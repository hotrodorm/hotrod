package org.hotrod.eclipseplugin.treeview;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class HotRodLabelProvider extends StyledCellLabelProvider {

  private static Styler changeStyler = null;

  private void initializeStyler() {
    if (HotRodLabelProvider.changeStyler == null) {
      HotRodLabelProvider.changeStyler = new Styler() {
        @Override
        public void applyStyles(final TextStyle textStyle) {
          Device device = Display.getCurrent();
          // Color c = new Color(device, 255, 0, 0); // red
          Color c = new Color(device, 68, 119, 67); // greenish
          textStyle.foreground = c;
        }
      };
    }
  }

  @Override
  public void update(final ViewerCell cell) {
    Object obj = cell.getElement();
    AbstractFace face = null;

    initializeStyler();

    // Icon

    try {
      face = (AbstractFace) obj; // any face
      cell.setImage(face.getImage());
    } catch (ClassCastException e) { // unknown
      Image image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
      cell.setImage(image);
    }

    // Label

    StyledString label = new StyledString("");
    if (face == null) {

      label.append("{not-a-TreeElement:" + obj.toString() + "}");

    } else {

      String prefix = face.getStatus().getPrefix();
      if (prefix != null) {
        // label.append(prefix, HotRodLabelProvider.changeStyler);
        label.append(prefix, StyledString.DECORATIONS_STYLER);
      }

      if (face.isGenerating()) {
        label.append(AbstractFace.GENERATING_MARKER, StyledString.COUNTER_STYLER);
      }
      label.append(face.getName());

      try {
        SettingsFace s = (SettingsFace) obj; // settings
      } catch (ClassCastException e1) {
        try {
          MainConfigFace c = (MainConfigFace) obj; // main config
          String relativePath = c.getRelativePath().isEmpty() ? "." : c.getRelativePath();
          label.append(" [" + relativePath + "]", StyledString.DECORATIONS_STYLER);
        } catch (ClassCastException e2) {
          try {
            FragmentConfigFace f = (FragmentConfigFace) obj; // fragment
            String relativePath = f.getRelativePath().isEmpty() ? "." : f.getRelativePath();
            label.append("  [" + relativePath + "]", StyledString.DECORATIONS_STYLER);
          } catch (ClassCastException e3) {
            try {
              AbstractMethodFace m = (AbstractMethodFace) obj; // method
              label.append("()");
              label.append(" " + m.getDecoration(), StyledString.DECORATIONS_STYLER);
            } catch (ClassCastException e4) { // any other face
              label.append(" " + face.getDecoration(), StyledString.DECORATIONS_STYLER);
            }
          }
        }
      }

    }

    // styledString.append(" counter", StyledString.COUNTER_STYLER);
    // styledString.append(" decoration", StyledString.DECORATIONS_STYLER);
    // styledString.append(" qualifier", StyledString.QUALIFIER_STYLER);

    cell.setText(label.toString());
    cell.setStyleRanges(label.getStyleRanges());

    super.update(cell);
  }

}
