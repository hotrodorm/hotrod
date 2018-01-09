package org.hotrod.eclipseplugin;

import java.util.ArrayList;
import java.util.List;

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

public class HotRodView extends ViewPart {

  private static final String GEN_ALL_ICON_PATH = "icons/gen-all6-16.png";
  private static final String AUTO_GEN_ON_ICON_PATH = "icons/auto-on1-16.png";
  private static final String AUTO_GEN_OFF_ICON_PATH = "icons/auto-off1-16.png";
  private static final String GEN_CHANGES_ICON_PATH = "icons/gen-chg1-16.png";
  private static final String GEN_SELECTION_ICON_PATH = "icons/gen-sel1-16.png";
  private static final String JDBC_PROPERTIES_ICON_PATH = "icons/jdbc2-16.png";

  /**
   * The ID of the view as specified by the extension. // public static final
   * String ID = "plugin001view3activator.views.HotRodView";
   */

  private TreeViewer viewer;
  private Action doubleClickAction;

  private Action actionGenerateAll;
  private Action actionGenerateChanges;
  private Action actionGenerateSelection;
  private Action actionAutoOnOff;
  private Action actionRemoveFile;
  private Action actionRemoveAllFiles;
  private Action actionJDBCProperties;

  private boolean autoGenerate = false;
  private ImageDescriptor autoOn;
  private ImageDescriptor autoOff;
  private ImageDescriptor generateAll;
  private ImageDescriptor generateChanges;
  private ImageDescriptor generateSelection;
  private ImageDescriptor JDBCProperties;

  private IMenuManager contextMenu = null;

  private HotRodViewContentProvider hotRodViewContentProvider;

  // Constructor

  public HotRodView() {
    this.autoOn = Activator.getImageDescriptor(AUTO_GEN_ON_ICON_PATH);
    this.autoOff = Activator.getImageDescriptor(AUTO_GEN_OFF_ICON_PATH);
    this.generateAll = Activator.getImageDescriptor(GEN_ALL_ICON_PATH);
    this.generateChanges = Activator.getImageDescriptor(GEN_CHANGES_ICON_PATH);
    this.generateSelection = Activator.getImageDescriptor(GEN_SELECTION_ICON_PATH);
    this.JDBCProperties = Activator.getImageDescriptor(JDBC_PROPERTIES_ICON_PATH);
  }

  public void createPartControl(final Composite parent) {
    this.viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

    List<String> files = new ArrayList<String>();
    files.add("config/hotrod/hotrod-1.xml");
    files.add("config/hotrod/hotrod-2.xml");

    this.hotRodViewContentProvider = new HotRodViewContentProvider(this, files);
    this.viewer.setContentProvider(this.hotRodViewContentProvider);

    this.viewer.setInput(getViewSite());
    this.viewer.setLabelProvider(new HotRodLabelProvider());

    getSite().setSelectionProvider(this.viewer);

    makeActions();
    configureToolBar();
    configureContextMenu();
    configureDoubleClickAction();

    // TreeMouseListener mouseListener = new TreeMouseListener(this.viewer);
    // viewer.getTree().addMouseTrackListener(mouseListener);

    this.hotRodViewContentProvider.setRefresh(true);
  }

  private void configureToolBar() {
    IActionBars bars = getViewSite().getActionBars();

    // Tool bar

    IToolBarManager toolBar = bars.getToolBarManager();
    toolBar.add(actionGenerateAll);
    toolBar.add(actionGenerateChanges);
    toolBar.add(actionGenerateSelection);
    toolBar.add(actionAutoOnOff);
    toolBar.add(new Separator());
    toolBar.add(actionRemoveFile);
    toolBar.add(actionRemoveAllFiles);
    toolBar.add(new Separator());
    toolBar.add(actionJDBCProperties);

    // This is the Down Arrow at the end of the tool bar

    IMenuManager menu = bars.getMenuManager();
    menu.add(actionGenerateAll);
    menu.add(actionGenerateChanges);
    menu.add(actionGenerateSelection);
    menu.add(actionAutoOnOff);
    menu.add(new Separator());
    menu.add(actionRemoveFile);
    menu.add(actionRemoveAllFiles);
    menu.add(new Separator());
    menu.add(actionJDBCProperties);

  }

  // This is the right-click context menu

