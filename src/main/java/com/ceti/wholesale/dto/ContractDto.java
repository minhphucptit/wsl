package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ContractDto {

      private String id;

      private String contractNumber;

      private String contractCategoryId;

      private Instant signDate;

      private Instant expireDate;

      private String deliveryMethod;

      private String paymentMethod;

      private String status;

      private String note;

      private String factoryId;

      @JsonGetter("sign_date")
      public Object getSignDate() {
            try {
                  return signDate.getEpochSecond();
            } catch (Exception e) {
                  return null;
            }
      }

      @JsonGetter("expire_date")
      public Object getExpireDate() {
            try {
                  return expireDate.getEpochSecond();
            } catch (Exception e) {
                  return null;
            }
      }
      
      private String customerId;
      
      private Integer paymentTerm;
      
      private CustomerDto customer;
      
      private ContractCategoryDto contractCategory;
}
