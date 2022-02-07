package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Table(name = "cost_track_board")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CostTrackBoard implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "truck_license_plate_number", columnDefinition = "varchar")
    private String truckLicensePlateNumber;

    @Column(name = "km")
    private Integer km;

    @Column(name = "truck_cost_type_id", columnDefinition = "varchar")
    private String truckCostTypeId;

    @Column(name = "brand", columnDefinition = "nvarchar")
    private String brand;

    @Column(name = "made_in", columnDefinition = "nvarchar")
    private String madeIn;

    @Column(name = "specifications", columnDefinition = "nvarchar")
    private String specifications;

    @Column(name = "action_date")
    private Instant actionDate;

    @Column(name = "warranty_date_to")
    private Instant warrantyDateTo;

    @Column(name = "type", columnDefinition = "varchar")
    private String type;

    @Column(name = "unit", columnDefinition = "varchar")
    private String unit;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "truck_driver_id", columnDefinition = "varchar")
    private String truckDriverId;

    @Column(name = "gara", columnDefinition = "nvarchar")
    private String gara;

    @Column(name = "note", columnDefinition = "nvarchar")
    private String note;

    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;
    
    @Column(name = "cost_track_board_type", columnDefinition = "varchar")
    private String costTrackBoardType;
    
    @Column(name = "equipment_id", columnDefinition = "varchar")
    private String equipmentId;
    
    @Column(name = "equipment_cost_type_name", columnDefinition = "nvarchar")
    private String equipmentCostTypeName;
    
    @Column(name = "equipment_supervisor", columnDefinition = "nvarchar")
    private String equipmentSupervisor;

}
