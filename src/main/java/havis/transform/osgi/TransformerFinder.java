package havis.transform.osgi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import havis.transform.TransformerFactory;

public class TransformerFinder {

	private final static Logger log = Logger.getLogger(TransformerFinder.class.getName());

	public static List<String> find(BundleContext context, Class<?> src, Class<?> dst) {
		List<String> names = new ArrayList<>();
		try {
			for (ServiceReference<TransformerFactory> ref : context.getServiceReferences(TransformerFactory.class, null)) {
				if (src.isAssignableFrom((Class<?>) ref.getProperty(Activator.SRC)) && dst.isAssignableFrom((Class<?>) ref.getProperty(Activator.DST))) {
					names.add((String) ref.getProperty(Activator.NAME));
				}
			}
		} catch (InvalidSyntaxException e) {
			log.log(Level.FINE, "Filter is invalid", e);
		}
		return names;
	}

	public static ServiceReference<TransformerFactory> get(BundleContext context, String name) {
		try {
			String filter = "(name=" + name + ")";
			for (ServiceReference<TransformerFactory> ref : context.getServiceReferences(TransformerFactory.class, filter)) {
				return ref;
			}
		} catch (InvalidSyntaxException e) {
			log.log(Level.FINE, "Filter is invalid", e);
		}
		return null;
	}
}