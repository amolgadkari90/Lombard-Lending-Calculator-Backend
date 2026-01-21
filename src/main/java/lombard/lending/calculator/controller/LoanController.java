package lombard.lending.calculator.controller;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombard.lending.calculator.dto.ApiResponse;
import lombard.lending.calculator.dto.LoanRequestDto;
import lombard.lending.calculator.dto.LoanResponseDto;
import lombard.lending.calculator.service.LoanCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/loans")
@RequiredArgsConstructor
@Slf4j
public class LoanController {
	
	private final LoanCalculatorService loanService ;
	
	@GetMapping("/health")
	public String health(){
		return "Loan service is UP";
	}
	
	
	//POST /api/loans/calculate
	@PostMapping("/calculate")
	ResponseEntity<ApiResponse<LoanResponseDto>> calculate(@RequestBody LoanRequestDto clientRequest, HttpServletRequest httpRequest){
		
		LoanResponseDto calculateAndSave = loanService.calculateAndSave(clientRequest.getPortfolioValue(), 
																		clientRequest.getLoanAmount());
		
		ApiResponse<LoanResponseDto> apiResponse = ApiResponse
															.<LoanResponseDto>builder()
															.success(true)
															.data(calculateAndSave)
															.message("LTV is calculated and saved successfully.")
															.path(httpRequest.getRequestURI())
															.statusCode(HttpStatus.OK.value())
															.timeStamp(OffsetDateTime.now())
															.build();
					
		
		return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);			
		
	}
	
	
	//GET /api/loans/history
	//Response: [array of last 5 calculations]
	
	@GetMapping("/history")
	 ApiResponse<List<LoanResponseDto>> getHistory(HttpServletRequest httpRequest){
		 List<LoanResponseDto> lastFiveCalculations = loanService.getLastFiveCalculations();
		 
		  ApiResponse<List<LoanResponseDto>> apiResponse = ApiResponse.<List<LoanResponseDto>>builder()
		  			.success(true)
		  			.data(lastFiveCalculations)
					.message("Last 5 calculations!")
					.path(httpRequest.getRequestURI())
					.statusCode(HttpStatus.OK.value())
					.timeStamp(OffsetDateTime.now())
					.build();
		 
		 return apiResponse;
	}


	
	 
	
	

}
