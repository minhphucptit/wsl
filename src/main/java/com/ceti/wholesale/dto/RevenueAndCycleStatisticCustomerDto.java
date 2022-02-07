package com.ceti.wholesale.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RevenueAndCycleStatisticCustomerDto {
		
	private BigDecimal totalBeginningDebt6;
	
	private BigDecimal totalBeginningDebt12;
	
	private BigDecimal totalBeginningDebt45;
	
	private BigDecimal totalIntermDebt6;
	
	private BigDecimal totalIntermDebt12;
	
	private BigDecimal totalIntermDebt45;
	
	private BigDecimal totalEndingDebt6;
	
	private BigDecimal totalEndingDebt12;
	
	private BigDecimal totalEndingDebt45;
	
	private BigDecimal totalRevenueDebt6;
	
	private BigDecimal totalRevenueDebt12;
	
	private BigDecimal totalRevenueDebt45;
	
	private BigDecimal totalCycle6;
	
	private BigDecimal totalCycle12;
	
	private BigDecimal totalCycle45;
	
	private List<RevenueAndCycleStatisticDto> revenueAndCycleStatistic;
}
