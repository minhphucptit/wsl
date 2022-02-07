package com.ceti.wholesale.model;

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
@Table(name = "contract_category")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ContractCategory {
      @Id
      @Column(name="id",unique = true, nullable = false)
      @GeneratedValue(generator = "uuid")
      @GenericGenerator(name = "uuid", strategy = "uuid")
      private String id;

      @Column(name="name", columnDefinition = "nvarchar", nullable = false)
      private String name;

      @Column(name="is_active")
      private Boolean isActive = true;
}
