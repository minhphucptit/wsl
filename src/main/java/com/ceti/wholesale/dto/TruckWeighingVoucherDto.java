package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TruckWeighingVoucherDto {

    private String id;

    private String no;

    private String voucherCode;

    private Instant voucherAt;

    private String companyId;

    private String productId;

    private String customerId;

    private String truckDriverId;

    private String truckLicensePlateNumber;

    private BigDecimal weighingResult1;

    private BigDecimal weighingResult2;

    private BigDecimal weighingResultFinal;

    private BigDecimal pressure;

    private String note;

    private String createdByFullName;

    private Instant createdAt;

    private String updateByFullName;

    private Instant updateAt;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private CompanyDto company;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private CustomerDto customer;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private TruckDriverDto truckDriver;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private TruckDto truck;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private ProductDto product;
    
    private String accNo;

    private Instant weighingTime1;

    private Instant weighingTime2;

    private String productName;

    private String factoryId;

    private String commandReferenceId;

    private String truckDriverFullName;

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

    @JsonGetter("weighing_time_1")
    public Object getWeighingTime1() {
        try {
            return weighingTime1.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("weighing_time_2")
    public Object getWeighingTime2() {
        try {
            return weighingTime2.getEpochSecond();
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
