package com.ceti.wholesale.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.common.constant.ConstantText;
import com.ceti.wholesale.common.constant.GBONAPCAOConstant;
import com.ceti.wholesale.common.enums.GoodsInOutTypeEnum;
import com.ceti.wholesale.common.enums.ProductTypeEnum;
import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.CreateFactoryImportExportVoucherRequestFromWarehouse;
import com.ceti.wholesale.controller.api.request.CreateFactoryImportVoucherRequest;
import com.ceti.wholesale.controller.api.request.CreateGoodsInOutRequest;
import com.ceti.wholesale.controller.api.request.UpdateFactoryImportVoucherRequest;
import com.ceti.wholesale.dto.FactoryImportVoucherDto;
import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.dto.ProductAccessoryDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.mapper.FactoryImportVoucherMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.CylinderDebt;
import com.ceti.wholesale.model.FactoryImportVoucher;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.FactoryImportVoucherRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.TruckWeighingVoucherRepository;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.FactoryImportVoucherService;
import com.ceti.wholesale.service.ProductAccessoryService;

@Service
@Transactional
public class FactoryImportVoucherServiceImpl implements FactoryImportVoucherService {

    @Autowired
    private FactoryImportVoucherRepository factoryImportVoucherRepository;

    @Autowired
    private GoodsInOutRepository goodsInOutRepository;

    @Autowired
    private CylinderDebtService cylinderDebtService;

    @Autowired
    private ProductAccessoryService productAccessoryService;

    @Autowired
    private CylinderDebtRepository cylinderDebtRepository;

    @Autowired
    private TruckWeighingVoucherRepository truckWeighingVoucherRepository;

    @Autowired
    private FactoryImportVoucherMapper factoryImportVoucherMapper;
    
    @Autowired
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

    @Autowired
    private  VoucherUtils voucherUtils;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

//    @Override
//    public Page<FactoryImportVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
//        Instant voucherAtFrom = where.getFirst("voucher_at_from") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_from")));
//        Instant voucherAtTo = where.getFirst("voucher_at_to") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_to")));
//        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "fiv");
//            List<FactoryImportVoucherDto> factoryImportVoucherDtos = factoryImportVoucherMapper.getList(
//                    where.getFirst("product_id"),
//                    where.getFirst("no"),where.getFirst("voucher_code"),where.getFirst("note"),where.getFirst("company_id"),
//                    where.getFirst("customer_id"),where.getFirst("truck_driver_id"),where.getFirst("truck_license_plate_number"),
//                    where.getFirst("salesman_id"),where.getFirst("factory_id"),where.getFirst("goods_in_out.type"),voucherAtFrom,voucherAtTo,pagingStr);
//            long totalItem = factoryImportVoucherMapper.countList(
//                    where.getFirst("product_id"),
//                    where.getFirst("no"),where.getFirst("voucher_code"),where.getFirst("note"),where.getFirst("company_id"),
//                    where.getFirst("customer_id"),where.getFirst("truck_driver_id"),where.getFirst("truck_license_plate_number"),
//                    where.getFirst("salesman_id"),where.getFirst("factory_id"),where.getFirst("goods_in_out.type"),voucherAtFrom,voucherAtTo);
//        ResultPage<Object[]> page = factoryImportVoucherDetailRepository.findAllWithFilter(pageable, where);
//        List<FactoryImportVoucherDto> factoryImportVoucherDtos = new ArrayList<>();
//        for (Object[] object : page.getPageList()) {
//            FactoryImportVoucher fiv = (FactoryImportVoucher) object[0];
//            FactoryImportVoucherDto factoryImportVoucherDto = CommonMapper.map(fiv, FactoryImportVoucherDto.class);
//
//            if (object[1] != null) {
//                Company company = (Company) object[1];
//                CompanyDto companyDto = CommonMapper.map(company, CompanyDto.class);
//                factoryImportVoucherDto.setCompany(companyDto);
//            }
//
//            if (object[2] != null) {
//                Customer customer = (Customer) object[2];
//                CustomerDto customerDto = CommonMapper.map(customer, CustomerDto.class);
//                factoryImportVoucherDto.setCustomer(customerDto);
//            }
//
//            if (object[3] != null) {
//                TruckDriver truckDriver = (TruckDriver) object[3];
//                TruckDriverDto truckDriverDto = CommonMapper.map(truckDriver, TruckDriverDto.class);
//                factoryImportVoucherDto.setTruckDriver(truckDriverDto);
//            }
//
//            if (object[4] != null) {
//                Truck truck = (Truck) object[4];
//                TruckDto truckDto = CommonMapper.map(truck, TruckDto.class);
//                factoryImportVoucherDto.setTruck(truckDto);
//            }
//
//            if (object[5] != null) {
//                Salesman salesman = (Salesman) object[5];
//                SalesmanDto salesmanDto = CommonMapper.map(salesman, SalesmanDto.class);
//                factoryImportVoucherDto.setSalesman(salesmanDto);
//            }
//
//            factoryImportVoucherDtos.add(factoryImportVoucherDto);
//        }
//        return new PageImpl<>(factoryImportVoucherDtos, pageable, totalItem);
//    }

