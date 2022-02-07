package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Table(name = "truck_cost_type")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TruckCostType implements Serializable {

    @Id
    @Column(name = "id", columnDefinition = "varchar")
    private String id;

    @Column(name = "name", columnDefinition = "nvarchar")
    private String name;

}
