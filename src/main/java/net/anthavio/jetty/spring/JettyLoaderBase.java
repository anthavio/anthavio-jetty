/**
 * 
 */
package net.anthavio.jetty.spring;

import java.util.LinkedList;
import java.util.List;

import net.anthavio.jetty.test.InstanceManagerBase;
import net.anthavio.jetty.test.InstanceManagerBase.ServerSetupData;
import net.anthavio.jetty.test.JettyConfig;
import net.anthavio.jetty.test.JettyConfigs;
import net.anthavio.spring.test.ContextRefLoader;

import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

/**
 * Spring test context loader starting Jetty instances
 * 
 * Cannot shutdown these instances though, because there is no stop/shutdown callback/lifecycle
 * 
 * @author vanek
 *
 */
public abstract class JettyLoaderBase extends ContextRefLoader {

	@Override
	public String[] processLocations(Class<?> testClass, String... locations) {

		JettyConfig[] configs = getJettyConfigs(testClass);
		ServerSetupData[] serverSetups = new ServerSetupData[configs.length];
		for (int i = 0; i < configs.length; ++i) {
			JettyConfig config = configs[i];
			serverSetups[i] = new ServerSetupData(config.home(), config.port(), config.configs(), config.cache());
			getInstanceManager().startServer(serverSetups[i]);
		}
		if (locations.length == 0) {
			//no spring context locations -> jetty start only
			return locations;
		} else {
			return super.processLocations(testClass, locations);
		}
	}

	protected abstract InstanceManagerBase<?> getInstanceManager();

	/**
	 * We must to override this because jetty shutdown brings down Web contexts with Spring contexts 
	 * and our own ContextRefLoader shutdown hooks will do mess 
	 */
	@Override
	public AbstractApplicationContext loadContext(String... locations) throws Exception {
		if (locations.length == 0) {
			//no spring context locations -> jetty start only
			logger.info("No location are specified. Returning empty context");
			return new StaticApplicationContext();
		} else {
			logger.info("Loading " + locations[0] + " context with selector " + locations[1]);
			BeanFactoryLocator locator = ContextSingletonBeanFactoryLocator.getInstance(locations[1]);
			BeanFactoryReference reference = locator.useBeanFactory(locations[0]);
			AbstractApplicationContext context = (AbstractApplicationContext) reference.getFactory();
			return context;
		}
	}

	public static ServerSetupData[] getServerSetups(Class<?> testClass) {
		JettyConfig[] jettyConfigs = getJettyConfigs(testClass);
		ServerSetupData[] serverSetups = new ServerSetupData[jettyConfigs.length];
		for (int i = 0; i < jettyConfigs.length; ++i) {
			JettyConfig config = jettyConfigs[i];
			serverSetups[i] = new ServerSetupData(config.home(), config.port(), config.configs(), config.cache());
		}
		return serverSetups;
	}

	public static JettyConfig[] getJettyConfigs(Class<?> testClass) {
		JettyConfigs multiConfig = testClass.getAnnotation(JettyConfigs.class);
		JettyConfig singleConfig = testClass.getAnnotation(JettyConfig.class);
		if ((multiConfig == null || multiConfig.value().length == 0) && singleConfig == null) {
			throw new IllegalArgumentException("Annotation @JettyConfig(s) not present on " + testClass.getName());
		}

		if (multiConfig != null && multiConfig.value().length != 0 && singleConfig != null) {
			throw new IllegalArgumentException("Both Annotations @JettyConfig(s) are present on " + testClass.getName());
		}
		List<JettyConfig> list = new LinkedList<JettyConfig>();
		if (singleConfig != null) {
			list.add(singleConfig);
		}
		if (multiConfig != null) {
			JettyConfig[] configs = multiConfig.value();
			for (JettyConfig config : configs) {
				list.add(config);
			}
		}
		return list.toArray(new JettyConfig[list.size()]);
	}
}
