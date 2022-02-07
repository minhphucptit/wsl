package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateTruckRequest {
    @NotNull(message = "license_place_number can not be null")
    @Length(max = 32, message = "license_place_number must be less than 32 digits")
    private String licensePlateNumber;
    @Length(max = 10, message = "truck_weight must be less than 10 digits")
    private BigDecimal truckWeight;
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
    private BigDecimal quota;
    private BigDecimal DORatio;

    public String getLicensePlateNumber() {
        return licensePlateNumber.trim();
    }
}
