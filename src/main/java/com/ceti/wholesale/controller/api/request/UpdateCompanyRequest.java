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
public class UpdateCompanyRequest {
    @Length(max = 255, message = "name must be less than 255 digits")
    private String name;

    @Length(max = 255, message = "address must be less than 255 digits")
    private String address;

    @Length(max = 32, message = "phone_number must be less than 32 digits")
    private String phoneNumber;

    @Length(max = 32, message = "tax_code must be less than 32 digits")
    private String taxCode;

    private Boolean isActive;
}
