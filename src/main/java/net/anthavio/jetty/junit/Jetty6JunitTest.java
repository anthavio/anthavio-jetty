package net.anthavio.jetty.junit;

import java.net.MalformedURLException;
import java.net.URL;

import net.anthavio.jetty.Jetty6Wrapper;
import net.anthavio.jetty.spring.Jetty6Loader;
import net.anthavio.jetty.test.Jetty6InstanceManager;
import net.anthavio.jetty.test.JettyConfig;
import net.anthavio.jetty.test.InstanceManagerBase.ServerSetupData;

import org.apache.commons.lang.UnhandledException;


/**
 * Subclass to archieve similar to @RunWith(Jetty6ClassRunner.class)
 * 
 * @author martin.vanek
 *
 */
public abstract class Jetty6JunitTest {

	private Jetty6InstanceManager manager = Jetty6InstanceManager.i();

	protected final Jetty6Wrapper jetty;

	protected final URL url;

	public Jetty6JunitTest() {

		String systemUrl = System.getProperty("gateway.url");//FIXME parametrize system property name
		try {
			if (systemUrl != null) {
				URL url = new URL(systemUrl);
				if (url.getHost().equals("localhost")) {
					//start jetty with URL port
					JettyConfig[] configs = Jetty6Loader.getJettyConfigs(getClass());
					JettyConfig config = configs[0];
					ServerSetupData jsd = new ServerSetupData(config.home(), url.getPort(), config.configs(), config.cache());
					jetty = manager.startServer(jsd);
					if (url.getPort() == 0) {
						//jetty dynamic port
						int port = jetty.getPort();
						this.url = new URL(url.getProtocol(), url.getHost(), port, url.getFile());
					} else {
						this.url = url;
					}
				} else {
					//non localhost url -> remote test
					this.url = url;
					this.jetty = null;
				}
			} else {
				//no system property url -> start jetty
				JettyConfig[] configs = Jetty6Loader.getJettyConfigs(getClass());
				JettyConfig config = configs[0];
				ServerSetupData jsd = new ServerSetupData(config.home(), config.port(), config.configs(), config.cache());
				jetty = manager.startServer(jsd);
				int port = jetty.getPort();
				this.url = new URL("http://localhost:" + port);
			}
		} catch (MalformedURLException mux) {
			throw new UnhandledException(mux);
		}
	}

	public URL getURL() {
		return url;
	}
}