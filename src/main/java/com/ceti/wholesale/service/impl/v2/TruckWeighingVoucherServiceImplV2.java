package com.ceti.wholesale.service.impl.v2;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import com.ceti.wholesale.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.common.constant.ConstantText;
import com.ceti.wholesale.common.enums.GoodsInOutTypeEnum;
import com.ceti.wholesale.common.enums.ProductEnum;
import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.v2.CreateTruckWeighingVoucherRequest;
import com.ceti.wholesale.controller.api.request.v2.UpdateTruckWeighingVoucherRequest;
import com.ceti.wholesale.dto.TruckWeighingVoucherDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.FactoryExportVoucherRepository;
import com.ceti.wholesale.repository.FactoryImportVoucherRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.ProductRepository;
import com.ceti.wholesale.repository.TruckWeighingVoucherDetailRepository;
import com.ceti.wholesale.repository.TruckWeighingVoucherRepository;
import com.ceti.wholesale.service.v2.TruckWeighingVoucherServiceV2;

@Service
@Transactional
public class TruckWeighingVoucherServiceImplV2 implements TruckWeighingVoucherServiceV2 {

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
    private FactoryRepository factoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Value(value = "${ceti.service.zoneId}")
    private String zoneId;

    @Autowired
    private  VoucherUtils voucherUtils;
    
    @Autowired
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

    @Autowired
    private Environment env;

    @Override
    public TruckWeighingVoucherDto createTruckWeighingVoucher(CreateTruckWeighingVoucherRequest createTruckWeighingVoucherRequest) {
        Instant now = Instant.now();
        if (!factoryRepository.existsById(createTruckWeighingVoucherRequest.getFactoryId())) {
            throw new NotFoundException("Nhà máy không tồn tại");
        }
        String id = null ;
        AccountVoucherCode accountVoucherCode = null;
        TruckWeighingVoucher truckWeighingVoucher = CommonMapper.map(createTruckWeighingVoucherRequest, TruckWeighingVoucher.class);
        truckWeighingVoucher.setCreatedAt(now);
        truckWeighingVoucher.setUpdateByFullName(createTruckWeighingVoucherRequest.getCreatedByFullName());
        truckWeighingVoucher.setProductId(ProductEnum.GBONAPCAO.name());
        truckWeighingVoucher.setProductName(ProductEnum.GBONAPCAO.getName());
        switch (createTruckWeighingVoucherRequest.getVoucherCode()) {
            case ConstantText.VOUCHER_CODE_PHIEU_CAN_NHAP:
                id = voucherUtils.genereateVoucherNO(createTruckWeighingVoucherRequest.getFactoryId(), VoucherEnum.VOUCHER_CODE_PHIEU_CAN_NHAP.getCode(),
                    i -> truckWeighingVoucherRepository.countTruckWeighingVoucherInDay(i, createTruckWeighingVoucherRequest.getFactoryId()));

                accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(createTruckWeighingVoucherRequest.getFactoryId(), VoucherEnum.VOUCHER_CODE_PHIEU_CAN_NHAP,
                		createTruckWeighingVoucherRequest.getVoucherAt());
                                
                truckWeighingVoucher.setNo(id);
                truckWeighingVoucher.setVoucherCode(VoucherEnum.VOUCHER_CODE_PHIEU_CAN_NHAP.getCode());
                truckWeighingVoucher.setId(id);
                break;
            case ConstantText.VOUCHER_CODE_PHIEU_CAN_XUAT:
                id = voucherUtils.genereateVoucherNO(createTruckWeighingVoucherRequest.getFactoryId(), VoucherEnum.VOUCHER_CODE_PHIEU_CAN_XUAT.getCode(),
                        i -> truckWeighingVoucherRepository.countTruckWeighingVoucherInDay(i, createTruckWeighingVoucherRequest.getFactoryId()));

                accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(createTruckWeighingVoucherRequest.getFactoryId(), VoucherEnum.VOUCHER_CODE_PHIEU_CAN_XUAT,
                		createTruckWeighingVoucherRequest.getVoucherAt());
                truckWeighingVoucher.setVoucherCode(VoucherEnum.VOUCHER_CODE_PHIEU_CAN_XUAT.getCode());
                truckWeighingVoucher.setId(id);
                truckWeighingVoucher.setNo(id);
                break;
        }
        truckWeighingVoucherRepository.save(truckWeighingVoucher);
        
        voucherUtils.createAccountVoucherCode(accountVoucherCode,id, id ,true , now, createTruckWeighingVoucherRequest.getCreatedByFullName());

        return CommonMapper.map(truckWeighingVoucher, TruckWeighingVoucherDto.class);
    }

