package org.hotrod.eclipseplugin;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.TreeItem;
import org.hotrod.eclipseplugin.treefaces.AbstractFace;

public class TreeMouseListener implements MouseTrackListener {

  private TreeViewer viewer;

  public TreeMouseListener(final TreeViewer viewer) {
    super();
    this.viewer = viewer;
  }

  @Override
  public void mouseEnter(final MouseEvent e) {
  }

  @Override
  public void mouseExit(final MouseEvent e) {
  }

  @Override
  public void mouseHover(final MouseEvent event) {

    TreeItem item = this.viewer.getTree().getItem(new Point(event.x, event.y));
    if (item != null && item.getData() instanceof AbstractFace) {
      AbstractFace element = (AbstractFace) item.getData();
      this.viewer.getTree().setToolTipText(element.getTooltip());
//      if (selectedElement.getResourceType() == MyResourceType.APP_GROUP
//          || selectedElement.getResourceType() == MyResourceType.DATA) {
//        if (selectedElement.getData() instanceof INameDescription)
//          this.viewer.getTree().setToolTipText(((INameDescription) selectedElement.getData()).getDescription());
//      } else {
//        this.viewer.getTree().setToolTipText(null);
//      }
    }
  }

}