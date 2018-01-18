package org.hotrod.eclipseplugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.hotrod.eclipseplugin.jdbc.ConnectToDatabaseWizard;
import org.hotrod.eclipseplugin.jdbc.JDBCPropertiesDialog;
import org.hotrod.eclipseplugin.jdbc.NavigationAwareWizardDialog;
import org.hotrod.eclipseplugin.treeview.AbstractFace;
import org.hotrod.eclipseplugin.treeview.ErrorMessageFace;
import org.hotrod.eclipseplugin.treeview.HotRodLabelProvider;
import org.hotrod.eclipseplugin.treeview.HotRodViewContentProvider;
import org.hotrod.eclipseplugin.treeview.MainConfigFace;

public class HotRodView extends ViewPart {

  private static final String GEN_ALL_ICON_PATH = "icons/gen-all6-16.png";
  private static final String AUTO_GEN_ON_ICON_PATH = "icons/auto-on1-16.png";
  private static final String AUTO_GEN_OFF_ICON_PATH = "icons/auto-off1-16.png";
  private static final String GEN_CHANGES_ICON_PATH = "icons/gen-chg1-16.png";
  private static final String GEN_SELECTION_ICON_PATH = "icons/gen-sel1-16.png";
  private static final String CONNECT_TO_DATABASE_ICON_PATH = "icons/connect-to-database2-16.png";

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
  private Action actionConnectToDatabase;

  private boolean autoGenerate = false;
  private ImageDescriptor autoOn;
  private ImageDescriptor autoOff;
  private ImageDescriptor generateAll;
  private ImageDescriptor generateChanges;
  private ImageDescriptor generateSelection;
  private ImageDescriptor connectToDatabase;

  private IMenuManager contextMenu = null;

  private HotRodViewContentProvider hotRodViewContentProvider;

  private IResourceChangeListener fileSystemListener;

  // Constructor

  public HotRodView() {
    this.autoOn = Activator.getImageDescriptor(AUTO_GEN_ON_ICON_PATH);
    this.autoOff = Activator.getImageDescriptor(AUTO_GEN_OFF_ICON_PATH);
    this.generateAll = Activator.getImageDescriptor(GEN_ALL_ICON_PATH);
    this.generateChanges = Activator.getImageDescriptor(GEN_CHANGES_ICON_PATH);
    this.generateSelection = Activator.getImageDescriptor(GEN_SELECTION_ICON_PATH);
    this.connectToDatabase = Activator.getImageDescriptor(CONNECT_TO_DATABASE_ICON_PATH);
  }

  @Override
  public void createPartControl(final Composite parent) {
    this.viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

    this.hotRodViewContentProvider = new HotRodViewContentProvider(this);

    int operations = DND.DROP_COPY | DND.DROP_MOVE;
    Transfer[] transfers = new Transfer[] { FileTransfer.getInstance() };
    HotRodDropTargetListener listener = new HotRodDropTargetListener(this.viewer,
        this.hotRodViewContentProvider.getFiles());
    this.viewer.addDropSupport(operations, transfers, listener);

    this.viewer.setContentProvider(this.hotRodViewContentProvider);

    this.viewer.setInput(getViewSite());
    this.viewer.setLabelProvider(new HotRodLabelProvider(parent));

    getSite().setSelectionProvider(this.viewer);

    makeActions();
    configureToolBar();
    configureContextMenu();
    configureDoubleClickAction();

    // TreeMouseListener mouseListener = new TreeMouseListener(this.viewer);
    // viewer.getTree().addMouseTrackListener(mouseListener);

    // this.fileSystemListener = new FileSystemChangesTestListener();
    this.fileSystemListener = new FileSystemChangesListener(this.hotRodViewContentProvider,
        this.hotRodViewContentProvider.getFiles());
    ResourcesPlugin.getWorkspace().addResourceChangeListener(this.fileSystemListener);

    this.hotRodViewContentProvider.setVisible(true);

    // Test mouse down

    // this.viewer.addTreeListener(listener);
    //
    // this.viewer.addListener(SWT.MouseDown, new Listener() {
    // public void handleEvent(Event event) {
    // Point point = new Point(event.x, event.y);
    // TreeItem item = viewer.getItem(point);
    // if (item != null) {
    // System.out.println("Mouse down: " + item);
    // }
    // }
    // });

    MouseClickListener mouseListener = new MouseClickListener(this.viewer);
    this.viewer.getControl().addMouseListener(mouseListener);

  }

  interface LinkOpener {
    void openLink(Object rowObject);
  }

  private final class MouseClickListener extends MouseAdapter {

    private final TreeViewer myViewer;

    public MouseClickListener(final TreeViewer viewer) {
      myViewer = viewer;
    }