    @Override
    public TruckWeighingVoucherDto updateTruckWeighingVoucher(String commandReferenceId, UpdateTruckWeighingVoucherRequest updateTruckWeighingVoucherRequest) {
        Optional<TruckWeighingVoucher> optional = truckWeighingVoucherRepository.findByCommandReferenceId(commandReferenceId);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu cân xe không tồn tại");
        }
        Instant now = Instant.now();
        TruckWeighingVoucher truckWeighingVoucher = optional.get();
        CommonMapper.copyPropertiesIgnoreNull(updateTruckWeighingVoucherRequest, truckWeighingVoucher);
        truckWeighingVoucher.setUpdateAt(now);
        if (updateTruckWeighingVoucherRequest.getWeighingResult2() != null) {
            BigDecimal weighingResultFinal = truckWeighingVoucher.getWeighingResult1().subtract(truckWeighingVoucher.getWeighingResult2()).abs();
            truckWeighingVoucher.setWeighingResultFinal(weighingResultFinal);
            Product product = new Product();
            Optional<Product> optionalProduct = productRepository.findByIdAndFactoryId(truckWeighingVoucher.getProductId(), truckWeighingVoucher.getFactoryId());
            if(optionalProduct.isPresent()){
                product = optionalProduct.get();
            }
            switch (truckWeighingVoucher.getVoucherCode()) {
                case ConstantText.VOUCHER_CODE_PHIEU_CAN_NHAP:
                    // create factory import voucher
                    FactoryImportVoucher factoryImportVoucher = new FactoryImportVoucher();
                    factoryImportVoucher.setId(truckWeighingVoucher.getId());
                    factoryImportVoucher.setNo(truckWeighingVoucher.getNo());
                    factoryImportVoucher.setVoucherCode(truckWeighingVoucher.getVoucherCode());
                    factoryImportVoucher.setVoucherAt(truckWeighingVoucher.getVoucherAt());
                    factoryImportVoucher.setTruckLicensePlateNumber(truckWeighingVoucher.getTruckLicensePlateNumber());
                    factoryImportVoucher.setTotalGoods(truckWeighingVoucher.getWeighingResultFinal());
                    factoryImportVoucher.setCreatedByFullName(truckWeighingVoucher.getCreatedByFullName());
                    factoryImportVoucher.setUpdateByFullName(truckWeighingVoucher.getCreatedByFullName());
                    factoryImportVoucher.setCreatedAt(now);
                    factoryImportVoucher.setUpdateAt(now);
                    factoryImportVoucher.setFactoryId(truckWeighingVoucher.getFactoryId());
                    factoryImportVoucherRepository.save(factoryImportVoucher);
                    // create goods in out
                    GoodsInOut goodsInOut = new GoodsInOut();
                    goodsInOut.setUpdateAt(now);
                    goodsInOut.setCreateAt(now);
                    goodsInOut.setUpdateByFullName(truckWeighingVoucher.getCreatedByFullName());
                    goodsInOut.setCreateByFullName(truckWeighingVoucher.getCreatedByFullName());
                    goodsInOut.setVoucherNo(truckWeighingVoucher.getNo());
                    goodsInOut.setVoucherCode(truckWeighingVoucher.getVoucherCode());
                    goodsInOut.setVoucherId(truckWeighingVoucher.getId());
                    goodsInOut.setInQuantity(weighingResultFinal);
                    goodsInOut.setVoucherAt(factoryImportVoucher.getVoucherAt());
                    goodsInOut.setProductId(truckWeighingVoucher.getProductId());
                    goodsInOut.setProductName(truckWeighingVoucher.getProductName());
                    goodsInOut.setProductType(product.getType());
                    goodsInOut.setUnit(product.getUnit());
                    goodsInOut.setWeight(product.getWeight());
                    goodsInOut.setType(GoodsInOutTypeEnum.NKNM.name());
                    goodsInOut.setFactoryId(truckWeighingVoucher.getFactoryId());
                    goodsInOutRepository.save(goodsInOut);

                    break;
                case ConstantText.VOUCHER_CODE_PHIEU_CAN_XUAT:
                    //create factory export voucher
                    FactoryExportVoucher factoryExportVoucher = new FactoryExportVoucher();
                    factoryExportVoucher.setId(truckWeighingVoucher.getId());
                    factoryExportVoucher.setNo(truckWeighingVoucher.getNo());
                    factoryExportVoucher.setVoucherCode(truckWeighingVoucher.getVoucherCode());
                    factoryExportVoucher.setVoucherAt(truckWeighingVoucher.getVoucherAt());
                    factoryExportVoucher.setTruckLicensePlateNumber(truckWeighingVoucher.getTruckLicensePlateNumber());
                    factoryExportVoucher.setTotalGoods(truckWeighingVoucher.getWeighingResultFinal());
                    factoryExportVoucher.setCreatedByFullName(truckWeighingVoucher.getCreatedByFullName());
                    factoryExportVoucher.setUpdateByFullName(truckWeighingVoucher.getCreatedByFullName());
                    factoryExportVoucher.setCreatedAt(now);
                    factoryExportVoucher.setUpdateAt(now);
                    factoryExportVoucher.setFactoryId(truckWeighingVoucher.getFactoryId());
                    factoryExportVoucherRepository.save(factoryExportVoucher);
                    // create goods in out
                    GoodsInOut goodsInOut1 = new GoodsInOut();
                    goodsInOut1.setUpdateAt(now);
                    goodsInOut1.setUpdateByFullName(truckWeighingVoucher.getCreatedByFullName());
                    goodsInOut1.setVoucherNo(truckWeighingVoucher.getNo());
                    goodsInOut1.setVoucherCode(truckWeighingVoucher.getVoucherCode());
                    goodsInOut1.setVoucherId(truckWeighingVoucher.getId());
                    goodsInOut1.setOutQuantity(weighingResultFinal);
                    goodsInOut1.setVoucherAt(factoryExportVoucher.getVoucherAt());
                    goodsInOut1.setProductId(truckWeighingVoucher.getProductId());
                    goodsInOut1.setProductName(truckWeighingVoucher.getProductName());
                    goodsInOut1.setProductType(product.getType());
                    goodsInOut1.setUnit(product.getUnit());
                    goodsInOut1.setWeight(product.getWeight());
                    goodsInOut1.setType(GoodsInOutTypeEnum.XKNM.name());
                    goodsInOut1.setFactoryId(truckWeighingVoucher.getFactoryId());
                    goodsInOutRepository.save(goodsInOut1);
                    break;
            }
        }
        truckWeighingVoucherRepository.save(truckWeighingVoucher);

