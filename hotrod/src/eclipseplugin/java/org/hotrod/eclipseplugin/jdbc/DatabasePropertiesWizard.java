package org.hotrod.eclipseplugin.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.hotrod.eclipseplugin.ProjectProperties;
import org.hotrod.eclipseplugin.ProjectProperties.CouldNotSavePropertiesException;
import org.hotrod.eclipseplugin.ProjectProperties.FileProperties;
import org.hotrod.eclipseplugin.jdbc.NavigationAwareWizardDialog.NavigationAwareWizard;
import org.hotrod.eclipseplugin.treeview.MainConfigFace;
import org.hotrod.eclipseplugin.utils.FUtil;
import org.hotrod.runtime.util.SUtils;

public class DatabasePropertiesWizard extends NavigationAwareWizard {

  private static final Logger log = Logger.getLogger(DatabasePropertiesWizard.class);

  private MainConfigFace mainConfigFace;
  private Shell shell;

  private ProjectProperties properties;
  private FileProperties fileProperties;

  protected DriverPage driverPage;
  protected ConnectionPage connectionPage;
  protected SelectionPage selectionPage;
  protected ConfirmationPage confirmationPage;

  protected DynamicallyLoadedDriver driver = null;
  protected SQLException driverException = null;

  protected Connection connection = null;
  protected SQLException connectionException = null;

  private List<String> availableCatalogs = null;
  private List<String> availableSchemas = null;

  private boolean onLastPage = false;

  private static String[] AVAILABLE_GENERATORS = { "MyBatis", "Spring JDBC", "MyBatis Spring" };

  public DatabasePropertiesWizard(final MainConfigFace mainConfigFace, final ProjectProperties properties,
      final Shell shell) {
    super();

    this.mainConfigFace = mainConfigFace;
    this.properties = properties;
    this.shell = shell;

    TrayDialog.setDialogHelpAvailable(false);

    this.fileProperties = this.properties.getFileProperties(this.mainConfigFace.getRelativeFileName());
    log.debug("[X1] this.fileProperties=" + this.fileProperties + " (rel file name="
        + this.mainConfigFace.getRelativeFileName() + ")");

    if (this.fileProperties == null) {
      this.fileProperties = new FileProperties(this.mainConfigFace.getRelativeFileName());
      this.properties.addFileProperties(this.mainConfigFace.getRelativeFileName(), this.fileProperties);
    }

    this.driverPage = new DriverPage();
    this.connectionPage = new ConnectionPage();
    this.selectionPage = new SelectionPage();
    this.confirmationPage = new ConfirmationPage();
    super.setHelpAvailable(false);
  }

  @Override
  public String getWindowTitle() {
    return "Configure Database Connection";
  }

  @Override
  public void addPages() {
    addPage(this.driverPage);
    addPage(this.connectionPage);
    addPage(this.selectionPage);
    addPage(this.confirmationPage);
  }

  @Override
  public boolean isHelpAvailable() {
    return false;
  }

  @Override
  public void preprocessBackButton(final IWizardPage currentPage) {
    log.debug("... preprocessBackButton() currentPage=" + currentPage);
    // this.clearMessages();
    if (currentPage == this.confirmationPage) {
      this.onLastPage = false;
    }
  }

  @Override
  public void preprocessNextButton(final IWizardPage currentPage) {
    log.debug("... preprocessNextButton()");

    // this.clearMessages();

    if (currentPage == this.driverPage) {

      this.driverPage.clearMessage();

      // Next on the Driver Page

      this.driver = null;
      this.driverException = null;
      log.debug("... --> on DriverPage");
      IProject project = this.mainConfigFace.getProject();
      List<String> driverClassPath = Arrays.asList(this.driverPage.fieldClassPathEntries.getItems());
      String driverClassName = this.driverPage.fieldDriverClassName.getText();
      try {
        this.driver = new DynamicallyLoadedDriver(project, driverClassPath, driverClassName);
        log.debug("... --> driver, success");
      } catch (SQLException e) {
        this.driverException = e;
        log.debug("... --> driver, failure");
      }

    } else if (currentPage == this.connectionPage) {

      // Next on the Connection Page

      this.connectionPage.clearMessage();

      log.debug("... --> on ConnectionPage");

      this.connection = null;
      this.connectionException = null;
      try {
        String url = this.connectionPage.getFieldURL().getText();
        String username = this.connectionPage.getFieldUsername().getText();
        String password = this.connectionPage.getFieldPassword().getText();

        setCursorBusy(true);
        this.connection = this.driver.getConnection(url, username, password);

        retrieveCatalogsAndSchemas();
        log.debug("this.availableCatalogs.size()="
            + (this.availableCatalogs == null ? "null" : this.availableCatalogs.size()));
        this.selectionPage.updateAvailableCatalogsAndSchemas();
        log.debug("... --> conn, success");

      } catch (SQLException e) {
        if (this.connection != null) {
          try {
            this.connection.close();
          } catch (SQLException e1) {
            // Ignore
          } finally {
            this.connection = null;
          }
        }
        this.connectionException = e;
        log.debug("... --> conn, failure");
      } finally {
        setCursorBusy(false);
      }

    } else if (currentPage == this.selectionPage) {

      // Next on the Selection Page

      this.selectionPage.clearMessage();

      this.onLastPage = true;

    }

  }

