package lombard.lending.calculator.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude (JsonInclude.Include.NON_NULL)
public class ApiResponse <T> {
	
	/*
	 * Success = True/False
	 * Data
	 * Message 
	 * Path
	 * StatusCode
	 * TimeStamp 
	 */
	
	 private boolean success; 
	 private T data;
	 private String message; 
	 private String path;
	 private int statusCode;
	 private OffsetDateTime timeStamp;
	 

}
