package com.ceti.wholesale.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EquipmentDto {
	
	private String id;
	
	private String name;

    private String origin ;

    private String manufactureYear ;

    private String usedYear ;
    
    private String brand ;

    private String symbol ;

    private String model ;

    private Integer quantity ;

    private String installLocation ;
 
    private String specification ;

    private Instant inspectionDate ;

    private Integer inspectionPeriod ;
    
    private String note ;
    
    private String factoryId;

    private Boolean isActive;
    
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<EquipmentAccessorieDto> equipmentAccessories;
    
    @JsonGetter("inspection_date")
    public Object getInspectionDate() {
        try {
            return inspectionDate.getEpochSecond();
        } catch (Exception e) {
            return null;
        }
    }
}
