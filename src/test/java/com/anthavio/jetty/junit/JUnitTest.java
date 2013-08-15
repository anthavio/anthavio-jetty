package com.anthavio.jetty.junit;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.anthavio.jetty.test.JettyConfig;

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
		System.out.println("xxxxxxxxxxxxxyyyyy");
	}

}
