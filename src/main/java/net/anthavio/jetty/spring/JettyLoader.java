/**
 * 
 */
package net.anthavio.jetty.spring;

import java.lang.reflect.Field;

import net.anthavio.jetty.JettyWrapper;
import net.anthavio.jetty.test.InstanceManagerBase;
import net.anthavio.jetty.test.InstanceManagerBase.ServerSetupData;
import net.anthavio.jetty.test.JettyInstanceManager;
import net.anthavio.jetty.test.JettyPort;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * Use like this...
 * 
 * @ContextConfiguration(loader = JettyLoader.class, locations = "spring-context-name")
 * @TestExecutionListeners(JettyLoader.class)
 * public class SpringTest extends AbstractTestNGSpringContextTests {
 * 
 * 	@JettyPort
 * 	public int port;
 *
 * 	@Test
 * 	public void test() {
 * 		Assertions.assertThat(port).isGreaterThan(0);
 * 	}
 * }
 * 
 * @author vanek
 *
 */
public class JettyLoader extends JettyLoaderBase implements TestExecutionListener {

	private ServerSetupData[] serverSetups;

	@Override
	protected InstanceManagerBase<JettyWrapper> getInstanceManager() {
		return JettyInstanceManager.i();
	}

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		serverSetups = getServerSetups(testContext.getTestClass());
	}

	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception {

		Field[] fields = testContext.getTestClass().getFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(JettyPort.class)) {
				if (field.getType() != int.class) {
					throw new IllegalStateException("@JettyPort annotated field must be of int type");
				}
				JettyPort annotation = field.getAnnotation(JettyPort.class);
				JettyWrapper jetty = getInstanceManager().getServer(serverSetups[annotation.jettyIndex()]);
				int[] ports = jetty.getLocalPorts();
				int port = ports[annotation.portIndex()];
				field.setAccessible(true);
				field.setInt(testContext.getTestInstance(), port);
			}
		}
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {

	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
	}

	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
	}

}
