package com.ceti.wholesale.controller.api.request.v2;

import java.math.BigDecimal;
import java.time.Instant;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateProductionMonitoringRequest {
	
    @NotNull(message = "customer_id can not be null")
	private String customerId;
	
    @NotNull(message = "quantity can not be null")
	private BigDecimal quantity;
	
    @NotNull(message = "voucher_at can not be null")
	private Instant voucherAt;

}
