package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Table(name = "goods_in_out_maintain_detail")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInOutMaintainDetail {
    @Id
    @Column(name = "id", columnDefinition = "varchar")
    private String id;

    @Column(name = "customer_id", columnDefinition = "varchar")
    private String customerId;

    @Column(name = "cost")
    private BigDecimal cost;

}
