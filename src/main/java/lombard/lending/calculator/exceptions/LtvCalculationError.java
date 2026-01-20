package lombard.lending.calculator.exceptions;

public class LtvCalculationError extends BaseException {	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8557269029038543624L;	
	private static final String ERROR_CODE="LTV_CALCULATION_FAILED";

    public LtvCalculationError(String message) {
        super(message, ERROR_CODE);
    }

    public LtvCalculationError(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }

    public LtvCalculationError(String message, Object... args) {
        super(message, ERROR_CODE, args);
    }

    public LtvCalculationError(String message, Throwable cause, Object... args) {
        super(message, ERROR_CODE, cause, args);
    }

}
