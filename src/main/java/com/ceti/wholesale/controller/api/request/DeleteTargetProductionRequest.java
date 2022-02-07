package com.ceti.wholesale.controller.api.request;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DeleteTargetProductionRequest {

	@NotNull(message = "customerCode can not be null")
	private String customerCode;

	@NotNull(message = "month can not be null")
	private Integer month;
	
	@NotNull(message = "year can not be null")
	private Integer year;
}
