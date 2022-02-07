package com.ceti.wholesale.controller.api.request;

import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateAllTruckMonthlyFinalDoRequest {
	
	@Valid
	private List<CreateTruckMonthlyFinalDoRequest> truckMonthlyFinalDos;

}
