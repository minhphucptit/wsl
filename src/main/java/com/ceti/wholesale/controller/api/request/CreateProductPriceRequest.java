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
public class CreateProductPriceRequest {

    private BigDecimal price;

    private BigDecimal buyPrice;

    private BigDecimal salePrice;

    @NotNull(message = "create_at can not be null")
    private Instant createAt;

    @NotNull(message = "create_by can not be null")
    @Length(max = 32, message = "create_by must be less than 32 digits")
    private String createBy;

    @NotNull(message = "update_at can not be null")
    private Instant updateAt;

    @NotNull(message = "update_by can not be null")
    @Length(max = 32, message = "update_by must be less than 32 digits")
    private String updateBy;

    @NotNull(message = "is_active can not be null")
    private Boolean isActive;
}
