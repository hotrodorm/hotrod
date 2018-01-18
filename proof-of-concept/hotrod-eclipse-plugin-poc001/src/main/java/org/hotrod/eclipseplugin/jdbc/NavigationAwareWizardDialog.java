package org.hotrod.eclipseplugin.jdbc;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class NavigationAwareWizardDialog extends WizardDialog {

  private ConnectToDatabaseWizard wizard;

  public NavigationAwareWizardDialog(final Shell parentShell, final ConnectToDatabaseWizard newWizard) {
    super(parentShell, newWizard);
    this.wizard = newWizard;
  }

  @Override
  protected void nextPressed() {
    this.wizard.preprocessNextButton(this.getCurrentPage());
    super.nextPressed();
  }

  @Override
  protected void backPressed() {
    this.wizard.preprocessBackButton(this.getCurrentPage());
    super.backPressed();
  }

}
