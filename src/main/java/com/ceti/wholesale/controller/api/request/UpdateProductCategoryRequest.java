package com.ceti.wholesale.controller.api.request;

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
public class UpdateProductCategoryRequest {
    @Length(max = 128, message = "name must be less than 128 digits")
    private String name;
    @Length(max = 32, message = "product_type_id must be less than 32 digits")
    private String productTypeId;

    private Boolean isActive;

}
