package org.hotrod.eclipseplugin.treeview;

import org.apache.log4j.Logger;
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
import org.hotrod.eclipseplugin.ErrorMessage;
import org.hotrod.runtime.dynamicsql.SourceLocation;

public class HotRodLabelProvider extends StyledCellLabelProvider {

  private static final Logger log = Logger.getLogger(HotRodLabelProvider.class);

  @SuppressWarnings("unused")
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

  // Tool tip

  @Override
  public String getToolTipText(final Object element) {
    log.debug("element=" + element + " (" + (element == null ? "<null>" : element.getClass().getName()) + ")");
    try {
      AbstractFace face = (AbstractFace) element;
      ErrorMessage errorMessage = face.getErrorMessage();
      if (errorMessage == null) {
        return null; // no tool tip
      } else {
        SourceLocation loc = errorMessage.getLocation();
        return errorMessage.getMessage() + (loc == null ? ""
            : ("\n" + "  at " + loc.getFile().getName() + " (line " + loc.getLineNumber() + ", col "
                + loc.getColumnNumber() + ") [click to source]"));
      }
    } catch (ClassCastException e) {
      return null; // no tool tip
    }
  }

  @Override
  public Point getToolTipShift(final Object object) {
    return new Point(5, 5);
  }

  @Override
  public int getToolTipDisplayDelayTime(final Object object) {
    return 100; // almost immediately
  }

  @Override
  public int getToolTipTimeDisplayed(final Object object) {
    return 120000; // stays visible for 2 minutes
  }

  // Label

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
      switch (face.getStatus()) {
      case UP_TO_DATE:
        prefix = "";
        break;
      case MODIFIED:
        prefix = "* ";
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

      prefix = (face.hasBranchChanges() ? ">" : "") + " " + prefix;
      // prefix = (face.hasBranchChanges() ? ">" : "") + " ";

      // status prefix

      if (prefix != null) {
        label.append(prefix, StyledString.DECORATIONS_STYLER);
      }

      // name

      if (face != null) {
        label.append(face.getName());
      }

      // suffix

      try {
        @SuppressWarnings("unused")
        SettingsFace s = (SettingsFace) obj; // settings
        // no sufix
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
              @SuppressWarnings("unused")
              AbstractMethodFace m = (AbstractMethodFace) obj; // daos & methods
              label.append("()");
            } catch (ClassCastException e4) { // any other face
              try {
                @SuppressWarnings("unused")
                ErrorMessage m = (ErrorMessage) obj; // error message
              } catch (ClassCastException e5) { // any other face
              }
            }
          }
        }
      }

      // generated mark

      if (face != null && face.getTag() != null && face.getTag().isGenerationComplete()) {
        // label.append(" \u2796", StyledString.COUNTER_STYLER);
        label.append(" [g]", StyledString.COUNTER_STYLER);
      }

      // type

      try {
        @SuppressWarnings("unused")
        SettingsFace s = (SettingsFace) obj; // settings
      } catch (ClassCastException e1) {
        try {
          @SuppressWarnings("unused")
          MainConfigFace c = (MainConfigFace) obj; // main config
        } catch (ClassCastException e2) {
          try {
            @SuppressWarnings("unused")
            FragmentConfigFace f = (FragmentConfigFace) obj; // fragment
          } catch (ClassCastException e3) {
            try {
              AbstractMethodFace m = (AbstractMethodFace) obj; // daos & methods
              label.append(" " + m.getDecoration(), StyledString.DECORATIONS_STYLER);
            } catch (ClassCastException e4) { // any other face
              try {
                @SuppressWarnings("unused")
                ErrorMessage m = (ErrorMessage) obj; // error message
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
  // log.debug("Click on a Link");
  // }
  // }
  // }
  // });
  // }

}
