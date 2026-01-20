package lombard.lending.calculator.exceptions;

public class LoanProcessingException extends BaseException {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1949480593261706062L;
	private static final String ERROR_CODE = "LOAN_CALCULATION_SAVING_EXCEPTION";
	
	public LoanProcessingException(String message, Throwable cause) {
		super(message, ERROR_CODE, cause);
		// TODO Auto-generated constructor stub
	}
	


}
