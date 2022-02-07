package com.ceti.wholesale.controller.api.request;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateProductionMonitoringRequest {
	
	@Valid
	private List<CreateProductionMonitoringRequest.Create> productionMonitorings;
	
	private boolean checkExists;
	
    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @Accessors(chain = true)
	public static class Create{
    			
		private String customerId;
		
    	private String customerCode;
		
		private BigDecimal quantity;
		
		private Instant voucherAt;
		
		private Integer rowLocation;
		
	}

}
