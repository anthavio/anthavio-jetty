package net.anthavio.jetty.junit;

import java.lang.reflect.Field;
import java.util.List;

import net.anthavio.jetty.Jetty6Wrapper;
import net.anthavio.jetty.ServerWrapper;
import net.anthavio.jetty.spring.Jetty6Loader;
import net.anthavio.jetty.test.InstanceManagerBase.ServerSetupData;
import net.anthavio.jetty.test.Jetty6InstanceManager;
import net.anthavio.jetty.test.JettyPort;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
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

	private ServerSetupData[] serverSetups;

	public Jetty6ClassRunner(Class<?> testClass) throws InitializationError {
		super(testClass);
		serverSetups = Jetty6Loader.getServerSetups(testClass);
	}

	/*
		public Jetty6Wrapper getJetty() {
			return manager.getServer(serverSetups[0]);
		}

		public Jetty6Wrapper getJetty(int index) {
			return manager.getServer(serverSetups[index]);
		}
	*/
	@Override
	protected Statement withBeforeClasses(Statement statement) {
		for (int i = 0; i < serverSetups.length; ++i) {
			ServerWrapper jetty = manager.startServer(serverSetups[i]);
		}
		return super.withBeforeClasses(statement);
	}

	@Override
	protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
		List<FrameworkField> list = getTestClass().getAnnotatedFields(JettyPort.class);
		for (FrameworkField field : list) {
			if (field.getField().getType() == int.class) {
				try {
					injectPort(field.getField(), target);
				} catch (Exception x) {
					throw new IllegalStateException("Failed to inject jetty port", x);
				}
			} else {
				throw new IllegalStateException("@JettyPort annotated field must be of int type");
			}

		}
		return super.withBefores(method, target, statement);
	}

	private void injectPort(Field field, Object target) throws IllegalArgumentException, IllegalAccessException {
		JettyPort annotation = field.getAnnotation(JettyPort.class);
		Jetty6Wrapper jetty = manager.getServer(serverSetups[annotation.jettyIndex()]);
		int[] ports = jetty.getLocalPorts();
		int port = ports[annotation.portIndex()];
		field.setInt(target, port);
	}

	@Override
	protected Statement withAfterClasses(Statement statement) {
		Statement after = super.withAfterClasses(statement);
		//stop jettys
		for (int i = 0; i < serverSetups.length; ++i) {
			manager.stopServer(serverSetups[i]);
		}
		return after;
	}

}
