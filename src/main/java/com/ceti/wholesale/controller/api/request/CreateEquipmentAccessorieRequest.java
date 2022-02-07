package com.ceti.wholesale.controller.api.request;



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
public class CreateEquipmentAccessorieRequest {
   
   private String name;
	 
   private String specification ;
	 
   private Integer quantity ;
	 
   private String note;
}
