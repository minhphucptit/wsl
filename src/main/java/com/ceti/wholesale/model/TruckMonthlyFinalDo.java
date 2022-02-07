package com.ceti.wholesale.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Table(name = "truck_monthly_final_do")
@Entity
public class TruckMonthlyFinalDo {
	
    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

	@Column(name = "month")
	private Integer month;
	
	@Column(name = "year")
	private Integer year;
    
	@Column(name = "truck_license_plate_number", columnDefinition = "varchar")
	private String truckLicensePlateNumber;
	
	@Column(name = "factory_id", columnDefinition = "varchar")
	private String factoryId;
    
    @Column(name = "final_do")
    private BigDecimal finalDo = BigDecimal.valueOf(0);

}
