package basicEvaluator;

public class EvaluatorException extends Exception {
	private static final long serialVersionUID = 5913857484521417548L;

	public EvaluatorException() {
	}

	public EvaluatorException(String message) {
		super(message);
	}

	public EvaluatorException(Throwable cause) {
		super(cause);
	}

	public EvaluatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public EvaluatorException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
