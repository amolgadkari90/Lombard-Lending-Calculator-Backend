package lombard.lending.calculator.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombard.lending.calculator.dto.LoanResponseDto;
import lombard.lending.calculator.entity.LoanCalculation;
import lombard.lending.calculator.mapper.LoanMapper;
import lombard.lending.calculator.repository.LoanCalculationRepository;
import lombard.lending.calculator.exceptions.InvalidInputException;
import lombard.lending.calculator.exceptions.LoanProcessingException;

@ExtendWith(MockitoExtension.class)
class LoanCalculatorServiceTest {
	
	@Mock
	private LoanCalculationRepository repository;
	
	@Mock
	private LoanMapper mapper;
	
	@InjectMocks
	private LoanCalculatorService service;
	
	private BigDecimal portfolioValue;
	private BigDecimal loanAmount;

	@BeforeEach
	void setUp() throws Exception {
		portfolioValue = new BigDecimal("100000");
	    loanAmount = new BigDecimal("40000");
	}

	@Test
	void testCalculateAndSave_success() {
	    // given
	    LoanCalculation savedEntity = LoanCalculation.builder()
	            .id(1L)
	            .portfolioValue(portfolioValue)
	            .loanAmount(loanAmount)
	            .build();

	    LoanResponseDto responseDto = new LoanResponseDto();

	    when(repository.save(any(LoanCalculation.class)))
	            .thenReturn(savedEntity);
	    when(mapper.toLoanDto(savedEntity))
	            .thenReturn(responseDto);

	    // when
	    LoanResponseDto result = service.calculateAndSave(portfolioValue, loanAmount);

	    // then
	    assertNotNull(result);
	    verify(repository, times(1)).save(any(LoanCalculation.class));
	    verify(mapper, times(1)).toLoanDto(savedEntity);
	}

	//no records
	@Test
	void testGetLastFiveCalculations_emptyList() {
	    when(repository.findTop5ByOrderByCreatedAtDesc())
	            .thenReturn(Collections.emptyList());

	    List<LoanResponseDto> result = service.getLastFiveCalculations();

	    assertNotNull(result);
	    assertTrue(result.isEmpty());
	}
	
	
	//records exist
	
	@Test
	void testGetLastFiveCalculations_success() {
	    LoanCalculation entity = LoanCalculation.builder().id(1L).build();
	    List<LoanCalculation> entities = List.of(entity);

	    LoanResponseDto dto = new LoanResponseDto();

	    when(repository.findTop5ByOrderByCreatedAtDesc())
	            .thenReturn(entities);
	    when(mapper.toLoanDtoList(entities))
	            .thenReturn(List.of(dto));

	    List<LoanResponseDto> result = service.getLastFiveCalculations();

	    assertEquals(1, result.size());
	    verify(mapper).toLoanDtoList(entities);
	}

	
	//Loan amount ≥ portfolio amount
	
	@Test
	void testCalculateAndSave_shouldThrowException_whenLoanGreaterThanPortfolio() {

	    BigDecimal loanGreaterThanPortfolio = new BigDecimal("120000");

	    InvalidInputException ex = assertThrows(
	            InvalidInputException.class,
	            () -> service.calculateAndSave(portfolioValue, loanGreaterThanPortfolio)
	    );

	    assertEquals("Loan amount must be less than Portfolio value", ex.getMessage());
	}
	
//	//Portfolio value is 0
//	
//	@Test
//	void testCalculateAndSave_shouldThrowException_whenPortfolioIsZero() {
//
//	    InvalidInputException ex = assertThrows(
//	            InvalidInputException.class,
//	            () -> service.calculateAndSave(BigDecimal.ZERO, loanAmount)
//	    );
//
//	    assertEquals("Portfolio value must be greater than zero", ex.getMessage());
//	}
	
	//Loan amount is 0
	
	@Test
	void testCalculateAndSave_shouldThrowException_whenLoanIsZero() {

	    InvalidInputException ex = assertThrows(
	            InvalidInputException.class,
	            () -> service.calculateAndSave(portfolioValue, BigDecimal.ZERO)
	    );

	    assertEquals("Loan amount must be greater than zero", ex.getMessage());
	}
	
//	//Null input
//	
//	@Test
//	void testCalculateAndSave_shouldThrowException_whenInputsAreNull() {
//
//	    assertThrows(
//	            InvalidInputException.class,
//	            () -> service.calculateAndSave(null, loanAmount)
//	    );
//	}

	//Database failure test
	
	@Test
	void testCalculateAndSave_shouldThrowLoanProcessingException_whenDatabaseFails() {

	    when(repository.save(any(LoanCalculation.class)))
	            .thenThrow(new DataAccessException("DB down") {});

	    LoanProcessingException ex = assertThrows(
	            LoanProcessingException.class,
	            () -> service.calculateAndSave(portfolioValue, loanAmount)
	    );

	    assertTrue(ex.getMessage().contains("Unable to process loan"));
	}




}
