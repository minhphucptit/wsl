package com.ceti.wholesale.model;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Table(name = "truck_driver")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TruckDriver implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "abbreviated_name", nullable = false)
    private String abbreviatedName;

    @Column(name = "full_name", columnDefinition = "nvarchar", nullable = false)
    private String fullName;

    @Column(name = "address", columnDefinition = "nvarchar")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "truck_license_plate_number")
    private String truckLicensePlateNumber;

    @Column(name = "driving_license_expire_at")
    private Instant drivingLicenseExpireAt;

    @Column(name = "fire_protection_license_expire_at")
    private Instant fireProtectionLicenseExpireAt;

    @Column(name = "lpg_license_expire_at")
    private Instant lpgLicenseExpireAt;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "date_start_work")
    private Instant dateStartWork;

    @Column(name = "personal_identification_number",columnDefinition = "varchar")
    private String personalIdentificationNumber;

    @Column(name = "year_of_birth")
    private Integer yearOfBirth;

    @Column(name = "driving_license_type")
    private String drivingLicenseType;

}
