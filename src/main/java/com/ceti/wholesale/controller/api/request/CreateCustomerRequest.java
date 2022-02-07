package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateCustomerRequest {
    @NotNull(message = "company_id can not be null")
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
    private String groupId;
    @NotBlank(message = "Yêu cầu điền mã")
    @Length(max = 64, message = "Mã tối đa 64 kí tự")
    private String code;
    private String factoryId;
    private String regionId;

    private String ownerCompany;

    private Boolean isWholesaleCustomer = false;

    private BigDecimal vat = BigDecimal.ZERO;

    public String getCompanyId() {
        return companyId != null ? companyId.trim() : null;
    }

    public String getName() {
        return name != null ? name.trim() : null;
    }

    public String getAddress() {
        return address != null ? address.trim() : null;
    }

    public String getPhoneNumber() {
        return phoneNumber != null ? phoneNumber.trim() : null;
    }

    public String getTaxCode() {
        return taxCode != null ? taxCode.trim() : null;
    }

    public String getCategory() {
        return category != null ? category.trim() : null;
    }

    public String getGroupId() {
        return groupId != null ? groupId.trim() : null;
    }

    public String getCode() {
        return code != null ? code.trim() : null;
    }
}
