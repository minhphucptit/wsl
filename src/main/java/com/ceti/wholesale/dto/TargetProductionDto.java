package com.ceti.wholesale.dto;

import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;

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
public class TargetProductionDto {
	private String customerId;
	private String customerCode;
	private String customerName;
	private BigDecimal quantity= BigDecimal.ZERO;
	private Instant createAt;
	private String createBy;
	private Integer month;
	private Integer year;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal weightDiscount = BigDecimal.ZERO;
    private BigDecimal quantityWithoutDiscount = BigDecimal.ZERO;
	

    @JsonGetter("created_at")
    public Object getCreateAt() {
        try {
            return createAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

}
