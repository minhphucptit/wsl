package com.ceti.wholesale.service.impl;

import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.common.constant.ConstantText;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.CreateCustomerReceiptPaymentVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateCustomerReceiptPaymentVoucherRequest;
import com.ceti.wholesale.dto.CustomerReceiptPaymentVoucherDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.CustomerReceiptPaymentVoucher;
import com.ceti.wholesale.repository.CustomerReceiptPaymentVoucherRepository;
import com.ceti.wholesale.service.CustomerReceiptPaymentVoucherService;

@Service
@Transactional
public class CustomerReceiptPaymentVoucherServiceImpl implements CustomerReceiptPaymentVoucherService {

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;
    @Autowired
    private CustomerReceiptPaymentVoucherRepository customerReceiptPaymentVoucherRepository;

    @Autowired
    private  VoucherUtils voucherUtils;

    @Override
    public Page<CustomerReceiptPaymentVoucherDto> getAllByCondition(
            String customerId, String customerFullName, String payer,
            Boolean payerMethod, String category, Boolean type, String factoryId, Pageable pageable) {

        Page<CustomerReceiptPaymentVoucher> page = customerReceiptPaymentVoucherRepository
                .getAllByCondition(customerId, customerFullName, payer, payerMethod, category, type, factoryId,
                        pageable);
        return CommonMapper.toPage(page, CustomerReceiptPaymentVoucherDto.class, pageable);
    }

    @Override
    public CustomerReceiptPaymentVoucherDto createCustomerReceiptPaymentVoucher(
            CreateCustomerReceiptPaymentVoucherRequest createCustomerReceiptPaymentVoucherRequest, String factoryId) {
        Instant now = Instant.now();
        new CustomerReceiptPaymentVoucher();
        CustomerReceiptPaymentVoucher customerReceiptPaymentVoucher;
        customerReceiptPaymentVoucher = CommonMapper.map(createCustomerReceiptPaymentVoucherRequest, CustomerReceiptPaymentVoucher.class);
        String no = voucherUtils
                .genereateVoucherNO(factoryId, createCustomerReceiptPaymentVoucherRequest.getVoucherCode(),
                        i -> customerReceiptPaymentVoucherRepository.countReceiptPaymentInday(i));
        customerReceiptPaymentVoucher.setNo(no);
        customerReceiptPaymentVoucher.setId(no);
        customerReceiptPaymentVoucher.setCreateAt(now);
        customerReceiptPaymentVoucher.setUpdateAt(now);
        customerReceiptPaymentVoucher.setUpdateBy(createCustomerReceiptPaymentVoucherRequest.getCreateBy());
        customerReceiptPaymentVoucher.setFactoryId(factoryId);
        customerReceiptPaymentVoucherRepository.save(customerReceiptPaymentVoucher);
        return CommonMapper.map(customerReceiptPaymentVoucher, CustomerReceiptPaymentVoucherDto.class);
    }

    @Override
    public CustomerReceiptPaymentVoucherDto updateCustomerReceiptPaymentVoucher(String id,
                                                                                UpdateCustomerReceiptPaymentVoucherRequest updateCustomerReceiptPaymentVoucherRequest) {
        Optional<CustomerReceiptPaymentVoucher> optionalCustomerReceiptPaymentVoucher = customerReceiptPaymentVoucherRepository.findById(id);
        if (optionalCustomerReceiptPaymentVoucher.isEmpty()) {
            throw new NotFoundException("Phiếu thu chi không tồn tại");
        }
        CustomerReceiptPaymentVoucher customerReceiptPaymentVoucher = optionalCustomerReceiptPaymentVoucher.get();
        CommonMapper.copyPropertiesIgnoreNull(updateCustomerReceiptPaymentVoucherRequest, customerReceiptPaymentVoucher);
        Instant now = Instant.now();
        customerReceiptPaymentVoucher.setUpdateAt(now);
        customerReceiptPaymentVoucherRepository.save(customerReceiptPaymentVoucher);
        return CommonMapper.map(customerReceiptPaymentVoucher, CustomerReceiptPaymentVoucherDto.class);
    }

    @Override
    public void deleteCustomerReceiptPaymentVoucher(String id) {
        if (!customerReceiptPaymentVoucherRepository.existsById(id)) {
            throw new NotFoundException("Phiếu thu chi khách hàng không tồn tại");
        }
        customerReceiptPaymentVoucherRepository.deleteById(id);
    }

}
