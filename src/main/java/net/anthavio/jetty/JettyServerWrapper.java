package net.anthavio.jetty;

/**
 * Common interface for ServerWrappers
 * 
 * @author martin.vanek
 *
 */
public interface JettyServerWrapper {

	public void start();

	public void stop();

	public boolean isStarted();
}
