package com.ceti.wholesale.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Table(name = "production_monitoring")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ProductionMonitoringId.class)
public class ProductionMonitoring implements Serializable {

    @Id
    @Column(name = "customer_id", columnDefinition = "nvarchar", nullable = false)
	private String customerId;
	
    @Id
    @Column(name = "voucher_at", nullable = false)
    private Instant voucherAt;
	
    @Column(name = "quantity")
	private BigDecimal quantity;
	
    @Column(name = "create_at")
	private Instant createAt;
	
    @Column(name = "create_by")
	private String createBy;
	
}
