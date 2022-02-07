package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CustomerDto {

    private String id;
    
    private String code;

    private String companyId;

    private String name;

    private String address;

    private String phoneNumber;

    private String taxCode;

    private String category;

    private String groupId;

    private String factoryId;

    private String ownerCompany;

    private Boolean isWholesaleCustomer;

    private Boolean isActive;

    private BigDecimal vat;

    private FactoryDto factory;

    private RegionDto regionDto;

    private CustomerCategoryDto customerCategory;

}
