package net.anthavio.jetty.spring;

import net.anthavio.jetty.spring.JettyLoader;
import net.anthavio.jetty.test.JettyConfig;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;


/**
 * 
 * @author martin.vanek
 *
 */
@JettyConfig(home = "src/test/jetty8", port = 0)
@ContextConfiguration(loader = JettyLoader.class)
public class SpringTest extends AbstractTestNGSpringContextTests {

	@Test
	public void test() {
		System.out.println("cxxxxxxxxxxxxxx");
	}
}
