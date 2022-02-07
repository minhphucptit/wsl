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
import com.ceti.wholesale.controller.api.request.CreatePaymentVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdatePaymentVoucherRequest;
import com.ceti.wholesale.dto.CustomerDto;
import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.dto.PaymentVoucherDto;
import com.ceti.wholesale.dto.ReturnVoucherTotalGoodsDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.mapper.PaymentVoucherMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.CylinderDebt;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.PaymentVoucher;
import com.ceti.wholesale.model.ReturnVoucher;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.CustomerRepository;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.PaymentVoucherRepository;
import com.ceti.wholesale.repository.ReturnVoucherRepository;
import com.ceti.wholesale.repository.SoldDeliveryVoucherRepository;
import com.ceti.wholesale.repository.SoldVoucherRepository;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.PaymentVoucherService;

@Transactional
@Service
public class PaymentVoucherServiceImpl implements PaymentVoucherService {

    @Autowired
    private PaymentVoucherRepository paymentVoucherRepository;

    @Autowired
    private SoldVoucherRepository soldVoucherRepository;

    @Autowired
    private SoldDeliveryVoucherRepository soldDeliveryVoucherRepository;

    @Autowired
    private GoodsInOutService goodsInOutService;

    @Autowired
    private GoodsInOutRepository goodsInOutRepository;

    @Autowired
    private CylinderDebtService cylinderDebtService;

    @Autowired
    private CylinderDebtRepository cylinderDebtRepository;

    @Autowired
    private PaymentVoucherMapper paymentVoucherMapper;

    @Autowired
    private  VoucherUtils voucherUtils;
    
    @Autowired
    private ReturnVoucherRepository returnVoucherRepository;
    
    @Autowired
    private AccountVoucherCodeRepository accountVoucherCodeRepository;
    
    @Autowired
    private CustomerRepository customerRepository;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

//    @Override
//    public Page<PaymentVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
//        ResultPage<Object[]> page = paymentVoucherDetailRepository.findAllWithFilter(pageable, where);
//        List<PaymentVoucherDto> paymentVoucherDtos = new ArrayList<>();
//        for (Object[] object : page.getPageList()) {
//            PaymentVoucher pv = (PaymentVoucher) object[0];
//            PaymentVoucherDto paymentVoucherDto = CommonMapper.map(pv, PaymentVoucherDto.class);
//
//            if (object[1] != null) {
//                Customer customer = (Customer) object[1];
//                CustomerDto customerDto = CommonMapper.map(customer, CustomerDto.class);
//                paymentVoucherDto.setCustomer(customerDto);
//            }
//
//            if (object[2] != null) {
//                TruckDriver truckDriver = (TruckDriver) object[2];
//                TruckDriverDto truckDriverDto = CommonMapper.map(truckDriver, TruckDriverDto.class);
//                paymentVoucherDto.setTruckDriver(truckDriverDto);
//            }
//
//            if (object[3] != null) {
//                Company company = (Company) object[3];
//                CompanyDto companyDto = CommonMapper.map(company, CompanyDto.class);
//                paymentVoucherDto.setCompany(companyDto);
//            }
//
//            paymentVoucherDtos.add(paymentVoucherDto);
//        }
//        Instant voucherAtFrom = where.getFirst("voucher_at_from") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_from")));
//        Instant voucherAtTo = where.getFirst("voucher_at_to") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_to")));
//        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "pv");
//        List<PaymentVoucherDto>paymentVoucherDtos =paymentVoucherMapper.getList(where.getFirst("product_id"),where.getFirst("sold_delivery_voucher_no"),where.getFirst("sold_voucher_no"),where.getFirst("voucher_code"),
//                where.getFirst("customer_id"),where.getFirst("factory_id"),voucherAtFrom,voucherAtTo,pagingStr);
//        long totalItems = paymentVoucherMapper.countList(where.getFirst("product_id"),where.getFirst("sold_delivery_voucher_no"),where.getFirst("sold_voucher_no"),where.getFirst("voucher_code"),
//                where.getFirst("customer_id"),where.getFirst("factory_id"),voucherAtFrom,voucherAtTo);
//        return new PageImpl<>(paymentVoucherDtos, pageable,totalItems);
//    }

