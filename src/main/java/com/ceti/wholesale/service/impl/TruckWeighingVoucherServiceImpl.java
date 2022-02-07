package com.ceti.wholesale.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.common.constant.ConstantText;
import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.enums.GoodsInOutTypeEnum;
import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.CreateTruckWeighingVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateTruckWeighingVoucherRequest;
import com.ceti.wholesale.dto.AccountVoucherCodeDto;
import com.ceti.wholesale.dto.CompanyDto;
import com.ceti.wholesale.dto.CustomerDto;
import com.ceti.wholesale.dto.ProductDto;
import com.ceti.wholesale.dto.TruckDriverDto;
import com.ceti.wholesale.dto.TruckDto;
import com.ceti.wholesale.dto.TruckWeighingVoucherDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.Company;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.FactoryExportVoucher;
import com.ceti.wholesale.model.FactoryImportVoucher;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.Product;
import com.ceti.wholesale.model.Truck;
import com.ceti.wholesale.model.TruckDriver;
import com.ceti.wholesale.model.TruckWeighingVoucher;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.FactoryExportVoucherRepository;
import com.ceti.wholesale.repository.FactoryImportVoucherRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.ProductRepository;
import com.ceti.wholesale.repository.TruckWeighingVoucherDetailRepository;
import com.ceti.wholesale.repository.TruckWeighingVoucherRepository;
import com.ceti.wholesale.service.TruckWeighingVoucherService;
import com.ceti.wholesale.service.v2.WarehouseCommunicationService;

@Service
@Transactional
public class TruckWeighingVoucherServiceImpl implements TruckWeighingVoucherService {

    @Autowired
    private TruckWeighingVoucherRepository truckWeighingVoucherRepository;

    @Autowired
    private TruckWeighingVoucherDetailRepository truckWeighingVoucherDetailRepository;

    @Autowired
    private FactoryImportVoucherRepository factoryImportVoucherRepository;

    @Autowired
    private FactoryExportVoucherRepository factoryExportVoucherRepository;

    @Autowired
    private GoodsInOutRepository goodsInOutRepository;

    @Autowired
    private CylinderDebtRepository cylinderDebtRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private  VoucherUtils voucherUtils;
    
    @Autowired
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

    @Value(value = "${ceti.service.zoneId}")
    private String zoneId;

    @Autowired
    private WarehouseCommunicationService warehouseCommunicationService;

