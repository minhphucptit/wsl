package com.ceti.wholesale.controller.api.request;

import java.math.BigDecimal;

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
public class CreateTruckMonthlyFinalDoRequest {
	
    @NotNull(message = "month can not be null")
	private Integer month;
	
    @NotNull(message = "year can not be null")
	private Integer year;
    
    @NotNull(message = "truck_license_plate_number can not be null")
	private String truckLicensePlateNumber;
    
    @NotNull(message = "factory_id can not be null")
    private String factoryId;
    
    private BigDecimal finalDo = BigDecimal.valueOf(0);

}
