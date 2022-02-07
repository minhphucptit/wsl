package com.ceti.wholesale.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.common.constant.ConstantText;
import com.ceti.wholesale.common.enums.GoodsInOutTypeEnum;
import com.ceti.wholesale.common.enums.ProductTypeEnum;
import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.CreateGoodsInOutRequest;
import com.ceti.wholesale.controller.api.request.CreateSoldVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateSoldVoucherRequest;
import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.dto.ProductAccessoryDto;
import com.ceti.wholesale.dto.SoldVoucherDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.mapper.SoldVoucherMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.CylinderDebt;
import com.ceti.wholesale.model.FactoryImportVoucher;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.PaymentVoucher;
import com.ceti.wholesale.model.SoldVoucher;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.PaymentVoucherRepository;
import com.ceti.wholesale.repository.SoldVoucherRepository;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.ProductAccessoryService;
import com.ceti.wholesale.service.SoldVoucherService;

@Service
@Transactional
public class SoldVoucherServiceImpl implements SoldVoucherService {

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;
    @Autowired
    private SoldVoucherRepository soldVoucherRepository;
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
    private PaymentVoucherRepository paymentVoucherRepository;
    @Autowired
    private SoldVoucherMapper soldVoucherMapper;
    @Autowired
    private  VoucherUtils voucherUtils;
    @Autowired
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

//    @Override
//    public Page<SoldVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
//        ResultPage<Object[]> page = soldVoucherDetailRepository.findAllWithFilter(pageable, where);
//        List<SoldVoucherDto> soldVoucherDtos = new ArrayList<>();
//        for (Object[] object : page.getPageList()) {
//            SoldVoucher sv = (SoldVoucher) object[0];
//            SoldVoucherDto soldVoucherDto = CommonMapper.map(sv, SoldVoucherDto.class);
//
//            if (object[1] != null) {
//                Company company = (Company) object[1];
//                CompanyDto companyDto = CommonMapper.map(company, CompanyDto.class);
//                soldVoucherDto.setCompany(companyDto);
//            }
//
//            if (object[2] != null) {
//                Customer customer = (Customer) object[2];
//                CustomerDto customerDto = CommonMapper.map(customer, CustomerDto.class);
//                soldVoucherDto.setCustomer(customerDto);
//            }
//
//            if (object[3] != null) {
//                TruckDriver truckDriver = (TruckDriver) object[3];
//                TruckDriverDto truckDriverDto = CommonMapper.map(truckDriver, TruckDriverDto.class);
//                soldVoucherDto.setTruckDriver(truckDriverDto);
//            }
//
//            if (object[4] != null) {
//                Truck truck = (Truck) object[4];
//                TruckDto truckDto = CommonMapper.map(truck, TruckDto.class);
//                soldVoucherDto.setTruck(truckDto);
//            }
//
//            if (object[5] != null) {
//                Salesman salesman = (Salesman) object[5];
//                SalesmanDto salesmanDto = CommonMapper.map(salesman, SalesmanDto.class);
//                soldVoucherDto.setSalesman(salesmanDto);
//            }
//
//            soldVoucherDtos.add(soldVoucherDto);
//        }
//        Instant voucherAtFrom = where.getFirst("voucher_at_from") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_from")));
//        Instant voucherAtTo = where.getFirst("voucher_at_to") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_to")));
//        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "sv");
//        List<SoldVoucherDto> soldVoucherDtos = soldVoucherMapper.getList(where.getFirst("product_id"),where.getFirst("no"),where.getFirst("company_id"),
//                where.getFirst("customer_id"),where.getFirst("truck_driver_id"),where.getFirst("truck_license_plate_number"),
//                where.getFirst("factory_id"),where.getFirst("goods_in_out.type"),voucherAtFrom,voucherAtTo,pagingStr);
//        long totalItems = soldVoucherMapper.countList(where.getFirst("product_id"),where.getFirst("no"),where.getFirst("company_id"),
//                where.getFirst("customer_id"),where.getFirst("truck_driver_id"),where.getFirst("truck_license_plate_number"),
//                where.getFirst("factory_id"),where.getFirst("goods_in_out.type"),voucherAtFrom,voucherAtTo);
//        return new PageImpl<>(soldVoucherDtos, pageable, totalItems);
//    }

    @Override
    public SoldVoucherDto createSoldVoucher(CreateSoldVoucherRequest request, String factory_id) {
        if (request.getGoodsInOut().isEmpty()) {
            throw new BadRequestException("Phải có hàng mới tạo được phiếu");
        }
        Instant now = Instant.now();
        String id = voucherUtils
                .genereateVoucherNO(factory_id, VoucherEnum.VOUCHER_CODE_XUAT_BAN.getCode(), i -> soldVoucherRepository.countSoldVoucherInDay(i));

        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factory_id, VoucherEnum.VOUCHER_CODE_XUAT_BAN,
        		request.getVoucherAt());
        
