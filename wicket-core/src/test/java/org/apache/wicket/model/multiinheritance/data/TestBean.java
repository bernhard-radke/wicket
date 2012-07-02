/**
 * 
 */
package org.apache.wicket.model.multiinheritance.data;

import java.io.Serializable;


/**
 * An abstract TestBean for multilevel model inheritance
 */
public abstract class TestBean implements Serializable
{
	private static final long serialVersionUID = 7421247633233526829L;

	private String name;

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
}
