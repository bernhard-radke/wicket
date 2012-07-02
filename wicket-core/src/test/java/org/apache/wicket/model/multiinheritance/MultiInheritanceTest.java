/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.model.multiinheritance;

import org.apache.wicket.Component;
import org.apache.wicket.WicketTestCase;
import org.apache.wicket.model.multiinheritance.data.Level1Bean;
import org.apache.wicket.model.multiinheritance.data.Level2Bean;
import org.apache.wicket.model.multiinheritance.data.RootBean;

/**
 * Test Model inheritance over more than on level of inheritance
 */
public class MultiInheritanceTest extends WicketTestCase
{
	/**
	 * Test that Model inheritance works over more than one level of inheritance
	 */
	public void testMultipleModelInheritance()
	{
		MultiInheritancePage page = new MultiInheritancePage(generateBeanTree());

		tester.startPage(page);


		tester.assertModelValue("beanTree", page.rootBean);
		tester.assertModelValue("beanTree:subBean", page.rootBean.getLevel1());
		tester.assertModelValue("beanTree:subBean:subBean", page.rootBean.getLevel1().getLevel2());
		Component level2Panel = page.get("beanTree:subBean:subBean");
		page.get("beanTree").detach();

		tester.clickLink("beanTree:subBean:subBean:clickme", true);
		tester.isComponentOnAjaxResponse(level2Panel);
		tester.assertModelValue("beanTree:subBean:subBean", page.rootBean.getLevel1().getLevel2());
		tester.assertLabel("beanTree:subBean:subBean:name", "new level2");
	}

	private RootBean generateBeanTree()
	{
		RootBean root = new RootBean();
		root.setName("root");

		Level1Bean lvl1 = new Level1Bean();
		lvl1.setName("lvl1");
		root.setLevel1(lvl1);

		Level2Bean lvl2 = new Level2Bean();
		lvl2.setName("lvl2");
		lvl1.setLevel2(lvl2);

		return root;
	}
}
