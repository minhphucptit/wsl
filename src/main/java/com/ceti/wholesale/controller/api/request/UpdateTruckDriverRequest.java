
package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateTruckDriverRequest {
    @Length(max = 64, message = "abbreviated_name must be less than 64 digits")
    private String abbreviatedName;

    @Length(max = 255, message = "full_name must be less than 255 digits")
    private String fullName;
    @Length(max = 255, message = "address must be less than 255 digits")
    private String address;

    @Length(max = 32, message = "phone_number must be less than 32 digits")
    private String phoneNumber;

    @Length(max = 32, message = "truck_license_place_number must be less than 32 digits")
    private String truckLicensePlateNumber;
    private Instant drivingLicenseExpireAt;
    private Instant fireProtectionLicenseExpireAt;
    private Instant lpgLicenseExpireAt;
    private Instant dateStartWork;
    private String personalIdentificationNumber;
    private Integer yearOfBirth;
    private String drivingLicenseType;

    private Boolean isActive;

}