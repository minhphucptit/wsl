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
public class ProductionMonitoringDto {
		
	private String customerId;
	
	private String customerCode;
	
	private String customerName;
	
	private BigDecimal quantity;
	
	private Instant voucherAt;
	
	private Instant createAt;
	
	private String createBy;
	
	@JsonGetter("create_at")
	public Object getCreateAt() {
	    try {
	        return createAt.getEpochSecond();
	    } catch (Exception e) {
	        return null;
	    }
	}
	
    @JsonGetter("voucher_at")
    public Object getVoucherAt() {
        try {
            return voucherAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

}
