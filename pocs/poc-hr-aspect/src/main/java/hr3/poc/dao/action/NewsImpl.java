package hr3.poc.dao.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import hr3.poc.aspect.LazySuperclassLoading;
import hr3.poc.dao.action.primitives.ActionDAO;
import hr3.poc.dao.action.primitives.NewsVO;
import hr3.poc.model.News;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NewsImpl extends NewsVO implements News, LazySuperclassLoading {

	private static final long serialVersionUID = 1L;

	// Add custom code below.

	@Autowired
	private ActionDAO actionDAO;

	// LazySuperclassLoading implementation

	private ActionImpl actionImpl;

	// override pk set
	@Override
	public void setActionId(Long id) {
		this.unloadSuperclass();
		super.setActionId(id);
	}

	@Override
	public boolean isLoaded() {
		return this.actionImpl != null;
	}

	@Override
	public void unloadSuperclass() {
		System.out.println(">>> ActionImpl.loadSuperclass: unloading superclass... because subclass(NewsImpl) id was to 'null'");
		this.actionImpl = null;
	}

	@Override
	public void loadSuperclass() {
		if (!this.isLoaded()) {
			System.out.println(">>> ActionImpl.loadSuperclass: loading superclass");
			this.actionImpl = actionDAO.selectByPK(this.getActionId());
		}
		else
			System.out.println(">>>ActionImpl.loadSuperclass: nothing to do...already loaded");

	}

	// superclass methods
	@Override
	public String getTitle() {
		return actionImpl.getTitle();
	}

	@Override
	public void setTitle(String title) {
		actionImpl.setTitle(title);		
	}

	@Override
	public String getSubTitle() {
		 return actionImpl.getSubTitle();
	}

	@Override
	public void setSubTitle(String subTitle) {
		actionImpl.setSubTitle(subTitle);
	}

	
}
