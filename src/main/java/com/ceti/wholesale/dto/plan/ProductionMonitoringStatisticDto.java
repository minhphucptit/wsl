package com.ceti.wholesale.dto.plan;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
public class ProductionMonitoringStatisticDto {

	private List<ProductionMonitoringStatisticDataDto>  productionMonitoringStatisticGroup;
	
	private List<ProductionMonitoringStatisticDataDto>  productionMonitoringStatisticDetail;

}