    @Override
    public FactoryImportVoucherDto createFactoryImportVoucher(
            CreateFactoryImportVoucherRequest createFactoryImportVoucherRequest, String factoryId) {
        if (createFactoryImportVoucherRequest.getGoodsInOut().isEmpty()) {
            throw new BadRequestException("Phải có hàng mới tạo được phiếu");
        }
        Instant now = Instant.now();
        String id = voucherUtils.genereateVoucherNO(factoryId, VoucherEnum.VOUCHER_CODE_PHIEU_NHAP_HANG_NHA_MAY.getCode(),
                i -> factoryImportVoucherRepository.countFactoryImportVoucherInDay(i));

        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryId, VoucherEnum.VOUCHER_CODE_PHIEU_NHAP_HANG_NHA_MAY,
        		createFactoryImportVoucherRequest.getVoucherAt());
        
        FactoryImportVoucher factoryImportVoucher = CommonMapper.map(createFactoryImportVoucherRequest, FactoryImportVoucher.class);
        factoryImportVoucher.setCreatedAt(now);
        factoryImportVoucher.setUpdateAt(now);
        factoryImportVoucher.setId(id);
        factoryImportVoucher.setUpdateByFullName(factoryImportVoucher.getCreatedByFullName());
        factoryImportVoucher.setNo(id);
        factoryImportVoucher.setVoucherCode(VoucherEnum.VOUCHER_CODE_PHIEU_NHAP_HANG_NHA_MAY.getCode());
        factoryImportVoucher.setFactoryId(factoryId);

        factoryImportVoucherRepository.save(factoryImportVoucher);

        voucherUtils.createAccountVoucherCode(accountVoucherCode, id, id ,true , now, factoryImportVoucher.getCreatedByFullName());
        
