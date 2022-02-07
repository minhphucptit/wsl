package com.ceti.wholesale.model;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Table(name = "equipment")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Equipment implements Serializable{
		@Id
		@Column(name = "id", unique = true, nullable = false, length = 32)
		@GeneratedValue(generator = "uuid")
		@GenericGenerator(name = "uuid", strategy = "uuid")
	    private String id;

	    @Column(name = "name",columnDefinition = "nvarchar")
	    private String name;
	    
	    @Column(name = "origin",columnDefinition = "nvarchar")
	    private String origin ;
	    
	    @Column(name = "manufacture_year")
	    private String manufactureYear ;
	    
	    @Column(name = "used_year")
	    private String usedYear ;
	    
	    @Column(name = "brand",columnDefinition = "nvarchar")
	    private String brand ;
	    
	    @Column(name = "symbol")
	    private String symbol ;
	    
	    @Column(name = "model")
	    private String model ;
	    
	    @Column(name = "quantity")
	    private Integer quantity ;
	    
	    @Column(name = "install_location",columnDefinition = "nvarchar")
	    private String installLocation ;
	    
	    @Column(name = "specification",columnDefinition = "nvarchar")
	    private String specification ;
	    
	    @Column(name = "inspection_date",columnDefinition = "nvarchar")
	    private Instant inspectionDate ;
	    
	    @Column(name = "inspection_period")
	    private Integer inspectionPeriod ;
	    
	    @Column(name = "note",columnDefinition = "nvarchar")
	    private String note ;
	    
	    @Column(name = "factory_id")
	    private String factoryId;

		@Column(name = "is_active")
		private Boolean isActive = true;

}
