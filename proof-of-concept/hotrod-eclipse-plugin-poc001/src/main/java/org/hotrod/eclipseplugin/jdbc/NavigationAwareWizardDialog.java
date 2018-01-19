package org.hotrod.eclipseplugin.jdbc;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class NavigationAwareWizardDialog extends WizardDialog {

  private NavigationAwareWizard wizard;

  public NavigationAwareWizardDialog(final Shell parentShell, final NavigationAwareWizard wizard) {
    super(parentShell, wizard);
    this.wizard = wizard;
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

  public static abstract class NavigationAwareWizard extends Wizard {

    public NavigationAwareWizard() {
      super();
    }

    public abstract void preprocessBackButton(IWizardPage currentPage);

    public abstract void preprocessNextButton(IWizardPage currentPage);

  }

}
