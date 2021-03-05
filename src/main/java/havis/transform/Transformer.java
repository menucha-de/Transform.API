package havis.transform;

import java.util.Map;

/**
 * Transforms one type to another
 */
public interface Transformer {

	/**
	 * Prefix used in properties
	 */
	final static String PREFIX = "Transformer.";

	/**
	 * Variable name in script based transformations
	 */
	final static String VARIABLE = "object";

	/**
	 * Initialize the transformer
	 * 
	 * 
	 * @param properties
	 *            The properties containing settings for transformation
	 * @throws ValidationException
	 *             If arguments are not valid
	 */
	void init(Map<String, String> properties) throws ValidationException;

	/**
	 * Transform the specified message
	 * 
	 * @param object
	 *            The object to transform
	 * @param <S>
	 *            The source type
	 * @param <T>
	 *            The destination type
	 * @return The transformed object
	 * @throws TransformException
	 *             If transformation fails
	 */
	<T, S> T transform(S object) throws TransformException;
}