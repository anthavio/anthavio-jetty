package net.anthavio.jetty;

/**
 * To distinguish jetty8 from jetty9 wrappers
 * 
 * @author martin.vanek
 *
 */
public class Jetty8Wrapper extends JettyWrapper {

	public Jetty8Wrapper() {
		this(System.getProperty("jetty.home", DEFAULT_JETTY_HOME));
	}

	public Jetty8Wrapper(String jettyHome) {
		this(jettyHome, -1, jettyHome + DEFAULT_JETTY_CFG);
	}

	public Jetty8Wrapper(String jettyHome, String... configs) {
		this(jettyHome, -1, configs);
	}

	public Jetty8Wrapper(String jettyHome, int port) {
		this(jettyHome, port, jettyHome + DEFAULT_JETTY_CFG);
	}

	/**
	 * @param port < 0 - jetty.xml, port 0 - dynamic, port > 0 static
	 */
	public Jetty8Wrapper(String jettyHome, int port, String... configs) {
		super(jettyHome, port, configs);
	}
}
