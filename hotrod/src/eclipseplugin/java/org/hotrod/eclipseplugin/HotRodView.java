package org.hotrod.eclipseplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.BuildException;
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
import org.hotrod.ant.Constants;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.HotRodAntTask.DisplayMode;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.eclipseplugin.ProjectProperties.FileProperties;
import org.hotrod.eclipseplugin.jdbc.DatabasePropertiesWizard;
import org.hotrod.eclipseplugin.jdbc.NavigationAwareWizardDialog;
import org.hotrod.eclipseplugin.treeview.AbstractFace;
import org.hotrod.eclipseplugin.treeview.ErrorMessageFace;
import org.hotrod.eclipseplugin.treeview.HotRodLabelProvider;
import org.hotrod.eclipseplugin.treeview.HotRodViewContentProvider;
import org.hotrod.eclipseplugin.treeview.MainConfigFace;
import org.hotrod.eclipseplugin.utils.EclipseFileGenerator;
import org.hotrod.eclipseplugin.utils.FUtil;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.LiveGenerator;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

public class HotRodView extends ViewPart {

  private static final String GEN_ALL_ICON_PATH = "eclipse-plugin/icons/gen-all6-16.png";
  private static final String AUTO_GEN_ON_ICON_PATH = "eclipse-plugin/icons/auto-on1-16.png";
  private static final String AUTO_GEN_OFF_ICON_PATH = "eclipse-plugin/icons/auto-off1-16.png";
  private static final String GEN_CHANGES_ICON_PATH = "eclipse-plugin/icons/gen-chg1-16.png";
  private static final String GEN_SELECTION_ICON_PATH = "eclipse-plugin/icons/gen-sel1-16.png";
  private static final String CONNECT_TO_DATABASE_ICON_PATH = "eclipse-plugin/icons/connect-to-database2-16.png";
  private static final String CONNECTED_ICON_PATH = "eclipse-plugin/icons/connected2-16.png";
  private static final String DISCONNECTED_ICON_PATH = "eclipse-plugin/icons/disconnected1-16.png";

  /**
   * The ID of the view as specified by the extension. // public static final
   * String ID = "plugin001view3activator.views.HotRodView";
   */

  private TreeViewer viewer;
  private Action doubleClickAction;

  private Action actionGenerateAll;
  private Action actionGenerateChanges;
  private Action actionGenerateSelected;
  private Action actionAutoOnOff;
  private Action actionRemoveFile;
  private Action actionRemoveAllFiles;
  private Action actionConnectToDatabase;
  private Action actionConfigureDatabaseConnection;

  private boolean autoGenerate = false;
  private boolean fileConnected = false;
  private ImageDescriptor autoOn;
  private ImageDescriptor autoOff;
  private ImageDescriptor generateAll;
  private ImageDescriptor generateChanges;
  private ImageDescriptor generateSelection;
  private ImageDescriptor configureDatabaseConnection;
  private ImageDescriptor connected;
  private ImageDescriptor disconnected;

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
    this.configureDatabaseConnection = Activator.getImageDescriptor(CONNECT_TO_DATABASE_ICON_PATH);
    this.connected = Activator.getImageDescriptor(CONNECTED_ICON_PATH);
    this.disconnected = Activator.getImageDescriptor(DISCONNECTED_ICON_PATH);
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
    // log("Mouse down: " + item);
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
      // log("Cell="+cell);

