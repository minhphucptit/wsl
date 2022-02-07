package com.ceti.wholesale.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import com.ceti.wholesale.controller.api.request.CreateFactoryExportVoucherRequest;
import com.ceti.wholesale.controller.api.request.CreateFactoryImportExportVoucherRequestFromWarehouse;
import com.ceti.wholesale.controller.api.request.CreateGoodsInOutRequest;
import com.ceti.wholesale.controller.api.request.UpdateFactoryExportVoucherRequest;
import com.ceti.wholesale.dto.FactoryExportVoucherDto;
import com.ceti.wholesale.dto.ProductAccessoryDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.mapper.FactoryExportVoucherMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.CylinderDebt;
import com.ceti.wholesale.model.FactoryExportVoucher;
import com.ceti.wholesale.model.FactoryImportVoucher;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.FactoryExportVoucherRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.TruckWeighingVoucherRepository;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.FactoryExportVoucherService;
import com.ceti.wholesale.service.ProductAccessoryService;

@Service
@Transactional
public class FactoryExportVoucherServiceImpl implements FactoryExportVoucherService {

    @Autowired
    private FactoryExportVoucherRepository factoryExportVoucherRepository;

    @Autowired
    private GoodsInOutRepository goodsInOutRepository;

    @Autowired
    private ProductAccessoryService productAccessoryService;

    @Autowired
    private CylinderDebtRepository cylinderDebtRepository;

    @Autowired
    private CylinderDebtService cylinderDebtService;

    @Autowired
    private TruckWeighingVoucherRepository truckWeighingVoucherRepository;

    @Autowired
    private FactoryExportVoucherMapper factoryExportVoucherMapper;
    
    @Autowired
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

    @Autowired
    private  VoucherUtils voucherUtils;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

//    @Override
//    public Page<FactoryExportVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
//        ResultPage<Object[]> page = factoryExportVoucherDetailRepository.findAllWithFilter(pageable, where);
//        List<FactoryExportVoucherDto> factoryExportVoucherDtos = new ArrayList<>();
//        for (Object[] object : page.getPageList()) {
//            FactoryExportVoucher fev = (FactoryExportVoucher) object[0];
//            FactoryExportVoucherDto factoryExportVoucherDto = CommonMapper.map(fev, FactoryExportVoucherDto.class);
//
//            if (object[1] != null) {
//                Company company = (Company) object[1];
//                CompanyDto companyDto = CommonMapper.map(company, CompanyDto.class);
//                factoryExportVoucherDto.setCompany(companyDto);
//            }
//
//            if (object[2] != null) {
//                Customer customer = (Customer) object[2];
//                CustomerDto customerDto = CommonMapper.map(customer, CustomerDto.class);
//                factoryExportVoucherDto.setCustomer(customerDto);
//            }
//
//            if (object[3] != null) {
//                TruckDriver truckDriver = (TruckDriver) object[3];
//                TruckDriverDto truckDriverDto = CommonMapper.map(truckDriver, TruckDriverDto.class);
//                factoryExportVoucherDto.setTruckDriver(truckDriverDto);
//            }
//
//            if (object[4] != null) {
//                Truck truck = (Truck) object[4];
//                TruckDto truckDto = CommonMapper.map(truck, TruckDto.class);
//                factoryExportVoucherDto.setTruck(truckDto);
//            }
//
//            if (object[5] != null) {
//                Salesman salesman = (Salesman) object[5];
//                SalesmanDto salesmanDto = CommonMapper.map(salesman, SalesmanDto.class);
//                factoryExportVoucherDto.setSalesman(salesmanDto);
//            }
//
//            factoryExportVoucherDtos.add(factoryExportVoucherDto);
//        }
//        Instant voucherAtFrom = where.getFirst("voucher_at_from") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_from")));
//        Instant voucherAtTo = where.getFirst("voucher_at_to") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_to")));
//        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "fev");
//        List<FactoryExportVoucherDto> factoryExportVoucherDtos = factoryExportVoucherMapper.getList(where.getFirst("company_id"),
//                where.getFirst("product_id"),where.getFirst("customer_id"),where.getFirst("truck_driver_id"),
//                where.getFirst("truck_license_plate_number"),where.getFirst("factory_id"),
//                where.getFirst("goods_in_out.type"),voucherAtFrom,voucherAtTo,pagingStr);
//        long totalItems = factoryExportVoucherMapper.countList(where.getFirst("company_id"),where.getFirst("product_id"),
//                where.getFirst("customer_id"),where.getFirst("truck_driver_id"),where.getFirst("truck_license_plate_number"),
//                where.getFirst("factory_id"),where.getFirst("goods_in_out.type"),voucherAtFrom,voucherAtTo);
//        return new PageImpl<>(factoryExportVoucherDtos, pageable, totalItems);
//    }

