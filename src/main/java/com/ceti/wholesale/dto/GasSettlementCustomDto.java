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
public class GasSettlementCustomDto {
	
	private BigDecimal totalQuantity;

	private BigDecimal totalDistance;
	
	private BigDecimal quota;
	
	private BigDecimal gasFollowGps;

	private BigDecimal doRatio;
	
	private BigDecimal psDifferenceInMonth;
	
	private BigDecimal psDifferenceInMonthBefore;
	
	private BigDecimal difference;
	
	private List<GasSettlementDto> gasSettlements;
}
