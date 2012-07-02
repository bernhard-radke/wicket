/**
 * 
 */
package org.apache.wicket.model.multiinheritance.panels;

/**
 * A Panel inheriting its model in level 1
 */
public class Level1BeanPanel extends TestBeanPanel
{
	private static final long serialVersionUID = 3612307267195160306L;

	/**
	 * @param id
	 */
	public Level1BeanPanel(String id)
	{
		super(id);
		add(new Level2BeanPanel("subBean"));
	}
}
