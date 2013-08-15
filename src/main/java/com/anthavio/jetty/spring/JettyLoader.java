/**
 * 
 */
package com.anthavio.jetty.spring;

import com.anthavio.jetty.test.InstanceManagerBase;
import com.anthavio.jetty.test.JettyInstanceManager;

/**
 * @ContextConfiguration(loader = JettyLoader.class, locations = "example-child-context")
 * 
 * 
 * @author vanek
 *
 */
public class JettyLoader extends JettyLoaderBase {

	@Override
	protected InstanceManagerBase<?> getInstanceManager() {
		return JettyInstanceManager.i();
	}

}
