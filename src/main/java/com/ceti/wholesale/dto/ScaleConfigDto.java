package com.ceti.wholesale.dto;

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
public class ScaleConfigDto {

    private Integer baudRate;

    private Integer numDataBits;

    private Integer parity;

    private Integer numStopBits;

    private Boolean dtrEnable;

    private String port;
    
    private Integer signalProcessing;

    private Boolean autoWeighing;

}
