/**
 * 
 */
package com.anthavio.jetty.spring;

import com.anthavio.jetty.test.Jetty6InstanceManager;
import com.anthavio.jetty.test.InstanceManagerBase;

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
