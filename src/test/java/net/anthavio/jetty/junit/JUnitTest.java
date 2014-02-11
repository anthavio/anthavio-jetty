package net.anthavio.jetty.junit;

import net.anthavio.jetty.test.JettyConfig;
import net.anthavio.jetty.test.JettyPort;

import org.fest.assertions.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author martin.vanek
 *
 */
@RunWith(Jetty6ClassRunner.class)
@JettyConfig(home = "src/test/jetty6", port = 0)
public class JUnitTest {

	//@Rule
	//public MethodRule rule;

	@JettyPort
	public int port;

	@Test
	public void test() {
		Assertions.assertThat(port).isGreaterThan(0);//FIXME does not work yet!
		System.out.println("Jetty6ClassRunner " + port);
	}
}