        return CommonMapper.map(truckWeighingVoucher, TruckWeighingVoucherDto.class);
    }

    @Override
    public void deleteTruckWeighingVoucherByCommandReferenceId(String commandReferenceId) {
        Optional<TruckWeighingVoucher> optional = truckWeighingVoucherRepository.findByCommandReferenceId(commandReferenceId);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu cân xe không tồn tại");
        }
        TruckWeighingVoucher truckWeighingVoucher = optional.get();
        truckWeighingVoucher.setPressure(BigDecimal.ZERO);
        truckWeighingVoucher.setWeighingResultFinal(BigDecimal.ZERO);
        truckWeighingVoucher.setWeighingResult1(BigDecimal.ZERO);
        truckWeighingVoucher.setWeighingResult2(BigDecimal.ZERO);
        truckWeighingVoucherRepository.save(truckWeighingVoucher);
        accountVoucherCodeRepository.deleteByVoucherId(truckWeighingVoucher.getId());
        
//        truckWeighingVoucherRepository.deleteByCommandReferenceId(commandReferenceId);
        factoryExportVoucherRepository.deleteById(truckWeighingVoucher.getId());
        factoryImportVoucherRepository.deleteById(truckWeighingVoucher.getId());
        goodsInOutRepository.deleteByVoucherId(truckWeighingVoucher.getId());
        cylinderDebtRepository.deleteByVoucherId(truckWeighingVoucher.getId());
    }
}
