package com.ceti.wholesale.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.ceti.wholesale.model.compositeid.ProductId;
import com.ceti.wholesale.model.compositeid.TruckDistanceId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Table(name = "truck_distance")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@IdClass(TruckDistanceId.class)
public class TruckDistance  implements Serializable {
    
    @Id
    @Column(name = "day")
    private Instant day;
    
    @Id
    @Column(name = "truck_license_plate_number", columnDefinition = "nvarchar")
    private String truckLicensePlateNumber;
    
    @Column(name = "distance")
    private BigDecimal distance;
}