  @Override
  public void preprocessClose(final IWizardPage currentPage) {
    if (this.driver != null) {
      try {
        this.driver.close();
      } catch (SQLException e) {
        // Ignore
      }
    }
  }

  private Cursor cursor = null;

  private void setCursorBusy(final boolean busy) {
    if (this.cursor != null) {
      this.cursor.dispose();
    }
    Display display = Display.getCurrent();
    this.cursor = busy ? new Cursor(display, SWT.CURSOR_WAIT) : new Cursor(display, SWT.CURSOR_ARROW);
    this.getShell().setCursor(this.cursor);
    // this.shell.setCursor(this.cursor);
  }

  private void retrieveCatalogsAndSchemas() throws SQLException {
    DatabaseMetaData dm = this.connection.getMetaData();

    // Catalogs

    log.debug("[CATALOGS] dm.supportsCatalogsInTableDefinitions()=" + dm.supportsCatalogsInTableDefinitions());

    if (dm.supportsCatalogsInTableDefinitions()) {
      ResultSet rs = null;
      try {
        this.availableCatalogs = new ArrayList<String>();
        log.debug("[CATALOGS] 1");
        rs = dm.getCatalogs();
        log.debug("[CATALOGS] 2");
        while (rs.next()) {
          log.debug("[CATALOGS] read one");
          String catalog = rs.getString(1);
          this.availableCatalogs.add(catalog);
        }
        log.debug("[CATALOGS] finished!");
      } finally {
        if (rs != null) {
          try {
            rs.close();
          } catch (Exception e) {
            // Ignore
          }
        }
      }
    }

    // Schemas

    if (dm.supportsSchemasInTableDefinitions()) {
      ResultSet rs = null;
      try {
        this.availableSchemas = new ArrayList<String>();
        rs = dm.getSchemas();
        while (rs.next()) {
          String schema = rs.getString(1);
          this.availableSchemas.add(schema);
        }
      } finally {
        if (rs != null) {
          try {
            rs.close();
          } catch (Exception e) {
            // Ignore
          }
        }
      }
    }

  }

  @Override
  public boolean canFinish() {
    return this.onLastPage;
  }

  @Override
  public boolean performFinish() {
    this.driverPage.retrieveValues();
    this.connectionPage.retrieveValues();
    this.selectionPage.retrieveValues();
    try {
      this.properties.save();
    } catch (CouldNotSavePropertiesException e) {
      MessageDialog.openError(this.shell, "Could not save HotRod Plugin's properties file",
          "Error: " + e.getMessage() + "\n\nCould not save HotRod Plugin's properties file.");
    }
    return true;
  }

  // TODO: Nothing to do; just a marker.

  // =======================
  // === Page 1 - Driver ===
  // =======================

  public class DriverPage extends WizardPage {

    private final String[] FILTER_NAMES = { "Java Archive (*.jar)" };
    private final String[] FILTER_EXTS = { "*.jar" };

    private Composite container;
    private org.eclipse.swt.widgets.List fieldClassPathEntries;
    private Text fieldDriverClassName;

    public DriverPage() {
      super("Select the JDBC driver");
      setTitle("Select the JDBC driver");
      setDescription("Add the JDBC driver jar file(s) and specify the driver class name.");
    }

    public void clearMessage() {
      this.setErrorMessage(null);
    }

