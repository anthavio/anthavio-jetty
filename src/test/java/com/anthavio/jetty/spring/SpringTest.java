package com.anthavio.jetty.spring;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.anthavio.jetty.test.JettyConfig;

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
