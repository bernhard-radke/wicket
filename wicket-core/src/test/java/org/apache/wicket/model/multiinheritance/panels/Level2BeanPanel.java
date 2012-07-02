/**
 * 
 */
package org.apache.wicket.model.multiinheritance.panels;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.multiinheritance.data.Level2Bean;


/**
 * A Panel inheriting its model in level 2
 */
public class Level2BeanPanel extends TestBeanPanel
{
	private static final long serialVersionUID = -1013493685199551760L;
	private static final Logger log = Logger.getLogger(Level2BeanPanel.class);

	/**
	 * @param id
	 */
	public Level2BeanPanel(String id)
	{
		super(id);
		AjaxLink<String> clickme = new AjaxLink<String>("clickme")
		{
			private static final long serialVersionUID = -4769087468386348492L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				log.debug(Level2BeanPanel.this.getDefaultModelObject().toString());
				((Level2Bean)Level2BeanPanel.this.getDefaultModelObject()).setName("new level2");
				target.add(this);
			}
		};
		clickme.add(new Label("linkLabel", "asdf"));
		add(clickme);
	}

	/** {@inheritDoc} **/
	@Override
	protected void onConfigure()
	{
		super.onConfigure();
		log.debug(Level2BeanPanel.this.getDefaultModelObject().toString());
	}
}
