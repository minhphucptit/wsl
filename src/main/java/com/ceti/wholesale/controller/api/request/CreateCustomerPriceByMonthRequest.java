package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateCustomerPriceByMonthRequest {
    @NotNull(message = "customerId can not be null")
    private String customerId;

    @NotNull(message = "customerName can not be null")
    private String customerName;

    private String customerCategory;

    private Integer year;

    private String factoryId;
}
