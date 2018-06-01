package org.hotrod.eclipseplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.hotrod.ant.Constants;
import org.hotrod.ant.HotRodAntTask.DisplayMode;
import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.AbstractConfigurationTag.TagStatus;
import org.hotrod.config.AbstractHotRodConfigTag;
import org.hotrod.config.EnumTag;
import org.hotrod.config.GenerationUnit;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.eclipseplugin.ProjectProperties.CouldNotSaveProjectPropertiesException;
import org.hotrod.eclipseplugin.WorkspaceProperties.CouldNotSaveWorkspacePropertiesException;
import org.hotrod.eclipseplugin.jdbc.DatabasePropertiesWizard;
import org.hotrod.eclipseplugin.jdbc.NavigationAwareWizardDialog;
import org.hotrod.eclipseplugin.treefaces.AbstractFace;
import org.hotrod.eclipseplugin.treefaces.MainConfigFace;
import org.hotrod.eclipseplugin.utils.EclipseFileGenerator;
import org.hotrod.eclipseplugin.utils.FUtil;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.CachedMetadata;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.generator.LiveGenerator;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrod.runtime.util.ListWriter;
import org.hotrod.runtime.util.SUtils;
import org.hotrod.utils.EUtils;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;

public class HotRodView extends ViewPart {

  public static final String ICONS_DIR = "eclipse-plugin-resources/icons/";

  private static final Logger log = Logger.getLogger(HotRodView.class);

  private static final String GEN_ALL_ICON_PATH = ICONS_DIR + "button-gen-all.png";
  private static final String GEN_SELECTION_ICON_PATH = ICONS_DIR + "button-gen-sel.png";
  private static final String GEN_CHANGES_ICON_PATH = ICONS_DIR + "button-gen-chg.png";
  private static final String AUTO_GEN_ON_ICON_PATH = ICONS_DIR + "button-auto-on.png";
  private static final String AUTO_GEN_OFF_ICON_PATH = ICONS_DIR + "button-auto-off.png";
  private static final String COPY_ERROR_PATH = ICONS_DIR + "button-copy-error.png";
  private static final String CONNECT_TO_DATABASE_ICON_PATH = ICONS_DIR + "button-configure-database.png";

  private TreeViewer viewer;
  private Action doubleClickAction;

  private Action actionGenerateAll;
  private Action actionGenerateSelected;
  private Action actionGenerateChanges;
  private Action actionAutoOnOff;
  private Action actionCopyError;
  private Action actionConfigureDatabaseConnection;
  private Action actionRemoveFile;
  private Action actionRemoveAllFiles;

  private ImageDescriptor imgGenerateAll;
  private ImageDescriptor imgGenerateSelection;
  private ImageDescriptor imgGenerateChanges;
  private ImageDescriptor imgAutoOn;
  private ImageDescriptor imgAutoOff;
  private ImageDescriptor imgCopyError;
  private ImageDescriptor imgConfigureDatabaseConnection;

  private IMenuManager contextMenu = null;

  private HotRodViewContentProvider hotRodViewContentProvider;

  private IResourceChangeListener fileSystemListener;

  // Constructor

  public HotRodView() {
    this.imgGenerateAll = Activator.getImageDescriptor(GEN_ALL_ICON_PATH);
    this.imgGenerateSelection = Activator.getImageDescriptor(GEN_SELECTION_ICON_PATH);
    this.imgGenerateChanges = Activator.getImageDescriptor(GEN_CHANGES_ICON_PATH);
    this.imgAutoOn = Activator.getImageDescriptor(AUTO_GEN_ON_ICON_PATH);
    this.imgAutoOff = Activator.getImageDescriptor(AUTO_GEN_OFF_ICON_PATH);
    this.imgCopyError = Activator.getImageDescriptor(COPY_ERROR_PATH);
    this.imgConfigureDatabaseConnection = Activator.getImageDescriptor(CONNECT_TO_DATABASE_ICON_PATH);
  }

