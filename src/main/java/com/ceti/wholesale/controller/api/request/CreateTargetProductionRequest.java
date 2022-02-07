package com.ceti.wholesale.controller.api.request;

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
public class CreateTargetProductionRequest {
	
	private String customerCode;

	private String customerName;

	private BigDecimal quantity;

	private Integer month;

	private Integer year;
	
	private Integer rowLocation;

	private BigDecimal discount;

	private BigDecimal weightDiscount;

	private BigDecimal quantityWithoutDiscount;

	private List<CreateOtherDiscountRequest> otherDiscounts;

}
