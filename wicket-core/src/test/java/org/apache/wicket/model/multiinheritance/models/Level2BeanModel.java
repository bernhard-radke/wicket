/**
 * 
 */
package org.apache.wicket.model.multiinheritance.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.multiinheritance.data.Level2Bean;


/**
 * Model on inheritance level 2
 */
public class Level2BeanModel implements IWrapModel<Level2Bean>
{
	private static final long serialVersionUID = 1741393491010500989L;

	private Level1BeanModel wrappedModel;

	/**
	 * Construct.
	 * 
	 * @param wrappedModel
	 */
	public Level2BeanModel(Level1BeanModel wrappedModel)
	{
		this.wrappedModel = wrappedModel;
	}

	/** {@inheritDoc} **/
	public Level2Bean getObject()
	{
		return wrappedModel.getObject().getLevel2();
	}

	/** {@inheritDoc} **/
	public void setObject(Level2Bean bean)
	{
		wrappedModel.getObject().setLevel2(bean);
	}

	/** {@inheritDoc} **/
	public void detach()
	{
		// nothing to do
	}

	/** {@inheritDoc} **/
	public IModel<?> getWrappedModel()
	{
		return wrappedModel;
	}
}
