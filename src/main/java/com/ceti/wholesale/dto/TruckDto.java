package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TruckDto {

    private String licensePlateNumber;

    private String factoryId;

    private String madeIn;

    private String truckVersion;

    private String manufacturers;

    private String trunkSize;

    private String trunkType;

    private BigDecimal numberKM;

    private Instant explosivesTransportPaperPresent;

    private Instant explosivesTransportPaperNext;

    private Instant carInspectionTimePresent;

    private Instant carInspectionTimeNext;

    private Instant carInsurancePresent;

    private  Instant carInsuranceNext;

    private BigDecimal truckWeight;

    private FactoryDto factory;

    private BigDecimal quota;

    private BigDecimal DORatio;

    @JsonGetter("explosives_transport_paper_present")
    public Object getExplosivesTransportPaperPresent() {
        try {
            return explosivesTransportPaperPresent.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("explosives_transport_paper_next")
    public Object getExplosivesTransportPaperNext() {
        try {
            return explosivesTransportPaperNext.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("car_inspection_time_present")
    public Object getCarInspectionTimePresent() {
        try {
            return carInspectionTimePresent.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("car_inspection_time_next")
    public Object getCarInspectionTimeNext() {
        try {
            return carInspectionTimeNext.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("car_insurance_present")
    public Object getCarInsurancePresent() {
        try {
            return carInsurancePresent.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("car_insurance_next")
    public Object getCarInsuranceNext() {
        try {
            return carInsuranceNext.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    private Boolean isActive;

}
