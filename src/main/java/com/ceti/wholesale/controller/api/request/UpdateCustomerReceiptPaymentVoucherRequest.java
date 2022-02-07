package com.ceti.wholesale.controller.api.request;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateCustomerReceiptPaymentVoucherRequest {

    @Length(max = 255, message = "customer's id must be less than 255 digits")
    private String customerId;

    @Length(max = 255, message = "customer's full name must be less than 255 digits")
    private String customerFullName;

    @Length(max = 255, message = "payer must be less than 255 digits")
    private String payer;

    @Length(max = 32, message = "payer method must be less than 32 digits")
    private Boolean payerMethod;

    @Length(max = 255, message = "address must be less than 255 digits")
    private String address;

    @Length(max = 255, message = "reason must be less than 255 digits")
    private String reason;

    private BigDecimal spendMoney;

    private BigDecimal collectMoney;

    @Length(max = 255, message = "note must be less than 255 digits")
    private String note;

    @Length(max = 32, message = "category must be less than 32 digits")
    private String category;

    @Length(max = 255, message = "category must be less than 255 digits")
    private String updateBy;

    private Instant voucherAt;
}
