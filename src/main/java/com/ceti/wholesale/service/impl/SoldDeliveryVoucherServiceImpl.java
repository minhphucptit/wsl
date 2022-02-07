package com.ceti.wholesale.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ceti.wholesale.dto.PaymentVoucherDto;
import com.ceti.wholesale.dto.ReturnVoucherTotalGoodsDto;
import com.ceti.wholesale.mapper.PaymentVoucherMapper;
import com.ceti.wholesale.model.*;
import com.ceti.wholesale.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.common.constant.ConstantText;
import com.ceti.wholesale.common.enums.GoodsInOutTypeEnum;
import com.ceti.wholesale.common.enums.ProductTypeEnum;
import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.CreateGoodsInOutRequest;
import com.ceti.wholesale.controller.api.request.CreateSoldDeliveryVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateSoldDeliveryVoucherRequest;
import com.ceti.wholesale.dto.ProductAccessoryDto;
import com.ceti.wholesale.dto.SoldDeliveryVoucherDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.mapper.SoldDeliveryVoucherMapper;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.ProductAccessoryService;
import com.ceti.wholesale.service.SoldDeliveryVoucherService;

@Service
@Transactional
public class SoldDeliveryVoucherServiceImpl implements SoldDeliveryVoucherService {

    @Autowired
    private SoldDeliveryVoucherRepository soldDeliveryVoucherRepository;

    @Autowired
    private GoodsInOutService goodsInOutService;

    @Autowired
    private GoodsInOutRepository goodsInOutRepository;

    @Autowired
    private ProductAccessoryService productAccessoryService;

    @Autowired
    private CylinderDebtService cylinderDebtService;

    @Autowired
    private CylinderDebtRepository cylinderDebtRepository;

    @Autowired
    private ReturnVoucherRepository returnVoucherRepository;

    @Autowired
    private PaymentVoucherRepository paymentVoucherRepository;

    @Autowired
    private SoldDeliveryVoucherMapper soldDeliveryVoucherMapper;
    
    @Autowired
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

    @Autowired
    private  VoucherUtils voucherUtils;

    @Autowired
    private PaymentVoucherMapper paymentVoucherMapper;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

//    @Override
//    public Page<SoldDeliveryVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
//        ResultPage<Object[]> page = soldDeliveryVoucherDetailRepository.findAllWithFilter(pageable, where);
//        List<SoldDeliveryVoucherDto> soldDeliveryVoucherDtos = new ArrayList<>();
//        for (Object[] object : page.getPageList()) {
//            SoldDeliveryVoucher sv = (SoldDeliveryVoucher) object[0];
//            SoldDeliveryVoucherDto soldDeliveryVoucherDto = CommonMapper.map(sv, SoldDeliveryVoucherDto.class);
//            if (object[1] != null) {
//                Company company = (Company) object[1];
//                CompanyDto companyDto = CommonMapper.map(company, CompanyDto.class);
//                soldDeliveryVoucherDto.setCompany(companyDto);
//            }
//
//            if (object[2] != null) {
//                Customer customer = (Customer) object[2];
//                CustomerDto customerDto = CommonMapper.map(customer, CustomerDto.class);
//                soldDeliveryVoucherDto.setCustomer(customerDto);
//            }
//
//            if (object[3] != null) {
//                TruckDriver truckDriver = (TruckDriver) object[3];
//                TruckDriverDto truckDriverDto = CommonMapper.map(truckDriver, TruckDriverDto.class);
//                soldDeliveryVoucherDto.setTruckDriver(truckDriverDto);
//            }
//
//            if (object[4] != null) {
//                Truck truck = (Truck) object[4];
//                TruckDto truckDto = CommonMapper.map(truck, TruckDto.class);
//                soldDeliveryVoucherDto.setTruck(truckDto);
//            }
//
//            if (object[5] != null) {
//                Salesman salesman = (Salesman) object[5];
//                SalesmanDto salesmanDto = CommonMapper.map(salesman, SalesmanDto.class);
//                soldDeliveryVoucherDto.setSalesman(salesmanDto);
//            }
//            soldDeliveryVoucherDtos.add(soldDeliveryVoucherDto);
//
//        }
//        Instant voucherAtFrom = where.getFirst("voucher_at_from") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_from")));
//        Instant voucherAtTo = where.getFirst("voucher_at_to") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_to")));
//        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "sdv");
//        List<SoldDeliveryVoucherDto> soldDeliveryVoucherDtos= soldDeliveryVoucherMapper.getList(where.getFirst("product_id"),where.getFirst("no"),where.getFirst("company_id"),
//                where.getFirst("customer_id"),where.getFirst("truck_license_plate_number"),
//                where.getFirst("factory_id"),where.getFirst("goods_in_out.type"),voucherAtFrom,voucherAtTo,where.getFirst("delivery_voucher_no"),pagingStr);
//        long totalItems = soldDeliveryVoucherMapper.countList(where.getFirst("product_id"),where.getFirst("no"),where.getFirst("company_id"),
//                where.getFirst("customer_id"),where.getFirst("truck_license_plate_number"),
//                where.getFirst("factory_id"),where.getFirst("goods_in_out.type"),voucherAtFrom,voucherAtTo,where.getFirst("delivery_voucher_no"));
//        return new PageImpl<>(soldDeliveryVoucherDtos, pageable, totalItems);
//    }