    @Override
    public void mouseDown(final MouseEvent e) {
      Point point = new Point(e.x, e.y);
      ViewerCell cell = myViewer.getCell(point);
      // if (cell != null && cell.getColumnIndex() == columnIndex) {
      // Rectangle rect = cell.getTextBounds();
      // rect.width = cell.getText().length() * charWidth;
      // if (rect.contains(point))
      // System.out.println("Cell="+cell);

      if (cell != null) {
        Object obj = cell.getElement();
        try {
          ErrorMessageFace em = (ErrorMessageFace) obj; // error message face
          System.out.println("THIS IS an error message!");

          // =================================================

          try {

            String filePath = em.getFile(); // "file path" ;

            // filePath = "project002/hotrod-6-bad.xml";
            filePath = "/home/valarcon/git/hotrod/proof-of-concept/hotrod-eclipse-plugin-poc001/test-workspace/project002/hotrod-7-bad.xml";

            // int lineNumber = em.getLineNumber();
            int lineNumber = 3;

            System.out.println("... opening: filePath=" + filePath + " line=" + lineNumber);

            IPath fromOSString = Path.fromOSString(filePath);
            IFile inputFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(fromOSString);
            System.out.println("... opening: inputFile=" + inputFile);

            if (inputFile != null) {

              // openAsTextFile(line, inputFile);
              openAsXMLFile(inputFile, lineNumber);

            }

          } catch (PartInitException e1) {
            // TODO Auto-generated catch block
            System.out.println("Could not open the text editor.");
            // e1.printStackTrace();
            // } catch (BadLocationException e1) {
            // System.out.println("Line does not exist.");
            // // TODO Auto-generated catch block
            // // e1.printStackTrace();
          } catch (CoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }

          // ==================================================

        } catch (ClassCastException e2) {
          System.out.println("Not a mainconfigfile.");
        }
      }
    }

    private void openAsTextFile(int line, IFile inputFile) throws PartInitException, BadLocationException {
      IWorkbenchPage page1 = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
      IEditorPart openEditor11 = IDE.openEditor(page1, inputFile);

      // org.eclipse.ui.texteditor.ITextEditor a;
      // org.eclipse.ui.texteditor.ITextEditor2 a2;
      // org.eclipse.ui.texteditor.ITextEditor3 a3;
      // org.eclipse.ui.texteditor.ITextEditor4 a4;
      // org.eclipse.ui.texteditor.ITextEditor5 a5;

      System.out.println("... opening: openEditor11=" + openEditor11);

      // org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart
      // ep =
      // (org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart)openEditor11;
      // ep.getEditorSite().getWorkbenchWindow()

      if (openEditor11 instanceof ITextEditor) {
        System.out.println("... opening: ITextEditor");
        ITextEditor textEditor = (ITextEditor) openEditor11;
        IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
        textEditor.selectAndReveal(document.getLineOffset(line - 1), document.getLineLength(line - 1));
        // } else if (openEditor11 instanceof MultiPageEditorPart) {
        // System.out.println("... opening: MultiPageEditorPart");
        // MultiPageEditorPart textEditor = (MultiPageEditorPart)
        // openEditor11;
        // textEditor.
        // IDocument document =
        // textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
        // textEditor.selectAndReveal(document.getLineOffset(line - 1),
        // document.getLineLength(line - 1));
      }

      // org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart
      // org.eclipse.ui.part.MultiPageEditorPart
    }

  }

  private void openAsXMLFile(final IFile ifile, final int lineNumber) throws CoreException {

    IWorkbench wb = PlatformUI.getWorkbench();
    IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
    IWorkbenchPage page = win.getActivePage();

    IMarker marker;
    marker = ifile.createMarker(IMarker.TEXT);
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put(IMarker.LINE_NUMBER, lineNumber);
    marker.setAttributes(map);
    IDE.openEditor(page, marker);
    marker.delete();
  }

  public void dispose() {
    ResourcesPlugin.getWorkspace().removeResourceChangeListener(this.fileSystemListener);
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
    toolBar.add(actionConnectToDatabase);

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
    menu.add(actionConnectToDatabase);

  }

  // This is the right-click context menu

