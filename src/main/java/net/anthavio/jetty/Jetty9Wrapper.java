package net.anthavio.jetty;

/**
 * Jetty 9 has Connectors redesigned and requires Java7. Screw it!
 * 
 * @author martin.vanek
 *
 */
public class Jetty9Wrapper extends JettyWrapper {

	public Jetty9Wrapper() {
		this(System.getProperty("jetty.home", DEFAULT_JETTY_HOME));
	}

	public Jetty9Wrapper(String jettyHome) {
		this(jettyHome, -1, jettyHome + DEFAULT_JETTY_CFG);
	}

	public Jetty9Wrapper(String jettyHome, String... configs) {
		this(jettyHome, -1, configs);
	}

	public Jetty9Wrapper(String jettyHome, int port) {
		this(jettyHome, port, jettyHome + DEFAULT_JETTY_CFG);
	}

	public Jetty9Wrapper(String jettyHome, int port, String... configs) {
		super(jettyHome, port, configs);
		throw new UnsupportedOperationException("Jetty 9 is not yet supported");
	}
	/*
		protected int getLocalPort(Server server) {
			List<NetworkConnector> connectors = getNetworkConnectors(server);
			if (connectors == null || connectors.size() == 0) {
				throw new IllegalStateException("Cannot find port. No NetworkConnector is configured for server");
			}
			//use port of the first connector
			int port = connectors.get(0).getLocalPort();

			//warn if more is found
			if (connectors.size() > 1) {
				List<Integer> ports = new LinkedList<Integer>();
				for (NetworkConnector connector : connectors) {
					ports.add(connector.getLocalPort());
				}
				log.info("Multile NetworkConnectors configured. Ports " + ports);
			}
			return port;
		}

		private List<NetworkConnector> getNetworkConnectors(Server server) {
			Connector[] connectors = server.getConnectors();
			if (connectors == null || connectors.length == 0) {
				throw new IllegalStateException("Cannot find port. No Connector is configured for server");
			}
			List<NetworkConnector> ret = new ArrayList<NetworkConnector>();
			for (Connector connector : connectors) {
				if (connector instanceof NetworkConnector) {
					ret.add((NetworkConnector) connector);
				}
			}
			return ret;
		}
	*/
}