    @Override
    public void createControl(final Composite parent) {
      this.container = new Composite(parent, SWT.NONE);
      {
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        this.container.setLayout(layout);
      }

      // Driver JAR files

      Label label = new Label(this.container, SWT.NONE);
      label.setText("Driver jar files:");
      {
        GridData gridData = new GridData();
        gridData.horizontalSpan = 3;
        label.setLayoutData(gridData);
      }

      this.fieldClassPathEntries = new org.eclipse.swt.widgets.List(this.container,
          SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
      {
        // GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
        // true);
        // GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        GridData gridData = new GridData();
        gridData.horizontalSpan = 2;
        gridData.verticalSpan = 2;
        // gridData.minimumHeight = 172;
        // gridData.widthHint = 400;
        gridData.heightHint = 172;
        // gridData.heightHint = 40;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.verticalAlignment = GridData.FILL_VERTICAL; // stays up
        // gridData.verticalAlignment = GridData.GRAB_VERTICAL; // moves down
        // gridData.verticalAlignment = GridData.VERTICAL_ALIGN_FILL; // stays
        // up
        this.fieldClassPathEntries.setLayoutData(gridData);
      }
      for (String entryPath : fileProperties.getDriverClassPathEntries()) {
        this.fieldClassPathEntries.add(entryPath);
      }

      Button addJar = new Button(this.container, SWT.PUSH);
      {
        // GridData gridData = new GridData(GridData.BEGINNING,
        // GridData.VERTICAL_ALIGN_BEGINNING, false, false);
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        gridData.widthHint = 65;
        addJar.setLayoutData(gridData);
      }
      addJar.setText("Add...");
      addJar.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(final SelectionEvent event) {
          FileDialog fileDialog = new FileDialog(getShell(), SWT.MULTI);
          fileDialog.setFilterNames(FILTER_NAMES);
          fileDialog.setFilterExtensions(FILTER_EXTS);
          File baseFolder = mainConfigFace.getProject().getLocation().toFile();
          String basePath = baseFolder.getPath();
          log.debug("--- basePath=" + basePath);
          fileDialog.setFilterPath(basePath);
          fileDialog.open();
          String path = fileDialog.getFilterPath();
          File folder = new File(path);
          log.debug("path=" + path);
          for (String fileName : fileDialog.getFileNames()) {
            log.debug("fileName=" + fileName);
            File f = new File(folder, fileName);
            File relFile = FUtil.getRelativeFile(f, baseFolder);
            String newPath = relFile != null ? relFile.getPath() : f.getPath();
            boolean alreadyIncluded = false;
            for (String existing : fieldClassPathEntries.getItems()) {
              if (existing.equals(newPath)) {
                alreadyIncluded = true;
              }
            }
            if (!alreadyIncluded) {
              fieldClassPathEntries.add(newPath);
            }
          }
        }
      });

      Button deleteJar = new Button(this.container, SWT.PUSH);
      {
        GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        gridData.widthHint = 65;
        deleteJar.setLayoutData(gridData);
      }
      deleteJar.setText("Delete");
      deleteJar.addSelectionListener(new SelectionListener() {

        @Override
        public void widgetSelected(final SelectionEvent event) {
          log.debug("-> [deleteJar] selected.");
          for (String fileName : fieldClassPathEntries.getSelection()) {
            log.debug(" * deleting: " + fileName);
            fieldClassPathEntries.remove(fileName);
          }
        }

        @Override
        public void widgetDefaultSelected(final SelectionEvent event) {
          log.debug("-> [deleteJar] default selected.");
        }

      });

      // Driver class name

      Label label1 = new Label(this.container, SWT.NONE);
      label1.setText("Driver class name:");
      // {
      // GridData gridData = new GridData();
      // gridData.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
      // label1.setLayoutData(gridData);
      // }

      this.fieldDriverClassName = new Text(this.container, SWT.BORDER | SWT.SINGLE);
      {
        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        // GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL,
        // GridData.CENTER, true, true);
        gridData.horizontalSpan = 2;
        // gridData.widthHint = 500;
        this.fieldDriverClassName.setLayoutData(gridData);
      }

      this.fieldDriverClassName.setText(fileProperties.getDriverClassName());
      this.fieldDriverClassName.addKeyListener(new KeyListener() {

        @Override
        public void keyPressed(final KeyEvent e) {
          // Ignore
        }

        @Override
        public void keyReleased(final KeyEvent e) {
          validate();
        }

      });

