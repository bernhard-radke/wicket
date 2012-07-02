/**
 * 
 */
package org.apache.wicket.model.multiinheritance.models;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentInheritedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.multiinheritance.data.Level1Bean;
import org.apache.wicket.model.multiinheritance.panels.Level2BeanPanel;


/**
 * Model on inheritance level 1
 */
public class Level1BeanModel
	implements
		IWrapModel<Level1Bean>,
		IComponentInheritedModel<Level1Bean>
{
	private static final long serialVersionUID = -6496855529796619369L;

	private RootBeanModel wrappedModel;

	/**
	 * Construct.
	 * 
	 * @param wrappedModel
	 */
	public Level1BeanModel(RootBeanModel wrappedModel)
	{
		this.wrappedModel = wrappedModel;
	}

	/** {@inheritDoc} **/
	public Level1Bean getObject()
	{
		return wrappedModel.getObject().getLevel1();
	}

	/** {@inheritDoc} **/
	public void setObject(Level1Bean bean)
	{
		wrappedModel.getObject().setLevel1(bean);
	}

	/** {@inheritDoc} **/
	public void detach()
	{
		// nothing to do?
	}

	/** {@inheritDoc} **/
	@SuppressWarnings("unchecked")
	public <W> IWrapModel<W> wrapOnInheritance(Component component)
	{
		if (component instanceof Level2BeanPanel)
		{
			return (IWrapModel<W>)new Level2BeanModel(this);
		}
		else
		{
			return null;
		}
	}

	/** {@inheritDoc} **/
	public IModel<?> getWrappedModel()
	{
		return wrappedModel;
	}
}