  @Override
  public void createPartControl(final Composite parent) {

    log.info("HotRod View starting...");

    this.viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

    ColumnViewerToolTipSupport.enableFor(this.viewer);

    this.hotRodViewContentProvider = new HotRodViewContentProvider(this);

    int operations = DND.DROP_COPY | DND.DROP_MOVE;
    Transfer[] transfers = new Transfer[] { FileTransfer.getInstance() };
    HotRodDropTargetListener dropListener = new HotRodDropTargetListener(this.viewer,
        this.hotRodViewContentProvider.getFiles());
    this.viewer.addDropSupport(operations, transfers, dropListener);

    getSite().setSelectionProvider(this.viewer);

    makeActions();
    configureToolBar();
    configureContextMenu();
    configureDoubleClickAction();

    // TreeMouseListener mouseListener = new TreeMouseListener(this.viewer);
    // viewer.getTree().addMouseTrackListener(mouseListener);

    // this.fileSystemListener = new FileSystemChangesTestListener();
    this.fileSystemListener = new FileSystemChangesListener(this, this.hotRodViewContentProvider,
        this.hotRodViewContentProvider.getFiles());
    ResourcesPlugin.getWorkspace().addResourceChangeListener(this.fileSystemListener);

    // Test mouse down

    // this.viewer.addTreeListener(listener);
    //
    // this.viewer.addListener(SWT.MouseDown, new Listener() {
    // public void handleEvent(Event event) {
    // Point point = new Point(event.x, event.y);
    // TreeItem item = viewer.getItem(point);
    // if (item != null) {
    // log.debug("Mouse down: " + item);
    // }
    // }
    // });

    MouseClickListener mouseListener = new MouseClickListener(this.viewer);
    this.viewer.getControl().addMouseListener(mouseListener);

    // Load files from previous session

    for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
      if (project.isOpen()) {
        ProjectProperties projectProperties = WorkspaceProperties.getInstance().getProjectProperties(project);
        for (String relativeFileName : projectProperties.getRelativeFileNames()) {
          File f = project.getLocation().append(relativeFileName).toFile();
          this.hotRodViewContentProvider.getFiles().addFile(f);
        }
      }
    }

    this.hotRodViewContentProvider.setVisible(true);
    this.viewer.setContentProvider(this.hotRodViewContentProvider);
    this.viewer.setInput(getViewSite());
    this.viewer.setLabelProvider(new HotRodLabelProvider(parent));

