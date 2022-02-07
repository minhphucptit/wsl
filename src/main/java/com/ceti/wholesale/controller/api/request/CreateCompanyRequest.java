package com.ceti.wholesale.controller.api.request;

import javax.validation.constraints.NotNull;

import com.ceti.wholesale.common.util.VNCharacterUtils;
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
public class CreateCompanyRequest {
    @NotNull(message = "id can not be null")
    @Length(max = 64, message = "id must be less than 64 digits")
    private String id;

    @NotNull(message = "name can not be null")
    @Length(max = 255, message = "name must be less than 255 digits")
    private String name;

    @Length(max = 255, message = "address must be less than 255 digits")
    private String address;

    @Length(max = 32, message = "phone_number must be less than 32 digits")
    private String phoneNumber;

    @Length(max = 32, message = "tax_code must be less than 32 digits")
    private String taxCode;

    public String getId() {
        return VNCharacterUtils.removeAccent(id);
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


}
