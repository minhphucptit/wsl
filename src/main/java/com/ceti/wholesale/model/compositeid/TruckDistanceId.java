package com.ceti.wholesale.model.compositeid;

import java.io.Serializable;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TruckDistanceId implements Serializable {
	
    private Instant day;

    private String truckLicensePlateNumber;

}
