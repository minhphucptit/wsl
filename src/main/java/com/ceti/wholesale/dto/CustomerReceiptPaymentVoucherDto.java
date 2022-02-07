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
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CustomerReceiptPaymentVoucherDto {

    private String id;

    private String no;

    private String customerId;

    private String customerFullName;

    private Instant voucherAt;

    private String payer;

    private Boolean payerMethod;

    private String address;

    private String reason;

    private String note;

    private String category;

    private String voucherCode;

    private Instant createAt;

    private String createBy;

    private Instant updateAt;

    private String updateBy;

    private String factoryId;

    private BigDecimal collectMoney;

    private BigDecimal spendMoney;

    @JsonGetter("voucher_at")
    public Object getVoucherAt() {
        try {
            return voucherAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("create_at")
    public Object getCreateAt() {
        try {
            return createAt.getEpochSecond();
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

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private CustomerDto customer;
}
