package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FactoryRotationVoucherDto {

    private String id;

    private String no;

    private String voucherCode;

    private Instant voucherAt;

    private String companyExportId;

    private String companyImportId;

    private String companyExportName;

    private String companyImportName;

    private String truckDriverId;

    private String truckLicensePlateNumber;

    private String createdByFullName;

    private Instant createdAt;

    private Instant updateAt;

    private String updateByFullName;

    private String note;

    private BigDecimal totalGoods = BigDecimal.ZERO;

    private List<GoodsInOutDto> goodsInOut;

    private String factoryId;

    private String truckDriverFullName;


    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private TruckDriverDto truckDriver;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private TruckDto truck;

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
