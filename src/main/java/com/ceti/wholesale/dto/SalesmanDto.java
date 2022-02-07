package com.ceti.wholesale.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class SalesmanDto {

    private String id;

    private String abbreviatedName;

    private String fullName;

    private String address;

    private String phoneNumber;

    private String factoryId;

    private Boolean isActive;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FactoryDto factory;
}