    @Override
    public SoldDeliveryVoucherDto createSoldDeliveryVoucher(CreateSoldDeliveryVoucherRequest request,
                                                            String factoryId) {
        if (request.getGoodsInOut().isEmpty()) {
            throw new BadRequestException("Phải có hàng mới tạo được phiếu");
        }
        Instant now = Instant.now();
        String id = voucherUtils.genereateVoucherNO(factoryId, VoucherEnum.VOUCHER_CODE_XUAT_XE_BAN.getCode(),
                i -> soldDeliveryVoucherRepository.countSoldDeliveryVoucherInDay(i));

        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryId, VoucherEnum.VOUCHER_CODE_XUAT_XE_BAN,
        		request.getVoucherAt());
        
        SoldDeliveryVoucher soldDeliveryVoucher = CommonMapper.map(request, SoldDeliveryVoucher.class);
        soldDeliveryVoucher.setCreatedAt(now);
        soldDeliveryVoucher.setUpdateAt(now);
        soldDeliveryVoucher.setUpdateByFullName(request.getCreatedByFullName());
        soldDeliveryVoucher.setNo(id);
        soldDeliveryVoucher.setId(id);
        soldDeliveryVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_XUAT_XE_BAN);
        soldDeliveryVoucher.setFactoryId(factoryId);

        soldDeliveryVoucherRepository.save(soldDeliveryVoucher);

        voucherUtils.createAccountVoucherCode(accountVoucherCode,id, id ,true , now, soldDeliveryVoucher.getCreatedByFullName());

        List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();

        if (goodsInOutRequests.size() != 0) {
            StringBuilder err = new StringBuilder();
            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                goodsInOutRequest.setCustomerId(soldDeliveryVoucher.getCustomerId());
                goodsInOutRequest.setCompanyId(soldDeliveryVoucher.getCompanyId());
                goodsInOutRequest.setVoucherAt(soldDeliveryVoucher.getVoucherAt());
                goodsInOutRequest.setXxeOutQuantity(BigDecimal.valueOf(0));
                goodsInOutRequest.setXbxOutQuantity(BigDecimal.valueOf(0));
                goodsInOutRequest.setNxeInQuantity(BigDecimal.valueOf(0));
                goodsInOutRequest.setTruckDriverId(soldDeliveryVoucher.getTruckDriverId());
                goodsInOutRequest.setTruckLicensePlateNumber(soldDeliveryVoucher.getTruckLicensePlateNumber());
                goodsInOutRequest.setSalesmanId(soldDeliveryVoucher.getSalesmanId());

                BigDecimal max = goodsInOutRepository
                        .getMaxXBXOutQuantityOrInQuantityOneProduct(request.getDeliveryVoucherId(),
                                goodsInOutRequest.getProductId(), goodsInOutRequest.getStt() );
                if (goodsInOutRequest.getOutQuantity() != null && goodsInOutRequest.getOutQuantity().doubleValue() > max.doubleValue()) {
                    err.append("Số lượng xuất bán của sản phẩm ").append(goodsInOutRequest.getProductName()).append(" phải nhỏ hơn hoặc bằng ").append(max).append("\n");
                } else {
                    if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.GAS.name())) {
                        List<ProductAccessoryDto> list = productAccessoryService.getByMainProductIdAndFactoryId(goodsInOutRequest.getProductId(), factoryId);
                        for (ProductAccessoryDto pad : list) {
                            GoodsInOut gio = new GoodsInOut();
                            gio.setVoucherNo(soldDeliveryVoucher.getNo());
                            gio.setVoucherCode(soldDeliveryVoucher.getVoucherCode());
                            gio.setVoucherId(soldDeliveryVoucher.getId());
                            gio.setType(goodsInOutRequest.getType());
                            gio.setCompanyId(soldDeliveryVoucher.getCompanyId());
                            gio.setCustomerId(soldDeliveryVoucher.getCustomerId());
                            gio.setProductId(pad.getSubProductId());
                            gio.setProductName(pad.getSubProductName());
                            gio.setProductType((pad.getSubProductType()));
                            gio.setVoucherAt(soldDeliveryVoucher.getVoucherAt());
                            gio.setOutQuantity(goodsInOutRequest.getOutQuantity().multiply(pad.getSubProductQuantity()));                        gio.setIsMainProduct(false);
                            gio.setFactoryId(factoryId);
                            gio.setTruckDriverId(soldDeliveryVoucher.getTruckDriverId());
                            gio.setTruckLicensePlateNumber(soldDeliveryVoucher.getTruckLicensePlateNumber());
                            gio.setSalesmanId(soldDeliveryVoucher.getSalesmanId());

                            //20/10/2021 Nhan: bỏ xuất nhập cấu hình trong phiếu xuất nhập
//                          goodsInOutRepository.save(gio);
                            if (pad.getSubProductType().equals(ProductTypeEnum.VO.name())&&!goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.XBTC.name())) {
                                cylinderDebtService
                                        .createCylinderDebt(new CylinderDebt(soldDeliveryVoucher.getId(), pad.getSubProductId(),
                                                soldDeliveryVoucher.getVoucherCode(),
                                                soldDeliveryVoucher.getVoucherAt(), soldDeliveryVoucher.getNo(),
                                                BigDecimal.valueOf(0),
                                                goodsInOutRequest.getOutQuantity()
                                                        .multiply(pad.getSubProductQuantity()),
                                                soldDeliveryVoucher.getCustomerId(),
                                                soldDeliveryVoucher.getCompanyId(), soldDeliveryVoucher.getNote(),
                                                soldDeliveryVoucher.getCreatedAt(),
                                                soldDeliveryVoucher.getCreatedByFullName(),
                                                soldDeliveryVoucher.getUpdateAt(),
                                                soldDeliveryVoucher.getUpdateByFullName(), factoryId).setGoodsInOutType(goodsInOutRequest.getType()));
                            }
                        }
                    }
                    goodsInOutRequest.setFactoryId(factoryId);
                    createGoodsInOut(goodsInOutRequest, accountVoucherCode.getAccNo(), soldDeliveryVoucher.getVoucherCode(), soldDeliveryVoucher.getId(), goodsInOutRequest.getStt(),now, request.getCreatedByFullName(),now, request.getCreatedByFullName());
                    goodsInOutRepository.updateXBXOutQuantityXXEOneProduct(request.getDeliveryVoucherId(), goodsInOutRequest.getProductId(), goodsInOutRequest.getStt() );

                }
            }
            if (err.length() > 0) {
                throw new BadRequestException(err.toString());
            }
        }
        SoldDeliveryVoucherDto soldDeliveryVoucherDto = CommonMapper.map(soldDeliveryVoucher, SoldDeliveryVoucherDto.class);
        soldDeliveryVoucherDto.setAccNo(accountVoucherCode.getAccNo());

        return soldDeliveryVoucherDto;
    }

    @Override
    public SoldDeliveryVoucherDto updateSoldDeliveryVoucher(String id, UpdateSoldDeliveryVoucherRequest request) {

        Optional<SoldDeliveryVoucher> optional = soldDeliveryVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu xuất bán hàng theo xe không tồn tại");
        }

        SoldDeliveryVoucher soldDeliveryVoucher = optional.get();
        Instant now = Instant.now();
        if(!request.getVoucherAt().equals(soldDeliveryVoucher.getVoucherAt())) {
            AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(soldDeliveryVoucher.getFactoryId(),
            		VoucherEnum.VOUCHER_CODE_XUAT_XE_BAN,request.getVoucherAt());
            
//            soldDeliveryVoucher.setNo(no);
            
            AccountVoucherCode oldAccountVoucherCode = accountVoucherCodeRepository.findById(id).orElse(null);
            
            if(oldAccountVoucherCode != null) {
                String updateBy = oldAccountVoucherCode.getUpdateBy();
                Instant updateAt = oldAccountVoucherCode.getUpdateAt();

                //tạo account voucher code mới
                voucherUtils.createAccountVoucherCode(oldAccountVoucherCode, UUID.randomUUID().toString().replaceAll("-", ""), oldAccountVoucherCode.getOldVoucherId(),
                        false , updateAt, updateBy);
                //update lại account voucher code cũ
                voucherUtils.createAccountVoucherCode(accountVoucherCode, oldAccountVoucherCode.getVoucherId(), oldAccountVoucherCode.getOldVoucherId() ,true , now, request.getUpdateByFullName());

            }
            
        }
        CommonMapper.copyPropertiesIgnoreNull(request, soldDeliveryVoucher);
        soldDeliveryVoucher.setUpdateAt(now);
        soldDeliveryVoucherRepository.save(soldDeliveryVoucher);

        goodsInOutRepository.deleteByVoucherId(id);
        cylinderDebtRepository.deleteByVoucherId(id);
        List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();

        if (goodsInOutRequests.size() != 0) {
            StringBuilder err = new StringBuilder();
            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                goodsInOutRequest.setCompanyId(soldDeliveryVoucher.getCompanyId());
                goodsInOutRequest.setCustomerId(soldDeliveryVoucher.getCustomerId());
                goodsInOutRequest.setVoucherAt(soldDeliveryVoucher.getVoucherAt());
                goodsInOutRequest.setTruckDriverId(soldDeliveryVoucher.getTruckDriverId());
                goodsInOutRequest.setSalesmanId(soldDeliveryVoucher.getSalesmanId());

                goodsInOutRequest.setTruckLicensePlateNumber(soldDeliveryVoucher.getTruckLicensePlateNumber());
                goodsInOutRepository.updateXBXOutQuantityXXEOneProduct(soldDeliveryVoucher.getDeliveryVoucherId(), goodsInOutRequest.getProductId(), goodsInOutRequest.getStt() );
                BigDecimal max = goodsInOutRepository
                        .getMaxXBXOutQuantityOrInQuantityOneProduct(soldDeliveryVoucher.getDeliveryVoucherId(),
                                goodsInOutRequest.getProductId(), goodsInOutRequest.getStt() );
                if (goodsInOutRequest.getOutQuantity() != null && goodsInOutRequest.getOutQuantity().doubleValue() > max.doubleValue()) {
                    err.append("Số lượng xuất bán của sản phẩm ").append(goodsInOutRequest.getProductName()).append(" phải nhỏ hơn hoặc bằng ").append(max).append("\n");
                } else {
                    if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.GAS.name())) {
                        List<ProductAccessoryDto> list = productAccessoryService.getByMainProductIdAndFactoryId(goodsInOutRequest.getProductId(), soldDeliveryVoucher.getFactoryId());
                        for (ProductAccessoryDto pad : list) {
                            GoodsInOut gio = new GoodsInOut();
                            gio.setVoucherNo(soldDeliveryVoucher.getNo());
                            gio.setVoucherCode(soldDeliveryVoucher.getVoucherCode());
                            gio.setVoucherId(soldDeliveryVoucher.getId());
                            gio.setType(goodsInOutRequest.getType());
                            gio.setCompanyId(soldDeliveryVoucher.getCompanyId());
                            gio.setCustomerId(soldDeliveryVoucher.getCustomerId());
                            gio.setProductId(pad.getSubProductId());
                            gio.setProductName(pad.getSubProductName());
                            gio.setProductType((pad.getSubProductType()));
                            gio.setVoucherAt(soldDeliveryVoucher.getVoucherAt());
                            gio.setOutQuantity(goodsInOutRequest.getOutQuantity().multiply(pad.getSubProductQuantity()));                        gio.setIsMainProduct(false);
                            gio.setFactoryId(soldDeliveryVoucher.getFactoryId());
                            gio.setTruckDriverId(soldDeliveryVoucher.getTruckDriverId());
                            gio.setTruckLicensePlateNumber(soldDeliveryVoucher.getTruckLicensePlateNumber());
                            gio.setSalesmanId(soldDeliveryVoucher.getSalesmanId());

                            //20/10/2021 Nhan: bỏ xuất nhập cấu hình trong phiếu xuất nhập
//                          goodsInOutRepository.save(gio);
                            if (pad.getSubProductType().equals(ProductTypeEnum.VO.name())&&!goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.XBTC.name())) {
                                cylinderDebtService
                                        .createCylinderDebt(new CylinderDebt(soldDeliveryVoucher.getId(), pad.getSubProductId(),
                                                soldDeliveryVoucher.getVoucherCode(),
                                                soldDeliveryVoucher.getVoucherAt(), soldDeliveryVoucher.getNo(),
                                                BigDecimal.valueOf(0),
                                                goodsInOutRequest.getOutQuantity()
                                                        .multiply(pad.getSubProductQuantity()),
                                                soldDeliveryVoucher.getCustomerId(),
                                                soldDeliveryVoucher.getCompanyId(), soldDeliveryVoucher.getNote(),
                                                soldDeliveryVoucher.getCreatedAt(),
                                                soldDeliveryVoucher.getCreatedByFullName(),
                                                soldDeliveryVoucher.getUpdateAt(),
                                                soldDeliveryVoucher.getUpdateByFullName(),
                                                soldDeliveryVoucher.getFactoryId()).setGoodsInOutType(goodsInOutRequest.getType()));
                            }
                        }
                    }
                    BigDecimal cylinderPrice = new BigDecimal(0);