  private void configureContextMenu() {
    MenuManager menuMgr = new MenuManager("#PopupMenu");
    menuMgr.setRemoveAllWhenShown(true);
    menuMgr.addMenuListener(new IMenuListener() {
      @Override
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
    contextMenu.add(actionConnectToDatabase);
    this.contextMenu = contextMenu;
  }

  private void configureDoubleClickAction() {
    viewer.addDoubleClickListener(new IDoubleClickListener() {
      @Override
      public void doubleClick(DoubleClickEvent event) {
        doubleClickAction.run();
      }
    });
  }

  // Utilities

  private void makeActions() {

    // Generate All

    actionGenerateAll = new Action() {
      @Override
      public void run() {
        showMessage("Generate All - executed");
      }
    };
    actionGenerateAll.setText("Generate All");
    actionGenerateAll.setToolTipText("Generate All");
    actionGenerateAll.setImageDescriptor(this.generateAll);

    // Generate Changes

    actionGenerateChanges = new Action() {
      @Override
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
      @Override
      public void run() {
        showMessage("Generate Selection - executed");
      }
    };
    actionGenerateSelection.setText("Generate Selection");
    actionGenerateSelection.setToolTipText("Generate Selection");
    actionGenerateSelection.setImageDescriptor(this.generateSelection);

    // Auto On/Off

    actionAutoOnOff = new Action() {
      @Override
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
      @Override
      public void run() {

        // System.out.println("--- Removing selection...");

        TreeSelection selection = (TreeSelection) viewer.getSelection();
        Set<MainConfigFace> mainConfigFaces = new HashSet<MainConfigFace>();
        for (Object obj : selection.toList()) {
          // System.out.println("SELECTION: obj=" + obj + (obj != null ? " (" +
          // obj.getClass().getName() + ")" : ""));
          AbstractFace face = (AbstractFace) obj;
          mainConfigFaces.add(face.getMainConfigFace());
        }
        for (MainConfigFace f : mainConfigFaces) {
          // System.out.println(" --> removing face: " + f.getAbsolutePath());
          f.remove();
        }
        hotRodViewContentProvider.refresh();

        // showMessage("Remove File - executed");
        // System.out.println("--- Removing selection COMPLETED");
      }
    };
    actionRemoveFile.setText("Remove File");
    actionRemoveFile.setToolTipText("Remove File");
    actionRemoveFile.setImageDescriptor(
        PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_REMOVE));

    // Remove All Files

    actionRemoveAllFiles = new Action() {
      @Override
      public void run() {
        // showMessage("Remove All Files - executed");
        hotRodViewContentProvider.getFiles().removeAll();
        hotRodViewContentProvider.refresh();
      }
    };
    actionRemoveAllFiles.setText("Remove All Files");
    actionRemoveAllFiles.setToolTipText("Remove All Files");
    actionRemoveAllFiles.setImageDescriptor(
        PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_REMOVEALL));

    // Configure JDBC Properties

    actionConnectToDatabase = new Action() {

      @Override
      public void run() {

        if (false) {
          IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("project002");
          String driverClassPath = "lib/jdbc-drivers/postgresql-9.4-1205.jdbc4.jar";
          String driverClassName = "org.postgresql.Driver";

          // try {
          // DynamicallyLoadedDriver driver = new
          // DynamicallyLoadedDriver(project, driverClassPath, driverClassName);
          // Connection conn =
          // driver.getConnection("jdbc:postgresql://192.168.56.46:5432/postgres",
          // "postgres",
          // "mypassword");
          // SQLRunner.run(conn);
          //
          // } catch (SQLException e) {
          // e.printStackTrace();
          // }
        }

        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

        if (false) {
          JDBCPropertiesDialog dialog = new JDBCPropertiesDialog(shell);
          dialog.create();
          if (dialog.open() == Window.OK) {
            System.out.println(dialog.getFirstName());
            System.out.println(dialog.getLastName());
          }
        }

        {
          TreeSelection selection = (TreeSelection) viewer.getSelection();
          if (selection.toList().isEmpty()) {
            showMessage("Please select a HotRod configuration file and then press Connect to database...");
          } else if (selection.toList().size() > 1) {
            showMessage("Please select only one HotRod configuration file and then press Connect to database...");
          } else {
            AbstractFace face = (AbstractFace) selection.toList().get(0);
            MainConfigFace mainConfigFace = face.getMainConfigFace();

            ConnectToDatabaseWizard wizard = new ConnectToDatabaseWizard(mainConfigFace,
                viewer.getControl().getShell());
            NavigationAwareWizardDialog dialog = new NavigationAwareWizardDialog(shell, wizard);
            dialog.open();

            // WizardDialog dialog = new WizardDialog(shell, wizard);
            // dialog.open();
            hotRodViewContentProvider.refresh();
          }
        }

        // showMessage("JDBC Properties - executed");
      }
    };
    actionConnectToDatabase.setText("Connect to database...");
    actionConnectToDatabase.setToolTipText("Connect to database...");
    actionConnectToDatabase.setImageDescriptor(this.connectToDatabase);

    doubleClickAction = new Action() {
      @Override
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
  @Override
  public void setFocus() {
    viewer.getControl().setFocus();
  }

}
