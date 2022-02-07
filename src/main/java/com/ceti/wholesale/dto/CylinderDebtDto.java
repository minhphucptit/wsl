package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CylinderDebtDto {

    private String id;

    private String voucherId;

    private String voucherCode;

    private Instant voucherAt;

    private String voucherNo;

    private BigDecimal inQuantity;

    private BigDecimal outQuantity;

    private String customerId;

    private String companyId;

    private String note;

    private Instant createdAt;

    private String createdBy;

    private Instant updateAt;

    private String updateBy;

    @JsonGetter("voucher_at")
    public Object getVoucherAt() {
        try {
            return voucherAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("created_at")
    public Object getCreatedAt() {
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
