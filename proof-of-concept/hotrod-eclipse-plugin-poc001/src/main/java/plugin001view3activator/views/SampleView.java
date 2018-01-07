package plugin001view3activator.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
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
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import plugin001_view3_activator.Activator;
import plugin001view3activator.views.tree.HotRodLabelProvider;
import plugin001view3activator.views.tree.HotRodViewContentProvider;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class SampleView extends ViewPart {

  private static final String MAIN_CONFIG_ICON_PATH = "icons/main-config3-16.png";
  private static final String FRAGMENT_CONFIG_ICON_PATH = "icons/fragment1-16.png";
  private static final String TABLE_ICON_PATH = "icons/table2-16.png";
  private static final String VIEW_ICON_PATH = "icons/view4-16.png";
  private static final String DAO_ICON_PATH = "icons/dao3-16.png";

  private static final String REGEN_ALL_ICON_PATH = "icons/regen-all3-16.png";
  private static final String AUTO_GEN_ON_ICON_PATH = "icons/auto-on1-16.png";
  private static final String AUTO_GEN_OFF_ICON_PATH = "icons/auto-off1-16.png";
  private static final String REGEN_CHANGES_ICON_PATH = "icons/regen-chg1-16.png";
  private static final String JDBC_PROPERTIES_ICON_PATH = "icons/jdbc2-16.png";

  /**
   * The ID of the view as specified by the extension.
   */
  public static final String ID = "plugin001view3activator.views.SampleView";

  private TreeViewer viewer;
  private Action doubleClickAction;

  private Action actionRegenerateAll;
  private Action actionAutoOnOff;
  private Action actionRegenerateChanges;
  private Action actionRemoveFile;
  private Action actionRemoveAllFiles;
  private Action actionJDBCProperties;

  private Image mainConfigIcon;
  private Image fragmentConfigIcon;
  private Image tableIcon;
  private Image viewIcon;
  private Image daoIcon;

  private ImageDescriptor regenerateAll;
  private boolean autoRegenerate = false;
  private ImageDescriptor autoOn;
  private ImageDescriptor autoOff;
  private ImageDescriptor regenerateChanges;
  private ImageDescriptor JDBCProperties;

  private IMenuManager menuManager = null;

  private TreeObject to1;
  private ConfigFile p1;

  // Constructor

  public SampleView() {
    this.mainConfigIcon = getImage(MAIN_CONFIG_ICON_PATH);
    this.fragmentConfigIcon = getImage(FRAGMENT_CONFIG_ICON_PATH);
    this.tableIcon = getImage(TABLE_ICON_PATH);
    this.viewIcon = getImage(VIEW_ICON_PATH);
    this.daoIcon = getImage(DAO_ICON_PATH);

    this.regenerateAll = Activator.getImageDescriptor(REGEN_ALL_ICON_PATH);
    this.autoOn = Activator.getImageDescriptor(AUTO_GEN_ON_ICON_PATH);
    this.autoOff = Activator.getImageDescriptor(AUTO_GEN_OFF_ICON_PATH);
    this.regenerateChanges = Activator.getImageDescriptor(REGEN_CHANGES_ICON_PATH);
    this.JDBCProperties = Activator.getImageDescriptor(JDBC_PROPERTIES_ICON_PATH);

  }

  private Image getImage(final String imagePath) {
    ImageDescriptor imgDesc = Activator.getImageDescriptor(imagePath);
    return imgDesc != null ? imgDesc.createImage() : null;
  }

  // Tree objects

  abstract class TreeObject implements IAdaptable {

    private String name;
    private TreeObject parent;

    public TreeObject(String name) {
      this.name = name;
      this.parent = null;
    }

    public String getName() {
      return name;
    }

    public void setParent(TreeObject parent) {
      this.parent = parent;
    }

    public TreeObject getParent() {
      return parent;
    }

    public String toString() {
      return getName();
    }

    public <T> T getAdapter(Class<T> key) {
      return null;
    }

    public TreeObject findRoot() {
      if (this.parent == null) {
        return this;
      } else {
        return this.parent.findRoot();
      }
    }

  }

  class TableDAO extends TreeObject {

    public TableDAO(String name) {
      super(name);
    }

    // public void setName(final String name) {
    // this.name = name;
    // if (this.parent!= null) {
    // this.parent.
    // }
    // }

  }

  class ViewDAO extends TreeObject {

    public ViewDAO(String name) {
      super(name);
    }

  }

  class CustomDAO extends TreeObject {

    public CustomDAO(String name) {
      super(name);
    }

  }

  abstract class ConfigFile extends TreeObject {

    private List<TreeObject> children;
    private ViewContentProvider listener;
    private TreeViewer viewer;

    public ConfigFile(final String name) {
      super(name);
      this.children = new ArrayList<TreeObject>();
      this.listener = null;
      this.viewer = null;
    }

    public void addChild(final TreeObject child) {
      this.children.add(child);
      child.setParent(this);
      if (this.listener != null) {
        this.listener.add(child);
      } else {
        if (this.viewer != null) {
          this.viewer.refresh(this, true);
        }
      }
    }

    public void updateChild() {
      if (this.viewer != null) {
        this.viewer.refresh(this, true);
      }
    }

    public void removeChild(final TreeObject child) {
      this.children.remove(child);
      child.setParent(null);
      if (this.listener != null) {
        this.listener.remove(child);
      } else {
        if (this.viewer != null) {
          this.viewer.refresh(this, true);
        }
      }
    }

    public TreeObject[] getChildren() {
      return (TreeObject[]) this.children.toArray(new TreeObject[0]);
    }

    public boolean hasChildren() {
      return this.children.size() > 0;
    }

    public void removeListener(final ViewContentProvider listener) {
      this.listener = null;
    }

    public void addListener(final ViewContentProvider listener) {
      this.listener = listener;
    }

    public void setViewer(final TreeViewer viewer) {
      this.viewer = viewer;
      for (TreeObject to : this.children) {
        try {
          ConfigFile f = (ConfigFile) to;
          f.setViewer(viewer);
        } catch (ClassCastException e) {
          // not a config file -- skip.
        }
      }
    }

    public Viewer getViewer() {
      return viewer;
    }

  }

  class MainConfigFile extends ConfigFile {

    public MainConfigFile(final String name, final TreeViewer viewer) {
      super(name);
    }

  }

  class FragmentConfigFile extends ConfigFile {

    public FragmentConfigFile(String name) {
      super(name);
    }

  }

  class ViewContentProvider implements ITreeContentProvider {

    private ConfigFile invisibleRoot;
    protected TreeViewer viewer;

    @Override
    public Object[] getElements(final Object inputElement) {
      if (inputElement.equals(getViewSite())) {
        if (invisibleRoot == null) {
          initialize(this.viewer);
        }
        return getChildren(invisibleRoot);
      }
      return getChildren(inputElement);
    }

    @Override
    public Object getParent(final Object child) {
      if (child instanceof TreeObject) {
        return ((TreeObject) child).getParent();
      }
      return null;
    }

    @Override
    public boolean hasChildren(final Object parent) {
      if (parent instanceof ConfigFile)
        return ((ConfigFile) parent).hasChildren();
      return false;
    }

    @Override
    public Object[] getChildren(final Object parent) {
      if (parent instanceof ConfigFile) {
        return ((ConfigFile) parent).getChildren();
      }
      return new Object[0];
    }

    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
      this.viewer = (TreeViewer) viewer;
      System.out.println("oldInput: " + oldInput);
      if (oldInput != null) {
        if (oldInput instanceof ConfigFile) {
          removeListenerFrom((ConfigFile) oldInput);
        }
      }
      System.out.println("newInput: " + newInput);
      if (newInput != null) {
        System.out.println("  -> class=" + newInput.getClass().getName());
        // ViewSite vs = (ViewSite) newInput;
        if (newInput instanceof ConfigFile) {
          System.out.println("  -> listener to: specific configFile");
          addListenerTo((ConfigFile) newInput);
        }
      }
    }

    protected void removeListenerFrom(final ConfigFile file) {
      file.removeListener(this);
    }

    protected void addListenerTo(final ConfigFile file) {
      file.addListener(this);
    }

    public void add(final TreeObject child) {
      System.out.println("- add event.");
      System.out.println("  -> child=" + child);
      // Object movingBox = ((Model)event.receiver()).getParent();
      // viewer.refresh(movingBox, false);
      this.viewer.refresh();
    }

    public void remove(final TreeObject child) {
      System.out.println("- remove event.");
      System.out.println("  -> child=" + child);
      this.viewer.refresh();
    }

    /*
     * We will set up a dummy model to initialize tree heararchy. In a real
     * code, you will connect to a real model and expose its hierarchy.
     */
    private void initialize(final TreeViewer viewer) {
      FragmentConfigFile f1 = new FragmentConfigFile("hotrod-fragment-1.xml");
      f1.addChild(new TableDAO("product"));
      f1.addChild(new ViewDAO("hot_product"));
      f1.addChild(new CustomDAO("AccountingDAO"));

      FragmentConfigFile f2 = new FragmentConfigFile("hotrod-fragment-2.xml");
      f2.addChild(new TableDAO("client"));

      p1 = new MainConfigFile("> hotrod-1.xml", viewer);
      to1 = new TableDAO("customer");
      TreeObject to2 = new ViewDAO("supplier");
      TreeObject to3 = new CustomDAO("OrdersDAO");
      p1.addChild(to1);
      p1.addChild(to2);
      p1.addChild(to3);
      p1.addChild(f1);
      p1.addChild(f2);

      MainConfigFile p2 = new MainConfigFile("hotrod-2.xml", viewer);
      TreeObject to4 = new TableDAO("employee");
      p2.addChild(to4);

      invisibleRoot = new MainConfigFile("", viewer);
      invisibleRoot.addChild(p1);
      invisibleRoot.addChild(p2);

      invisibleRoot.setViewer(viewer);

    }
  }

  class ViewLabelProvider extends LabelProvider {

    public String getText(Object obj) {
      return obj.toString();
    }

    public Image getImage(Object obj) {

      if (obj instanceof MainConfigFile) {
        return mainConfigIcon;
      }
      if (obj instanceof FragmentConfigFile) {
        return fragmentConfigIcon;
      }
      if (obj instanceof TableDAO) {
        return tableIcon;
      }
      if (obj instanceof ViewDAO) {
        return viewIcon;
      }
      if (obj instanceof CustomDAO) {
        return daoIcon;
      }

      String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
      Image image = PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
      return image;

    }

  }

  /**
   * This is a callback that will allow us to create the viewer and initialize
   * it.
   */
  public void createPartControl(Composite parent) {
    viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

    // viewer.setContentProvider(new ViewContentProvider());
    viewer.setContentProvider(new HotRodViewContentProvider(this));

    viewer.setInput(getViewSite());
    // viewer.setLabelProvider(new ViewLabelProvider());
    viewer.setLabelProvider(new HotRodLabelProvider());

    // Create the help context id for the viewer's control
    PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "plugin001-view3-activator.viewer");
    getSite().setSelectionProvider(viewer);
    makeActions();
    hookContextMenu();
    hookDoubleClickAction();
    contributeToActionBars();
  }

  private void hookContextMenu() {
    MenuManager menuMgr = new MenuManager("#PopupMenu");
    menuMgr.setRemoveAllWhenShown(true);
    menuMgr.addMenuListener(new IMenuListener() {
      public void menuAboutToShow(IMenuManager manager) {
        SampleView.this.fillContextMenu(manager);
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
        p1.addChild(new TableDAO("new-table..."));
        to1.name = "> " + to1.name;
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
        to1.name = "customer (refreshed)";
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
