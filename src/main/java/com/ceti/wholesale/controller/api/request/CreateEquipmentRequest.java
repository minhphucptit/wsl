package com.ceti.wholesale.controller.api.request;

import java.time.Instant;
import java.util.List;

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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateEquipmentRequest {

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

	    private Boolean isActive;
	    
	    List<CreateEquipmentAccessorieRequest> equipmentAccessories;
}
