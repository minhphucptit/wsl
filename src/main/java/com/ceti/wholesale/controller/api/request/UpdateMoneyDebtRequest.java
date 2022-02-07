package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateMoneyDebtRequest {

    @NotNull(message = "year can not be null")
    private int year;

    @Length(max = 32, message = "customerId must be less than 32 digits")
    @NotNull(message = "customerId can not be null")
    private String customerId;

    @NotNull(message = "inventory can not be null")
    private BigDecimal inventory;

    @Length(max = 255, message = "udpateBy must be less than 255 digits")
    @NotNull(message = "udpateBy can not be null")
    private String updateBy;

    @NotNull(message = "udpateAt can not be null")
    private Instant updateAt;

    @Length(max = 255, message = "note must be less than 255 digits")
    private String note;

}
