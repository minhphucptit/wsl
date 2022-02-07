package com.ceti.wholesale.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonGetter;
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
public class GasSettlementDto {
		
	private Instant day;
	
	private BigDecimal quantity;
	
	private BigDecimal distance;
	
	private String truckLicensePlateNumber;
	
    @JsonGetter("day")
    public Object getDay() {
        try {
            return day.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

}