    @Override
    public Page<TruckWeighingVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
        ResultPage<Object[]> page = truckWeighingVoucherDetailRepository.findAllWithFilter(pageable, where);
        List<TruckWeighingVoucherDto> truckWeighingVoucherDtos = new ArrayList<>();
        for (Object[] object : page.getPageList()) {
            TruckWeighingVoucher twv = (TruckWeighingVoucher) object[0];
            TruckWeighingVoucherDto truckWeighingVoucherDto = CommonMapper.map(twv, TruckWeighingVoucherDto.class);

            if (object[1] != null) {
                Company company = (Company) object[1];
                CompanyDto companyDto = CommonMapper.map(company, CompanyDto.class);
                truckWeighingVoucherDto.setCompany(companyDto);
            }

            if (object[2] != null) {
                Customer customer = (Customer) object[2];
                CustomerDto customerDto = CommonMapper.map(customer, CustomerDto.class);
                truckWeighingVoucherDto.setCustomer(customerDto);
            }

            if (object[3] != null) {
                TruckDriver truckDriver = (TruckDriver) object[3];
                TruckDriverDto truckDriverDto = CommonMapper.map(truckDriver, TruckDriverDto.class);
                truckWeighingVoucherDto.setTruckDriver(truckDriverDto);
            }

            if (object[4] != null) {
                Truck truck = (Truck) object[4];
                TruckDto truckDto = CommonMapper.map(truck, TruckDto.class);
                truckWeighingVoucherDto.setTruck(truckDto);
            }

            if (object[5] != null) {
                Product product = (Product) object[5];
                ProductDto productDto = CommonMapper.map(product, ProductDto.class);
                truckWeighingVoucherDto.setProduct(productDto);
            }
            
            if (object[6] != null) {
                AccountVoucherCode accountVoucherCode = (AccountVoucherCode) object[6];
                if(accountVoucherCode != null) {
                    truckWeighingVoucherDto.setAccNo(accountVoucherCode.getAccNo());
                }
            }

            truckWeighingVoucherDtos.add(truckWeighingVoucherDto);
        }
        return new PageImpl<>(truckWeighingVoucherDtos, pageable, page.getTotalItems());
    }

    @Override
    public TruckWeighingVoucherDto createTruckWeighingVoucher(CreateTruckWeighingVoucherRequest createTruckWeighingVoucherRequest,
                                                              String factoryId) {
        Instant now = Instant.now();
        TruckWeighingVoucher truckWeighingVoucher = CommonMapper.map(createTruckWeighingVoucherRequest, TruckWeighingVoucher.class);
        truckWeighingVoucher.setCreatedAt(now);
        truckWeighingVoucher.setWeighingTime1(now);
        truckWeighingVoucher.setUpdateAt(now);
        truckWeighingVoucher.setUpdateByFullName(createTruckWeighingVoucherRequest.getCreatedByFullName());
        truckWeighingVoucher.setFactoryId(factoryId);
        String id = null;
        AccountVoucherCode accountVoucherCode = null;
        switch (createTruckWeighingVoucherRequest.getVoucherCode()) {
            case ConstantText.VOUCHER_CODE_PHIEU_CAN_NHAP:
                id = voucherUtils.genereateVoucherNO(factoryId, VoucherEnum.VOUCHER_CODE_PHIEU_CAN_NHAP.getCode(),
                        i -> truckWeighingVoucherRepository.countTruckWeighingVoucherInDay(i, factoryId));

                accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryId, VoucherEnum.VOUCHER_CODE_PHIEU_CAN_NHAP,
                		createTruckWeighingVoucherRequest.getVoucherAt());
                truckWeighingVoucher.setNo(id);
                truckWeighingVoucher.setVoucherCode(VoucherEnum.VOUCHER_CODE_PHIEU_CAN_NHAP.getCode());
                truckWeighingVoucher.setId(id);
                break;
            case ConstantText.VOUCHER_CODE_PHIEU_CAN_XUAT:
            	id = voucherUtils.genereateVoucherNO(factoryId, VoucherEnum.VOUCHER_CODE_PHIEU_CAN_XUAT.getCode(),
                        i -> truckWeighingVoucherRepository.countTruckWeighingVoucherInDay(i, factoryId));

                accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryId, VoucherEnum.VOUCHER_CODE_PHIEU_CAN_XUAT,
                		createTruckWeighingVoucherRequest.getVoucherAt());
                
                truckWeighingVoucher.setNo(id);
                truckWeighingVoucher.setVoucherCode(VoucherEnum.VOUCHER_CODE_PHIEU_CAN_XUAT.getCode());
                truckWeighingVoucher.setId(id);
                break;
        }
        truckWeighingVoucherRepository.save(truckWeighingVoucher);

        voucherUtils.createAccountVoucherCode(accountVoucherCode, id, id ,true , now,
        		truckWeighingVoucher.getCreatedByFullName());

        if (createTruckWeighingVoucherRequest.getTruckWeighingVoucher() != null) {
            updateTruckWeighingVoucher(truckWeighingVoucher.getId(), createTruckWeighingVoucherRequest.getTruckWeighingVoucher());
        }
        return CommonMapper.map(truckWeighingVoucher, TruckWeighingVoucherDto.class);
    }

    @Override
    public TruckWeighingVoucherDto updateTruckWeighingVoucher(String id, UpdateTruckWeighingVoucherRequest updateTruckWeighingVoucherRequest) {
        Optional<TruckWeighingVoucher> optional = truckWeighingVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu cân xe không tồn tại");
        }
        Instant now = Instant.now();
        TruckWeighingVoucher truckWeighingVoucher = optional.get();
        
        if(!updateTruckWeighingVoucherRequest.getVoucherAt().equals(truckWeighingVoucher.getVoucherAt())) {
            AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(truckWeighingVoucher.getFactoryId(),
            		VoucherEnum.getEnumFromCode(truckWeighingVoucher.getVoucherCode()), updateTruckWeighingVoucherRequest.getVoucherAt());
                        
            AccountVoucherCode oldAccountVoucherCode = accountVoucherCodeRepository.findById(id).orElse(null);
            
            if(oldAccountVoucherCode != null) {
                String updateBy = oldAccountVoucherCode.getUpdateBy();
                Instant updateAt = oldAccountVoucherCode.getUpdateAt();

                //tạo account voucher code mới
                voucherUtils.createAccountVoucherCode(oldAccountVoucherCode, UUID.randomUUID().toString().replaceAll("-", ""), oldAccountVoucherCode.getOldVoucherId(),
                        false , updateAt, updateBy);

                //update lại account voucher code cũ
                voucherUtils.createAccountVoucherCode(accountVoucherCode, oldAccountVoucherCode.getVoucherId(), oldAccountVoucherCode.getOldVoucherId(),
                		 true , now, updateTruckWeighingVoucherRequest.getCreatedByFullName());

            }
            
        }
        
        CommonMapper.copyPropertiesIgnoreNull(updateTruckWeighingVoucherRequest, truckWeighingVoucher);
        truckWeighingVoucher.setUpdateAt(now);
        truckWeighingVoucher.setTruckDriverId(updateTruckWeighingVoucherRequest.getTruckDriverId());
