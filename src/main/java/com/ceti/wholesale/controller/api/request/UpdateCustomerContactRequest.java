package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateCustomerContactRequest {
    @Length(max = 32, message = "customer_id must be less than 32 digits")
    private String customerId;

    private String customerName;

    private String email;

    private String name;

    @Length(max = 20, message = "telephone_number must be less than 20 digits")
    private String taxCode;

}
