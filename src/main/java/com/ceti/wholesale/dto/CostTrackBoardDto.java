package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CostTrackBoardDto {

    private String id;

    private String truckLicensePlateNumber;

    private Integer km;

    private String truckCostTypeId;

    private String brand;

    private String madeIn;

    private String specifications;

    private Instant actionDate;

    private Instant warrantyDateTo;

    private String type;

    private String unit;

    private BigDecimal unitPrice;

    private BigDecimal quantity;

    private BigDecimal total;

    private String truckDriverId;

    private String gara;

    private String note;

    private String factoryId;

    private TruckCostTypeDto truckCostType;

    private String factoryName;

    private String truckCostTypeName;

    private String truckDriverFullName;
    
    private String costTrackBoardType;
    
    private String equipmentId;
    
    private String equipmentCostTypeName;
    
    private String equipmentSupervisor;
    
    private EquipmentDto equipment;

    @JsonGetter("action_date")
    public Object getActionDate() {
        try {
            return actionDate.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

    @JsonGetter("warranty_date_to")
    public Object getWarrantyDateTo() {
        try {
            return warrantyDateTo.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }

}
