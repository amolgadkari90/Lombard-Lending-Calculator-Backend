package lombard.lending.calculator.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class LoanRequestDto {
	
	BigDecimal portfolioValue; 
	BigDecimal loanAmount;

}
