/**
 * 
 */
package org.apache.wicket.model.multiinheritance.panels;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;


/**
 * Abstract superclass of Bean Panels for multilevel model inheritance test showing the name of the
 * testbean
 */
public class TestBeanPanel extends Panel
{
	private static final long serialVersionUID = -8354559475876916310L;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public TestBeanPanel(String id)
	{
		super(id);
	}

	/** {@inheritDoc} **/
	@Override
	protected void onConfigure()
	{
		add(new Label("name", new PropertyModel<String>(getDefaultModelObject(), "name")));
		super.onConfigure();
	}
}
