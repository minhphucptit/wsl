package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PSDifferenceTrackingDto {
    private String factoryId;
    private String factoryName;
    private String truckLicensePlateNumber;
    private BigDecimal quota;
    private BigDecimal distance;
    private BigDecimal gasFollowGps;
    private BigDecimal doBonus;
    private BigDecimal totalQuantity;
    private BigDecimal psDifference;
    private BigDecimal totalDifferenceMonthBefore;
    private BigDecimal totalDifference;
    private TruckMonthlyFinalDoDto truckMonthlyFinalDo;

}
