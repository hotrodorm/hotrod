package org.hotrod.eclipseplugin;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.hotrod.eclipseplugin.elements.HotRodLabelProvider;
import org.hotrod.eclipseplugin.elements.HotRodViewContentProvider;
import org.hotrod.eclipseplugin.elements.TableElement;

public class HotRodView extends ViewPart {

  private static final String REGEN_ALL_ICON_PATH = "icons/regen-all3-16.png";
  private static final String AUTO_GEN_ON_ICON_PATH = "icons/auto-on1-16.png";
  private static final String AUTO_GEN_OFF_ICON_PATH = "icons/auto-off1-16.png";
  private static final String REGEN_CHANGES_ICON_PATH = "icons/regen-chg1-16.png";
  private static final String JDBC_PROPERTIES_ICON_PATH = "icons/jdbc2-16.png";

  /**
   * The ID of the view as specified by the extension.
   */
  public static final String ID = "plugin001view3activator.views.HotRodView";

  private TreeViewer viewer;
  private Action doubleClickAction;

  private Action actionRegenerateAll;
  private Action actionAutoOnOff;
  private Action actionRegenerateChanges;
  private Action actionRemoveFile;
  private Action actionRemoveAllFiles;
  private Action actionJDBCProperties;

  private ImageDescriptor regenerateAll;
  private boolean autoRegenerate = false;
  private ImageDescriptor autoOn;
  private ImageDescriptor autoOff;
  private ImageDescriptor regenerateChanges;
  private ImageDescriptor JDBCProperties;

  private IMenuManager menuManager = null;

  private HotRodViewContentProvider hotRodViewContentProvider;

  // Constructor

  public HotRodView() {
    this.regenerateAll = Activator.getImageDescriptor(REGEN_ALL_ICON_PATH);
    this.autoOn = Activator.getImageDescriptor(AUTO_GEN_ON_ICON_PATH);
    this.autoOff = Activator.getImageDescriptor(AUTO_GEN_OFF_ICON_PATH);
    this.regenerateChanges = Activator.getImageDescriptor(REGEN_CHANGES_ICON_PATH);
    this.JDBCProperties = Activator.getImageDescriptor(JDBC_PROPERTIES_ICON_PATH);

  }

  /**
   * This is a callback that will allow us to create the viewer and initialize
   * it.
   */
  public void createPartControl(final Composite parent) {
    viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

    this.hotRodViewContentProvider = new HotRodViewContentProvider(this);
    viewer.setContentProvider(this.hotRodViewContentProvider);

    viewer.setInput(getViewSite());
    viewer.setLabelProvider(new HotRodLabelProvider());

    // Create the help context id for the viewer's control
    PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "plugin001-view3-activator.viewer");
    getSite().setSelectionProvider(this.viewer);
    makeActions();
    hookContextMenu();
    hookDoubleClickAction();
    contributeToActionBars();

    // TreeMouseListener mouseListener = new TreeMouseListener(this.viewer);
    // viewer.getTree().addMouseTrackListener(mouseListener);