    @Override
    public PaymentVoucherDto createPaymentVoucher(CreatePaymentVoucherRequest request, String factoryId) {
        if (!request.getVoucherCode().equals(VoucherEnum.VOUCHER_CODE_PHIEU_THANH_TOAN.getCode()) && paymentVoucherRepository.existsByVoucherId(request.getVoucherId())) {
            String voucherType = request.getVoucherCode().equals(VoucherEnum.VOUCHER_CODE_PHIEU_THANH_TOAN_THEO_XUAT_BAN.getCode()) ? "Phiếu xuất bán hàng" : "Phiếu xuất bán hàng theo xe";
            throw new BadRequestException(voucherType + " này đã tạo phiếu thanh toán");
        }
        //Theo yêu cầu khách hàng ngày 01/12/2021
//        if (request.getGoodsInOut().isEmpty()) {
//            throw new BadRequestException("Phải có hàng mới tạo được phiếu");
//        }
        Instant now = Instant.now();
        String id = voucherUtils.genereateVoucherNO(factoryId, request.getVoucherCode(),
                i -> paymentVoucherRepository.countPaymentVoucherInDay(i));

        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryId, VoucherEnum.getEnumFromCode(request.getVoucherCode()),
        		request.getVoucherAt());
        
        PaymentVoucher paymentVoucher = CommonMapper.map(request, PaymentVoucher.class);
        paymentVoucher.setCreatedAt(now);
        paymentVoucher.setId(id);
        paymentVoucher.setUpdateAt(now);
        paymentVoucher.setUpdateByFullName(request.getCreatedByFullName());
        paymentVoucher.setNo(id);
        paymentVoucher.setFactoryId(factoryId);

        paymentVoucherRepository.save(paymentVoucher);

