package lombard.lending.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombard.lending.calculator.dto.LoanResponseDto;
import lombard.lending.calculator.entity.LoanCalculation;
import lombard.lending.calculator.enums.LoanStatus;
import lombard.lending.calculator.exceptions.GenericException;
import lombard.lending.calculator.exceptions.InvalidInputException;
import lombard.lending.calculator.exceptions.LoanProcessingException;
import lombard.lending.calculator.exceptions.LtvCalculationError;
import lombard.lending.calculator.mapper.LoanMapper;
import lombard.lending.calculator.repository.LoanCalculationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanCalculatorService {
	
	private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
	private static final BigDecimal SAFE_LTV = BigDecimal.valueOf(50);
	private static final BigDecimal WARNING_LTV = BigDecimal.valueOf(60);
	
	private final LoanCalculationRepository repository;
	private final LoanMapper mapper;
	//private final LoanCalculation loanCalculation ;
	//private final LoanResponseDto response;
	
	public LoanResponseDto calculateAndSave(BigDecimal portfolioValue, BigDecimal loanAmount){
				
		validateInputs(portfolioValue, loanAmount);
		
	    BigDecimal ltv = loanAmount
	            						.divide(portfolioValue, 10, RoundingMode.HALF_UP)
	            						.multiply(HUNDRED)
	            						.setScale(2, RoundingMode.HALF_UP);
	    
	    BigDecimal safeLtv = portfolioValue
	            						.multiply(SAFE_LTV)
	            						.divide(HUNDRED, 2, RoundingMode.HALF_UP);

	    BigDecimal warningLtv = portfolioValue
	            						.multiply(WARNING_LTV)
	            						.divide(HUNDRED, 2, RoundingMode.HALF_UP);
				 
		LoanStatus status = determineStatus(ltv);
		
		LoanCalculation calculated = LoanCalculation
									.builder()
									.portfolioValue(portfolioValue)
									.loanAmount(loanAmount)
									.ltv(ltv)
									.safeLtv(safeLtv)
									.warningLtv(warningLtv)
									.status(status.name())
									.build();
		
		LoanCalculation savedLoan ;
		
		try {
		    savedLoan = repository.save(calculated);
		    log.info("Loan calculation persisted | loanId={}", savedLoan.getId());
		    return mapper.toLoanDto(savedLoan);

		} catch (DataAccessException ex) {
		    // database failure
		    log.error(
		        "Database error while saving loan | portfolioValue={} loanAmount={}",
		        portfolioValue, loanAmount, ex
		    );

		    throw new LoanProcessingException(
		        "Unable to process loan at the moment. Please try again later.",ex );

		} catch (Exception ex) {
		    // unexpected system failure
		    log.error("Unexpected error during loan calculation saving.", ex);

		    throw new LoanProcessingException(
		        "Unexpected error occurred while saving loan.", ex);
		}		
		
	}
	
	//
	private void validateInputs(BigDecimal portfolioValue, BigDecimal loanAmount) {
		
		if (portfolioValue.compareTo(loanAmount) <= 0) {
	        throw new InvalidInputException("Loan amount must be less than Portfolio value");
	    }
		
	    if (portfolioValue == null || loanAmount == null) {
	        throw new InvalidInputException("Portfolio value and loan amount must not be null");
	    }

	    if (portfolioValue.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new InvalidInputException("Portfolio value must be greater than zero");
	    }

	    if (loanAmount.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new InvalidInputException("Loan amount must be greater than zero");
	    }
	    
	}
	
	//
	private LoanStatus determineStatus(BigDecimal ltv) {
		if(ltv.compareTo(SAFE_LTV) <= 0) {
			return LoanStatus.APPROVED;
		}else if (ltv.compareTo(WARNING_LTV) <= 0) {
			return LoanStatus.REVIEW_REQUIRED;
		}
		return LoanStatus.REJECTED;
	}


	@Transactional(readOnly = true)
	public List<LoanResponseDto> getLastFiveCalculations() {
		// TODO Auto-generated method stub
		
		//<QUERY TYPE><LIMIT>By<CONDITIONS>OrderBy<SORT FIELD><DIRECTION>
		//SELECT *FROM loan_calculations ORDER BY created_at DESC LIMIT 5;
		//find, read, get, query -> SELECT
		//top, top5, first, first5 -> LIMIT 5
		//By -> Conditions start here
		//OrderBy -> sorting starts
		//CreatedAt -> entity field name (must match Java field, not DB column)
		//Desc -> descending order

		List<LoanCalculation> listLastFiveRecords = repository.findTop5ByOrderByCreatedAtDesc();
		if (listLastFiveRecords.isEmpty()) {
		    return Collections.emptyList();
		}		
		return mapper.toLoanDtoList(listLastFiveRecords);
	}
	
}
