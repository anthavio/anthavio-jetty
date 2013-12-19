/**
 * 
 */
package net.anthavio.jetty.spring;

import net.anthavio.jetty.test.InstanceManagerBase;
import net.anthavio.jetty.test.Jetty6InstanceManager;

/**
 * Spring test context loader starting Jetty instances
 * 
 * Cannot shutdown these instances though
 * 
 * @author vanek
 *
 */
public class Jetty6Loader extends JettyLoaderBase {

	@Override
	protected InstanceManagerBase<?> getInstanceManager() {
		return Jetty6InstanceManager.i();
	}
}