      // Page assembled

      setControl(this.container);

      validate();

    }

    private void validate() {
      if (!SUtils.isEmpty(this.fieldDriverClassName.getText())) {
        setPageComplete(true);
      } else {
        setPageComplete(false);
      }
    }

    @Override
    public IWizardPage getNextPage() {

      log.debug("*** getNextPage()");

      if (driver != null || driverException == null) {
        return super.getNextPage();
      } else {
        this.setErrorMessage(driverException.getMessage());
        return driverPage;
      }

    }

    // Retrieve values

    public void retrieveValues() {
      fileProperties.setDriverClassPathEntries(Arrays.asList(this.fieldClassPathEntries.getItems()));
      fileProperties.setDriverClassName(this.fieldDriverClassName.getText());
    }

  }

  // TODO: nothing todo. Just a marker.

  // ===========================
  // === Page 2 - Connection ===
  // ===========================

  public class ConnectionPage extends WizardPage {

    private Composite container;
    private Text fieldURL;
    private Text fieldUsername;
    private Text fieldPassword;

    public ConnectionPage() {
      super("Select a database");
      setTitle("Select a database");
      setDescription("Specify which database you want to connect to.");
    }

    public void clearMessage() {
      this.setErrorMessage(null);
    }

    @Override
    public void createControl(final Composite parent) {

      this.container = new Composite(parent, SWT.NONE);
      {
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        this.container.setLayout(layout);
      }

      // URL

      Label label1 = new Label(this.container, SWT.NONE);
      label1.setText("URL:");

      this.fieldURL = new Text(this.container, SWT.BORDER | SWT.SINGLE);
      this.fieldURL.setText(fileProperties.getUrl());
      {
        // GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        // gridData.widthHint = 460;
        this.fieldURL.setLayoutData(gridData);
      }
      this.fieldURL.addKeyListener(new KeyListener() {

        @Override
        public void keyPressed(final KeyEvent e) {
          // Ignore
        }

        @Override
        public void keyReleased(final KeyEvent e) {
          validate();
        }

      });

      // Username

      Label label2 = new Label(this.container, SWT.NONE);
      label2.setText("Username:");

      this.fieldUsername = new Text(this.container, SWT.BORDER | SWT.SINGLE);
      {
        GridData gridData = new GridData();
        gridData.widthHint = 150;
        this.fieldUsername.setLayoutData(gridData);
      }
      this.fieldUsername.setText(fileProperties.getUsername());

      // Password

      Label label3 = new Label(this.container, SWT.NONE);
      label3.setText("Password:");

      this.fieldPassword = new Text(this.container, SWT.BORDER | SWT.SINGLE);
      {
        GridData gridData = new GridData();
        gridData.widthHint = 150;
        this.fieldPassword.setLayoutData(gridData);
      }
      this.fieldPassword.setText(fileProperties.getPassword());

      // Page assembled

      setControl(this.container);

      this.validate();

    }

    @Override
    public IWizardPage getNextPage() {
      if (connection != null || connectionException == null) {
        return super.getNextPage();
      } else {
        this.setErrorMessage(connectionException.getMessage());
        return connectionPage;
      }
    }

    private void validate() {
      if (!SUtils.isEmpty(this.fieldURL.getText())) {
        setPageComplete(true);
      } else {
        setPageComplete(false);
      }
    }

    // Getters

    public Text getFieldURL() {
      return fieldURL;
    }

    public Text getFieldUsername() {
      return fieldUsername;
    }

    public Text getFieldPassword() {
      return fieldPassword;
    }

    // Retrieve values

    public void retrieveValues() {
      fileProperties.setUrl(this.fieldURL.getText());
      fileProperties.setUsername(this.fieldUsername.getText());
      fileProperties.setPassword(this.fieldPassword.getText());
    }

  }

  // ==========================
  // === Page 3 - Selection ===
  // ==========================

  // TODO: nothing todo. Just a marker.

  public class SelectionPage extends WizardPage {

    private Composite container;
    private Label labelCatalog;
    private Combo fieldCatalog;
    private Label labelSchema;
    private Combo fieldSchema;
    private Combo fieldGenerator;

    public SelectionPage() {
      super("Select the default database schema");
      log.debug("[[ SelectionPage ]]");
      setTitle("Select the default database schema");
      setDescription("Choose the default database catalog and/or schema.");
    }

    public void clearMessage() {
      this.setErrorMessage(null);
    }

    @Override
    public void createControl(final Composite parent) {
      log.debug("[[ SelectionPage ]] - createControls()");

      this.container = new Composite(parent, SWT.NONE);
      {
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        this.container.setLayout(layout);
      }

      // Catalog

      this.labelCatalog = new Label(this.container, SWT.NONE);
      this.labelCatalog.setText("Default catalog:");
      this.fieldCatalog = new Combo(this.container, SWT.DROP_DOWN | SWT.READ_ONLY);
      {
        GridData gridData = new GridData();
        gridData.widthHint = 250;
        this.fieldCatalog.setLayoutData(gridData);
      }

      // Schema

      this.labelSchema = new Label(this.container, SWT.NONE);
      this.labelSchema.setText("Default schema:");
      this.fieldSchema = new Combo(this.container, SWT.DROP_DOWN | SWT.READ_ONLY);
      {
        GridData gridData = new GridData();
        gridData.widthHint = 250;
        this.fieldSchema.setLayoutData(gridData);
      }
      log.debug("[X3] fileProperties.getSchema()=" + fileProperties.getSchema());

      // Generator

      Label label3 = new Label(this.container, SWT.NONE);
      label3.setText("Generator:");
      this.fieldGenerator = new Combo(this.container, SWT.DROP_DOWN | SWT.READ_ONLY);
      this.fieldGenerator.setItems(AVAILABLE_GENERATORS);
      this.fieldGenerator.setText(fileProperties.getGenerator());

      // Page assembled

      setControl(this.container);

      this.validate();
    }

    public void updateAvailableCatalogsAndSchemas() {

      if (availableCatalogs != null) {
        this.fieldCatalog.setItems(availableCatalogs.toArray(new String[0]));
        this.fieldCatalog.setEnabled(true);
      } else {
        fileProperties.setCatalog("");
        this.fieldCatalog.setItems(new String[0]);
        this.fieldCatalog.setEnabled(false);
      }
      this.fieldCatalog.setText(fileProperties.getCatalog());

      if (availableSchemas != null) {
        this.fieldSchema.setItems(availableSchemas.toArray(new String[0]));
        this.fieldSchema.setEnabled(true);
      } else {
        fileProperties.setSchema("");
        this.fieldSchema.setItems(new String[0]);
        this.fieldSchema.setEnabled(false);
      }
      this.fieldSchema.setText(fileProperties.getSchema());

    }

    private void validate() {
      // if (!SUtil.isEmpty(this.fieldURL.getText())) {
      setPageComplete(true);
      // } else {
      // setPageComplete(false);
      // }
    }

    @Override
    public IWizardPage getNextPage() {
      return super.getNextPage();
    }

    // Getters

    // public Combo getFieldCatalog() {
    // return fieldCatalog;
    // }

    public Combo getFieldSchema() {
      return fieldSchema;
    }

    public Combo getFieldGenerator() {
      return fieldGenerator;
    }

    // Retrieve values

    public void retrieveValues() {
      fileProperties.setCatalog(this.fieldCatalog.getText());
      fileProperties.setSchema(this.fieldSchema.getText());
      fileProperties.setGenerator(this.fieldGenerator.getText());
    }

  }

  // =============================
  // === Page 4 - Confirmation ===
  // =============================

  // TODO: nothing todo. Just a marker.

  public class ConfirmationPage extends WizardPage {

    private Composite container;

    public ConfirmationPage() {
      super("Save the database connection");
      log.debug("[[ ConfirmationPage ]]");
      setTitle("Save the database connection");
      setDescription("Save your database connection details to a local file.");
    }

    public void clearMessage() {
      this.setErrorMessage(null);
    }

    @Override
    public void createControl(final Composite parent) {
      log.debug("[[ ConfirmationPage ]] - createControls()");

      this.container = new Composite(parent, SWT.NONE);
      {
        GridLayout layout = new GridLayout();
        this.container.setLayout(layout);
      }

      Label text = new Label(this.container, SWT.NONE);
      text.setText("The database connection properties have been verified and are ready to be saved.");

      // Page assembled

      setControl(this.container);

      setPageComplete(true);
    }

    @Override
    public IWizardPage getPreviousPage() {
      log.debug("[Last] getPreviousPage()");
      return super.getPreviousPage();
    }

  }

}