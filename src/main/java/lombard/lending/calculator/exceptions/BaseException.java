package lombard.lending.calculator.exceptions;

import java.util.Arrays;

public abstract class BaseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4798546755748932382L;
	private final String errorCode;
	private final Object[] args;

	protected BaseException(String message, String errorCode) {
		this(message, errorCode, null, (Object[]) null);
	}

	protected BaseException(String message, String errorCode, Throwable cause) {
		this(message, errorCode, cause, (Object[]) null);
	}

	protected BaseException(String message, String errorCode, Object... args) {
		this(message, errorCode, null, args);
	}

	protected BaseException(String message, String errorCode, Throwable cause, Object... args) {
		super(message, cause);
		this.errorCode = errorCode;
		this.args = args;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public Object[] getArgs() {
		return args;
	}

	@Override
	public String toString() {
		return "BaseException{" + "message='" + getMessage() + '\'' + ", errorCode='" + errorCode + '\'' + ", args="
				+ Arrays.toString(args) + '}';
	}
}