    this.hotRodViewContentProvider.setRefresh(true);
  }

  private void hookContextMenu() {
    MenuManager menuMgr = new MenuManager("#PopupMenu");
    menuMgr.setRemoveAllWhenShown(true);
    menuMgr.addMenuListener(new IMenuListener() {
      public void menuAboutToShow(IMenuManager manager) {
        HotRodView.this.fillContextMenu(manager);
      }
    });
    Menu menu = menuMgr.createContextMenu(viewer.getControl());
    viewer.getControl().setMenu(menu);
    getSite().registerContextMenu(menuMgr, viewer);
  }

  private void contributeToActionBars() {
    IActionBars bars = getViewSite().getActionBars();
    fillLocalPullDown(bars.getMenuManager());
    fillLocalToolBar(bars.getToolBarManager());
  }

  // This is the Down Arrow at the end of the tool bar
  private void fillLocalPullDown(IMenuManager manager) {
    manager.add(actionRegenerateAll);
    manager.add(actionRegenerateChanges);
    manager.add(actionAutoOnOff);
    manager.add(new Separator());
    manager.add(actionRemoveFile);
    manager.add(actionRemoveAllFiles);
    manager.add(new Separator());
    manager.add(actionJDBCProperties);
  }

  // This is the right-click context menu
  private void fillContextMenu(IMenuManager manager) {
    manager.add(actionRegenerateAll);
    manager.add(actionRegenerateChanges);
    manager.add(actionAutoOnOff);
    manager.add(new Separator());
    manager.add(actionRemoveFile);
    manager.add(actionRemoveAllFiles);
    manager.add(new Separator());
    manager.add(actionJDBCProperties);
    this.menuManager = manager;
  }

  // This is the tool bar
  private void fillLocalToolBar(IToolBarManager manager) {
    manager.add(actionRegenerateAll);
    manager.add(actionRegenerateChanges);
    manager.add(actionAutoOnOff);
    manager.add(new Separator());
    manager.add(actionRemoveFile);
    manager.add(actionRemoveAllFiles);
    manager.add(new Separator());
    manager.add(actionJDBCProperties);
  }

  private void makeActions() {

    actionRegenerateAll = new Action() {
      public void run() {
        showMessage("Regenerate All - executed");
      }
    };
    actionRegenerateAll.setText("Regenerate All");
    actionRegenerateAll.setToolTipText("Regenerate All");
    actionRegenerateAll.setImageDescriptor(this.regenerateAll);

    actionRegenerateChanges = new Action() {
      public void run() {
        // showMessage("Regenerated Changes - executed");
        System.out.println("*** Adding a table...");
        hotRodViewContentProvider.getMainConfigs().get(0).addChild(new TableElement("new_table", true));

        // p1.addChild(new TableDAO("new-table..."));
        // to1.name = "> " + to1.name;
        System.out.println("*** table added.");
      }
    };
    actionRegenerateChanges.setText("Regenerate Changes");
    actionRegenerateChanges.setToolTipText("Regenerate Changes");
    actionRegenerateChanges.setImageDescriptor(this.regenerateChanges);

    actionAutoOnOff = new Action() {
      public void run() {
        autoRegenerate = !autoRegenerate;
        String text = "Auto Regenerate (" + (autoRegenerate ? "On" : "Off") + ")";
        actionAutoOnOff.setText(text);
        actionAutoOnOff.setChecked(autoRegenerate);
        actionAutoOnOff.setToolTipText(text);
        actionAutoOnOff.setImageDescriptor(autoRegenerate ? autoOn : autoOff);
        actionRegenerateChanges.setEnabled(!autoRegenerate);
        if (menuManager != null) {
          menuManager.update();
        }
      }
    };
    String text = "Auto Regenerate (" + (autoRegenerate ? "On" : "Off") + ")";
    actionAutoOnOff.setText(text);
    actionAutoOnOff.setChecked(autoRegenerate);
    actionAutoOnOff.setToolTipText(text);
    actionAutoOnOff.setImageDescriptor(autoRegenerate ? autoOn : autoOff);

    actionRemoveFile = new Action() {
      public void run() {
        showMessage("Remove File - executed");
      }
    };
    actionRemoveFile.setText("Remove File");
    actionRemoveFile.setToolTipText("Remove File");
    actionRemoveFile.setImageDescriptor(
        PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_REMOVE));

    actionRemoveAllFiles = new Action() {
      public void run() {
        showMessage("Remove All Files - executed");
      }
    };
    actionRemoveAllFiles.setText("Remove All Files");
    actionRemoveAllFiles.setToolTipText("Remove All Files");
    actionRemoveAllFiles.setImageDescriptor(
        PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_REMOVEALL));

    actionJDBCProperties = new Action() {
      public void run() {
        // to1.name = "customer (refreshed)";
        showMessage("JDBC Properties - executed");
      }
    };
    actionJDBCProperties.setText("JDBC Properties ");
    actionJDBCProperties.setToolTipText("JDBC Properties");
    actionJDBCProperties.setImageDescriptor(this.JDBCProperties);

    doubleClickAction = new Action() {
      public void run() {
        ISelection selection = viewer.getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        showMessage("Double-click detected on " + obj.toString());
      }
    };

  }

  private void hookDoubleClickAction() {
    viewer.addDoubleClickListener(new IDoubleClickListener() {
      public void doubleClick(DoubleClickEvent event) {
        doubleClickAction.run();
      }
    });
  }

  private void showMessage(String message) {
    MessageDialog.openInformation(viewer.getControl().getShell(), "Sample View 2", message);
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus() {
    viewer.getControl().setFocus();
  }
}
