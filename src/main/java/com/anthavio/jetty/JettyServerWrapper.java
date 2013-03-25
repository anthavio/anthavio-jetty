package com.anthavio.jetty;

/**
 * 
 * @author martin.vanek
 *
 */
public interface JettyServerWrapper {

	public void start();

	public void stop();

	public boolean isStarted();
}
