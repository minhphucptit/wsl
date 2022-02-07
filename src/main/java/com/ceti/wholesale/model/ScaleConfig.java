package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Data
@Table(name = "scale_config")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ScaleConfig {
    @Id
    @Column(name = "factory_id", columnDefinition = "varchar")
    private String factoryId;

    @Column(name = "baud_rate")
    private Integer baudRate;

    @Column(name = "num_data_bits")
    private Integer numDataBits;

    @Column(name = "parity")
    private Integer parity;

    @Column(name = "num_stop_bits")
    private Integer numStopBits;

    @Column(name = "dtr_enable", columnDefinition = "bit")
    private Boolean dtrEnable;

    @Column(name = "port", columnDefinition = "varchar")
    private String port;
    
    @Column(name = "signal_processing")
    private Integer signalProcessing;

    @Column(name = "auto_weighing",columnDefinition = "bit")
    private Boolean autoWeighing;

}