    @Override
    public FactoryExportVoucherDto createFactoryExportVoucher(
            CreateFactoryExportVoucherRequest createFactoryExportVoucherRequest, String factoryId) {
        if (createFactoryExportVoucherRequest.getGoodsInOut().isEmpty()) {
            throw new BadRequestException("Phải có hàng mới tạo được phiếu");
        }
        Instant now = Instant.now();
        String id = voucherUtils.genereateVoucherNO(factoryId, VoucherEnum.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY.getCode(),
                i -> factoryExportVoucherRepository.countFactoryExportVoucherInDay(i));

        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryId, VoucherEnum.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY,
        		createFactoryExportVoucherRequest.getVoucherAt());
        
        FactoryExportVoucher factoryExportVoucher = CommonMapper.map(createFactoryExportVoucherRequest, FactoryExportVoucher.class);
        factoryExportVoucher.setCreatedAt(now);
        factoryExportVoucher.setUpdateAt(now);
        factoryExportVoucher.setId(id);
        factoryExportVoucher.setUpdateByFullName(factoryExportVoucher.getCreatedByFullName());
        factoryExportVoucher.setNo(id);
        factoryExportVoucher.setVoucherCode(VoucherEnum.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY.getCode());
        factoryExportVoucher.setFactoryId(factoryId);

        factoryExportVoucherRepository.save(factoryExportVoucher);

        voucherUtils.createAccountVoucherCode(accountVoucherCode,id, id ,true , now, factoryExportVoucher.getCreatedByFullName());