        List<GoodsInOutDto> goodsInOutDto = new ArrayList<>();
        if (!createFactoryImportVoucherRequest.getGoodsInOut().isEmpty()) {
            List<CreateGoodsInOutRequest> goodsInOutRequests = createFactoryImportVoucherRequest.getGoodsInOut();

            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                GoodsInOut goodsInOut = new GoodsInOut();

                CommonMapper.copyPropertiesIgnoreNull(goodsInOutRequest, goodsInOut);
                goodsInOut.setCompanyId(factoryImportVoucher.getCompanyId());
                goodsInOut.setCustomerId(factoryImportVoucher.getCustomerId());
                goodsInOut.setVoucherAt(factoryImportVoucher.getVoucherAt());
                goodsInOut.setVoucherNo(factoryImportVoucher.getNo());
                goodsInOut.setVoucherCode(factoryImportVoucher.getVoucherCode());
                goodsInOut.setVoucherId(factoryImportVoucher.getId());
                goodsInOut.setIsMainProduct(true);
                goodsInOut.setInFactory(createFactoryImportVoucherRequest.getInFactory());
                goodsInOut.setFactoryId(factoryId);
                goodsInOut.setTruckDriverId(factoryImportVoucher.getTruckDriverId());
                goodsInOut.setTruckLicensePlateNumber(factoryImportVoucher.getTruckLicensePlateNumber());
                goodsInOut.setSalesmanId(factoryImportVoucher.getSalesmanId());
                goodsInOut.setStt(goodsInOutRequests.indexOf(goodsInOutRequest));
                goodsInOut.setCreateAt(now);
                goodsInOut.setUpdateAt(now);
                goodsInOut.setCreateByFullName(createFactoryImportVoucherRequest.getCreatedByFullName());
                goodsInOut.setUpdateByFullName(createFactoryImportVoucherRequest.getCreatedByFullName());
                goodsInOutRepository.save(goodsInOut);

                // nếu sản phẩm là gas thì xuất cả cấu hình, ghi nhận công nợ vỏ
                if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.GAS.name())) {
                    List<ProductAccessoryDto> list = productAccessoryService.getByMainProductIdAndFactoryId(goodsInOutRequest.getProductId(), factoryId);
                    for (ProductAccessoryDto pad : list) {
                        GoodsInOut gio = new GoodsInOut();
                        gio.setVoucherNo(factoryImportVoucher.getNo());
                        gio.setVoucherAt(factoryImportVoucher.getVoucherAt());
                        gio.setVoucherCode(factoryImportVoucher.getVoucherCode());
                        gio.setVoucherId(factoryImportVoucher.getId());
                        gio.setCustomerId(factoryImportVoucher.getCustomerId());
                        gio.setCompanyId(factoryImportVoucher.getCompanyId());
                        gio.setType(goodsInOutRequest.getType());
                        gio.setProductId(pad.getSubProductId());
                        gio.setProductName(pad.getSubProductName());
                        gio.setProductType((pad.getSubProductType()));
                        gio.setInQuantity(goodsInOutRequest.getInQuantity().multiply(pad.getSubProductQuantity()));
                        gio.setIsMainProduct(false);
                        gio.setInFactory(createFactoryImportVoucherRequest.getInFactory());
                        gio.setFactoryId(factoryId);
                        gio.setTruckDriverId(factoryImportVoucher.getTruckDriverId());
                        gio.setTruckLicensePlateNumber(factoryImportVoucher.getTruckLicensePlateNumber());
                        gio.setSalesmanId(factoryImportVoucher.getSalesmanId());

                        //20/10/2021 Nhan: bỏ xuất nhập cấu hình trong phiếu xuất nhập
//                      goodsInOutRepository.save(gio);
                        if(gio.getProductType().equals(ProductTypeEnum.VO.name())){
                            cylinderDebtService
                                    .createCylinderDebt(new CylinderDebt(factoryImportVoucher.getId(), gio.getProductId(),
                                            factoryImportVoucher.getVoucherCode(), factoryImportVoucher.getVoucherAt(),
                                            factoryImportVoucher.getNo(),
                                            gio.getInQuantity(), BigDecimal.valueOf(0),
                                            factoryImportVoucher.getCustomerId(), factoryImportVoucher.getCompanyId(),
                                            factoryImportVoucher.getNote(), factoryImportVoucher.getCreatedAt(),
                                            factoryImportVoucher.getCreatedByFullName(), factoryImportVoucher.getUpdateAt(),
                                            factoryImportVoucher.getUpdateByFullName(), factoryId));
                        }
                    }

                }

                if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.VO.name())) {
                    cylinderDebtService
                            .createCylinderDebt(new CylinderDebt(factoryImportVoucher.getId(), goodsInOutRequest.getProductId(),
                                    factoryImportVoucher.getVoucherCode(), factoryImportVoucher.getVoucherAt(),
                                    factoryImportVoucher.getNo(),
                                    goodsInOutRequest.getInQuantity(), BigDecimal.valueOf(0),
                                    factoryImportVoucher.getCustomerId(), factoryImportVoucher.getCompanyId(),
                                    factoryImportVoucher.getNote(), factoryImportVoucher.getCreatedAt(),
                                    factoryImportVoucher.getCreatedByFullName(), factoryImportVoucher.getUpdateAt(),
                                    factoryImportVoucher.getUpdateByFullName(), factoryId).setGoodsInOutType(goodsInOutRequest.getType()));
                }
                goodsInOutDto.add(CommonMapper.map(goodsInOut, GoodsInOutDto.class));
            }
        }
        
        FactoryImportVoucherDto factoryImportVoucherDto = CommonMapper.map(factoryImportVoucher, FactoryImportVoucherDto.class);
        factoryImportVoucherDto.setAccNo(accountVoucherCode.getAccNo());
        return factoryImportVoucherDto;
    }

    @Override
    public FactoryImportVoucherDto updateFactoryImportVoucher(String id, UpdateFactoryImportVoucherRequest request) {
        Optional<FactoryImportVoucher> optional = factoryImportVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu nhập hàng không tồn tại");
        }
        
        FactoryImportVoucher factoryImportVoucher = optional.get();
        Instant now = Instant.now();

        if(!request.getVoucherAt().equals(factoryImportVoucher.getVoucherAt())) {
            AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryImportVoucher.getFactoryId(), VoucherEnum.VOUCHER_CODE_PHIEU_NHAP_HANG_NHA_MAY,
            		request.getVoucherAt());
            
