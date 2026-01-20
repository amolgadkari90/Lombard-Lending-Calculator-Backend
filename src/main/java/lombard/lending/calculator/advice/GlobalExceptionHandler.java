package lombard.lending.calculator.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import lombard.lending.calculator.dto.ErrorResponse;
import lombard.lending.calculator.exceptions.BaseException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	/*
	 * Invalid business input 	-> InvalidInputException 			-> 400
	 * LTV rule violation 		-> LtvCalculationError 				-> 400
	 * Bean validation failure 	-> MethodArgumentNotValidException 	-> 400
	 * 
	 * */

	//Business Logic Errors 400
 @ExceptionHandler(BaseException.class)
 public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, WebRequest request) {

     log.warn("Business exception: {}", ex.getMessage());

     ErrorResponse errorResponse = ErrorResponse.builder()
             .success(false)
             .status(HttpStatus.BAD_REQUEST.value())
             .errorCode(ex.getErrorCode())
             .errorMessage(ex.getMessage())
             .path(getRequestPath(request))
             .build();

     return ResponseEntity.badRequest().body(errorResponse);
 }
 
 @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
 public ResponseEntity<ErrorResponse> handleUnreadableMessage(
         Exception ex,
         WebRequest request) {

     ErrorResponse errorResponse = ErrorResponse.builder()
             .success(false)
             .status(HttpStatus.BAD_REQUEST.value())
             .errorCode("INVALID_REQUEST_BODY")
             .errorMessage("Malformed JSON request")
             .path(getRequestPath(request))
             .build();

     return ResponseEntity.badRequest().body(errorResponse);
 }


 //Validation Errors (@Valid)
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<ErrorResponse> handleValidationException(
         MethodArgumentNotValidException ex,
         WebRequest request) {

     Map<String, String> errors = new HashMap<>();

     ex.getBindingResult().getAllErrors().forEach(error -> {
         String field = ((FieldError) error).getField();
         String message = error.getDefaultMessage();
         errors.put(field, message);
     });

     ErrorResponse errorResponse = ErrorResponse.builder()
             .success(false)
             .status(HttpStatus.BAD_REQUEST.value())
             .errorCode("VALIDATION_FAILED")
             .errorMessage("Validation failed")
             .errors(errors)
             .path(getRequestPath(request))
             .build();

     return ResponseEntity.badRequest().body(errorResponse);
 }

 //Generic Errors 500
 /*
  * Unexpected bug	-> Exception 	-> 500
  * */
 @ExceptionHandler(Exception.class)
 public ResponseEntity<ErrorResponse> handleUnexpectedException(
         Exception ex,
         WebRequest request) {

     log.error("Unhandled exception", ex);

     ErrorResponse errorResponse = ErrorResponse.builder()
             .success(false)
             .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
             .errorCode("INTERNAL_SERVER_ERROR")
             .errorMessage("Something went wrong. Please contact support.")
             .path(getRequestPath(request))
             .build();

     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(errorResponse);
 }

 private String getRequestPath(WebRequest request) {
     return request.getDescription(false).replace("uri=", "");
 }
	
	

}
