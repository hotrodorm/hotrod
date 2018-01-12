package org.hotrod.eclipseplugin;

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;

public class FileSystemChangesListener implements IResourceChangeListener {

  private FileChangeListener listener = null;

  public FileSystemChangesListener(final FileChangeListener listener) {
    this.listener = listener;
  }

  @Override
  public void resourceChanged(final IResourceChangeEvent event) {
    if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
      printFileChanges(event.getDelta());
    }
  }

  private void printFileChanges(final IResourceDelta delta) {
    System.out.println("*** kind=" + renderDeltaKind(delta.getKind()) + " type="
        + renderResourceType(delta.getResource().getType()) + " r=" + delta.getResource().getLocation());
    int kind = delta.getKind();
    if (kind == IResourceDelta.ADDED || kind == IResourceDelta.REMOVED || kind == IResourceDelta.CHANGED) {
      IResource r = delta.getResource();
      if (r.getType() == IResource.FILE) {
        switch (kind) {
        case IResourceDelta.ADDED:
          this.listener.informFileAdded(r.getLocation().toFile());
          break;
        case IResourceDelta.REMOVED:
          this.listener.informFileRemoved(r.getLocation().toFile());
          break;
        case IResourceDelta.CHANGED:
          this.listener.informFileChanged(r.getLocation().toFile());
          break;
        }
      }
      for (IResourceDelta d : delta.getAffectedChildren()) {
        printFileChanges(d);
      }
    }
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

    void informFileAdded(File f);

    void informFileRemoved(File f);

    void informFileChanged(File f);
  }

}
