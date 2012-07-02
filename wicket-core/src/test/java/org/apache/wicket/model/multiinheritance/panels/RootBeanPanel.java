/**
 * 
 */
package org.apache.wicket.model.multiinheritance.panels;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.multiinheritance.data.RootBean;
import org.apache.wicket.model.multiinheritance.models.RootBeanModel;


/**
 * Root Panel for multilevel model inheritance test
 */
public class RootBeanPanel extends TestBeanPanel
{
	private static final long serialVersionUID = 3541236566272466242L;
	private static final Logger log = Logger.getLogger(RootBeanPanel.class);

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param bean
	 */
	public RootBeanPanel(String id, RootBean bean)
	{
		super(id);
		setDefaultModel(new RootBeanModel(bean));
		this.add(new Level1BeanPanel("subBean"));
		AjaxLink<Void> clickmeLvl2 = new AjaxLink<Void>("clickmeLvl2")
		{
			private static final long serialVersionUID = -3612916509576030048L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				log.debug(((RootBean)RootBeanPanel.this.getDefaultModelObject()).getLevel1()
					.getLevel2()
					.toString());
			}
		};
		this.add(clickmeLvl2);
	}
}
