package com.ceti.wholesale.dto;

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
public class EquipmentAccessorieDto {
	
	   private String id;
	   private String equipmentId ;
	   
	   private String name;
	 
	   private String specification ;
		 
	   private Integer quantity ;
		 
	   private String note;
	   
	   private String factoryId;
}