        voucherUtils.createAccountVoucherCode(accountVoucherCode, id, id ,true , now, paymentVoucher.getCreatedByFullName());

        
        List<GoodsInOutDto> goodsInOutDtos = new ArrayList<>();
        if (!request.getGoodsInOut().isEmpty()) {
            List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();

            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                goodsInOutRequest.setCustomerId(paymentVoucher.getCustomerId());
                goodsInOutRequest.setCompanyId(paymentVoucher.getCompanyId());
                goodsInOutRequest.setVoucherAt(paymentVoucher.getVoucherAt());
                goodsInOutRequest.setTruckDriverId(paymentVoucher.getTruckDriverId());
                goodsInOutRequest.setTruckLicensePlateNumber(paymentVoucher.getTruckLicensePlateNumber());
//                goodsInOutRequest.setType(GoodsInOutTypeEnum.TTKH.name());
                goodsInOutRequest.setFactoryId(factoryId);

                GoodsInOutDto goodsInOutDto = createGoodsInOut(goodsInOutRequest, paymentVoucher.getNo(), paymentVoucher.getVoucherCode(),
                		paymentVoucher.getId(), goodsInOutRequests.indexOf(goodsInOutRequest),now,request.getCreatedByFullName(),now,request.getCreatedByFullName());

                goodsInOutDtos.add(goodsInOutDto);

                // create cylinder dept
                if(!goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.TCV.name())&&goodsInOutRequest.getProductType().equals(ProductTypeEnum.VO.name())) {
                    cylinderDebtService.createCylinderDebt(new CylinderDebt(paymentVoucher.getId(), goodsInOutDto.getProductId(),
                            paymentVoucher.getVoucherCode(), paymentVoucher.getVoucherAt(), paymentVoucher.getNo(),
                            goodsInOutDto.getInQuantity(), BigDecimal.valueOf(0), paymentVoucher.getCustomerId(), "",
                            paymentVoucher.getNote(), paymentVoucher.getCreatedAt(),
                            paymentVoucher.getCreatedByFullName(), paymentVoucher.getUpdateAt(),
                            paymentVoucher.getUpdateByFullName(), factoryId).setGoodsInOutType(goodsInOutRequest.getType()));
                }
            }
        }
        switch (paymentVoucher.getVoucherCode()) {
            case ConstantText.VOUCHER_CODE_PHIEU_THANH_TOAN_THEO_XUAT_BAN:
                soldVoucherRepository.updateTotalPaymentReceivedAndTotalGoodsReturnAndPaymentVoucherId(paymentVoucher.getTotalPaymentReceived(), paymentVoucher.getTotalGoodsReturn(), paymentVoucher.getVoucherId(), paymentVoucher.getId());
                break;
            case ConstantText.VOUCHER_CODE_PHIEU_THANH_TOAN_THEO_XUAT_XE_BAN:
                soldDeliveryVoucherRepository.updateTotalPaymentReceivedAndTotalGoodsReturnAndPaymentVoucherId(paymentVoucher.getTotalPaymentReceived(), paymentVoucher.getTotalGoodsReturn(), paymentVoucher.getVoucherId(), paymentVoucher.getId());
                
                //Tạo phiếu nhập kho theo xe
                createReturnVoucher(paymentVoucher);
                break;
            default:
                break;
        }
        
        PaymentVoucherDto paymentVoucherDto = CommonMapper.map(paymentVoucher, PaymentVoucherDto.class);
        paymentVoucherDto.setAccNo(accountVoucherCode.getAccNo());
        
        return paymentVoucherDto;
    }

    @Override
    public PaymentVoucherDto updatePaymentVoucher(String id, UpdatePaymentVoucherRequest request) {
        Optional<PaymentVoucher> optional = paymentVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu thanh toán không tồn tại");
        }
        PaymentVoucher paymentVoucher = optional.get();
        Instant now = Instant.now();
        if(!request.getVoucherAt().equals(paymentVoucher.getVoucherAt())) {
            AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(paymentVoucher.getFactoryId(), VoucherEnum.getEnumFromCode(paymentVoucher.getVoucherCode()),
            		request.getVoucherAt());
            
//            paymentVoucher.setNo(no);
            
            AccountVoucherCode oldAccountVoucherCode = accountVoucherCodeRepository.findById(id).orElse(null);
            
            if(oldAccountVoucherCode != null) {
                String updateBy = oldAccountVoucherCode.getUpdateBy();
                Instant updateAt = oldAccountVoucherCode.getUpdateAt();

                //tạo account voucher code mới
                voucherUtils.createAccountVoucherCode(oldAccountVoucherCode,UUID.randomUUID().toString().replaceAll("-", ""), oldAccountVoucherCode.getOldVoucherId(),
                        false , updateAt, updateBy);
                //update lại account voucher code cũ
                voucherUtils.createAccountVoucherCode(accountVoucherCode, oldAccountVoucherCode.getVoucherId(), oldAccountVoucherCode.getOldVoucherId() ,true , now, request.getUpdateByFullName());

            } 
            
        }
        
        CommonMapper.copyPropertiesIgnoreNull(request, paymentVoucher);
        paymentVoucher.setUpdateAt(now);
        paymentVoucher.setTruckDriverId(request.getTruckDriverId());
        paymentVoucherRepository.save(paymentVoucher);

        goodsInOutRepository.deleteByVoucherId(id);
        cylinderDebtRepository.deleteByVoucherId(id);
        if (!request.getGoodsInOut().isEmpty()) {
            List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();

            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                goodsInOutRequest.setCompanyId(paymentVoucher.getCompanyId());
                goodsInOutRequest.setCustomerId(paymentVoucher.getCustomerId());
                goodsInOutRequest.setVoucherAt(paymentVoucher.getVoucherAt());
                goodsInOutRequest.setTruckDriverId(paymentVoucher.getTruckDriverId());
                goodsInOutRequest.setTruckLicensePlateNumber(paymentVoucher.getTruckLicensePlateNumber());
//                goodsInOutRequest.setType(GoodsInOutTypeEnum.TTKH.name());
                goodsInOutRequest.setFactoryId(paymentVoucher.getFactoryId());
                GoodsInOutDto goodsInOutDto = createGoodsInOut(goodsInOutRequest, paymentVoucher.getNo(),
                		paymentVoucher.getVoucherCode(), paymentVoucher.getId(), goodsInOutRequests.indexOf(goodsInOutRequest),paymentVoucher.getCreatedAt(),paymentVoucher.getCreatedByFullName(),now,request.getUpdateByFullName());
                if(!goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.TCV.name())&&goodsInOutRequest.getProductType().equals(ProductTypeEnum.VO.name())) {
                    cylinderDebtService.createCylinderDebt(new CylinderDebt(paymentVoucher.getId(), goodsInOutDto.getProductId(),
                            paymentVoucher.getVoucherCode(), paymentVoucher.getVoucherAt(), paymentVoucher.getNo(),
                            goodsInOutDto.getInQuantity(), BigDecimal.valueOf(0), paymentVoucher.getCustomerId(), "",
                            paymentVoucher.getNote(), paymentVoucher.getCreatedAt(),
                            paymentVoucher.getCreatedByFullName(), paymentVoucher.getUpdateAt(),
                            paymentVoucher.getUpdateByFullName(), paymentVoucher.getFactoryId()).setGoodsInOutType(goodsInOutRequest.getType()));
                }
            }
        }
        if (request.getTotalGoodsReturn() != null || request.getTotalPaymentReceived() != null) {
            switch (paymentVoucher.getVoucherCode()) {
                case ConstantText.VOUCHER_CODE_PHIEU_THANH_TOAN_THEO_XUAT_BAN:
                	soldVoucherRepository.updateTotalPaymentReceivedAndTotalGoodsReturnAndPaymentVoucherId(paymentVoucher.getTotalPaymentReceived(), paymentVoucher.getTotalGoodsReturn(), paymentVoucher.getVoucherId(), id);
                	break;
                case ConstantText.VOUCHER_CODE_PHIEU_THANH_TOAN_THEO_XUAT_XE_BAN:
                	soldDeliveryVoucherRepository.updateTotalPaymentReceivedAndTotalGoodsReturnAndPaymentVoucherId(paymentVoucher.getTotalPaymentReceived(), paymentVoucher.getTotalGoodsReturn(), paymentVoucher.getVoucherId(), id);
                	
                    //Tạo phiếu nhập kho theo xe
                    createReturnVoucher(paymentVoucher);
                	break;
                default:
                    break;
            }
        }
        return CommonMapper.map(paymentVoucher, PaymentVoucherDto.class);
    }

    @Override
    public void deletePaymentVoucher(String id) {
    	Optional<PaymentVoucher> optional = paymentVoucherRepository.findById(id);
    	
        if (!optional.isPresent()) {
            throw new NotFoundException("Phiếu thanh toán không tồn tại");
        }
        PaymentVoucher oldPaymentVoucher = optional.get();
        String voucherId = oldPaymentVoucher.getVoucherId();
        // xóa phiếu thanh toán thì cập nhật lại các trường liên quan đến việc thanh toán của phiêu xuất bán/xuất xe bán
        switch (optional.get().getVoucherCode()) {
            case ConstantText.VOUCHER_CODE_PHIEU_THANH_TOAN_THEO_XUAT_BAN:
                soldVoucherRepository.updateTotalPaymentReceivedAndTotalGoodsReturnAndPaymentVoucherId(BigDecimal.ZERO, BigDecimal.ZERO, voucherId, null);
                break;
            case ConstantText.VOUCHER_CODE_PHIEU_THANH_TOAN_THEO_XUAT_XE_BAN:
                soldDeliveryVoucherRepository.updateTotalPaymentReceivedAndTotalGoodsReturnAndPaymentVoucherId(BigDecimal.ZERO, BigDecimal.ZERO, voucherId, null);
                createReturnVoucher(oldPaymentVoucher);
                break;
            default:
                break;
        }
        PaymentVoucher paymentVoucher = new PaymentVoucher();
        paymentVoucher.setId(id);
        paymentVoucher.setFactoryId(oldPaymentVoucher.getFactoryId());
        paymentVoucher.setVoucherCode(oldPaymentVoucher.getVoucherCode());
        paymentVoucher.setVoucherAt(oldPaymentVoucher.getVoucherAt());
        paymentVoucher.setTotalGoodsReturn(BigDecimal.ZERO);
        paymentVoucher.setTotalPaymentReceived(BigDecimal.ZERO);
        accountVoucherCodeRepository.deleteByVoucherId(id);
        paymentVoucherRepository.save(paymentVoucher);
        goodsInOutRepository.deleteByVoucherId(id);
        cylinderDebtRepository.deleteByVoucherId(id);

    }

    @Override
    public PaymentVoucherDto getDetailPaymentVoucher(String id) {
        Optional<PaymentVoucher> optional = paymentVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu thanh toán không tồn tại");
        }
        PaymentVoucher paymentVoucher = optional.get();
        PaymentVoucherDto paymentVoucherDto = CommonMapper.map(paymentVoucher, PaymentVoucherDto.class);
        List<GoodsInOut> goodsInOuts = goodsInOutRepository.getByVoucherIdAndIsMainProduct(id, true);
        List<GoodsInOutDto> goodsInOutDtos = CommonMapper.toList(goodsInOuts, GoodsInOutDto.class);
        paymentVoucherDto.setGoodsInOut(goodsInOutDtos);
        
        AccountVoucherCode accountVoucherCode = accountVoucherCodeRepository.findById(id).orElse(null);
        
        if(accountVoucherCode != null) {
        	paymentVoucherDto.setAccNo(accountVoucherCode.getAccNo());
        }
        
        AccountVoucherCode solAccountVoucherCode = accountVoucherCodeRepository.
                findById(paymentVoucherDto.getVoucherId()).orElse(null);


        if(accountVoucherCode != null) {
            switch (paymentVoucher.getVoucherCode()){
                case ConstantText.VOUCHER_CODE_PHIEU_THANH_TOAN_THEO_XUAT_BAN:
                    paymentVoucherDto.setSoldVoucherAccNo(solAccountVoucherCode.getAccNo());
                    break;
                case ConstantText.VOUCHER_CODE_PHIEU_THANH_TOAN_THEO_XUAT_XE_BAN:
                    paymentVoucherDto.setSoldDeliveryVoucherAccNo(solAccountVoucherCode.getAccNo());
                    break;
                default:
                    break;
            }
        }
        
        Customer customer = customerRepository.findById(paymentVoucherDto.getCustomerId()).orElse(null);
        
        if(customer != null) {
        	paymentVoucherDto.setCustomer(CommonMapper.map(customer, CustomerDto.class));
        }

        return paymentVoucherDto;
    }
    
    private GoodsInOutDto createGoodsInOut(CreateGoodsInOutRequest request, String voucherNo, String voucherCode, String voucherId, int stt,Instant createAt,String createBy,Instant updateAt,String updateBy) {
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
        return CommonMapper.map(goodsInOut, GoodsInOutDto.class);
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
}