      if (cell != null) {
        Object obj = cell.getElement();

        try {
          ErrorMessageFace em = (ErrorMessageFace) obj; // error message face
          log("THIS IS an error message!");

          // =================================================

          try {
            String filePath = em.getLocation().getFile().getPath();
            int lineNumber = em.getLocation().getLineNumber();
            log("... opening: filePath=" + filePath + " line=" + lineNumber);

            IPath fromOSString = Path.fromOSString(filePath);
            IFile inputFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(fromOSString);
            log("... opening: inputFile=" + inputFile);

            if (inputFile != null) {

              // openAsTextFile(line, inputFile);
              openAsXMLFile(inputFile, lineNumber);

            }

          } catch (PartInitException e1) {
            // TODO Auto-generated catch block
            log("Could not open the text editor.");
            // e1.printStackTrace();
            // } catch (BadLocationException e1) {
            // log("Line does not exist.");
            // // TODO Auto-generated catch block
            // // e1.printStackTrace();
          } catch (CoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }

          // ==================================================

        } catch (ClassCastException e2) {
          // ignore
          // log("Not an error message face.");
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

      log("... opening: openEditor11=" + openEditor11);

      // org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart
      // ep =
      // (org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart)openEditor11;
      // ep.getEditorSite().getWorkbenchWindow()

      if (openEditor11 instanceof ITextEditor) {
        log("... opening: ITextEditor");
        ITextEditor textEditor = (ITextEditor) openEditor11;
        IDocument document = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
        textEditor.selectAndReveal(document.getLineOffset(line - 1), document.getLineLength(line - 1));
        // } else if (openEditor11 instanceof MultiPageEditorPart) {
        // log("... opening: MultiPageEditorPart");
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
    toolBar.add(actionGenerateSelected);
    toolBar.add(new Separator());
    toolBar.add(actionAutoOnOff);
    toolBar.add(actionConnectToDatabase);
    toolBar.add(actionConfigureDatabaseConnection);
    toolBar.add(new Separator());
    toolBar.add(actionRemoveFile);
    toolBar.add(actionRemoveAllFiles);

    // This is the Down Arrow at the end of the tool bar

    IMenuManager menu = bars.getMenuManager();
    menu.add(actionGenerateAll);
    menu.add(actionGenerateChanges);
    menu.add(actionGenerateSelected);
    menu.add(new Separator());
    menu.add(actionAutoOnOff);
    menu.add(actionConnectToDatabase);
    menu.add(actionConfigureDatabaseConnection);
    menu.add(new Separator());
    menu.add(actionRemoveFile);
    menu.add(actionRemoveAllFiles);

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
    contextMenu.add(actionGenerateSelected);
    contextMenu.add(new Separator());
    contextMenu.add(actionAutoOnOff);
    contextMenu.add(actionConnectToDatabase);
    contextMenu.add(actionConfigureDatabaseConnection);
    contextMenu.add(new Separator());
    contextMenu.add(actionRemoveFile);
    contextMenu.add(actionRemoveAllFiles);
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
        log("[generateall - starting]");

        // TODO: implement generate all

        TreeSelection selection = (TreeSelection) viewer.getSelection();
        if (selection.toList().isEmpty()) {
          showMessage("Please select a HotRod configuration file and then press Generate All...");
        } else if (selection.toList().size() > 1) {
          showMessage("Please select only one HotRod configuration file and then press Generate All...");
        } else {

          AbstractFace face = (AbstractFace) selection.toList().get(0);
          MainConfigFace mainConfigFace = face.getMainConfigFace();
          ProjectProperties projectProperties = ProjectPropertiesCache
              .getProjectProperties(mainConfigFace.getProject());
          if (projectProperties == null) {
            showMessage("Project properties are not yet configured");
          } else {
            log("file=" + mainConfigFace.getRelativeFileName());
            FileProperties fileProperties = projectProperties.getFileProperties(mainConfigFace.getRelativeFileName());
            if (fileProperties == null) {
              showMessage("File properties are not yet configured");
            } else {
              log("generating all - starting");
              generateAll(mainConfigFace.getConfig(), fileProperties, mainConfigFace.getProject());
              log("generating all - complete");
            }
          }

        }

        log("[generateall - finished]");
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
        log("*** Adding a table... (not implemented anymore)");
        // hotRodViewContentProvider.getMainConfigs().get(0).addChild(new
        // TableElement("new_table", true));

        // p1.addChild(new TableDAO("new-table..."));
        // to1.name = "> " + to1.name;
        // log("*** table added.");
      }
    };
    actionGenerateChanges.setText("Generate Changes");
    actionGenerateChanges.setToolTipText("Generate Changes");
    actionGenerateChanges.setImageDescriptor(this.generateChanges);

    // Generate Selection

    actionGenerateSelected = new Action() {
      @Override
      public void run() {
        showMessage("Generate Selected - executed");
      }
    };
    actionGenerateSelected.setText("Generate Selected");
    actionGenerateSelected.setToolTipText("Generate Selected");
    actionGenerateSelected.setImageDescriptor(this.generateSelection);

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
        actionGenerateSelected.setEnabled(!autoGenerate);

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

    // Connect to Database

    actionConnectToDatabase = new Action() {
      @Override
      public void run() {
        fileConnected = !fileConnected;
        String text = fileConnected ? "Disconnect from database" : "Connect to database";
        actionConnectToDatabase.setText(text);
        actionConnectToDatabase.setChecked(fileConnected);
        actionConnectToDatabase.setToolTipText(text);
        actionConnectToDatabase.setImageDescriptor(fileConnected ? connected : disconnected);

        // actionGenerateChanges.setEnabled(!autoGenerate);
        // actionGenerateSelection.setEnabled(!autoGenerate);

        if (contextMenu != null) {
          contextMenu.update();
        }
      }
    };
    String t1 = fileConnected ? "Disconnect from database" : "Connect to database";
    actionConnectToDatabase.setText(t1);
    actionConnectToDatabase.setChecked(fileConnected);
    actionConnectToDatabase.setToolTipText(t1);
    actionConnectToDatabase.setImageDescriptor(fileConnected ? connected : disconnected);

    // Configure Database Connection

    actionConfigureDatabaseConnection = new Action() {

      @Override
      public void run() {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        TreeSelection selection = (TreeSelection) viewer.getSelection();
        if (selection.toList().isEmpty()) {
          showMessage("Please select a HotRod configuration file and then press Configure database connection...");
        } else if (selection.toList().size() > 1) {
          showMessage(
              "Please select only one HotRod configuration file and then press Configure database connection...");
        } else {
          AbstractFace face = (AbstractFace) selection.toList().get(0);
          MainConfigFace mainConfigFace = face.getMainConfigFace();

          // try {
          // log("[X1] will load properties");
          ProjectProperties projectProperties =
              // ProjectProperties.load(mainConfigFace.getProject());
              ProjectPropertiesCache.getProjectProperties(mainConfigFace.getProject());

          // log("[X1] properties loaded");

          DatabasePropertiesWizard wizard = new DatabasePropertiesWizard(mainConfigFace, projectProperties,
              viewer.getControl().getShell());
          NavigationAwareWizardDialog dialog = new NavigationAwareWizardDialog(shell, wizard);
          dialog.open();
          hotRodViewContentProvider.refresh();

          // } catch (CouldNotLoadPropertiesException e) {
          // // log("[X1] Could not load properties");
          // MessageDialog.openError(shell, "Invalid HotRod Plugin's properties
          // file",
          // "Invalid HotRod Plugin's properties file: " +
          // ProjectProperties.CONFIG_FILE_NAME + "\n\nError: "
          // + e.getMessage()
          // + "\n\nPlease manually restore this file, or remove it to create a
          // new one from scratch.");
          // }

        }
      }

    };
    actionConfigureDatabaseConnection.setText("Configure database connection...");
    actionConfigureDatabaseConnection.setToolTipText("Configure database connection...");
    actionConfigureDatabaseConnection.setImageDescriptor(this.configureDatabaseConnection);

    // Remove File

    actionRemoveFile = new Action() {
      @Override
      public void run() {

        // log("--- Removing selection...");

        TreeSelection selection = (TreeSelection) viewer.getSelection();
        Set<MainConfigFace> mainConfigFaces = new HashSet<MainConfigFace>();
        for (Object obj : selection.toList()) {
          // log("SELECTION: obj=" + obj + (obj != null ? " (" +
          // obj.getClass().getName() + ")" : ""));
          AbstractFace face = (AbstractFace) obj;
          mainConfigFaces.add(face.getMainConfigFace());
        }
        for (MainConfigFace f : mainConfigFaces) {
          // log(" --> removing face: " + f.getAbsolutePath());
          f.remove();
        }
        hotRodViewContentProvider.refresh();

        // showMessage("Remove File - executed");
        // log("--- Removing selection COMPLETED");
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

    // Double click

    doubleClickAction = new Action() {
      @Override
      public void run() {
        ISelection selection = viewer.getSelection();
        Object obj = ((IStructuredSelection) selection).getFirstElement();
        showMessage("Double-click detected on " + obj.toString());
      }
    };

  }

  private void showMessage(final String message) {
    MessageDialog.openInformation(viewer.getControl().getShell(), "Sample View 2", message);
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  @Override
  public void setFocus() {
    viewer.getControl().setFocus();
  }

  // Generation

  private void generateAll(final HotRodConfigTag config, final FileProperties fileProperties, final IProject project) {

    File projectDir = project.getLocation().toFile();

    List<String> classPath = new ArrayList<String>();
    for (String p : fileProperties.getDriverClassPathEntries()) {
      if (FUtil.isAbsolute(new File(p))) {
        classPath.add(p);
      } else {
        File f = new File(projectDir, p);
        classPath.add(f.getAbsolutePath());
      }
    }

    DatabaseLocation loc = new DatabaseLocation(fileProperties.getDriverClassName(), fileProperties.getUrl(),
        fileProperties.getUsername(), fileProperties.getPassword(), fileProperties.getCatalog(),
        fileProperties.getSchema(), classPath);

    try {
      HotRodGenerator g = config.getGenerators().getSelectedGeneratorTag().getGenerator(loc, config, DisplayMode.LIST);
      log("Generator instantiated.");

      g.prepareGeneration();
      log("Generation prepared.");

      try {
        LiveGenerator liveGenerator = (LiveGenerator) g;

        // a live generator
        FileGenerator fg = new EclipseFileGenerator(project);
        liveGenerator.generate(fg);

      } catch (ClassCastException e) {
        // not a live generator
        g.generate();
      }

      log("Generation complete.");

    } catch (ControlledException e) {
      if (e.getLocation() == null) {
        throw new BuildException(Constants.TOOL_NAME + " could not generate the persistence code:\n" + e.getMessage());
      } else {
        throw new BuildException(
            Constants.TOOL_NAME + " could not generate the persistence code. Invalid configuration in "
                + e.getLocation().render() + ":\n" + e.getMessage());
      }
    } catch (UncontrolledException e) {
      e.printStackTrace();
      throw new BuildException(Constants.TOOL_NAME + " could not generate the persistence code.");
    } catch (Throwable t) {
      t.printStackTrace();
      throw new BuildException(Constants.TOOL_NAME + " could not generate the persistence code.");
    }

  }

  private static void log(final String txt) {
    System.out.println("[" + new Object() {
    }.getClass().getEnclosingClass().getName() + "] " + txt);
  }

}
