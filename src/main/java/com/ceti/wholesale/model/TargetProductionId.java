package com.ceti.wholesale.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TargetProductionId implements Serializable {
	
	private String customerCode;
	
	private Integer month;
	
	private Integer year;

}
