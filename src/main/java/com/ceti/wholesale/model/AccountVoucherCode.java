package com.ceti.wholesale.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@Table(name = "account_voucher_code")
@Entity
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountVoucherCode {
	
    @Id
    @Column(name = "voucher_id", unique = true, nullable = false)
//	@GeneratedValue(generator = "uuid")
//	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String voucherId;
    
    @Column(name="acc_no")
    private String accNo;
    
    @Column(name="old_voucher_id")
    private String oldVoucherId;
    
    @Column(name="group_code")
    private String groupCode;
    
    @Column(name="active")
    private boolean active;
    
    @Column(name="update_at")
    private Instant updateAt;
    
    @Column(name="update_by" , columnDefinition = "nvarchar")
    private String updateBy;

    @Column(name="stt")
    private Integer stt;

    @Column(name="factory_id")
    private String factoryId;

    @Column(name="voucher_at")
    private Instant voucherAt;
}
