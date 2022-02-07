package com.ceti.wholesale.dto.plan;

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
public class ProductionMonitoringStatisticDataDto {
	
	private String customerCode;
	
	private String customerName;
	
	private String companyId;

	private String region;

	private String customerCategory;
	
	private BigDecimal quantity;
	
	private Instant voucherAt;
	
	private BigDecimal totalQuantity;

	private BigDecimal lastMonthTotalQuantity;

	private BigDecimal targetQuantity;
	
	private Boolean isTotal;
	
	private Boolean isTotalRegion;
	
	private Boolean isTotalCompany;

    @JsonGetter("voucher_at")
    public Object getVoucherAt() {
        try {
            return voucherAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

}