//            factoryImportVoucher.setNo(no);
            
            AccountVoucherCode oldAccountVoucherCode = accountVoucherCodeRepository.findById(id).orElse(null);
            
            if(oldAccountVoucherCode != null) {
                String updateBy = oldAccountVoucherCode.getUpdateBy();
                Instant updateAt = oldAccountVoucherCode.getUpdateAt();

                //tạo account voucher code mới
                voucherUtils.createAccountVoucherCode(oldAccountVoucherCode, UUID.randomUUID().toString().replaceAll("-", ""), oldAccountVoucherCode.getOldVoucherId(),
                         false , updateAt, updateBy);
                //update lại account voucher code cũ
                voucherUtils.createAccountVoucherCode(accountVoucherCode, oldAccountVoucherCode.getVoucherId(), oldAccountVoucherCode.getOldVoucherId(), true , now, request.getUpdateByFullName());

            }
            
        }
        CommonMapper.copyPropertiesIgnoreNull(request, factoryImportVoucher);
        factoryImportVoucher.setUpdateAt(now);
        factoryImportVoucher.setTruckDriverId(request.getTruckDriverId());
        factoryImportVoucher.setSalesmanId(request.getSalesmanId());
        factoryImportVoucherRepository.save(factoryImportVoucher);

        goodsInOutRepository.deleteByVoucherId(id);
        cylinderDebtRepository.deleteByVoucherId(id);
        List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();
        if (goodsInOutRequests.size() != 0) {
            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                GoodsInOut goodsInOut = CommonMapper.map(goodsInOutRequest, GoodsInOut.class);

                goodsInOut.setCompanyId(factoryImportVoucher.getCompanyId());
                goodsInOut.setCustomerId(factoryImportVoucher.getCustomerId());
                goodsInOut.setVoucherAt(factoryImportVoucher.getVoucherAt());
                goodsInOut.setVoucherNo(factoryImportVoucher.getNo());
                goodsInOut.setVoucherCode(factoryImportVoucher.getVoucherCode());
                goodsInOut.setVoucherId(factoryImportVoucher.getId());
                goodsInOut.setIsMainProduct(true);
                goodsInOut.setInFactory(request.getInFactory());
                goodsInOut.setFactoryId(factoryImportVoucher.getFactoryId());
                goodsInOut.setTruckDriverId(factoryImportVoucher.getTruckDriverId());
                goodsInOut.setTruckLicensePlateNumber(factoryImportVoucher.getTruckLicensePlateNumber());
                goodsInOut.setSalesmanId(factoryImportVoucher.getSalesmanId());
                goodsInOut.setStt(goodsInOutRequests.indexOf(goodsInOutRequest));
                goodsInOut.setCreateByFullName(factoryImportVoucher.getCreatedByFullName());
                goodsInOut.setCreateAt(factoryImportVoucher.getCreatedAt());
                goodsInOut.setUpdateAt(now);
                goodsInOut.setUpdateByFullName(request.getUpdateByFullName());
                goodsInOutRepository.save(goodsInOut);

                // insert cau hinh
                if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.GAS.name())) {
                    List<ProductAccessoryDto> list = productAccessoryService.getByMainProductIdAndFactoryId(goodsInOutRequest.getProductId(), factoryImportVoucher.getFactoryId());
                    for (ProductAccessoryDto pad : list) {
                        GoodsInOut gio = new GoodsInOut();
                        gio.setVoucherNo(factoryImportVoucher.getNo());
                        gio.setVoucherAt(factoryImportVoucher.getVoucherAt());
                        gio.setVoucherCode(factoryImportVoucher.getVoucherCode());
                        gio.setVoucherId(factoryImportVoucher.getId());
                        gio.setCustomerId(factoryImportVoucher.getCustomerId());
                        gio.setCompanyId(factoryImportVoucher.getCompanyId());
                        gio.setType(goodsInOutRequest.getType());
//                        if (goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.NKTH.name()) && pad.getSubProductId().equals(ProductIdEnum.GBONAPCAO.name())) {
//                            gio.setProductId(ProductIdEnum.GASTHUHOI.name());
//                        } else {
//                            gio.setProductId(pad.getSubProductId());
//                        }
                        gio.setProductId(pad.getSubProductId());
                        gio.setProductName(pad.getSubProductName());
                        gio.setProductType((pad.getSubProductType()));
                        gio.setInQuantity(goodsInOutRequest.getInQuantity().multiply(pad.getSubProductQuantity()));
                        gio.setIsMainProduct(false);
                        gio.setInFactory(request.getInFactory());
                        gio.setFactoryId(factoryImportVoucher.getFactoryId());
                        gio.setTruckDriverId(factoryImportVoucher.getTruckDriverId());
                        gio.setTruckLicensePlateNumber(factoryImportVoucher.getTruckLicensePlateNumber());
                        gio.setSalesmanId(factoryImportVoucher.getSalesmanId());

                        //20/10/2021 Nhan: bỏ xuất nhập cấu hình trong phiếu xuất nhập
//                      goodsInOutRepository.save(gio);
                        if(gio.getProductType().equals(ProductTypeEnum.VO.name())){
                            cylinderDebtService
                                    .createCylinderDebt(new CylinderDebt(factoryImportVoucher.getId(), gio.getProductId(),
                                            factoryImportVoucher.getVoucherCode(), factoryImportVoucher.getVoucherAt(),
                                            factoryImportVoucher.getNo(),
                                            gio.getInQuantity(), BigDecimal.valueOf(0),
                                            factoryImportVoucher.getCustomerId(), factoryImportVoucher.getCompanyId(),
                                            factoryImportVoucher.getNote(), factoryImportVoucher.getCreatedAt(),
                                            factoryImportVoucher.getCreatedByFullName(), factoryImportVoucher.getUpdateAt(),
                                            factoryImportVoucher.getUpdateByFullName(), factoryImportVoucher.getFactoryId()).setGoodsInOutType(goodsInOutRequest.getType()));
                        }
                    }

                }
                if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.VO.name())) {
                    cylinderDebtService
                            .createCylinderDebt(new CylinderDebt(factoryImportVoucher.getId(), goodsInOutRequest.getProductId(),
                                    factoryImportVoucher.getVoucherCode(), factoryImportVoucher.getVoucherAt(),
                                    factoryImportVoucher.getNo(),
                                    goodsInOutRequest.getInQuantity(), BigDecimal.valueOf(0),
                                    factoryImportVoucher.getCustomerId(), factoryImportVoucher.getCompanyId(),
                                    factoryImportVoucher.getNote(), factoryImportVoucher.getCreatedAt(),
                                    factoryImportVoucher.getCreatedByFullName(), factoryImportVoucher.getUpdateAt(),
                                    factoryImportVoucher.getUpdateByFullName(), factoryImportVoucher.getFactoryId()).setGoodsInOutType(goodsInOutRequest.getType()));
                }
            }
        }

        return CommonMapper.map(factoryImportVoucher, FactoryImportVoucherDto.class);
    }

    @Override
    public void deleteFactoryImportVoucher(String id) {
    	Optional<FactoryImportVoucher> optional = factoryImportVoucherRepository.findById(id);
    	
        if (!optional.isPresent()) {
            throw new NotFoundException("Phiếu nhập nhà máy không tồn tại");
        }
        FactoryImportVoucher factoryImportVoucher = new FactoryImportVoucher();
        factoryImportVoucher.setId(id);
        factoryImportVoucher.setFactoryId(optional.get().getFactoryId());
        factoryImportVoucher.setVoucherCode(optional.get().getVoucherCode());
        factoryImportVoucher.setVoucherAt(optional.get().getVoucherAt());
        factoryImportVoucher.setTotalGoods(BigDecimal.ZERO);
        factoryImportVoucher.setTotalPayment(BigDecimal.ZERO);
        factoryImportVoucherRepository.save(factoryImportVoucher);
        accountVoucherCodeRepository.deleteByVoucherId(id);
        truckWeighingVoucherRepository.deleteById(id);
        goodsInOutRepository.deleteByVoucherId(id);
        cylinderDebtRepository.deleteByVoucherId(id);
    }

    @Override
    public FactoryImportVoucherDto createFactoryImportVoucherFromWarehouse(CreateFactoryImportExportVoucherRequestFromWarehouse request, String factoryId, String userId) {
        FactoryImportVoucher factoryImportVoucher = new FactoryImportVoucher();
        Instant now = Instant.now();
        Instant voucherAt = LocalDate.ofInstant(request.getVoucherAt(), ZoneId.of(zoneId)).atStartOfDay(ZoneId.of(zoneId)).toInstant();
        String no = voucherUtils.genereateVoucherNO(factoryId, ConstantText.VOUCHER_CODE_PHIEU_NHAP_HANG_NHA_MAY,
                i -> factoryImportVoucherRepository.countFactoryImportVoucherInDay(i));
        factoryImportVoucher.setCreatedAt(now);
        factoryImportVoucher.setUpdateAt(now);
        factoryImportVoucher.setVoucherAt(voucherAt);
        factoryImportVoucher.setId(no);
        factoryImportVoucher.setUpdateByFullName(userId);
        factoryImportVoucher.setCreatedByFullName(userId);
        factoryImportVoucher.setNo(no);
        factoryImportVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_PHIEU_NHAP_HANG_NHA_MAY);
        factoryImportVoucher.setFactoryId(factoryId);
        factoryImportVoucher.setTotalGoods(request.getActualVolume());
        factoryImportVoucherRepository.save(factoryImportVoucher);

        GoodsInOut goodsInOut = new GoodsInOut();
        goodsInOut.setIsMainProduct(true);
        goodsInOut.setVoucherNo(factoryImportVoucher.getNo());
        goodsInOut.setVoucherAt(factoryImportVoucher.getVoucherAt());
        goodsInOut.setVoucherCode(factoryImportVoucher.getVoucherCode());
        goodsInOut.setVoucherId(factoryImportVoucher.getId());
        goodsInOut.setProductId(GBONAPCAOConstant.ID);
        goodsInOut.setInQuantity(request.getActualVolume());
        if (request.getVoucherType().equals(ConstantText.VOUCHER_TYPE_NKNM)) {
            goodsInOut.setType(ConstantText.VOUCHER_TYPE_NKNM);
        } else if (request.getVoucherType().equals(ConstantText.VOUCHER_TYPE_NKTT)) {
            goodsInOut.setType(ConstantText.VOUCHER_TYPE_NKTT);
        }
        goodsInOut.setProductName(GBONAPCAOConstant.NAME);
        goodsInOut.setUnit(GBONAPCAOConstant.UNIT);
        goodsInOut.setProductType(GBONAPCAOConstant.PRODUCT_TYPE);
        goodsInOut.setWeight(BigDecimal.ONE);
        goodsInOut.setFactoryId(factoryId);
        goodsInOut.setUpdateByFullName(userId);
        goodsInOut.setUpdateAt(now);
        goodsInOut.setCreateAt(now);
        goodsInOut.setCreateByFullName(userId);

        goodsInOutRepository.save(goodsInOut);

        return CommonMapper.map(factoryImportVoucher, FactoryImportVoucherDto.class);
    }
}
