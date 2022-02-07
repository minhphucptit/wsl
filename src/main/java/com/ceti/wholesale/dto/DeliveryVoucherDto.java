package com.ceti.wholesale.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

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
public class DeliveryVoucherDto {
	
	private String accNo;
	
    private String no;

    private String id;

    private String voucherCode;

    private Instant voucherAt;

    private String truckDriverId;

    private String truckLicensePlateNumber;

    private BigDecimal totalGoods;

    private String note;

    private String createdByFullName;

    private Instant createdAt;

    private String updateByFullName;

    private String companyId;

    private Instant updateAt;

    private List<GoodsInOutDto> goodsInOut;

    private TruckDriverDto truckDriver;

    private TruckDto truck;

    private CompanyDto company;

    private String factoryId;

    private String truckDriverFullName;
    
    private String salesmanId;
    
    private String salesmanFullName;

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
