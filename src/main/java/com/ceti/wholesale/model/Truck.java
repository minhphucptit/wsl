package com.ceti.wholesale.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Table(name = "truck")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Truck implements Serializable {

    @Id
    @Column(name = "license_plate_number", unique = true, nullable = false)
    private String licensePlateNumber;

    @Column(name = "truck_weight")
    private BigDecimal truckWeight = BigDecimal.valueOf(0);

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "made_in", columnDefinition = "nvarchar")
    private String madeIn;

    @Column(name = "truck_version", columnDefinition = "varchar")
    private String truckVersion;

    @Column(name = "manufacturers", columnDefinition = "nvarchar")
    private String manufacturers;

    @Column(name = "trunk_size", columnDefinition = "varchar")
    private String trunkSize;

    @Column(name = "trunk_type", columnDefinition = "nvarchar")
    private String trunkType;

    @Column(name = "number_kM")
    private BigDecimal numberKM;

    @Column(name = "explosives_transport_paper_present")
    private Instant explosivesTransportPaperPresent;

    @Column(name = "explosives_transport_paper_next")
    private Instant explosivesTransportPaperNext;

    @Column(name = "car_inspection_time_present")
    private Instant carInspectionTimePresent;

    @Column(name = "car_inspection_time_next")
    private Instant carInspectionTimeNext;

    @Column(name = "car_insurance_present")
    private Instant carInsurancePresent;

    @Column(name = "car_insurance_next")
    private  Instant carInsuranceNext;

    @Column(name = "quota")
    private BigDecimal quota;

    @Column(name = "DO_ratio")
    private BigDecimal DORatio;
}
