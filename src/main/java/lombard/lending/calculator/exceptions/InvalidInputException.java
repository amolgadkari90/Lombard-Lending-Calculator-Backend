package lombard.lending.calculator.exceptions;

public class InvalidInputException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5109311059635318447L;

	private static final String ERROR_CODE = "INVALID_INPUT_EXCEPTION";

	public InvalidInputException(String message) {
		super(message, ERROR_CODE);
	}

	public InvalidInputException(String message, Throwable cause) {
		super(message, ERROR_CODE, cause);
	}

	public InvalidInputException(String message, Object... args) {
		super(message, ERROR_CODE, args);
	}

	public InvalidInputException(String message, Throwable cause, Object... args) {
		super(message, ERROR_CODE, cause, args);
	}

}
