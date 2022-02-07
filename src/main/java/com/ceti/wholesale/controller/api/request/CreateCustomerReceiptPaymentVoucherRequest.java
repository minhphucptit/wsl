package com.ceti.wholesale.controller.api.request;

import java.math.BigDecimal;
import java.time.Instant;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateCustomerReceiptPaymentVoucherRequest {

    @NotNull(message = "customer's id can not be null")
    @Length(max = 255, message = "customer's id must be less than 255 digits")
    private String customerId;

    @NotNull(message = "customer's full name can not be null")
    @Length(max = 255, message = "customer's full name must be less than 255 digits")
    private String customerFullName;

    @Length(max = 255, message = "payer must be less than 255 digits")
    private String payer;

    private Boolean payerMethod;

    @Length(max = 255, message = "address must be less than 255 digits")
    private String address;

    @Length(max = 255, message = "reason must be less than 255 digits")
    private String reason;

    private BigDecimal spendMoney;

    private BigDecimal collectMoney;

    @NotNull(message = "voucherAt can not be null")
    private Instant voucherAt;

    @Length(max = 255, message = "note must be less than 255 digits")
    private String note;

    @NotNull(message = "category method can not be null")
    @Length(max = 32, message = "category must be less than 32 digits")
    private String category;

    @NotNull(message = "create_by can not be null")
    @Length(max = 255, message = "create_by must be less than 255 digits")
    private String createBy;

    private String voucherCode;

}
