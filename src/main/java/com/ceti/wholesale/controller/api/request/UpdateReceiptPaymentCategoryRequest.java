package com.ceti.wholesale.controller.api.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateReceiptPaymentCategoryRequest {

    @Length(max = 32, message = "customer's id must be less than 32 digits")
    private String id;

    @Length(max = 255, message = "customer's full name must be less than 255 digits")
    private String name;

    @Length(max = 255, message = "payer must be less than 255 digits")
    private Boolean type;

    private Boolean isActive;

}
