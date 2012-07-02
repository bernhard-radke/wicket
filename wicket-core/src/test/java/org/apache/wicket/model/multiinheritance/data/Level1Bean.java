/**
 * 
 */
package org.apache.wicket.model.multiinheritance.data;

/**
 * A TestBean at inheritance level 1 for multiinheritance test
 */
public class Level1Bean extends TestBean
{
	private static final long serialVersionUID = -1727455620047596222L;

	private Level2Bean level2;

	/**
	 * @return the level2
	 */
	public Level2Bean getLevel2()
	{
		return level2;
	}

	/**
	 * @param level2
	 *            the level2 to set
	 */
	public void setLevel2(Level2Bean level2)
	{
		this.level2 = level2;
	}
}