        SoldVoucher soldVoucher = CommonMapper.map(request, SoldVoucher.class);
        soldVoucher.setCreatedAt(now);
        soldVoucher.setUpdateAt(now);
        soldVoucher.setUpdateByFullName(request.getCreatedByFullName());
        soldVoucher.setNo(id);
        soldVoucher.setId(id);
        soldVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_XUAT_BAN);
        soldVoucher.setFactoryId(factory_id);

        soldVoucherRepository.save(soldVoucher);
        voucherUtils.createAccountVoucherCode(accountVoucherCode, id, id ,true , now, soldVoucher.getCreatedByFullName());

        List<GoodsInOutDto> goodsInOutDtos = new ArrayList<>();
        if (!request.getGoodsInOut().isEmpty()) {
            List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();

            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                goodsInOutRequest.setCompanyId(soldVoucher.getCompanyId());
                goodsInOutRequest.setCustomerId(soldVoucher.getCustomerId());
                goodsInOutRequest.setVoucherAt(soldVoucher.getVoucherAt());
                goodsInOutRequest.setTruckDriverId(soldVoucher.getTruckDriverId());
                goodsInOutRequest.setTruckLicensePlateNumber(soldVoucher.getTruckLicensePlateNumber());
                goodsInOutRequest.setSalesmanId(soldVoucher.getSalesmanId());
                goodsInOutRequest.setSalesmanId2(soldVoucher.getSalesmanId2());

                GoodsInOut goodsInOut = new GoodsInOut();
                CommonMapper.copyPropertiesIgnoreNull(goodsInOutRequest, goodsInOut);

                if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.GAS.name())) {
                    List<ProductAccessoryDto> list = productAccessoryService.getByMainProductIdAndFactoryId(goodsInOutRequest.getProductId(), factory_id);
                    for (ProductAccessoryDto pad : list) {
                        if (pad.getSubProductType().equals(ProductTypeEnum.VO.name())&&!goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.XBTC.name())) {
                            cylinderDebtService.createCylinderDebt(new CylinderDebt(soldVoucher.getId(), pad.getSubProductId(), soldVoucher.getVoucherCode(),
                                    soldVoucher.getVoucherAt(), soldVoucher.getNo(), BigDecimal.valueOf(0), goodsInOutRequest.getOutQuantity().multiply(pad.getSubProductQuantity()),
                                    soldVoucher.getCustomerId(), soldVoucher.getCompanyId(), soldVoucher.getNote(), soldVoucher.getCreatedAt(),
                                    soldVoucher.getCreatedByFullName(), soldVoucher.getUpdateAt(), soldVoucher.getUpdateByFullName(), factory_id).setGoodsInOutType(goodsInOutRequest.getType()));
                        }
                    }
                }

                goodsInOutRequest.setFactoryId(factory_id);
                createGoodsInOut(goodsInOutRequest, soldVoucher.getNo(), soldVoucher.getVoucherCode(), soldVoucher.getId(),goodsInOutRequests.indexOf(goodsInOutRequest),now,request.getCreatedByFullName(),now,request.getCreatedByFullName());

                GoodsInOutDto goodsInOutDto = CommonMapper.map(goodsInOut, GoodsInOutDto.class);
                goodsInOutDtos.add(goodsInOutDto);

            }
        }
        
        SoldVoucherDto soldVoucherDto = CommonMapper.map(soldVoucher, SoldVoucherDto.class);
        soldVoucherDto.setAccNo(accountVoucherCode.getAccNo()); 
        return soldVoucherDto;
    }

    @Override
    public SoldVoucherDto updateSoldVoucher(String id, UpdateSoldVoucherRequest request) {
        Optional<SoldVoucher> optional = soldVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu xuất bán không tồn tại");
        }
        SoldVoucher soldVoucher = optional.get();
        Instant now = Instant.now();
        if(!request.getVoucherAt().equals(soldVoucher.getVoucherAt())) {
            AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(soldVoucher.getFactoryId(),
            		VoucherEnum.VOUCHER_CODE_XUAT_BAN, request.getVoucherAt());
            
//            soldVoucher.setNo(no);
            
            AccountVoucherCode oldAccountVoucherCode = accountVoucherCodeRepository.findById(id).orElse(null);
            
            if(oldAccountVoucherCode != null) {
                String updateBy = oldAccountVoucherCode.getUpdateBy();
                Instant updateAt = oldAccountVoucherCode.getUpdateAt();

                //tạo account voucher code mới
                voucherUtils.createAccountVoucherCode(oldAccountVoucherCode,UUID.randomUUID().toString().replaceAll("-", ""), oldAccountVoucherCode.getOldVoucherId(),
                         false ,  updateAt, updateBy);
                //update lại account voucher code cũ
                voucherUtils.createAccountVoucherCode(accountVoucherCode, oldAccountVoucherCode.getVoucherId(), oldAccountVoucherCode.getOldVoucherId() ,true , now, request.getUpdateByFullName());

            }
            
        }
        
        CommonMapper.copyPropertiesIgnoreNull(request, soldVoucher);
        soldVoucher.setUpdateAt(now);
        soldVoucher.setTruckDriverId(request.getTruckDriverId());
        soldVoucher.setSalesmanId(request.getSalesmanId());
        soldVoucherRepository.save(soldVoucher);

        goodsInOutRepository.deleteByVoucherId(id);
        cylinderDebtRepository.deleteByVoucherId(id);
        List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();
        if (goodsInOutRequests.size() != 0) {
            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                goodsInOutRequest.setCompanyId(soldVoucher.getCompanyId());
                goodsInOutRequest.setCustomerId(soldVoucher.getCustomerId());
                goodsInOutRequest.setVoucherAt(soldVoucher.getVoucherAt());
                goodsInOutRequest.setFactoryId(soldVoucher.getFactoryId());
                goodsInOutRequest.setTruckDriverId(soldVoucher.getTruckDriverId());
                goodsInOutRequest.setTruckLicensePlateNumber(soldVoucher.getTruckLicensePlateNumber());
                goodsInOutRequest.setSalesmanId(soldVoucher.getSalesmanId());
                goodsInOutRequest.setSalesmanId2(soldVoucher.getSalesmanId2());

                GoodsInOut goodsInOut = new GoodsInOut();
                CommonMapper.copyPropertiesIgnoreNull(goodsInOutRequest, goodsInOut);

                if (goodsInOutRequest.getProductType().equals("GAS") || goodsInOutRequest.getProductType().equals("GASBON")) {
                    List<ProductAccessoryDto> list = productAccessoryService.getByMainProductIdAndFactoryId(goodsInOutRequest.getProductId(), soldVoucher.getFactoryId());
                    for (ProductAccessoryDto pad : list) {
                        if (pad.getSubProductType().equals(ProductTypeEnum.VO.name())&&!goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.XBTC.name())) {
                            cylinderDebtService.createCylinderDebt(new CylinderDebt(soldVoucher.getId(), pad.getSubProductId(), soldVoucher.getVoucherCode(),
                                    soldVoucher.getVoucherAt(), soldVoucher.getNo(), BigDecimal.valueOf(0), goodsInOutRequest.getOutQuantity().multiply(pad.getSubProductQuantity()),
                                    soldVoucher.getCustomerId(), soldVoucher.getCompanyId(), soldVoucher.getNote(), soldVoucher.getCreatedAt(),
                                    soldVoucher.getCreatedByFullName(), soldVoucher.getUpdateAt(), soldVoucher.getUpdateByFullName(), soldVoucher.getFactoryId()).setGoodsInOutType(goodsInOutRequest.getType()));
                        }

                    }
                }

                createGoodsInOut(goodsInOutRequest, soldVoucher.getNo(), soldVoucher.getVoucherCode(), soldVoucher.getId(),
                        		goodsInOutRequests.indexOf(goodsInOutRequest),soldVoucher.getCreatedAt(),soldVoucher.getCreatedByFullName(),now,request.getUpdateByFullName());

            }
        }

        return CommonMapper.map(soldVoucher, SoldVoucherDto.class);

    }

    @Override
    public void deleteSoldVoucher(String id) {
    	Optional<SoldVoucher> optional = soldVoucherRepository.findById(id);
    	
        if (!optional.isPresent()) {
            throw new NotFoundException("Phiếu xuất bán hàng không tồn tại");
        }
        SoldVoucher soldVoucher = new SoldVoucher();
        soldVoucher.setId(id);
        soldVoucher.setFactoryId(optional.get().getFactoryId());
        soldVoucher.setVoucherCode(optional.get().getVoucherCode());
        soldVoucher.setVoucherAt(optional.get().getVoucherAt());
        soldVoucher.setTotalGoods(BigDecimal.ZERO);
        soldVoucher.setTotalPaymentReceived(BigDecimal.ZERO);
        soldVoucher.setTotalGoodsReturn(BigDecimal.ZERO);
        soldVoucher.setTotalReceivable(BigDecimal.ZERO);
        soldVoucherRepository.save(soldVoucher);
        accountVoucherCodeRepository.deleteByVoucherId(id);
        
//        if (!soldVoucherRepository.existsById(id)) {
//            throw new NotFoundException("Phiếu xuất bán hàng không tồn tại");
//        }
//        soldVoucherRepository.deleteById(id);
//        List<String> paymentVoucherIds = paymentVoucherRepository.findIdByVoucherId(id);
//       paymentVoucherRepository.deleteByIdIn(paymentVoucherIds);
        
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
        goodsInOut.setCreateByFullName(createBy);
        goodsInOut.setCreateAt(createAt);
        goodsInOutRepository.save(goodsInOut);
    }

}
