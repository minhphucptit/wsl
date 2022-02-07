package com.ceti.wholesale.controller.api.request;

import com.ceti.wholesale.common.util.VNCharacterUtils;
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
public class CreateSalemanRequest {
    @NotNull(message = "id can not be null")
    @Length(max = 32, message = "id must be less than 32 digits")
    private String id;

    @NotNull(message = "abbreviated_name can not be null")
    @Length(max = 64, message = "abbreviated_name must be less than 64 digits")
    private String abbreviatedName;

    @NotNull(message = "full_name can not be null")
    @Length(max = 255, message = "full_name must be less than 255 digits")
    private String fullName;

    @Length(max = 255, message = "address must be less than 255 digits")
    private String address;

    @Length(max = 32, message = "phone_number must be less than 32 digits")
    private String phoneNumber;

    public String getId() {
        return VNCharacterUtils.removeAccent(id);
    }

    public String getAbbreviatedName() {
        return abbreviatedName != null ? abbreviatedName.trim() : null;
    }

    public String getFullName() {
        return fullName != null ? fullName.trim() : null;
    }

    public String getAddress() {
        return address != null ? address.trim() : null;
    }

    public String getPhoneNumber() {
        return phoneNumber != null ? phoneNumber.trim() : null;
    }


}
