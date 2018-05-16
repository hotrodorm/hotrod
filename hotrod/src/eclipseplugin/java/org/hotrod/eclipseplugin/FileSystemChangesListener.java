package org.hotrod.eclipseplugin;

import java.io.File;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;

public class FileSystemChangesListener implements IResourceChangeListener {

  private static final Logger log = Logger.getLogger(FileSystemChangesListener.class);

  private HotRodViewContentProvider provider;
  private FileChangeListener listener;

  public FileSystemChangesListener(final HotRodViewContentProvider provider, final FileChangeListener listener) {
    this.provider = provider;
    this.listener = listener;
  }

  @Override
  public void resourceChanged(final IResourceChangeEvent event) {
    if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
      boolean refresh = processFileChanges(event.getDelta());
      log.debug("refresh=" + refresh);
      if (refresh) {
        this.provider.refresh();
      }
    }
  }

  private boolean processFileChanges(final IResourceDelta delta) {
    log.debug("*** kind=" + renderDeltaKind(delta.getKind()) + " type="
        + renderResourceType(delta.getResource().getType()) + " r=" + delta.getResource().getLocation());
    int kind = delta.getKind();
    int flags = delta.getFlags();
    boolean isMarkersOnlyChanges = flags == IResourceDelta.MARKERS;
    boolean refresh = false;
    if (!isMarkersOnlyChanges) { // Ignore markers-only changes
      if (kind == IResourceDelta.ADDED || kind == IResourceDelta.REMOVED || kind == IResourceDelta.CHANGED) {
        IResource r = delta.getResource();
        if (r.getType() == IResource.FILE) {
          File file = r.getLocation().toFile();
          switch (kind) {
          case IResourceDelta.ADDED:
            refresh = refresh | this.listener.informFileAdded(file);
            break;
          case IResourceDelta.REMOVED:
            refresh = refresh | this.listener.informFileRemoved(file);
            break;
          case IResourceDelta.CHANGED:
            refresh = refresh | this.listener.informFileChanged(file);
            break;
          }
        }
        for (IResourceDelta d : delta.getAffectedChildren()) {
          refresh = refresh | processFileChanges(d);
        }
      }
    }
    return refresh;
  }

  private String renderDeltaKind(final int kind) {
    switch (kind) {
    case IResourceDelta.ADDED:
      return "ADDED";
    case IResourceDelta.REMOVED:
      return "REMOVED";
    case IResourceDelta.CHANGED:
      return "CHANGED";
    case IResourceDelta.ADDED_PHANTOM:
      return "ADDED_PHANTOM";
    case IResourceDelta.REMOVED_PHANTOM:
      return "REMOVED_PHANTOM";
    default:
      return "(unknown delta kind " + kind + ")";
    }
  }

  private String renderResourceType(final int type) {
    switch (type) {
    case IResource.FILE:
      return "FILE";
    case IResource.FOLDER:
      return "FOLDER";
    case IResource.PROJECT:
      return "PROJECT";
    case IResource.ROOT:
      return "ROOT";
    default:
      return "(unknown resource type " + type + ")";
    }
  }

  public static interface FileChangeListener {

    boolean informFileAdded(File f);

    boolean informFileRemoved(File f);

    boolean informFileChanged(File f);

  }

}
