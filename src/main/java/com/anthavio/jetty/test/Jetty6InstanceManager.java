package com.anthavio.jetty.test;

import com.anthavio.jetty.Jetty6Wrapper;

/**
 * Jetty 6 instance manager
 * 
 * @author martin.vanek
 *
 */
public class Jetty6InstanceManager extends InstanceManagerBase<Jetty6Wrapper> {

	private static Jetty6InstanceManager singleton = new Jetty6InstanceManager();

	public static Jetty6InstanceManager i() {
		return singleton;
	}

	private Jetty6InstanceManager() {

	}

	@Override
	protected Jetty6Wrapper newServerInstance(ServerSetupData jsd) {
		return new Jetty6Wrapper(jsd.getServerHome(), jsd.getPort(), jsd.getConfigs());
	}
}