//                    if (goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.XBTC.name())) {
//                        cylinderPrice = productRepository.getBylinderPriceByCustomerAndProduct(goodsInOutRequest.getCustomerId(), goodsInOutRequest.getProductId(), soldDeliveryVoucher.getFactoryId());
//                    }
                    goodsInOutRequest.setFactoryId(soldDeliveryVoucher.getFactoryId());
                    createGoodsInOut(goodsInOutRequest, soldDeliveryVoucher.getNo(),
                                    soldDeliveryVoucher.getVoucherCode(), soldDeliveryVoucher.getId(), goodsInOutRequest.getStt(),soldDeliveryVoucher.getCreatedAt(),soldDeliveryVoucher.getCreatedByFullName(),now, request.getUpdateByFullName());
                    goodsInOutRepository.updateXBXOutQuantityXXEOneProduct(soldDeliveryVoucher.getDeliveryVoucherId(), goodsInOutRequest.getProductId(), goodsInOutRequest.getStt() );

                }
            }
            if (err.length() > 0) {
                throw new BadRequestException(err.toString());
            }
        }
        return CommonMapper
                .map(soldDeliveryVoucher, SoldDeliveryVoucherDto.class);


    }
    public void createReturnVoucher(PaymentVoucher paymentVoucher) {

        ReturnVoucher returnVoucher = new ReturnVoucher();
        returnVoucher.setVoucherAt(paymentVoucher.getVoucherAt());
        returnVoucher.setCompanyId(paymentVoucher.getCompanyId());
        returnVoucher.setTruckDriverId(paymentVoucher.getTruckDriverId());
        returnVoucher.setTruckLicensePlateNumber(paymentVoucher.getTruckLicensePlateNumber());
        returnVoucher.setTotalGoods(BigDecimal.ZERO);
        returnVoucher.setTruckDriverFullName(paymentVoucher.getTruckDriverFullName());
        returnVoucher.setCreatedByFullName("ADMIN");
        returnVoucher.setDeliveryVoucherNo(paymentVoucher.getDeliveryVoucherNo());
        returnVoucher.setDeliveryVoucherId(paymentVoucher.getDeliveryVoucherId());


        String deliveryVoucherNo  = paymentVoucher.getDeliveryVoucherNo();
        String factoryId = paymentVoucher.getFactoryId();

        ReturnVoucher oldReturnVoucher = returnVoucherRepository.findByDeliveryVoucherNo(deliveryVoucherNo);

        String no = null;
        if(oldReturnVoucher != null) {
            // 06/12/2021 NamLH fix issue #1699
            no = oldReturnVoucher.getNo();
            returnVoucherRepository.deleteByDeliveryVoucherNo(deliveryVoucherNo);
            goodsInOutRepository.deleteByVoucherId(oldReturnVoucher.getId());
        }

        List<List> data = paymentVoucherMapper.getListReturnProducts(deliveryVoucherNo, factoryId);

        List<GoodsInOut> goodsInOuts = ( List<GoodsInOut>) (Object)data.get(0);

        List<ReturnVoucherTotalGoodsDto> totalGoodsInOut = (List<ReturnVoucherTotalGoodsDto>) (Object)data.get(1);

        if(totalGoodsInOut.get(0) != null) {
            returnVoucher.setTotalGoods(totalGoodsInOut.get(0).getTotalGoods());
        }

        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryId, VoucherEnum.VOUCHER_CODE_NHAP_XE,
                returnVoucher.getVoucherAt());

        Instant now = Instant.now();
        // 06/12/2021 NamLH fix issue #1699
        if (no == null) {
            no = voucherUtils.genereateVoucherNO(factoryId, ConstantText.VOUCHER_CODE_NHAP_XE, i -> returnVoucherRepository.countReturnVoucherInDay(i));
        }
        returnVoucher.setCreatedAt(now);
        returnVoucher.setUpdateAt(now);
        returnVoucher.setId(no);
        returnVoucher.setUpdateByFullName(returnVoucher.getCreatedByFullName());
        returnVoucher.setNo(no);
        returnVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_NHAP_XE);
        returnVoucher.setFactoryId(factoryId);
        if (!goodsInOuts.isEmpty()) {
            returnVoucherRepository.save(returnVoucher);
            voucherUtils.createAccountVoucherCode(accountVoucherCode, no, no ,true, now, returnVoucher.getCreatedByFullName());

            for (GoodsInOut goodsInOut : goodsInOuts) {
                CommonMapper.copyPropertiesIgnoreNull(returnVoucher, goodsInOut);
                goodsInOut.setVoucherNo(returnVoucher.getNo());
                goodsInOut.setVoucherId(returnVoucher.getId());
                goodsInOut.setStt(goodsInOuts.indexOf(goodsInOut));
                goodsInOut.setId(null);
                goodsInOut.setUpdateAt(now);
                goodsInOut.setUpdateByFullName(returnVoucher.getCreatedByFullName());
                goodsInOut.setCreateAt(now);
                goodsInOut.setCreateByFullName(returnVoucher.getCreatedByFullName());
                goodsInOutRepository.save(goodsInOut);
            }
        }
    }
    @Override
    public void deleteSoldDeliveryVoucher(String id) {
    	
    	Optional<SoldDeliveryVoucher> optional = soldDeliveryVoucherRepository.findById(id);

        if (!optional.isPresent()) {
            throw new NotFoundException("Phiếu xuất bán hàng theo xe không tồn tại");
        }
        SoldDeliveryVoucher oldSoldDeliveryVoucher =optional.get();
        String deliveryVoucherId = oldSoldDeliveryVoucher.getDeliveryVoucherId();
        SoldDeliveryVoucher soldDeliveryVoucher = new SoldDeliveryVoucher();
        soldDeliveryVoucher.setId(id);
        soldDeliveryVoucher.setFactoryId(oldSoldDeliveryVoucher.getFactoryId());
        soldDeliveryVoucher.setVoucherCode(oldSoldDeliveryVoucher.getVoucherCode());
        soldDeliveryVoucher.setVoucherAt(oldSoldDeliveryVoucher.getVoucherAt());
        soldDeliveryVoucher.setTotalGoods(BigDecimal.ZERO);
        soldDeliveryVoucher.setTotalReceivable(BigDecimal.ZERO);
        soldDeliveryVoucher.setTotalPaymentReceived(BigDecimal.ZERO);
        soldDeliveryVoucher.setTotalGoodsReturn(BigDecimal.ZERO);
        soldDeliveryVoucherRepository.save(soldDeliveryVoucher);
        accountVoucherCodeRepository.deleteByVoucherId(id);

        PaymentVoucher oldPaymentVoucher = paymentVoucherRepository.findByVoucherId(id);
        if(oldPaymentVoucher !=null) {
            createReturnVoucher(oldPaymentVoucher);
        }
        // khi xoá phiếu xuất xe thì xóa phiễu xuất bán hàng theo xe, nhập kho thu hồi, phiếu thanh toán tương ứng,
        // xóa kho nhà máy và công nợ vỏ liên quan
        List<PaymentVoucher> paymentVouchers = paymentVoucherRepository.findAllByVoucherId(id);
        
        List<String> paymentVoucherIds = new ArrayList<String>();
        for(PaymentVoucher item : paymentVouchers) {
        	paymentVoucherIds.add(item.getId());
            PaymentVoucher paymentVoucher = new PaymentVoucher();
            paymentVoucher.setId(item.getId());
            paymentVoucher.setFactoryId(item.getFactoryId());
            paymentVoucher.setVoucherCode(item.getVoucherCode());
            paymentVoucher.setVoucherAt(item.getVoucherAt());
            paymentVoucher.setTotalGoodsReturn(BigDecimal.ZERO);
            paymentVoucher.setTotalPaymentReceived(BigDecimal.ZERO);
            paymentVoucherRepository.save(paymentVoucher);
            accountVoucherCodeRepository.deleteByVoucherId(item.getId());
        }

        List<String> ids = new ArrayList<>(paymentVoucherIds);
        ids.add(id);
        goodsInOutRepository.deleteByVoucherIdIn(ids);

        // cập nhật lại số lượng đã bán mỗi sản phẩm ở phiếu xuất xe
        List<GoodsInOut> goodsInOuts = goodsInOutRepository.findByVoucherId(deliveryVoucherId);
        goodsInOuts.stream().forEach(goodsInOut -> {
            goodsInOutRepository.updateXBXOutQuantityXXEOneProduct(deliveryVoucherId, goodsInOut.getProductId(), goodsInOut.getStt());
        });
        cylinderDebtRepository.deleteByVoucherIdIn(ids);
    }
    
    private void createGoodsInOut(CreateGoodsInOutRequest request, String voucherNo, String voucherCode, String voucherId, int stt,Instant createAt,String createBy,Instant updateAt,String updateBy) {
        GoodsInOut goodsInOut = CommonMapper.map(request, GoodsInOut.class);
        goodsInOut.setVoucherNo(voucherNo);
        goodsInOut.setVoucherCode(voucherCode);
        goodsInOut.setVoucherId(voucherId);
        goodsInOut.setStt(stt);
        goodsInOut.setUpdateAt(updateAt);
        goodsInOut.setUpdateByFullName(updateBy);
        goodsInOut.setCreateAt(createAt);
        goodsInOut.setCreateByFullName(createBy);
        goodsInOutRepository.save(goodsInOut);
    }
}
