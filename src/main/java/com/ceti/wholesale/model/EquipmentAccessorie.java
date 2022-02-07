package com.ceti.wholesale.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Table(name = "equipment_accessorie")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentAccessorie implements Serializable{
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 32)
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id ;
	
	 @Column(name = "equipment_id")
    private String equipmentId ;
	 
	 @Column(name = "name",columnDefinition = "nvarchar")
	    private String name ;
	 
	 @Column(name = "specification",columnDefinition = "nvarchar")
    private String specification ;
	 
	 @Column(name = "quantity")
    private Integer quantity ;
	 
	 @Column(name = "note",columnDefinition = "nvarchar")
    private String note;
	 
	 @Column(name = "factory_id")
	    private String factoryId;
}
