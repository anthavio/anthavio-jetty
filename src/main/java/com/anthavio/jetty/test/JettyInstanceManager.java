package com.anthavio.jetty.test;

import com.anthavio.jetty.JettyWrapper;

/**
 * Jetty 7,8 instance manager
 * 
 * @author martin.vanek
 *
 */
public class JettyInstanceManager extends InstanceManagerBase<JettyWrapper> {

	private static JettyInstanceManager singleton = new JettyInstanceManager();

	public static JettyInstanceManager i() {
		return singleton;
	}

	private JettyInstanceManager() {

	}

	@Override
	protected JettyWrapper newServerInstance(ServerSetupData jsd) {
		return new JettyWrapper(jsd.getServerHome(), jsd.getPort(), jsd.getConfigs());
	}

}
