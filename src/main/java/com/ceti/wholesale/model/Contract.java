package com.ceti.wholesale.model;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

@Data
@Table(name = "contract")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Contract {
      @Id
      @Column(name = "id", unique = true, nullable = false, length = 32)
      @GeneratedValue(generator = "uuid")
      @GenericGenerator(name = "uuid", strategy = "uuid")
      private String id;

      @Column(name="contract_number", nullable = false)
      private String contractNumber;

      @Column(name="contract_category_id", nullable = false)
      private String contractCategoryId;

      @Column(name="sign_date")
      private Instant signDate;

      @Column(name="expire_date")
      private Instant expireDate;

      @Column(name="delivery_method")
      private String deliveryMethod;

      @Column(name="payment_method",columnDefinition = "nvarchar")
      private String paymentMethod;

      @Column(name="status")
      private String status;

      @Column(name="note", columnDefinition = "nvarchar")
      private String note;

      @Column(name="factory_id", columnDefinition = "varchar")
      private String factoryId;
      
      @Column(name="customer_id", columnDefinition = "varchar")
      private String customerId;
      
      @Column(name = "payment_term")
      private Integer paymentTerm;
}
