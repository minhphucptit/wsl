package com.ceti.wholesale.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class GasRefuelingVoucherDto {
	
	private String accNo;
	
    private String id;

    private String no;
    
    private Instant createdAt;
    
    private String createdByFullName;
    
    private Instant updateAt;
    
    private String updateByFullName;
    
    private String note;

    private BigDecimal totalGas = BigDecimal.ZERO;
    
    private Instant voucherAt;
    
    private String voucherCode;
    
    private String factoryId;
    
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<GoodsInOutDto> goodsInOut;
    
    @JsonGetter("voucher_at")
    public Object getVoucherAt() {
        try {
            return voucherAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("created_at")
    public Object getCreateAt() {
        try {
            return createdAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("update_at")
    public Object getUpdateAt() {
        try {
            return updateAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

}
