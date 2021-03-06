package org.hotrod.eclipseplugin.treeview;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class HotRodLabelProvider extends StyledCellLabelProvider {

  private Composite parent;

  private static Styler changeStyler = null;

  public HotRodLabelProvider(final Composite parent) {
    super();
    this.parent = parent;
  }

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
  public String getToolTipText(final Object element) {
    System.out.println("element=" + element + " (" + (element == null ? "<null>" : element.getClass().getName()) + ")");
    AbstractFace face = (AbstractFace) element;
    String longText = "This is my tooltip than can be:\nreally long and long and "
        + "long and long and long and long and long and long. "
        + "Yep it can CONTINUE to be long and long and long and " + "long and long and long. "
        + "\nPLUS MORE 1: more and more and more and more and more and more and more and more and more and."
        + "\nPLUS MORE 2: more and more and more and more and more and more and more and more and more and."
        + "\nPLUS MORE 3: more and more and more and more and more and more and more and more and more and."
        + "\nPLUS MORE 4: more and more and more and more and more and more and more and more and more and."
        + "\nPLUS MORE 5: more and more and more and more and more and more and more and more and more and.";
    return face.getName().endsWith("income") ? longText : null;
  }

  @Override
  public Point getToolTipShift(Object object) {
    return new Point(5, 5);
  }

  @Override
  public int getToolTipDisplayDelayTime(Object object) {
    return 100;
  }

  @Override
  public int getToolTipTimeDisplayed(Object object) {
    return 30000;
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

      // String prefix = face.getTreeStatus().getPrefix() + ":" +
      // face.getStatus().getPrefix();

      String prefix;
      switch (face.getTreeStatus()) {
      case UNAFFECTED:
        prefix = "";
        break;
      case MODIFIED:
        prefix = "> ";
        break;
      case ADDED:
        prefix = "+ ";
        break;
      case DELETED:
        prefix = "- ";
        break;
      default:
        prefix = "";
        break;
      }

      // String prefix = face.getTreeStatus().getPrefix();
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

          // org.eclipse.ui.forms.widgets.Hyperlink.Hyperlink hl;

          // =========================================================================

          // Hyperlink link = new Hyperlink(this.parent, SWT.FILL |
          // SWT.READ_ONLY);
          // link.setText("My HyperLink");

          // /* make text look like a link */
          // StyledString text = new StyledString();
          //
          // String caption = "1234567890";
          //
          // StyleRange myStyledRange = new StyleRange(0, caption.length(),
          // Display.getCurrent().getSystemColor(SWT.COLOR_BLUE), null);
          // myStyledRange.underline = true;
          // text.append(caption, StyledString.DECORATIONS_STYLER);
          // cell.setText(text.toString());
          //
          // StyleRange[] range = { myStyledRange };
          // cell.setStyleRanges(range);
          //
          // super.update(cell);

          // =========================================================================

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
              try {
                ErrorMessageFace m = (ErrorMessageFace) obj; // error message
              } catch (ClassCastException e5) { // any other face
                label.append(" " + face.getDecoration(), StyledString.DECORATIONS_STYLER);
              }
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

  // public String getText(Object element) {
  // return "My Tooltip 1.";
  // }

  // private void listen() {
  // styledText.addListener(SWT.MouseDown, event -> {
  // // It is up to the application to determine when and how a link should be
  // activated.
  // // In this snippet links are activated on mouse down when the control key
  // is held down
  // if ((event.stateMask & SWT.MOD1) != 0) {
  // int offset = styledText.getOffsetAtPoint(new Point (event.x, event.y));
  // if (offset != -1) {
  // StyleRange style1 = null;
  // try {
  // style1 = styledText.getStyleRangeAtOffset(offset);
  // } catch (IllegalArgumentException e) {
  // // no character under event.x, event.y
  // }
  // if (style1 != null && style1.underline && style1.underlineStyle ==
  // SWT.UNDERLINE_LINK) {
  // System.out.println("Click on a Link");
  // }
  // }
  // }
  // });
  // }

}
