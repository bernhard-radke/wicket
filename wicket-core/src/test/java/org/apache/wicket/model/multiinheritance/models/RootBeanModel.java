/**
 * 
 */
package org.apache.wicket.model.multiinheritance.models;

import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentInheritedModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.multiinheritance.data.RootBean;
import org.apache.wicket.model.multiinheritance.panels.Level1BeanPanel;


/**
 * root model for multilevel model inheritance test
 */
public class RootBeanModel implements IComponentInheritedModel<RootBean>
{
	private static final long serialVersionUID = -3679905535631006281L;

	private RootBean bean;

	/**
	 * Construct.
	 * 
	 * @param bean
	 */
	public RootBeanModel(RootBean bean)
	{
		this.bean = bean;
	}

	/** {@inheritDoc} **/
	public RootBean getObject()
	{
		return bean;
	}

	/** {@inheritDoc} **/
	public void setObject(RootBean bean)
	{
		this.bean = bean;
	}

	/** {@inheritDoc} **/
	public void detach()
	{
		// nothing to do
	}

	/** {@inheritDoc} **/
	@SuppressWarnings("unchecked")
	public <W> IWrapModel<W> wrapOnInheritance(Component component)
	{
		if (component instanceof Level1BeanPanel)
		{
			return (IWrapModel<W>)new Level1BeanModel(this);
		}
		else
		{
			return null;
		}
	}
}
