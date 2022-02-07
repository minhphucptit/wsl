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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateCustomerRequest {
    @Length(max = 64, message = "company_id must be less than 64 digits")
    private String companyId;
    @NotNull(message = "name can not be null")
    @Length(max = 255, message = "name must be less than 255 digits")
    private String name;
    @Length(max = 255, message = "address must be less than 255 digits")
    private String address;
    @Length(max = 32, message = "phone_number must be less than 32 digits")
    private String phoneNumber;
    @Length(max = 32, message = "tax_code must be less than 32 digits")
    private String taxCode;
    @Length(max = 32, message = "category must be less than 32 digits")
    private String category;
    @Length(max = 32, message = "group_id must be less than 32 digits")
    private String groupId;
    private String factoryId;
    private String regionId;
    private String ownerCompany;

    private Boolean isWholesaleCustomer = false;

    private Boolean isActive;

    private BigDecimal vat;

    @Length(max = 64, message = "Mã tối đa 64 kí tự")
    private String code;
}
