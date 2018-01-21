package org.hotrod.eclipseplugin;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.hotrod.eclipseplugin.treeview.HotRodViewContentProvider;

public class FileSystemChangesListener implements IResourceChangeListener {

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
      if (refresh) {
        this.provider.refresh();
      }
    }
  }

  private boolean processFileChanges(final IResourceDelta delta) {
    log("*** kind=" + renderDeltaKind(delta.getKind()) + " type=" + renderResourceType(delta.getResource().getType())
        + " r=" + delta.getResource().getLocation());
    int kind = delta.getKind();
    boolean refresh = false;
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

  private void log(final String txt) {
    // System.out.println(txt);
  }

}
