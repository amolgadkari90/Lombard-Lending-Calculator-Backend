package lombard.lending.calculator.exceptions;

public class GenericException extends BaseException{


	/**
	 * 
	 */
	private static final long serialVersionUID = 207402088044821082L;
	private static final String ERROR_CODE="GENERIC_EXCEPTION";
	
    public GenericException(String message) {
        super(message, ERROR_CODE);
    }

    public GenericException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }

    public GenericException(String message, Object... args) {
        super(message, ERROR_CODE, args);
    }

    public GenericException(String message, Throwable cause, Object... args) {
        super(message, ERROR_CODE, cause, args);
    }
}
