/**
 * 
 */
package org.apache.wicket.model.multiinheritance.data;

/**
 * A TestBean at inheritance level 2 for multiinheritance test
 */
public class Level2Bean extends TestBean
{
	private static final long serialVersionUID = 3454692202776105812L;

	/** {@inheritDoc} **/
	@Override
	public String toString()
	{
		return "Level2Bean: " + getName();
	}
}
