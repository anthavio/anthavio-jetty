/**
 * 
 */
package com.anthavio.jetty.spring;

import com.anthavio.jetty.test.JettyInstanceManager;
import com.anthavio.jetty.test.InstanceManagerBase;

/**
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