        if (!createFactoryExportVoucherRequest.getGoodsInOut().isEmpty()) {
            List<CreateGoodsInOutRequest> goodsInOutRequests = createFactoryExportVoucherRequest.getGoodsInOut();

            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                GoodsInOut goodsInOut = new GoodsInOut();
                CommonMapper.copyPropertiesIgnoreNull(goodsInOutRequest, goodsInOut);

                goodsInOut.setVoucherNo(factoryExportVoucher.getNo());
                goodsInOut.setVoucherCode(factoryExportVoucher.getVoucherCode());
                goodsInOut.setVoucherId(factoryExportVoucher.getId());
                goodsInOut.setCompanyId(factoryExportVoucher.getCompanyId());
                goodsInOut.setCustomerId(factoryExportVoucher.getCustomerId());
                goodsInOut.setVoucherAt(factoryExportVoucher.getVoucherAt());
                goodsInOut.setIsMainProduct(true);
                goodsInOut.setInFactory(createFactoryExportVoucherRequest.getInFactory());
                goodsInOut.setFactoryId(factoryId);
                goodsInOut.setTruckDriverId(factoryExportVoucher.getTruckDriverId());
                goodsInOut.setTruckLicensePlateNumber(factoryExportVoucher.getTruckLicensePlateNumber());
                goodsInOut.setSalesmanId(factoryExportVoucher.getSalesmanId());
                goodsInOut.setStt(goodsInOutRequests.indexOf(goodsInOutRequest));
                goodsInOut.setQuantityAt(createFactoryExportVoucherRequest.getQuantityAt());
                goodsInOut.setCreateAt(now);
                goodsInOut.setCreateByFullName(createFactoryExportVoucherRequest.getCreatedByFullName());
                goodsInOut.setUpdateByFullName(createFactoryExportVoucherRequest.getCreatedByFullName());
                goodsInOut.setUpdateAt(now);

                goodsInOutRepository.save(goodsInOut);

                // nếu sản phẩm là gas thì xuất cả cấu hình, ghi nhận công nợ vỏ
                if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.GAS.name())) {
                    List<ProductAccessoryDto> list = productAccessoryService.getByMainProductIdAndFactoryId(goodsInOutRequest.getProductId(), factoryId);
                    for (ProductAccessoryDto pad : list) {
                        GoodsInOut gio = new GoodsInOut();
                        gio.setVoucherNo(factoryExportVoucher.getNo());
                        gio.setVoucherAt(factoryExportVoucher.getVoucherAt());
                        gio.setVoucherCode(factoryExportVoucher.getVoucherCode());
                        gio.setVoucherId(factoryExportVoucher.getId());
                        gio.setCustomerId(factoryExportVoucher.getCustomerId());
                        gio.setCompanyId(factoryExportVoucher.getCompanyId());
                        gio.setType(goodsInOutRequest.getType());
//                        if (goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.XTTH.name()) && pad.getSubProductId().equals(ProductIdEnum.GBONAPCAO.name())) {
//                            gio.setProductId(ProductIdEnum.GASTHUHOI.name());
//                        } else {
//                            gio.setProductId(pad.getSubProductId());
//                        }
                        gio.setProductId(pad.getSubProductId());
                        gio.setProductName(pad.getSubProductName());
                        gio.setProductType((pad.getSubProductType()));
                        gio.setOutQuantity(goodsInOutRequest.getOutQuantity().multiply(pad.getSubProductQuantity()));
                        gio.setIsMainProduct(false);
                        gio.setInFactory(createFactoryExportVoucherRequest.getInFactory());
                        gio.setFactoryId(factoryId);
                        gio.setTruckDriverId(factoryExportVoucher.getTruckDriverId());
                        gio.setTruckLicensePlateNumber(factoryExportVoucher.getTruckLicensePlateNumber());
                        gio.setSalesmanId(factoryExportVoucher.getSalesmanId());

                        //20/10/2021 Nhan: bỏ xuất nhập cấu hình trong phiếu xuất nhập
//                      goodsInOutRepository.save(gio);
                        if(gio.getProductType().equals(ProductTypeEnum.VO.name())) {
                            cylinderDebtService
                                    .createCylinderDebt(new CylinderDebt(factoryExportVoucher.getId(), gio.getProductId(),
                                            factoryExportVoucher.getVoucherCode(), factoryExportVoucher.getVoucherAt(),
                                            factoryExportVoucher.getNo(), BigDecimal.valueOf(0),
                                            gio.getOutQuantity(), factoryExportVoucher.getCustomerId(),
                                            factoryExportVoucher.getCompanyId(), factoryExportVoucher.getNote(),
                                            factoryExportVoucher.getCreatedAt(), factoryExportVoucher.getCreatedByFullName(),
                                            factoryExportVoucher.getUpdateAt(), factoryExportVoucher.getUpdateByFullName(),
                                            factoryId).setGoodsInOutType(goodsInOutRequest.getType()));
                        }
                    }
                }
                if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.VO.name())
                        && !goodsInOutRequest.getOutQuantity().equals(BigDecimal.valueOf(0))) {
                    cylinderDebtService
                            .createCylinderDebt(new CylinderDebt(factoryExportVoucher.getId(), goodsInOutRequest.getProductId(),
                                    factoryExportVoucher.getVoucherCode(), factoryExportVoucher.getVoucherAt(),
                                    factoryExportVoucher.getNo(), BigDecimal.valueOf(0),
                                    goodsInOutRequest.getOutQuantity(), factoryExportVoucher.getCustomerId(),
                                    factoryExportVoucher.getCompanyId(), factoryExportVoucher.getNote(),
                                    factoryExportVoucher.getCreatedAt(), factoryExportVoucher.getCreatedByFullName(),
                                    factoryExportVoucher.getUpdateAt(), factoryExportVoucher.getUpdateByFullName(),
                                    factoryId).setGoodsInOutType(goodsInOutRequest.getType()));
                }

            }
        }
        
        FactoryExportVoucherDto factoryExportVoucherDto = CommonMapper.map(factoryExportVoucher, FactoryExportVoucherDto.class);
        factoryExportVoucherDto.setAccNo(accountVoucherCode.getAccNo());
        return factoryExportVoucherDto;
    }

    @Override
    public FactoryExportVoucherDto updateFactoryExportVoucher(String id, UpdateFactoryExportVoucherRequest request) {
        Optional<FactoryExportVoucher> optional = factoryExportVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu xuất hàng không tồn tại");
        }
        FactoryExportVoucher factoryExportVoucher = optional.get();
        Instant now = Instant.now();
        
        if(!request.getVoucherAt().equals(factoryExportVoucher.getVoucherAt())) {
            AccountVoucherCode accountVoucherCode  = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryExportVoucher.getFactoryId(),
            		VoucherEnum.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY, request.getVoucherAt());
            
