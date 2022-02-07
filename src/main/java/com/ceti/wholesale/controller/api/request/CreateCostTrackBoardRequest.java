package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateCostTrackBoardRequest {

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
      
      private String equipmentId;
      
      private String costTrackBoardType;
      
      private String equipmentCostTypeName;
      
      private String equipmentSupervisor;
}
