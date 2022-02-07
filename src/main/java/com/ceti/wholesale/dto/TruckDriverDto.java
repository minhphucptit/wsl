package com.ceti.wholesale.dto;

import java.time.Instant;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TruckDriverDto {

    private String id;

    private String abbreviatedName;

    private String fullName;

    private String address;

    private String phoneNumber;

    private String truckLicensePlateNumber;

    private Instant dateStartWork;

    private String personalIdentificationNumber;

    private Integer yearOfBirth;

    private String drivingLicenseType;

    @Column(name = "driving_license_expire_at")
    private Instant drivingLicenseExpireAt;

    @Column(name = "fire_protection_license_expire_at")
    private Instant fireProtectionLicenseExpireAt;

    @Column(name = "lpg_license_expire_at")
    private Instant lpgLicenseExpireAt;

    @JsonGetter("driving_license_expire_at")
    public Object getDrivingLicenseExpireAt() {
        try {
            return drivingLicenseExpireAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("fire_protection_license_expire_at")
    public Object getFireProtectionLicenseExpireAt() {
        try {
            return fireProtectionLicenseExpireAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("lpg_license_expire_at")
    public Object getLpgLicenseExpireAt() {
        try {
            return lpgLicenseExpireAt.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("date_start_work")
    public Object getDateStartWork() {
        try {
            return dateStartWork.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }


    private Boolean isActive;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FactoryDto factory;

}