//        truckWeighingVoucher.setWeighingTime2(now);
        if(truckWeighingVoucher.getWeighingTime2() != null) {
            goodsInOutRepository.deleteByVoucherId(truckWeighingVoucher.getId());
            BigDecimal weighingResultFinal = truckWeighingVoucher.getWeighingResult1().subtract(truckWeighingVoucher.getWeighingResult2()).abs();
            truckWeighingVoucher.setWeighingResultFinal(weighingResultFinal);
            Product product = new Product();
            Optional<Product> optionalProduct = productRepository.findByIdAndFactoryId(truckWeighingVoucher.getProductId(), truckWeighingVoucher.getFactoryId());
            if(optionalProduct.isPresent()){
                product = optionalProduct.get();
            }
            switch (truckWeighingVoucher.getVoucherCode()) {
                case ConstantText.VOUCHER_CODE_PHIEU_CAN_NHAP:
                    factoryImportVoucherRepository.deleteById(truckWeighingVoucher.getId());
                    // create factory import voucher
                    FactoryImportVoucher factoryImportVoucher = new FactoryImportVoucher();
                    factoryImportVoucher.setId(truckWeighingVoucher.getId());
                    factoryImportVoucher.setNo(truckWeighingVoucher.getNo());
                    factoryImportVoucher.setVoucherCode(truckWeighingVoucher.getVoucherCode());
                    factoryImportVoucher.setVoucherAt(truckWeighingVoucher.getVoucherAt());
                    factoryImportVoucher.setCompanyId(truckWeighingVoucher.getCompanyId());
                    factoryImportVoucher.setCustomerId(truckWeighingVoucher.getCustomerId());
                    factoryImportVoucher.setTruckDriverId(truckWeighingVoucher.getTruckDriverId());
                    factoryImportVoucher.setTruckLicensePlateNumber(truckWeighingVoucher.getTruckLicensePlateNumber());
                    factoryImportVoucher.setTruckDriverFullName(truckWeighingVoucher.getTruckDriverFullName());
                    factoryImportVoucher.setTotalGoods(truckWeighingVoucher.getWeighingResultFinal());
                    factoryImportVoucher.setNote(truckWeighingVoucher.getNote());
                    factoryImportVoucher.setCreatedByFullName(truckWeighingVoucher.getUpdateByFullName());
                    factoryImportVoucher.setUpdateByFullName(truckWeighingVoucher.getUpdateByFullName());
                    factoryImportVoucher.setCreatedAt(now);
                    factoryImportVoucher.setUpdateAt(now);
                    factoryImportVoucher.setFactoryId(truckWeighingVoucher.getFactoryId());
                    factoryImportVoucher.setInFactory(true);
                    factoryImportVoucherRepository.save(factoryImportVoucher);
                    // create goods in out
                    GoodsInOut goodsInOut = new GoodsInOut();
                    goodsInOut.setCreateAt(truckWeighingVoucher.getCreatedAt());
                    goodsInOut.setCreateByFullName(truckWeighingVoucher.getCreatedByFullName());
                    goodsInOut.setUpdateAt(now);
                    goodsInOut.setUpdateByFullName(truckWeighingVoucher.getUpdateByFullName());
                    goodsInOut.setVoucherNo(truckWeighingVoucher.getNo());
                    goodsInOut.setVoucherCode(truckWeighingVoucher.getVoucherCode());
                    goodsInOut.setVoucherId(truckWeighingVoucher.getId());
                    goodsInOut.setInQuantity(weighingResultFinal);
                    goodsInOut.setProductId(truckWeighingVoucher.getProductId());
                    goodsInOut.setVoucherAt(truckWeighingVoucher.getVoucherAt());
                    goodsInOut.setCompanyId(truckWeighingVoucher.getCompanyId());
                    goodsInOut.setCustomerId(truckWeighingVoucher.getCustomerId());
                    goodsInOut.setProductName(product.getName());
                    goodsInOut.setProductType(product.getType());
                    goodsInOut.setUnit(product.getUnit());
                    goodsInOut.setWeight(product.getWeight());
                    goodsInOut.setType(GoodsInOutTypeEnum.NKNM.name());
                    goodsInOut.setFactoryId(truckWeighingVoucher.getFactoryId());
                    goodsInOut.setTruckDriverId(truckWeighingVoucher.getTruckDriverId());
                    goodsInOut.setTruckLicensePlateNumber(truckWeighingVoucher.getTruckLicensePlateNumber());
                    goodsInOutRepository.save(goodsInOut);

                    break;
                case ConstantText.VOUCHER_CODE_PHIEU_CAN_XUAT:
                    factoryExportVoucherRepository.deleteById(truckWeighingVoucher.getId());
                    //create factory export voucher
                    FactoryExportVoucher factoryExportVoucher = new FactoryExportVoucher();
                    factoryExportVoucher.setId(truckWeighingVoucher.getId());
                    factoryExportVoucher.setNo(truckWeighingVoucher.getNo());
                    factoryExportVoucher.setVoucherCode(truckWeighingVoucher.getVoucherCode());
                    factoryExportVoucher.setVoucherAt(truckWeighingVoucher.getVoucherAt());
                    factoryExportVoucher.setCompanyId(truckWeighingVoucher.getCompanyId());
                    factoryExportVoucher.setCustomerId(truckWeighingVoucher.getCustomerId());
                    factoryExportVoucher.setTruckDriverId(truckWeighingVoucher.getTruckDriverId());
                    factoryExportVoucher.setTruckLicensePlateNumber(truckWeighingVoucher.getTruckLicensePlateNumber());
                    factoryExportVoucher.setTruckDriverFullName(truckWeighingVoucher.getTruckDriverFullName());
                    factoryExportVoucher.setTotalGoods(truckWeighingVoucher.getWeighingResultFinal());
                    factoryExportVoucher.setNote(truckWeighingVoucher.getNote());
                    factoryExportVoucher.setCreatedByFullName(truckWeighingVoucher.getUpdateByFullName());
                    factoryExportVoucher.setUpdateByFullName(truckWeighingVoucher.getUpdateByFullName());
                    factoryExportVoucher.setCreatedAt(now);
                    factoryExportVoucher.setUpdateAt(now);
                    factoryExportVoucher.setFactoryId(truckWeighingVoucher.getFactoryId());
                    factoryExportVoucher.setInFactory(true);
                    factoryExportVoucherRepository.save(factoryExportVoucher);
                    // create goods in out
                    GoodsInOut goodsInOut1 = new GoodsInOut();
                    goodsInOut1.setCreateAt(truckWeighingVoucher.getCreatedAt());
                    goodsInOut1.setCreateByFullName(truckWeighingVoucher.getCreatedByFullName());
                    goodsInOut1.setUpdateAt(now);
                    goodsInOut1.setUpdateByFullName(truckWeighingVoucher.getUpdateByFullName());
                    goodsInOut1.setVoucherNo(truckWeighingVoucher.getNo());
                    goodsInOut1.setVoucherCode(truckWeighingVoucher.getVoucherCode());
                    goodsInOut1.setVoucherId(truckWeighingVoucher.getId());
                    goodsInOut1.setOutQuantity(weighingResultFinal);
                    goodsInOut1.setProductId(truckWeighingVoucher.getProductId());
                    goodsInOut1.setVoucherAt(truckWeighingVoucher.getVoucherAt());
                    goodsInOut1.setCustomerId(truckWeighingVoucher.getCustomerId());
                    goodsInOut1.setCompanyId(truckWeighingVoucher.getCompanyId());
                    goodsInOut1.setProductName(product.getName());
                    goodsInOut1.setProductType(product.getType());
                    goodsInOut1.setUnit(product.getUnit());
                    goodsInOut1.setWeight(product.getWeight());
                    goodsInOut1.setType(GoodsInOutTypeEnum.XKNM.name());
                    goodsInOut1.setFactoryId(truckWeighingVoucher.getFactoryId());
                    goodsInOut1.setTruckDriverId(truckWeighingVoucher.getTruckDriverId());
                    goodsInOut1.setTruckLicensePlateNumber(truckWeighingVoucher.getTruckLicensePlateNumber());
                    goodsInOutRepository.save(goodsInOut1);
                    break;
            }
            // 04/08/2021 NamLH add: gọi api warehouse để cập nhật lại số cân với command reference id và 2 số cân
            Map<String, Object> warehouseData = new HashMap<>();
            warehouseData.put("id", truckWeighingVoucher.getCommandReferenceId());
            warehouseData.put("weight1", String.valueOf(truckWeighingVoucher.getWeighingResult1()));
            warehouseData.put("weight2", String.valueOf(truckWeighingVoucher.getWeighingResult2()));
            warehouseCommunicationService.updateTruckCommand(warehouseData);
        }
        truckWeighingVoucherRepository.save(truckWeighingVoucher);

        return CommonMapper.map(truckWeighingVoucher, TruckWeighingVoucherDto.class);
    }

    @Override
    public void deleteTruckWeighingVoucher(String id) {
        Optional<TruckWeighingVoucher> optional = truckWeighingVoucherRepository.findById(id);
    	
        if (!optional.isPresent()) {
            throw new NotFoundException("Phiếu cân xe không tồn tại");
        }
        TruckWeighingVoucher truckWeighingVoucher = new TruckWeighingVoucher();
        truckWeighingVoucher.setId(id);
        truckWeighingVoucher.setFactoryId(optional.get().getFactoryId());
        truckWeighingVoucher.setVoucherCode(optional.get().getVoucherCode());
        truckWeighingVoucher.setVoucherAt(optional.get().getVoucherAt());
        truckWeighingVoucher.setPressure(BigDecimal.ZERO);
        truckWeighingVoucher.setWeighingResultFinal(BigDecimal.ZERO);
        truckWeighingVoucher.setWeighingResult1(BigDecimal.ZERO);
        truckWeighingVoucher.setWeighingResult2(BigDecimal.ZERO);
        truckWeighingVoucherRepository.save(truckWeighingVoucher);
        accountVoucherCodeRepository.deleteByVoucherId(id);
        
        if(optional.get().getVoucherCode().equals(ConstantText.VOUCHER_CODE_PHIEU_CAN_NHAP)){
            factoryImportVoucherRepository.deleteById(id);
        }else{
            factoryExportVoucherRepository.deleteById(id);
        }
        truckWeighingVoucherRepository.deleteById(id);
        goodsInOutRepository.deleteByVoucherId(id);
    }

}
