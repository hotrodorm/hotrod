package org.hotrod.eclipseplugin;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;

public class FileSystemChangesTestListener implements IResourceChangeListener {

  @Override
  public void resourceChanged(final IResourceChangeEvent event) {
    log("--- Something changed ---");
    if (event.getResource() != null) {
      log("Change detected on: " + event.getResource().getFullPath());
    } else {
      log("Other change detected: build-kind=" + event.getBuildKind() + " type=" + renderEventType(event.getType())
          + " source=" + event.getSource() + " delta=" + event.getDelta());
      if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
        IResourceDelta delta = event.getDelta();
        printDelta(delta, "");
      }
    }
  }

  private void printDelta(final IResourceDelta delta, final String indent) {
    log(indent + "delta kind=" + renderDeltaKind(delta.getKind()) + " flags=" + renderDeltaFlags(delta.getFlags())
        + " full-path=" + delta.getFullPath() + " rel-path=" + delta.getProjectRelativePath());

    IResource r = delta.getResource();
    log(indent + " -> r=" + r);
    log(indent + " -> type=" + renderResourceType(r.getType()));
    log(indent + " -> location=" + r.getLocation());
    log(indent + " -> name=" + r.getName());
    log(indent + " -> full-path=" + r.getFullPath());
    log(indent + " -> rel-path=" + r.getProjectRelativePath());
    for (IResourceDelta d : delta.getAffectedChildren()) {
      printDelta(d, indent + ". ");
    }
  }

  private String renderEventType(final int type) {
    switch (type) {
    case IResourceChangeEvent.POST_CHANGE:
      return "POST_CHANGE";
    case IResourceChangeEvent.POST_BUILD:
      return "POST_BUILD";
    case IResourceChangeEvent.PRE_BUILD:
      return "PRE_BUILD";
    case IResourceChangeEvent.PRE_CLOSE:
      return "PRE_CLOSE";
    case IResourceChangeEvent.PRE_DELETE:
      return "PRE_DELETE";
    case IResourceChangeEvent.PRE_REFRESH:
      return "PRE_REFRESH";
    default:
      return "(unknown event type " + type + ")";
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

  private String renderDeltaFlags(final int flags) {
    StringBuilder sb = new StringBuilder();

    if ((flags & IResourceDelta.CONTENT) != 0) {
      sb.append(" CONTENT");
    }
    if ((flags & IResourceDelta.DERIVED_CHANGED) != 0) {
      sb.append(" DERIVED_CHANGED");
    }
    if ((flags & IResourceDelta.ENCODING) != 0) {
      sb.append(" ENCODING");
    }
    if ((flags & IResourceDelta.DESCRIPTION) != 0) {
      sb.append(" DESCRIPTION");
    }
    if ((flags & IResourceDelta.OPEN) != 0) {
      sb.append(" OPEN");
    }
    if ((flags & IResourceDelta.TYPE) != 0) {
      sb.append(" TYPE");
    }
    if ((flags & IResourceDelta.SYNC) != 0) {
      sb.append(" SYNC");
    }
    if ((flags & IResourceDelta.MARKERS) != 0) {
      sb.append(" MARKERS");
    }
    if ((flags & IResourceDelta.REPLACED) != 0) {
      sb.append(" REPLACED");
    }
    if ((flags & IResourceDelta.LOCAL_CHANGED) != 0) {
      sb.append(" LOCAL_CHANGED");
    }
    if ((flags & IResourceDelta.MOVED_TO) != 0) {
      sb.append(" MOVED_TO");
    }
    if ((flags & IResourceDelta.MOVED_FROM) != 0) {
      sb.append(" MOVED_FROM");
    }
    if ((flags & IResourceDelta.COPIED_FROM) != 0) {
      sb.append(" COPIED_FROM");
    }

    return sb.toString();
  }

  private void log(final String txt) {
    // System.out.println(txt);
  }

}
