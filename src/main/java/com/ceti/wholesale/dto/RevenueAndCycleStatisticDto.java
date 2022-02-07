package com.ceti.wholesale.dto;

import java.math.BigDecimal;

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
public class RevenueAndCycleStatisticDto {
	
	private CustomerDto customer;
	
	private BigDecimal beginningDebt6;
	
	private BigDecimal beginningDebt12;
	
	private BigDecimal beginningDebt45;
	
	private BigDecimal intermDebt6;
	
	private BigDecimal intermDebt12;
	
	private BigDecimal intermDebt45;
	
	private BigDecimal endingDebt6;
	
	private BigDecimal endingDebt12;
	
	private BigDecimal endingDebt45;
	
	private BigDecimal revenueDebt6;
	
	private BigDecimal revenueDebt12;
	
	private BigDecimal revenueDebt45;
	
	private BigDecimal cycle6;
	
	private BigDecimal cycle12;
	
	private BigDecimal cycle45;

}
