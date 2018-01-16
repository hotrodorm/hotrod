package org.hotrod.eclipseplugin.jdbc;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class JDBCPropertiesDialog extends TitleAreaDialog {

  private Text fieldFirstName;
  private Text fieldLastName;

  private String firstName;
  private String lastName;

  private Shell parentShell;

  private Text fieldDriverClassPath;
  private Text fieldDriverClassName;
  private Text fieldURL;
  private Text fieldUsername;
  private Text fieldPassword;
  private Text fieldCatalog;
  private Text fieldSchema;
  private Text fieldGenerator;

  private String driverClassPath;
  private String driverClassName;
  private String url;
  private String username;
  private String password;
  private String catalog;
  private String schema;
  private String generator;

  public JDBCPropertiesDialog(final Shell parentShell) {
    super(parentShell);
    this.parentShell = parentShell;
  }

  @Override
  public void create() {
    super.create();
    setTitle("JDBC Properties");
    setMessage("This is a TitleAreaDialog", IMessageProvider.INFORMATION);
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    GridLayout layout = new GridLayout(2, false);
    container.setLayout(layout);

    // createFirstName(container);
    // createLastName(container);
    createFields(container);

    return area;
  }

  private void createFields(final Composite container) {

    GridData layoutData = new GridData();
    layoutData.grabExcessHorizontalSpace = true;
    layoutData.horizontalAlignment = GridData.FILL;

    // Driver Class Path

    {
      Label label = new Label(container, SWT.NONE);
      label.setText("JDBC Driver File");
      this.fieldDriverClassPath = new Text(container, SWT.BORDER);
      this.fieldDriverClassPath.setLayoutData(layoutData);
    }

//    Button browse = new Button(container, SWT.PUSH);
//    browse.setText("Browse ...");
//    browse.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 0));
//    browse.addSelectionListener(new SelectionAdapter() {
//      public void widgetSelected(SelectionEvent e) {
//        DirectoryDialog dialog = new DirectoryDialog(parentShell, SWT.NULL);
//        String path = dialog.open();
//        if (path != null) {
//          // do stuff with path
//        }
//      }
//
//    });

    // Driver Class Name

    {
      Label label = new Label(container, SWT.NONE);
      label.setText("JDBC Driver Class Name");
      this.fieldDriverClassName = new Text(container, SWT.BORDER);
      this.fieldDriverClassName.setLayoutData(layoutData);
    }

    // URL

    {
      Label label = new Label(container, SWT.NONE);
      label.setText("URL");
      this.fieldURL = new Text(container, SWT.BORDER);
      this.fieldURL.setLayoutData(layoutData);
    }

    // Username

    {
      Label label = new Label(container, SWT.NONE);
      label.setText("Username");
      this.fieldUsername = new Text(container, SWT.BORDER);
      this.fieldUsername.setLayoutData(layoutData);
    }

    // Password

    {
      Label label = new Label(container, SWT.NONE);
      label.setText("Password");
      this.fieldPassword = new Text(container, SWT.BORDER);
      this.fieldPassword.setLayoutData(layoutData);
    }

    // Catalog

    {
      Label label = new Label(container, SWT.NONE);
      label.setText("Catalog");
      this.fieldCatalog = new Text(container, SWT.BORDER);
      this.fieldCatalog.setLayoutData(layoutData);
    }

    // Schema

    {
      Label label = new Label(container, SWT.NONE);
      label.setText("Schema");
      this.fieldSchema = new Text(container, SWT.BORDER);
      this.fieldSchema.setLayoutData(layoutData);
    }

    // Generator

    {
      Label label = new Label(container, SWT.NONE);
      label.setText("Generator");
      this.fieldGenerator = new Text(container, SWT.BORDER);
      this.fieldGenerator.setLayoutData(layoutData);
    }

  }

  private void createFirstName(final Composite container) {
    Label lbtFirstName = new Label(container, SWT.NONE);
    lbtFirstName.setText("First Name");

    GridData dataFirstName = new GridData();
    dataFirstName.grabExcessHorizontalSpace = true;
    dataFirstName.horizontalAlignment = GridData.FILL;

    fieldFirstName = new Text(container, SWT.BORDER);
    fieldFirstName.setLayoutData(dataFirstName);
  }

  private void createLastName(final Composite container) {
    Label lbtLastName = new Label(container, SWT.NONE);
    lbtLastName.setText("Last Name");

    GridData dataLastName = new GridData();
    dataLastName.grabExcessHorizontalSpace = true;
    dataLastName.horizontalAlignment = GridData.FILL;
    fieldLastName = new Text(container, SWT.BORDER);
    fieldLastName.setLayoutData(dataLastName);
  }

  @Override
  protected boolean isResizable() {
    return true;
  }

  // save content of the Text fields because they get disposed
  // as soon as the Dialog closes
  private void saveInput() {
    // this.firstName = this.fieldFirstName.getText();
    // this.lastName = this.fieldLastName.getText();
    this.driverClassPath = this.fieldDriverClassPath.getText();
    this.driverClassName = this.fieldDriverClassName.getText();
    this.url = this.fieldURL.getText();
    this.username = this.fieldUsername.getText();
    this.password = this.fieldPassword.getText();
    this.catalog = this.fieldCatalog.getText();
    this.schema = this.fieldSchema.getText();
    this.generator = this.fieldGenerator.getText();
  }

  @Override
  protected void okPressed() {
    saveInput();
    super.okPressed();
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  // Getters

  public String getDriverClassPath() {
    return driverClassPath;
  }

  public String getDriverClassName() {
    return driverClassName;
  }

  public String getUrl() {
    return url;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getCatalog() {
    return catalog;
  }

  public String getSchema() {
    return schema;
  }

  public String getGenerator() {
    return generator;
  }

}