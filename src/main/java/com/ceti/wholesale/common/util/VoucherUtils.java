package com.ceti.wholesale.common.util;

import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
public class VoucherUtils {

    @Autowired
    private AccountVoucherCodeRepository accountVoucherCodeRepository;
    
    @Autowired
    private Map<String,String> factoryMap;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;


    public String genereateVoucherNO(String factoryId, String voucherCode , Count count) {
        DateTimeFormatter formatterNo = DateTimeFormatter.ofPattern("yyMMdd");
        LocalDateTime today = LocalDateTime.ofInstant(Instant.now() , ZoneId.of(zoneId));
        String stringToday = today.format(formatterNo);
        String formattedNumericNo = String.format("%03d" ,
                count.getHighestNumber(factoryMap.get(factoryId)+"-"+voucherCode+stringToday) + 1);
        String factoryCode = factoryMap.get(factoryId)+"-";
        return factoryCode +voucherCode + stringToday + "-" + formattedNumericNo;
    }


    //Tạo phiếu theo voucher at, mỗi ngày 1 phiếu
    public String genereateVoucherNOWithVoucherAt(String factoryId, String voucherCode, Instant voucherAt) {
        DateTimeFormatter formatterNo = DateTimeFormatter.ofPattern("yyMMdd");
        LocalDateTime today = LocalDateTime.ofInstant(voucherAt , ZoneId.of(zoneId));
        String stringToday = today.format(formatterNo);
        return factoryMap.get(factoryId) + "-" + voucherCode + stringToday;
    }

    @FunctionalInterface
    public interface Count {
    	// lấy số phiếu lớn nhất hôm nay
        int getHighestNumber(String noSample);
    }
    
    public AccountVoucherCode genereateVoucherNOWithVoucherAtAndVoucherEnum(String factoryId, VoucherEnum voucherEnum , Instant voucherAt) {
        DateTimeFormatter formatterNo = DateTimeFormatter.ofPattern("yyMMdd");
        LocalDateTime today = LocalDateTime.ofInstant(voucherAt , ZoneId.of(zoneId));
        String stringToday = today.format(formatterNo);
        Integer stt =accountVoucherCodeRepository.countNumberOfVouchers(stringToday, voucherEnum.getGroupCode(), factoryId) + 1;
        String formattedNumericNo = String.format("%03d" , stt);
        return new AccountVoucherCode().setAccNo(factoryMap.get(factoryId)+"-" +voucherEnum.getCode() + stringToday + "-" + formattedNumericNo)
                .setStt(stt).setFactoryId(factoryId).setVoucherAt(voucherAt).setGroupCode(voucherEnum.getGroupCode());
    }

    public void createAccountVoucherCode(AccountVoucherCode accountVoucherCode, String voucherId, String oldVoucherId, boolean active, Instant updateAt, String updateBy) {
        AccountVoucherCode accVoucherCode = new AccountVoucherCode();
        CommonMapper.copyPropertiesIgnoreNull(accountVoucherCode, accVoucherCode);
        accVoucherCode.setVoucherId(voucherId);
        accVoucherCode.setOldVoucherId(oldVoucherId);
        accVoucherCode.setActive(active);
        accVoucherCode.setUpdateAt(updateAt);
        accVoucherCode.setUpdateBy(updateBy);
        accountVoucherCodeRepository.save(accVoucherCode);
    }

}