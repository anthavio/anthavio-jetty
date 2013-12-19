package net.anthavio.jetty.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.anthavio.jetty.JettyServerWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * Base class for server instance manager
 * 
 * @author martin.vanek
 *
 */
public abstract class InstanceManagerBase<T extends JettyServerWrapper> {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private Map<ServerSetupData, T> cache = new HashMap<ServerSetupData, T>();

	private void housekeeping(ServerSetupData newSetup) {
		Set<ServerSetupData> keySet = cache.keySet();
		for (ServerSetupData oldSetup : keySet) {
			T server = null;
			switch (oldSetup.cache) {
			case NEVER:
				//remove abandoned entries
				server = cache.remove(oldSetup);
				break;
			case CHANGE:
				//remove change driven entries
				if (!newSetup.equals(oldSetup)) {
					//XXX this will never work...
					server = cache.remove(oldSetup);
				}
				break;
			}
			if (server != null) {
				logger.debug("Cache remove " + oldSetup);
				try {
					if (server.isStarted()) {
						server.stop();
					}
				} catch (Exception x) {
					logger.warn("Exception while stopping server " + x);
				}
			}
		}
	}

	public T startServer(ServerSetupData setup) {
		//houskeeping first...
		housekeeping(setup);

		T server = cache.get(setup);
		if (server == null) {
			logger.debug("Cache miss " + setup);
			server = newServerInstance(setup);
			server.start();
			cache.put(setup, server);
		} else {
			logger.debug("Cache hit " + setup);
		}

		return server;
	}

	protected abstract T newServerInstance(ServerSetupData setup);

	/**
	 * For a good citizents that are cleaning after themselves
	 */
	public void stopServer(ServerSetupData setup) {
		if (setup.cache == Cache.NEVER) {
			T server = cache.remove(setup);
			if (server == null) {
				throw new IllegalArgumentException("Server not found in cache " + setup);
			}
			try {
				logger.debug("Cache remove " + setup);
				if (server.isStarted()) {
					server.stop();
				}
			} catch (Exception x) {
				logger.warn("Exception while stopping server " + x);
			}
		}
	}

	/**
	 * Return instance
	 */
	public T getServer(ServerSetupData setup) {
		return cache.get(setup);
	}

	public static enum Cache {
		NEVER, //don't cache
		FOREVER, //cache forever
		CHANGE; //cache until different instance is requested
	}

	/**
	 * This class duplicates @JettyConfig annotation simple because Java annotation 
	 * is not a regular class and cannot have methods (and we need override hashCode and equals)
	 * 
	 * @author martin.vanek
	 *
	 */
	public static class ServerSetupData {

		private final String serverHome;

		private final int port;

		private final String[] configs;

		private final Cache cache;

		public ServerSetupData(String jettyHome, int port, String[] configs, Cache cache) {
			this.serverHome = jettyHome;
			this.port = port;
			this.configs = configs;
			this.cache = cache;
		}

		public String getServerHome() {
			return serverHome;
		}

		public int getPort() {
			return port;
		}

		public String[] getConfigs() {
			return configs;
		}

		public Cache getCache() {
			return cache;
		}

		@Override
		public String toString() {
			return serverHome + " " + port + " " + Arrays.asList(configs) + " " + cache;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(configs);
			result = prime * result + ((serverHome == null) ? 0 : serverHome.hashCode());
			result = prime * result + port;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ServerSetupData other = (ServerSetupData) obj;
			if (!Arrays.equals(configs, other.configs))
				return false;
			if (serverHome == null) {
				if (other.serverHome != null)
					return false;
			} else if (!serverHome.equals(other.serverHome))
				return false;
			if (port != other.port)
				return false;
			return true;
		}

	}
}
