package com.ceti.wholesale.dto;

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
public class BeginningMoneyDebtDto {

    private String id;

    private int year;

    private String customerId;

    private BigDecimal inventory = BigDecimal.ZERO;

    private String createBy;

    private Instant createAt;

    private Instant updateAt;

    private String updateBy;

    private String note;

    private String factoryId;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private CustomerDto customer;
}
