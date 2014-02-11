package net.anthavio.jetty;

/**
 * Common interface for ServerWrappers
 * 
 * @author martin.vanek
 *
 */
public interface ServerWrapper {

	public void start();

	public void stop();

	public boolean isStarted();

	public int[] getLocalPorts();

}