//            factoryExportVoucher.setNo(no);
            
            AccountVoucherCode oldAccountVoucherCode = accountVoucherCodeRepository.findById(id).orElse(null);
            
            if(oldAccountVoucherCode != null) {
                String updateBy = oldAccountVoucherCode.getUpdateBy();
                Instant updateAt = oldAccountVoucherCode.getUpdateAt();

                //tạo account voucher code mới
                voucherUtils.createAccountVoucherCode(oldAccountVoucherCode,UUID.randomUUID().toString().replaceAll("-", ""), oldAccountVoucherCode.getOldVoucherId(),
                        false , updateAt, updateBy);

                //update lại account voucher code cũ
                voucherUtils.createAccountVoucherCode(accountVoucherCode,oldAccountVoucherCode.getVoucherId(), oldAccountVoucherCode.getOldVoucherId() ,true , now, request.getUpdateByFullName());

            }         
        }
        CommonMapper.copyPropertiesIgnoreNull(request, factoryExportVoucher);
        factoryExportVoucher.setUpdateAt(now);
        factoryExportVoucher.setTruckDriverId(request.getTruckDriverId());
        factoryExportVoucher.setSalesmanId(request.getSalesmanId());
        factoryExportVoucherRepository.save(factoryExportVoucher);

        goodsInOutRepository.deleteByVoucherId(id);
        cylinderDebtRepository.deleteByVoucherId(id);
        List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();
        if (goodsInOutRequests.size() != 0) {
            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                GoodsInOut goodsInOut = new GoodsInOut();
                CommonMapper.copyPropertiesIgnoreNull(goodsInOutRequest, goodsInOut);
                goodsInOut.setVoucherNo(factoryExportVoucher.getNo());
                goodsInOut.setVoucherCode(factoryExportVoucher.getVoucherCode());
                goodsInOut.setVoucherId(factoryExportVoucher.getId());
                goodsInOut.setCompanyId(factoryExportVoucher.getCompanyId());
                goodsInOut.setCustomerId(factoryExportVoucher.getCustomerId());
                goodsInOut.setVoucherAt(factoryExportVoucher.getVoucherAt());
                goodsInOut.setIsMainProduct(true);
                goodsInOut.setInFactory(request.getInFactory());
                goodsInOut.setFactoryId(factoryExportVoucher.getFactoryId());
                goodsInOut.setTruckDriverId(factoryExportVoucher.getTruckDriverId());
                goodsInOut.setTruckLicensePlateNumber(factoryExportVoucher.getTruckLicensePlateNumber());
                goodsInOut.setSalesmanId(factoryExportVoucher.getSalesmanId());
                goodsInOut.setStt(goodsInOutRequests.indexOf(goodsInOutRequest));
                goodsInOut.setQuantityAt(request.getQuantityAt());
                goodsInOut.setCreateAt(factoryExportVoucher.getCreatedAt());
                goodsInOut.setCreateByFullName(factoryExportVoucher.getCreatedByFullName());
                goodsInOut.setUpdateAt(now);
                goodsInOut.setUpdateByFullName(request.getUpdateByFullName());
                goodsInOutRepository.save(goodsInOut);

                if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.GAS.name())) {
                    List<ProductAccessoryDto> list = productAccessoryService.getByMainProductIdAndFactoryId(goodsInOutRequest.getProductId(), factoryExportVoucher.getFactoryId());
                    for (ProductAccessoryDto pad : list) {
                        GoodsInOut gio = new GoodsInOut();
                        gio.setVoucherNo(factoryExportVoucher.getNo());
                        gio.setVoucherAt(factoryExportVoucher.getVoucherAt());
                        gio.setVoucherCode(factoryExportVoucher.getVoucherCode());
                        gio.setVoucherId(factoryExportVoucher.getId());
                        gio.setCustomerId(factoryExportVoucher.getCustomerId());
                        gio.setCompanyId(factoryExportVoucher.getCompanyId());
                        gio.setType(goodsInOutRequest.getType());
//                        if (goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.XTTH.name()) && pad.getSubProductId().equals(ProductIdEnum.GBONAPCAO.name())) {
//                            gio.setProductId(ProductIdEnum.GASTHUHOI.name());
//                        } else {
//                            gio.setProductId(pad.getSubProductId());
//                        }
                        gio.setProductId(pad.getSubProductId());
                        gio.setProductName(pad.getSubProductName());
                        gio.setProductType((pad.getSubProductType()));
                        gio.setOutQuantity(goodsInOutRequest.getOutQuantity().multiply(pad.getSubProductQuantity()));
                        gio.setIsMainProduct(false);
                        gio.setInFactory(request.getInFactory());
                        gio.setFactoryId(factoryExportVoucher.getFactoryId());
                        gio.setTruckDriverId(factoryExportVoucher.getTruckDriverId());
                        gio.setTruckLicensePlateNumber(factoryExportVoucher.getTruckLicensePlateNumber());
                        gio.setSalesmanId(factoryExportVoucher.getSalesmanId());

                        //20/10/2021 Nhan: bỏ xuất nhập cấu hình trong phiếu xuất nhập
//                      goodsInOutRepository.save(gio);
                        if(gio.getProductType().equals(ProductTypeEnum.VO.name())) {
                            cylinderDebtService
                                    .createCylinderDebt(new CylinderDebt(factoryExportVoucher.getId(), gio.getProductId(),
                                            factoryExportVoucher.getVoucherCode(), factoryExportVoucher.getVoucherAt(),
                                            factoryExportVoucher.getNo(), BigDecimal.valueOf(0),
                                            gio.getOutQuantity(), factoryExportVoucher.getCustomerId(),
                                            factoryExportVoucher.getCompanyId(), factoryExportVoucher.getNote(),
                                            factoryExportVoucher.getCreatedAt(), factoryExportVoucher.getCreatedByFullName(),
                                            factoryExportVoucher.getUpdateAt(), factoryExportVoucher.getUpdateByFullName(),
                                            factoryExportVoucher.getFactoryId()).setGoodsInOutType(goodsInOutRequest.getType()));
                        }
                    }
                }

                if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.VO.name()) && !goodsInOutRequest.getOutQuantity().equals(BigDecimal.valueOf(0))) {
                    cylinderDebtService
                            .createCylinderDebt(new CylinderDebt(factoryExportVoucher.getId(), goodsInOutRequest.getProductId(),
                                    factoryExportVoucher.getVoucherCode(), factoryExportVoucher.getVoucherAt(),
                                    factoryExportVoucher.getNo(), BigDecimal.valueOf(0),
                                    goodsInOutRequest.getOutQuantity(), factoryExportVoucher.getCustomerId(),
                                    factoryExportVoucher.getCompanyId(), factoryExportVoucher.getNote(),
                                    factoryExportVoucher.getCreatedAt(), factoryExportVoucher.getCreatedByFullName(),
                                    factoryExportVoucher.getUpdateAt(), factoryExportVoucher.getUpdateByFullName(),
                                    factoryExportVoucher.getFactoryId()).setGoodsInOutType(goodsInOutRequest.getType()));

                }
            }
        }

        return CommonMapper.map(factoryExportVoucher, FactoryExportVoucherDto.class);
    }

    @Override
    public void deleteFactoryExportVoucher(String id) {
    	Optional<FactoryExportVoucher> optional = factoryExportVoucherRepository.findById(id);
    	
        if (!optional.isPresent()) {
            throw new NotFoundException("Phiếu xuất nhà máy không tồn tại");
        }
        FactoryExportVoucher factoryExportVoucher = new FactoryExportVoucher();
        factoryExportVoucher.setId(id);
        factoryExportVoucher.setFactoryId(optional.get().getFactoryId());
        factoryExportVoucher.setVoucherCode(optional.get().getVoucherCode());
        factoryExportVoucher.setVoucherAt(optional.get().getVoucherAt());
        factoryExportVoucher.setTotalGoods(BigDecimal.ZERO);
        factoryExportVoucher.setTotalPaymentReceived(BigDecimal.ZERO);
        factoryExportVoucherRepository.save(factoryExportVoucher);
        accountVoucherCodeRepository.deleteByVoucherId(id);

        truckWeighingVoucherRepository.deleteById(id);
        goodsInOutRepository.deleteByVoucherId(id);
        cylinderDebtRepository.deleteByVoucherId(id);
    }

    @Override
    public FactoryExportVoucherDto createFactoryExportVoucherFromWarehouse(CreateFactoryImportExportVoucherRequestFromWarehouse request, String factoryId, String userId) {
        FactoryExportVoucher factoryExportVoucher = new FactoryExportVoucher();
        Instant voucherAt = LocalDate.ofInstant(request.getVoucherAt(), ZoneId.of(zoneId)).atStartOfDay(ZoneId.of(zoneId)).toInstant();
        Instant now = Instant.now();
        String no = voucherUtils.genereateVoucherNO(factoryId, ConstantText.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY,
                i -> factoryExportVoucherRepository.countFactoryExportVoucherInDay(i));
        factoryExportVoucher.setCreatedAt(now);
        factoryExportVoucher.setUpdateAt(now);
        factoryExportVoucher.setVoucherAt(voucherAt);
        factoryExportVoucher.setId(no);
        factoryExportVoucher.setUpdateByFullName(userId);
        factoryExportVoucher.setCreatedByFullName(userId);
        factoryExportVoucher.setNo(no);
        factoryExportVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_PHIEU_XUAT_HANG_NHA_MAY);
        factoryExportVoucher.setFactoryId(factoryId);
        factoryExportVoucher.setTotalGoods(request.getActualVolume());
        factoryExportVoucherRepository.save(factoryExportVoucher);

        GoodsInOut goodsInOut = new GoodsInOut();
        goodsInOut.setIsMainProduct(true);
        goodsInOut.setVoucherNo(factoryExportVoucher.getNo());
        goodsInOut.setVoucherAt(factoryExportVoucher.getVoucherAt());
        goodsInOut.setVoucherCode(factoryExportVoucher.getVoucherCode());
        goodsInOut.setVoucherId(factoryExportVoucher.getId());
        goodsInOut.setProductId(GBONAPCAOConstant.ID);
        goodsInOut.setOutQuantity(request.getActualVolume());
        if (request.getVoucherType().equals(ConstantText.VOUCHER_TYPE_XKNM)) {
            goodsInOut.setType(ConstantText.VOUCHER_TYPE_XKNM);
        } else if (request.getVoucherType().equals(ConstantText.VOUCHER_TYPE_XKTT)) {
            goodsInOut.setType(ConstantText.VOUCHER_TYPE_XKTT);
        }
        goodsInOut.setProductName(GBONAPCAOConstant.NAME);
        goodsInOut.setUnit(GBONAPCAOConstant.UNIT);
        goodsInOut.setProductType(GBONAPCAOConstant.PRODUCT_TYPE);
        goodsInOut.setWeight(BigDecimal.ONE);
        goodsInOut.setFactoryId(factoryId);
        goodsInOut.setCreateAt(now);
        goodsInOut.setCreateByFullName(userId);
        goodsInOut.setUpdateByFullName(userId);
        goodsInOut.setUpdateAt(now);

        goodsInOutRepository.save(goodsInOut);

        return CommonMapper.map(factoryExportVoucher, FactoryExportVoucherDto.class);
    }

}
