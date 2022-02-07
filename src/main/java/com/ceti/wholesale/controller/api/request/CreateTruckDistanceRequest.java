package com.ceti.wholesale.controller.api.request;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateTruckDistanceRequest {

    private BigDecimal distance;
    
    private Instant day;
    
    private String truckLicensePlateNumber;
}
