package org.hotrod.eclipseplugin.jdbc;

import org.eclipse.jface.wizard.IWizardPage;

public interface NavigationAwareWizard {

  void preprocessBackButton(IWizardPage currentPage);

  void preprocessNextButton(IWizardPage currentPage);

}
