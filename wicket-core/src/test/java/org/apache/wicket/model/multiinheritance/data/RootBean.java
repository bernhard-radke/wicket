/**
 * 
 */
package org.apache.wicket.model.multiinheritance.data;

/**
 * A TestBean as root for multiinheritance test
 */
public class RootBean extends TestBean
{
	private static final long serialVersionUID = 2689548247799840064L;

	private Level1Bean level1;

	/**
	 * @return the level1
	 */
	public Level1Bean getLevel1()
	{
		return level1;
	}

	/**
	 * @param level1
	 *            the level1 to set
	 */
	public void setLevel1(Level1Bean level1)
	{
		this.level1 = level1;
	}
}