    // for (MainConfigFace face :
    // this.hotRodViewContentProvider.getFiles().getLoadedFiles()) {
    // face.refreshView();
    // }

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
    public void mouseDown(final MouseEvent event) {
      Point point = new Point(event.x, event.y);
      ViewerCell cell = myViewer.getCell(point);
      // if (cell != null && cell.getColumnIndex() == columnIndex) {
      // Rectangle rect = cell.getTextBounds();
      // rect.width = cell.getText().length() * charWidth;
      // if (rect.contains(point))
      // log.debug("Cell="+cell);

      if (cell != null) {
        Object obj = cell.getElement();

        try {
          AbstractFace face = (AbstractFace) obj;
          ErrorMessage em = (ErrorMessage) face.getBranchErrorMessage();
          if (em != null) {
            SourceLocation location = em.getLocation();
            if (location != null) {
              String filePath = em.getLocation().getFile().getPath();
              int lineNumber = em.getLocation().getLineNumber();
              IPath fromOSString = Path.fromOSString(filePath);
              IFile inputFile = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(fromOSString);
              if (inputFile != null) {
                try {
                  openAsXMLFile(inputFile, lineNumber);
                } catch (CoreException e1) {
                  log.error("Could not open configuration file '" + filePath + "'.", e1);
                }
              }

            }
          }
        } catch (ClassCastException e2) {
          // ignore
        }

      }
    }

  }

  private void openAsXMLFile(final IFile ifile, final int lineNumber) throws CoreException {
    IWorkbench wb = PlatformUI.getWorkbench();
    IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
    IWorkbenchPage page = win.getActivePage();

    IMarker marker = ifile.createMarker(IMarker.TEXT);
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put(IMarker.LINE_NUMBER, lineNumber);
    marker.setAttributes(map);
    IDE.openEditor(page, marker);
    marker.delete();

    IDE.openEditor(page, ifile);
  }

  public void dispose() {
    ResourcesPlugin.getWorkspace().removeResourceChangeListener(this.fileSystemListener);
  }

  private void configureToolBar() {
    IActionBars bars = getViewSite().getActionBars();

    // Tool bar

    IToolBarManager toolBar = bars.getToolBarManager();
    toolBar.add(actionGenerateAll);
    toolBar.add(actionGenerateSelected);
    toolBar.add(new Separator());
    toolBar.add(actionGenerateChanges);
    toolBar.add(actionAutoOnOff);
    toolBar.add(new Separator());
    toolBar.add(actionCopyError);
    toolBar.add(new Separator());
    toolBar.add(actionConfigureDatabaseConnection);
    toolBar.add(new Separator());
    toolBar.add(actionRemoveFile);
    toolBar.add(actionRemoveAllFiles);

    // This is the Down Arrow at the end of the tool bar

    IMenuManager menu = bars.getMenuManager();
    menu.add(actionGenerateAll);
    menu.add(actionGenerateSelected);
    menu.add(new Separator());
    menu.add(actionGenerateChanges);
    menu.add(actionAutoOnOff);
    menu.add(new Separator());
    menu.add(actionCopyError);
    menu.add(new Separator());
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
    contextMenu.add(actionGenerateSelected);
    contextMenu.add(new Separator());
    contextMenu.add(actionGenerateChanges);
    contextMenu.add(actionAutoOnOff);
    contextMenu.add(new Separator());
    contextMenu.add(actionCopyError);
    contextMenu.add(new Separator());
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

    // Generate All -- TODO: Nothing to do -- just a marker

    actionGenerateAll = new Action() {
      @Override
      public void run() {

        for (MainConfigFace mf : hotRodViewContentProvider.getFiles().getLoadedFiles()) {
          log.debug("mf: " + mf.getRelativeFileName() + " / " + mf.getRelativePath());
          ProjectProperties projectProperties = WorkspaceProperties.getInstance().getProjectProperties(mf.getProject());

          if (projectProperties == null) {
            showMessage("Project properties are not yet configured on file '" + mf.getRelativePath() + "'.");
            return;
          } else {
            FileProperties fileProperties = projectProperties.getFileProperties(mf.getRelativeFileName());
            if (fileProperties == null) {
              showMessage("File properties are not yet configured on file '" + mf.getRelativePath() + "'.");
              return;
            }
            if (!fileProperties.isConfigured()) {
              showMessage("The database connection for the file '" + mf.getRelativeFileName()
                  + "' has not yet been configured. " + "\n\nPlease configure it and try again.");
              return;
            }
          }
        }

        for (MainConfigFace mf : hotRodViewContentProvider.getFiles().getLoadedFiles()) {
          ProjectProperties projectProperties = WorkspaceProperties.getInstance().getProjectProperties(mf.getProject());
          FileProperties fileProperties = projectProperties.getFileProperties(mf.getRelativeFileName());
          log.debug("generating all - starting");

          mf.getConfig().resetTreeGeneration();
          mf.getConfig().markGenerateTree();
          generateFile(mf, projectProperties, fileProperties, false);
          mf.refreshView();

        }

      }
    };
    actionGenerateAll.setText("Generate all");
    actionGenerateAll.setToolTipText("Generate all");
    actionGenerateAll.setImageDescriptor(this.imgGenerateAll);

    // Generate Selection -- TODO: Nothing to do -- just a marker

    actionGenerateSelected = new Action() {

      @Override
      public void run() {

        for (MainConfigFace mf : hotRodViewContentProvider.getFiles().getLoadedFiles()) {
          mf.getConfig().resetTreeGeneration();
        }

        TreeSelection selection = (TreeSelection) viewer.getSelection();
        log.debug("selection 1: " + selection);
        List<?> sel = selection.toList();
        log.debug("selection 2: size=" + selection.size());
        if (!sel.isEmpty()) {

          // Mark selection for generation and identify main faces

          LinkedHashSet<MainConfigFace> selectedMainFaces = new LinkedHashSet<MainConfigFace>();
          for (Object obj : sel) {
            AbstractFace selectedFace = (AbstractFace) obj;
            log.debug(" - selected face=" + selectedFace.getName());
            selectedFace.getTag().markGenerateTree();
            selectedMainFaces.add(selectedFace.getMainConfigFace());
          }

          // Verify file properties are set up for all faces

          boolean allConfigured = true;
          for (MainConfigFace mf : selectedMainFaces) {
            ProjectProperties projectProperties = WorkspaceProperties.getInstance()
                .getProjectProperties(mf.getProject());
            if (projectProperties == null) {
              showMessage("The file '" + mf.getRelativeFileName()
                  + "' has not yet been configured. Please click on Configure File and try again.");
              allConfigured = false;
            } else {
              FileProperties fileProperties = projectProperties.getFileProperties(mf.getRelativeFileName());
              if (fileProperties == null) {
                showMessage("The file '" + mf.getRelativeFileName()
                    + "' has not yet been configured. Please click on Configure File and try again.");
                allConfigured = false;
              }
              if (!fileProperties.isConfigured()) {
                showMessage("The database connection for the file '" + mf.getRelativeFileName()
                    + "' has not yet been configured. " + "\n\nPlease configure it and try again.");
                return;
              }
            }
          }

          // Generate all main faces

          if (allConfigured) {
            for (MainConfigFace mainFace : selectedMainFaces) {
              ProjectProperties projectProperties = WorkspaceProperties.getInstance()
                  .getProjectProperties(mainFace.getProject());
              FileProperties fileProperties = projectProperties.getFileProperties(mainFace.getRelativeFileName());
              generateFile(mainFace, projectProperties, fileProperties, true);
            }
          }

        }

        for (MainConfigFace face : hotRodViewContentProvider.getFiles().getLoadedFiles()) {
          face.refreshView();
        }

      }

    };
    actionGenerateSelected.setText("Generate selected");
    actionGenerateSelected.setToolTipText("Generate selected");
    actionGenerateSelected.setImageDescriptor(this.imgGenerateSelection);

    // Generate Changes -- TODO: Nothing to do -- just a marker

    actionGenerateChanges = new Action() {
      @Override
      public void run() {

        generateChanges();

        log.debug("[generate changes - finished]");

      }

    };
    actionGenerateChanges.setText("Generate changes");
    actionGenerateChanges.setToolTipText("Generate changes");
    actionGenerateChanges.setImageDescriptor(this.imgGenerateChanges);

    // Auto On/Off -- TODO: Nothing to do -- just a marker

    actionAutoOnOff = new Action() {
      @Override
      public void run() {
        WorkspaceProperties.getInstance().flipAutogenerateOnChanges();
        boolean autoGenerateOnChanges = WorkspaceProperties.getInstance().isAutogenerateOnChanges();
        try {
          WorkspaceProperties.getInstance().save();
        } catch (CouldNotSaveWorkspacePropertiesException e) {
          log.error("Could not save workspace properties.", e);
        }
        String text = "Auto generate changes (" + (autoGenerateOnChanges ? "on" : "off") + ")";
        actionAutoOnOff.setText(text);
        actionAutoOnOff.setChecked(autoGenerateOnChanges);
        actionAutoOnOff.setToolTipText(text);
        actionAutoOnOff.setImageDescriptor(autoGenerateOnChanges ? imgAutoOn : imgAutoOff);

        actionGenerateChanges.setEnabled(!autoGenerateOnChanges);
        actionGenerateSelected.setEnabled(!autoGenerateOnChanges);

        if (contextMenu != null) {
          contextMenu.update();
        }

        if (autoGenerateOnChanges) {
          generateChanges();
        }

      }
    };
    boolean autoGenerateOnChanges = WorkspaceProperties.getInstance().isAutogenerateOnChanges();
    String text = "Auto generate changes (" + (autoGenerateOnChanges ? "on" : "off") + ")";
    actionAutoOnOff.setText(text);
    actionAutoOnOff.setChecked(autoGenerateOnChanges);
    actionAutoOnOff.setToolTipText(text);
    actionAutoOnOff.setImageDescriptor(autoGenerateOnChanges ? imgAutoOn : imgAutoOff);

    actionGenerateChanges.setEnabled(!autoGenerateOnChanges);
    actionGenerateSelected.setEnabled(!autoGenerateOnChanges);

    // Copy Error -- TODO: Nothing to do -- just a marker

    actionCopyError = new Action() {
      @Override
      public void run() {

        ListWriter w = new ListWriter("\n\n---\n\n");

        for (MainConfigFace mf : hotRodViewContentProvider.getFiles().getLoadedFiles()) {
          ErrorMessage errorMessage = mf.getBranchErrorMessage();
          if (errorMessage != null) {
            w.add(errorMessage.getMessage()
                + (errorMessage.getLocation() != null ? "\n  at " + errorMessage.getLocation().render() : ""));
          }
        }

        Clipboard clipboard = new Clipboard(Display.getCurrent());
        TextTransfer textTransfer = TextTransfer.getInstance();
        clipboard.setContents(new Object[] { w.toString() }, new Transfer[] { textTransfer });
        clipboard.dispose();

      }
    };
    actionCopyError.setText("Copy errors to clipboard");
    actionCopyError.setToolTipText("Copy errors to clipboard");
    actionCopyError.setImageDescriptor(imgCopyError);
    refreshToolbar();

    // Configure Database Connection -- TODO: Nothing to do -- just a marker

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
          // log.debug("[X1] will load properties");
          ProjectProperties projectProperties =
              // ProjectProperties.load(mainConfigFace.getProject());
              WorkspaceProperties.getInstance().getProjectProperties(mainConfigFace.getProject());

          // log.debug("[X1] properties loaded");

          DatabasePropertiesWizard wizard = new DatabasePropertiesWizard(mainConfigFace, projectProperties,
              viewer.getControl().getShell());
          NavigationAwareWizardDialog dialog = new NavigationAwareWizardDialog(shell, wizard);
          dialog.open();
          hotRodViewContentProvider.refresh();

          // } catch (CouldNotLoadPropertiesException e) {
          // // log.debug("[X1] Could not load properties");
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
    actionConfigureDatabaseConnection.setImageDescriptor(this.imgConfigureDatabaseConnection);

    // Remove File

    actionRemoveFile = new Action() {
      @Override
      public void run() {

        TreeSelection selection = (TreeSelection) viewer.getSelection();
        Set<MainConfigFace> mainConfigFaces = new HashSet<MainConfigFace>();
        for (Object obj : selection.toList()) {
          AbstractFace face = (AbstractFace) obj;
          mainConfigFaces.add(face.getMainConfigFace());
        }
        for (MainConfigFace f : mainConfigFaces) {
          f.remove();
          try {
            WorkspaceProperties.getInstance().getProjectProperties(f.getProject())
                .removeFileProperties(f.getRelativeFileName());
          } catch (CouldNotSaveProjectPropertiesException e) {
            log.error("Could not remove file properties: " + e.getMessage());
          }
        }
        hotRodViewContentProvider.refresh();

      }
    };
    actionRemoveFile.setText("Remove file");
    actionRemoveFile.setToolTipText("Remove file");
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
    actionRemoveAllFiles.setText("Remove all files");
    actionRemoveAllFiles.setToolTipText("Remove all files");
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

  public void refreshToolbar() {
    // log.info("/// computing Copy Error enabled");
    boolean errorsPresent = false;
    for (MainConfigFace mf : hotRodViewContentProvider.getFiles().getLoadedFiles()) {
      log.info("/// face " + mf.getRelativeFileName() + " - error="
          + (mf.getBranchErrorMessage() == null ? "null" : mf.getBranchErrorMessage().getMessage()));
      if (mf.getBranchErrorMessage() != null) {
        errorsPresent = true;
      }
    }
    log.info("/// CopyError enabled = " + errorsPresent);
    actionCopyError.setEnabled(errorsPresent);
  }

  private void generateChanges() {
    log.info("@@@ ini 1");
    for (MainConfigFace face : hotRodViewContentProvider.getFiles().getLoadedFiles()) {
      face.getConfig().resetTreeGeneration();
    }

    boolean allConfigured = true;

    for (MainConfigFace mf : hotRodViewContentProvider.getFiles().getLoadedFiles()) {
      mf.getConfig().resetTreeGeneration();
      boolean modified = markChanges(mf.getTag(), 0);
      log.info("@@@ modified=" + modified);
      if (modified) {
        ProjectProperties projectProperties = WorkspaceProperties.getInstance().getProjectProperties(mf.getProject());
        if (projectProperties == null) {
          showMessage("The file '" + mf.getRelativeFileName()
              + "' has not yet been configured. Please click on Configure File and try again.");
          allConfigured = false;
        } else {
          FileProperties fileProperties = projectProperties.getFileProperties(mf.getRelativeFileName());
          if (fileProperties == null) {
            showMessage("The file '" + mf.getRelativeFileName()
                + "' has not yet been configured. Please click on Configure File and try again.");
            allConfigured = false;
          }
          if (!fileProperties.isConfigured()) {
            showMessage("The database connection for the file '" + mf.getRelativeFileName()
                + "' has not yet been configured. " + "\n\nPlease configure it and try again.");
            allConfigured = false;
          }
        }
      }
    }

    if (allConfigured) {
      for (MainConfigFace mainFace : hotRodViewContentProvider.getFiles().getLoadedFiles()) {
        boolean treeIncludesIsToBeGenerated = mainFace.getTag().treeIncludesIsToBeGenerated();
        log.info("@@@ generate: " + mainFace.getDecoration() + " itbg=" + treeIncludesIsToBeGenerated);
        if (treeIncludesIsToBeGenerated) {
          ProjectProperties projectProperties = WorkspaceProperties.getInstance()
              .getProjectProperties(mainFace.getProject());
          FileProperties fileProperties = projectProperties.getFileProperties(mainFace.getRelativeFileName());
          log.info("@@@ now generate: " + mainFace.getDecoration());
          generateFile(mainFace, projectProperties, fileProperties, true);
        }
      }
    }

    for (MainConfigFace face : hotRodViewContentProvider.getFiles().getLoadedFiles()) {
      face.refreshView();
    }
  }

  // TODO: Nothing to do -- just a marker

  public void informFileAdded(final MainConfigFace face, final boolean changesDetected) {
    log.info("informing file added. face.isValid()=" + face.isValid());
    if (face.isValid() && changesDetected && WorkspaceProperties.getInstance().isAutogenerateOnChanges()) {
      // generation must be delayed until all file changes events are complete
      HotRodDelayedGenerationJob job = new HotRodDelayedGenerationJob(face);
      job.setRule(face.getProject()); // needs exclusive access to the project
      job.schedule();
    } else {
      face.computeBranchMarkers();
      hotRodViewContentProvider.refresh();
      // face.refreshView();
      refreshToolbar();
    }
  }

  public void informFileChangesDetected() {
    for (MainConfigFace face : this.hotRodViewContentProvider.getFiles().getLoadedFiles()) {
      informFileChangesDetected(face);
    }
  }

  public void informFileChangesDetected(final MainConfigFace face) {
    log.info("informing file changed. face.isValid()=" + face.isValid());
    if (face.isValid() && WorkspaceProperties.getInstance().isAutogenerateOnChanges()) {
      // generation must be delayed until all file changes events are complete
      HotRodDelayedGenerationJob job = new HotRodDelayedGenerationJob(face);
      job.setRule(face.getProject()); // needs exclusive access to the project
      job.schedule();
    } else {
      face.computeBranchMarkers();
      hotRodViewContentProvider.refresh();
      // face.refreshView();
      refreshToolbar();
    }
  }

  private class HotRodDelayedGenerationJob extends WorkspaceJob {

    private MainConfigFace face;

    public HotRodDelayedGenerationJob(final MainConfigFace face) {
      super("Generating file " + face.getRelativeFileName());
      this.face = face;
      log.debug("Scheduling changes generation...");
    }

    @Override
    public IStatus runInWorkspace(final IProgressMonitor pm) throws CoreException {
      log.info("Running changes generation...");
      try {
        this.face.getConfig().resetTreeGeneration();
        boolean modified = markChanges(this.face.getTag(), 0);
        log.info("modified=" + modified);
        if (modified) {
          ProjectProperties projectProperties = WorkspaceProperties.getInstance()
              .getProjectProperties(this.face.getProject());
          if (projectProperties == null) {
            showMessage("The file '" + this.face.getRelativeFileName()
                + "' has not yet been configured. Please click on Configure File and try again.");
          } else {
            FileProperties fileProperties = projectProperties.getFileProperties(this.face.getRelativeFileName());
            if (fileProperties == null) {
              showMessage("The file '" + this.face.getRelativeFileName()
                  + "' has not yet been configured. Please click on Configure File and try again.");
            } else {
              if (this.face.getTag().treeIncludesIsToBeGenerated()) {
                generateFile(this.face, projectProperties, fileProperties, true);
              }
            }
          }
        }
        return Status.OK_STATUS;
      } catch (Exception e) {
        log.error("Failed to generate.", e);
        Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Could not generate file changes", e);
        throw new CoreException(status);
      } finally {
        this.face.computeBranchMarkers();
        hotRodViewContentProvider.refresh();
        // this.face.refreshView();
        refreshToolbar();
      }
    }

  }

  private boolean markChanges(final AbstractConfigurationTag t, final int level) {
    log.info(">>> [..] " + SUtils.getFiller(". ", level) + t.getInternalCaption() + " t.getStatus()=" + t.getStatus());
    try {
      @SuppressWarnings("unused")
      GenerationUnit<?> unit = (GenerationUnit<?>) t;
      // continue only if this is a GenerationUnit.
      log.info(
          ">>> [GU] " + SUtils.getFiller(". ", level) + t.getInternalCaption() + " t.getStatus()=" + t.getStatus());
      boolean modified = t.getStatus() != TagStatus.UP_TO_DATE;
      if (modified) {
        t.markGenerate();
      }
      for (AbstractConfigurationTag subtag : t.getSubTags()) {
        if (markChanges(subtag, level + 1)) {
          modified = true;
        }
      }
      return modified;
    } catch (ClassCastException e) {
      log.debug(">>> [not a GU] " + t.getInternalCaption() + " (class " + t.getClass().getName() + ") t.getStatus()="
          + t.getStatus());
      return false;
    }
  }

  private void showMessage(final String message) {
    log.info("Message: " + message);
    MessageDialog.openInformation(viewer.getControl().getShell(), Constants.TOOL_NAME, message);
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  @Override
  public void setFocus() {
    viewer.getControl().setFocus();
  }

  // Generation

  private void generateFile(final MainConfigFace mainFace, final ProjectProperties projectProperties,
      final FileProperties fileProperties, final boolean incrementalMode) {

    HotRodConfigTag config = mainFace.getConfig();
    IProject project = mainFace.getProject();

    config.eraseTreeErrorMessages();

    if (incrementalMode && !config.treeIncludesIsToBeGenerated()) {
      showMessage("Nothing to generate.");
    } else {

      // log.debug(">>> 5 fileProperties.getCachedMetadata()=" +
      // fileProperties.getCachedMetadata());
      // log.debug(">>> 5
      // fileProperties.getCachedMetadata().getSelectMetadataCache()="
      // + fileProperties.getCachedMetadata().getSelectMetadataCache());

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

      // If settings were changed, (re)generate all

      if (config.getGenerators().isToBeGenerated()) {
        config.markGenerateTree();
      }

      // Generate

      DatabaseLocation loc = new DatabaseLocation(fileProperties.getDriverClassName(), fileProperties.getUrl(),
          fileProperties.getUsername(), fileProperties.getPassword(), fileProperties.getCatalog(),
          fileProperties.getSchema(), classPath);

      try {

        CachedMetadata cachedMetadata = fileProperties.getCachedMetadata();

        config.logGenerateMark("Generate Marks (ready to generate)", '-');

        log.info("will instantiate");
        HotRodGenerator g = config.getGenerators().getSelectedGeneratorTag().instantiateGenerator(cachedMetadata, loc,
            config, DisplayMode.LIST, incrementalMode);

        log.info("Will prepare.");
        g.prepareGeneration();
        log.info("Generation prepared.");

        try {
          LiveGenerator liveGenerator = (LiveGenerator) g;

          // a live generator

          FileGenerator fg = new EclipseFileGenerator(project);
          log.info("will generate live");
          liveGenerator.generate(fg);

          GenerationUnit<AbstractHotRodConfigTag> unit = (GenerationUnit<AbstractHotRodConfigTag>) config;

          @SuppressWarnings("unused")
          boolean successful = unit.concludeGeneration(cachedMetadata.getConfig(), g.getAdapter());

          config.logGenerateMark("Generate Marks (concluded)", '-');

        } catch (ClassCastException e) {

          // a batch generator

          log.info("will generate batch");
          g.generate();

        }

        // Mark Settings as generated

        config.getGenerators().setTreeStatus(TagStatus.UP_TO_DATE);

        // Save the cache

        fileProperties.save();

        HotRodConfigTag ch = cachedMetadata.getConfig();
        if (ch != null) {
          log.debug("...*** Enums from cache config ***");
          for (EnumTag et : ch.getAllEnums()) {
            log.debug("***    enum '" + et.getName() + "'");
          }
          log.debug("...*** End of enums from cache config ***");
        } else {
          log.debug("... *** cached-config is null.");
        }

        // Generation complete

        log.info("Generation complete.");

      } catch (ControlledException e) {
        log.info("", e);
        // if (e.getLocation() != null) {
        // showMessage(Constants.TOOL_NAME + " 1 could not generate the
        // persistence code. Invalid configuration in "
        // + e.getLocation().render() + ":\n\n" + e.getMessage());
        // } else {
        // showMessage(Constants.TOOL_NAME + " 2 could not generate the
        // persistence code:\n" + e.getMessage());
        // }
      } catch (UncontrolledException e) {
        log.error("Could not generate the persistence code.", e.getCause());
        ErrorMessage msg = new ErrorMessage(null,
            "Could not generate the persistence code: " + EUtils.renderMessages(e));
        mainFace.setInvalid(msg);
        // String msg = EUtils.renderMessages(e.getCause(), " - ", "", "\n");
        // showMessage(Constants.TOOL_NAME + " could not generate the
        // persistence code:\n" + msg);
      } catch (Throwable t) {
        log.error("Could not generate the persistence code.", t);
        ErrorMessage msg = new ErrorMessage(null,
            "Could not generate the persistence code: " + EUtils.renderMessages(t));
        mainFace.setInvalid(msg);
        // String msg = EUtils.renderMessages(t, " - ", "", "\n");
        // showMessage(Constants.TOOL_NAME + " could not generate the
        // persistence code:\n" + msg);
      } finally {
        // log.info("FINALLY BLOCK!");
        mainFace.computeBranchMarkers();
        hotRodViewContentProvider.refresh();
        // mainFace.refreshView();
        refreshToolbar();
      }

    }

  }

}
