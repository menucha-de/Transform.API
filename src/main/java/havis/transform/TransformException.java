package havis.transform;

import java.util.concurrent.ExecutionException;

public class TransformException extends ExecutionException {

	private static final long serialVersionUID = 1L;

	public TransformException() {
		super();
	}

	public TransformException(String message) {
		super(message);
	}

	public TransformException(String message, Throwable cause) {
		super(message, cause);
	}
}