package lombard.lending.calculator.entity;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "loan_calculations")
public class LoanCalculation {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	//1234567890123.45 (13 + 2 = 15)
	@Column(name = "portfolio_value", nullable = false, precision = 15, scale = 2)
	private BigDecimal portfolioValue;
	
	@Column(name = "loan_amount", nullable = false, precision = 15, scale = 2)
	private BigDecimal loanAmount;
	
	@Column(name  = "ltv", nullable = false, precision = 15, scale = 2)
	private BigDecimal ltv;
	
	@Column(name  = "safe_ltv", nullable = false, precision = 15, scale = 2)
	private BigDecimal safeLtv;
	
	@Column(name  = "warning_ltv", nullable = false, precision = 15, scale = 2)
	private BigDecimal warningLtv;	
	
	@Column(nullable = false, length = 20)
	private String status;
	
	@Column(name = "created_at", nullable =  false ,updatable = false)
	private OffsetDateTime createdAt ;
	
	@PrePersist
	void createdOn() {
		this.createdAt = OffsetDateTime.now(Clock.systemUTC());
	}

}
