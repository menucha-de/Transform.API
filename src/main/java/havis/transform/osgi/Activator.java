package havis.transform.osgi;

import havis.transform.Transformer;
import havis.transform.TransformerFactory;
import havis.transform.TransformerProperties;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.PrototypeServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleWiring;

public class Activator implements BundleActivator {

	private final static Logger log = Logger.getLogger(Activator.class.getName());

	final static String NAME = "name";
	final static String SRC = "src";
	final static String DST = "dst";

	private List<ServiceRegistration<Transformer>> handlers = new ArrayList<>();

	@Override
	public void start(BundleContext context) throws Exception {
		ClassLoader current = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(context.getBundle().adapt(BundleWiring.class).getClassLoader());

			ServiceLoader<TransformerFactory> loader = ServiceLoader.load(TransformerFactory.class);
			Iterator<TransformerFactory> i = loader.iterator();
			while (i.hasNext()) {
				final TransformerFactory factory = i.next();
				TransformerProperties name = factory.getClass().getAnnotation(havis.transform.TransformerProperties.class);
				if (name != null) {

					Dictionary<String, Object> properties = new Hashtable<>();
					properties.put(NAME, name.value());
					properties.put(SRC, name.src());
					properties.put(DST, name.dst());

					log.log(Level.FINE, "Register prototype service factory {0} (''{1}'': ''{2}'')", new Object[] { Transformer.class.getName(), NAME, name.value() });
					handlers.add(context.registerService(Transformer.class, new PrototypeServiceFactory<Transformer>() {
						@Override
						public Transformer getService(Bundle bundle, ServiceRegistration<Transformer> registration) {
							return factory.newInstance();
						}

						@Override
						public void ungetService(Bundle bundle, ServiceRegistration<Transformer> registration, Transformer service) {
						}
					}, properties));

				} else {
					log.log(Level.FINE, "Missing adapter name for instance ''{0}''", factory.getClass().getName());
				}
			}
		} finally {
			Thread.currentThread().setContextClassLoader(current);
		}
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		for (ServiceRegistration<Transformer> handler : handlers)
			handler.unregister();
		handlers.clear();
	}
}