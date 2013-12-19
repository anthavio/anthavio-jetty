/**
 * 
 */
package net.anthavio.jetty.spring;

import net.anthavio.jetty.test.InstanceManagerBase;
import net.anthavio.jetty.test.JettyInstanceManager;

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
