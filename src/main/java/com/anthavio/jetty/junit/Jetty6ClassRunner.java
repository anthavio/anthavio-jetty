package com.anthavio.jetty.junit;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.anthavio.jetty.Jetty6Wrapper;
import com.anthavio.jetty.spring.Jetty6Loader;
import com.anthavio.jetty.test.InstanceManagerBase.ServerSetupData;
import com.anthavio.jetty.test.Jetty6InstanceManager;
import com.anthavio.jetty.test.JettyConfig;

/**
 * JUnit ClassRunner launching Jetty instances using @JettyConfig class annotations
 * 
 * @RunWith(Jetty6ClassRunner.class)
 * @JettyConfig(port = 0)
 * public class MyFunkyTest { ...
 * 
 * Similar can be done with @BeforeClass and @AfterClass annotations, but this is more convinient
 * 
 * @author martin.vanek
 *
 */
public class Jetty6ClassRunner extends BlockJUnit4ClassRunner {

	private Jetty6InstanceManager manager = Jetty6InstanceManager.i();

	private ServerSetupData[] jettys;

	public Jetty6ClassRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
		JettyConfig[] configs = Jetty6Loader.getJettyConfigs(testClass);
		for (int i = 0; i < configs.length; ++i) {
			JettyConfig config = configs[i];
			jettys[i] = new ServerSetupData(config.home(), config.port(), config.configs(), config.cache());
		}
	}

	public Jetty6Wrapper getJetty() {
		return manager.getServer(jettys[0]);
	}

	public Jetty6Wrapper getJetty(int index) {
		return manager.getServer(jettys[index]);
	}

	@Override
	protected Statement withBeforeClasses(Statement statement) {
		for (int i = 0; i < jettys.length; ++i) {
			manager.startServer(jettys[i]);
		}
		return super.withBeforeClasses(statement);
	}

	@Override
	protected Statement withAfterClasses(Statement statement) {
		Statement after = super.withAfterClasses(statement);
		//stop jettys
		for (int i = 0; i < jettys.length; ++i) {
			manager.stopServer(jettys[i]);
		}
		return after;
	}

}
