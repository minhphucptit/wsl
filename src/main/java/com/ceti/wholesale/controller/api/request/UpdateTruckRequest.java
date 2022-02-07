package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateTruckRequest {
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
    private Boolean isActive;
    private BigDecimal quota;
    private BigDecimal DORatio;

}
