package org.hotrod.eclipseplugin;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TransferData;

public class HotRodDropTargetListener extends ViewerDropAdapter {

  private static final Logger log = Logger.getLogger(HotRodDropTargetListener.class);

  private LoadedConfigurationFiles files;

  protected HotRodDropTargetListener(final Viewer viewer, final LoadedConfigurationFiles files) {
    super(viewer);
    this.files = files;
  }

  /*
   * Switches the cursor to copy mode (+ sign) even if the user does a default
   * drag and drop (move mode).
   */
  public void dragOver(final DropTargetEvent event) {
    event.detail = DND.DROP_COPY;
  }

  /*
   * The validateDrop() method is called when the mouse moves in the receiving
   * view over targets (or when the user changes the drop type with the modifier
   * keys), to check if the operation is allowed.
   */
  @Override
  public boolean validateDrop(final Object currentTarget, final int currentOperation, final TransferData type) {
    boolean allowed = FileTransfer.getInstance().isSupportedType(type);
    return allowed;
  }

  /*
   * performDrop() is called when the user lets go of the mouse button,
   * indicating that he/she wants the drop to occur.
   */
  @Override
  public boolean performDrop(final Object data) {
    log.info("dropping files.");
    if (data instanceof String[]) {
      String[] files = (String[]) data;
      for (String fullPathName : files) {
        File f = new File(fullPathName);
        log.info(" - dropping file f=" + f);
        this.files.addFile(f);
      }
    }
    return true;
  }

}
