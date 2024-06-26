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
public class RevenueTruckAndDriverStatisticDto {
	
	private String factoryName;
	
	private String truckOrDriver;
	
	private BigDecimal gasQuantity;
	
	private BigDecimal gasKg;
	
	private BigDecimal gbonQuantity;
	
	private BigDecimal weight;

}
