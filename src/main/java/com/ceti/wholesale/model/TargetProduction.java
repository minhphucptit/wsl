package com.ceti.wholesale.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Table(name = "target_production")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@IdClass(TargetProductionId.class)
public class TargetProduction implements Serializable {


	@Id
	@Column(name = "customer_code", nullable = false, columnDefinition = "nvarchar")
	private String customerCode;
	
	@Id
	@Column(name = "month", nullable = false)
	private Integer month;
	
	@Id
	@Column(name = "year", nullable = false)
	private Integer year;

	@Column(name = "customer_name", nullable = false, columnDefinition = "nvarchar")
	private String customerName;

	@Column(name = "quantity", nullable = false)
	private BigDecimal quantity;
	
	@Column(name = "create_at", nullable = false)
	private Instant createAt;
	
	@Column(name = "create_by", nullable = false)
	private String createBy;

	@Column(name = "discount")
	private BigDecimal discount;

	@Column(name = "weight_discount")
	private BigDecimal weightDiscount;

	@Column(name = "quantity_without_discount")
	private BigDecimal quantityWithoutDiscount;
}
