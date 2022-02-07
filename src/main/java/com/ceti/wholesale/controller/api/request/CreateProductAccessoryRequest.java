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
public class CreateProductAccessoryRequest {

    private String mainProductId;

    @NotNull(message = "subProductId can not be null")
    private String subProductId;

    @NotNull(message = "subProductName can not be null")
    private String subProductName;

    private BigDecimal subProductQuantity = BigDecimal.ZERO;

    private String subProductType;
}
