package net.anthavio.jetty.junit;

import net.anthavio.jetty.Jetty6Wrapper;
import net.anthavio.jetty.spring.Jetty6Loader;
import net.anthavio.jetty.test.Jetty6InstanceManager;
import net.anthavio.jetty.test.JettyConfig;
import net.anthavio.jetty.test.InstanceManagerBase.ServerSetupData;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;


/**
 * JUnit ClassRunner launching Jetty instances using @JettyConfig class annotations
 * 
 * @RunWith(Jetty6ClassRunner.class)
 * @JettyConfig(port = 0)
 * public class MyFunkyTest { ...
 * 
 * Similar can be done with @BeforeClass and @AfterClass annotations, but this is more convenient
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
		jettys = new ServerSetupData[configs.length];
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
			Jetty6Wrapper jetty = manager.startServer(jettys[i]);
		}
		//TODO set port to test field
		/*
		List<FrameworkField> list = getTestClass().getAnnotatedFields(JettyPort.class);
		for (FrameworkField field : list) {
			if (field.getField().getType() == int.class) {
				field.getField().setInt(field, 0);
			} else if (field.getField().getType() == Integer.class) {

			} else {
				throw new IllegalStateException("JettyPort annotated field must be of int or integer type");
			}
			
		}
		*/
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
