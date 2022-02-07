package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateContractRequest {

      private String contractNumber;

      private String contractCategoryId;

      private Instant signDate;

      private Instant expireDate;

      private String deliveryMethod;

      private String paymentMethod;

      private String status;

      private String note;
      
      private String customerId;
      
      private Integer paymentTerm;
}
