package lombard.lending.calculator.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import lombard.lending.calculator.dto.LoanResponseDto;
import lombard.lending.calculator.entity.LoanCalculation;

@Mapper(componentModel = "Spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LoanMapper {
	
    @Mapping(source = "safeLtv", target = "maxLoanAmount")
    @Mapping(source = "warningLtv", target = "marginCallThreshold")
	LoanResponseDto toLoanDto(LoanCalculation loanCalculation);
	
	List<LoanResponseDto> toLoanDtoList(List<LoanCalculation> loanCalculations);


}
