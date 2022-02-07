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
public class CreateMoneyInStockRequest {

    @NotNull(message = "year can not be null")
    private Integer year;

    @NotNull(message = "inventory can not be null")
    private BigDecimal inventory;

    @Length(max = 255, message = "note must be less than 255 digits")
    private String note;

    @Length(max = 255, message = "create_by must be less than 255 digits")
    private String createBy;

    @Length(max = 255, message = "update_by must be less than 255 digits")
    private String updateBy;

    private Instant creatAt;

    private Instant updateAt;

}
