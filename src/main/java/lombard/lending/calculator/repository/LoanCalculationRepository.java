package lombard.lending.calculator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lombard.lending.calculator.entity.LoanCalculation;

@Repository
public interface LoanCalculationRepository extends JpaRepository<LoanCalculation, Long> {

	List<LoanCalculation> findTop5ByOrderByCreatedAtDesc();

}
