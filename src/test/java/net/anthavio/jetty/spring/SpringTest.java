package net.anthavio.jetty.spring;

import net.anthavio.jetty.test.JettyConfig;
import net.anthavio.jetty.test.JettyPort;

import org.fest.assertions.api.Assertions;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * 
 * @author martin.vanek
 *
 */
@JettyConfig(home = "src/test/jetty8", port = 0)
@ContextConfiguration(loader = JettyLoader.class)
@TestExecutionListeners(JettyLoader.class)
public class SpringTest extends AbstractTestNGSpringContextTests {

	@JettyPort
	public int port;

	@Test
	public void test() {
		Assertions.assertThat(port).isGreaterThan(0);
		//System.out.println("cxxxxxxxxxxxxxx");
	}
}
