package com.ceti.wholesale.controller.api.request;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

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
public class UpdateTargetProductionRequest {

	@NotNull(message = "customerCode can not be null")
	private String customerCode;

	private String customerName;

	@NotNull(message = "quantity can not be null")
	private BigDecimal quantity;
	
	@NotNull(message = "month can not be null")
	private Integer month;
	
	@NotNull(message = "year can not be null")
	private Integer year;

	private BigDecimal discount;

	private BigDecimal weightDiscount;

	private BigDecimal quantityWithoutDiscount;

	private List<CreateOtherDiscountRequest> otherDiscounts;
}
