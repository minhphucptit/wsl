package com.ceti.wholesale.controller.api.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateTruckDistanceListRequest {

    @Valid
    private List<CreateTruckDistanceListRequest.Create> truckDistanceList;

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @Accessors(chain = true)
    public static class Create {

        private BigDecimal distance;

        private Instant day;

        private String truckLicensePlateNumber;

        private Integer rowLocation;

        public String getTruckLicensePlateNumber() {
            if (truckLicensePlateNumber != null){
                return truckLicensePlateNumber.trim();
            }else {
                return null;
            }
        }
    }
}
