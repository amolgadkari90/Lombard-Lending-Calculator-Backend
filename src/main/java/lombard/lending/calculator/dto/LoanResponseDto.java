package lombard.lending.calculator.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponseDto {
	
//	Response: { 
//		  "ltv": 40.0, 
//		  "status": "APPROVED",
//		  "maxLoanAmount": 50000,
//		  "marginCallThreshold": 60000,
//		  "timestamp": "2026-01-16T10:30:00Z"
//		}
	
	private BigDecimal ltv;
	
	private String status;
	
	private BigDecimal maxLoanAmount;
	
	private BigDecimal marginCallThreshold;
	
	private OffsetDateTime createdAt ;

}