  private void configureContextMenu() {
    MenuManager menuMgr = new MenuManager("#PopupMenu");
    menuMgr.setRemoveAllWhenShown(true);
    menuMgr.addMenuListener(new IMenuListener() {
      public void menuAboutToShow(IMenuManager manager) {
        HotRodView.this.fillContextMenu(manager);
      }
    });
    Menu menu = menuMgr.createContextMenu(viewer.getControl());
    this.viewer.getControl().setMenu(menu);
    getSite().registerContextMenu(menuMgr, viewer);
  }

  private void fillContextMenu(final IMenuManager contextMenu) {
    contextMenu.add(actionGenerateAll);
    contextMenu.add(actionGenerateChanges);
    contextMenu.add(actionGenerateSelection);
    contextMenu.add(actionAutoOnOff);
    contextMenu.add(new Separator());
    contextMenu.add(actionRemoveFile);
    contextMenu.add(actionRemoveAllFiles);
    contextMenu.add(new Separator());
    contextMenu.add(actionJDBCProperties);
    this.contextMenu = contextMenu;
  }

  private void configureDoubleClickAction() {
    viewer.addDoubleClickListener(new IDoubleClickListener() {
      public void doubleClick(DoubleClickEvent event) {
        doubleClickAction.run();
      }
    });
  }

  // Utilities

  private void makeActions() {

    // Generate All

    actionGenerateAll = new Action() {
      public void run() {
        showMessage("Generate All - executed");
      }
    };
    actionGenerateAll.setText("Generate All");
    actionGenerateAll.setToolTipText("Generate All");
    actionGenerateAll.setImageDescriptor(this.generateAll);

    // Generate Changes

    actionGenerateChanges = new Action() {
      public void run() {
        // showMessage("Generate Changes - executed");
        System.out.println("*** Adding a table... (not implemented anymore)");
        // hotRodViewContentProvider.getMainConfigs().get(0).addChild(new
        // TableElement("new_table", true));

        // p1.addChild(new TableDAO("new-table..."));
        // to1.name = "> " + to1.name;
        // System.out.println("*** table added.");
      }
    };
    actionGenerateChanges.setText("Generate Changes");
    actionGenerateChanges.setToolTipText("Generate Changes");
    actionGenerateChanges.setImageDescriptor(this.generateChanges);

    // Generate Selection

    actionGenerateSelection = new Action() {
      public void run() {
        showMessage("Generate Selection - executed");
      }
    };
    actionGenerateSelection.setText("Generate Selection");
    actionGenerateSelection.setToolTipText("Generate Selection");
    actionGenerateSelection.setImageDescriptor(this.generateSelection);

    // Auto On/Off

    actionAutoOnOff = new Action() {
      public void run() {
        autoGenerate = !autoGenerate;
        String text = "Auto Generate (" + (autoGenerate ? "On" : "Off") + ")";
        actionAutoOnOff.setText(text);
        actionAutoOnOff.setChecked(autoGenerate);
        actionAutoOnOff.setToolTipText(text);
        actionAutoOnOff.setImageDescriptor(autoGenerate ? autoOn : autoOff);

        actionGenerateChanges.setEnabled(!autoGenerate);
        actionGenerateSelection.setEnabled(!autoGenerate);

        if (contextMenu != null) {
          contextMenu.update();
        }
      }
    };
    String text = "Auto Generate (" + (autoGenerate ? "On" : "Off") + ")";
    actionAutoOnOff.setText(text);
    actionAutoOnOff.setChecked(autoGenerate);
    actionAutoOnOff.setToolTipText(text);
    actionAutoOnOff.setImageDescriptor(autoGenerate ? autoOn : autoOff);

    // Remove File

    actionRemoveFile = new Action() {
      public void run() {
        showMessage("Remove File - executed");
      }
    };
    actionRemoveFile.setText("Remove File");
    actionRemoveFile.setToolTipText("Remove File");
    actionRemoveFile.setImageDescriptor(
        PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_REMOVE));

    // Remove All Files

    actionRemoveAllFiles = new Action() {
      public void run() {
        showMessage("Remove All Files - executed");
      }
    };
    actionRemoveAllFiles.setText("Remove All Files");
    actionRemoveAllFiles.setToolTipText("Remove All Files");
    actionRemoveAllFiles.setImageDescriptor(
        PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_REMOVEALL));

    // Configure JDBC Properties

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
