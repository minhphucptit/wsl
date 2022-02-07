package com.ceti.wholesale.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.CreateDeliveryVoucherRequest;
import com.ceti.wholesale.controller.api.request.CreateGoodsInOutRequest;
import com.ceti.wholesale.controller.api.request.UpdateDeliveryVoucherRequest;
import com.ceti.wholesale.dto.DeliveryVoucherDto;
import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.mapper.DeliveryVoucherMapper;
import com.ceti.wholesale.service.DeliveryVoucherService;
import com.ceti.wholesale.service.GoodsInOutService;

@Service
@Transactional
public class DeliveryVoucherServiceImpl implements DeliveryVoucherService {

    @Autowired
    private DeliveryVoucherRepository deliveryVoucherRepository;
    @Autowired
    private GoodsInOutService goodsInOutService;
    @Autowired
    private GoodsInOutRepository goodsInOutRepository;

    @Autowired
    private ReturnVoucherRepository returnVoucherRepository;

    @Autowired
    private SoldDeliveryVoucherRepository soldDeliveryVoucherRepository;

    @Autowired
    private RecallVoucherRepository recallVoucherRepository;

    @Autowired
    private PaymentVoucherRepository paymentVoucherRepository;

    @Autowired
    private CylinderDebtRepository cylinderDebtRepository;

    @Autowired
    private DeliveryVoucherMapper deliveryVoucherMapper;

    @Autowired
    private  VoucherUtils voucherUtils;
    
    @Autowired
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

//    @Override
//    public Page<DeliveryVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
//        ResultPage<Object[]> page = deliveryVoucherDetailRepository.findAllWithFilter(pageable, where);
//        List<DeliveryVoucherDto> deliveryVoucherDtos = new ArrayList<>();
//        for (Object[] object : page.getPageList()) {
//            DeliveryVoucher dv = (DeliveryVoucher) object[0];
//            DeliveryVoucherDto deliveryVoucherDto = CommonMapper.map(dv, DeliveryVoucherDto.class);
//
//            if (object[1] != null) {
//                TruckDriver truckDriver = (TruckDriver) object[1];
//                TruckDriverDto truckDriverDto = CommonMapper.map(truckDriver, TruckDriverDto.class);
//                deliveryVoucherDto.setTruckDriver(truckDriverDto);
//            }
//
//            if (object[2] != null) {
//                Truck truck = (Truck) object[2];
//                TruckDto truckDto = CommonMapper.map(truck, TruckDto.class);
//                deliveryVoucherDto.setTruck(truckDto);
//            }
//
//            if (object[3] != null) {
//                Company company = (Company) object[3];
//                CompanyDto companyDto = CommonMapper.map(company, CompanyDto.class);
//                deliveryVoucherDto.setCompany(companyDto);
//            }
//
//            deliveryVoucherDtos.add(deliveryVoucherDto);
//        }
//        Instant voucherAtFrom = where.getFirst("voucher_at_from") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_from")));
//        Instant voucherAtTo = where.getFirst("voucher_at_to") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_to")));
//        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "dv");
//        List<DeliveryVoucherDto> deliveryVoucherDtos = deliveryVoucherMapper.getList(where.getFirst("product_id"),where.getFirst("no"),
//                where.getFirst("truck_license_plate_number"),
//                where.getFirst("truck_driver_id"),
//                where.getFirst("factory_id"),voucherAtFrom,voucherAtTo,pagingStr);
//        long totalItems = deliveryVoucherMapper.countList(where.getFirst("product_id"),where.getFirst("no"),
//                where.getFirst("truck_license_plate_number"),
//                where.getFirst("truck_driver_id"),
//                where.getFirst("factory_id"),voucherAtFrom,voucherAtTo);
//        return new PageImpl<>(deliveryVoucherDtos, pageable, totalItems);
//    }

    @Override
    public DeliveryVoucherDto createDeliveryVoucher(CreateDeliveryVoucherRequest request, String factory_id) {
        if (request.getGoodsInOut().isEmpty()) {
            throw new BadRequestException("Phải có hàng mới tạo được phiếu");
        }
        Instant now = Instant.now();
        
        String id = voucherUtils.genereateVoucherNO(factory_id, VoucherEnum.VOUCHER_CODE_XUAT_XE.getCode(), i -> deliveryVoucherRepository.countDeliveryVoucherInDay(i));
        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factory_id, VoucherEnum.VOUCHER_CODE_XUAT_XE,
        		request.getVoucherAt());
        DeliveryVoucher deliveryVoucher = CommonMapper.map(request, DeliveryVoucher.class);
        deliveryVoucher.setCreatedAt(now);
        deliveryVoucher.setUpdateAt(now);
        deliveryVoucher.setId(id);
        deliveryVoucher.setUpdateByFullName(request.getCreatedByFullName());
        deliveryVoucher.setNo(id);
        deliveryVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_XUAT_XE);
        deliveryVoucher.setFactoryId(factory_id);
        deliveryVoucherRepository.save(deliveryVoucher);

        voucherUtils.createAccountVoucherCode(accountVoucherCode, id, id ,true , now, request.getCreatedByFullName());

        List<GoodsInOutDto> goodsInOutDto = new ArrayList<>();
        if (!request.getGoodsInOut().isEmpty()) {
            List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();

            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                goodsInOutRequest.setCompanyId(deliveryVoucher.getCompanyId());
                goodsInOutRequest.setVoucherAt(deliveryVoucher.getVoucherAt());
                goodsInOutRequest.setTruckDriverId(deliveryVoucher.getTruckDriverId());
                goodsInOutRequest.setOutQuantity(BigDecimal.ZERO);
                goodsInOutRequest.setTruckLicensePlateNumber(deliveryVoucher.getTruckLicensePlateNumber());
                GoodsInOut goodsInOut = new GoodsInOut();

                CommonMapper.map(goodsInOutRequest, GoodsInOut.class);
                goodsInOutRequest.setFactoryId(factory_id);
                createGoodsInOut(goodsInOutRequest, deliveryVoucher.getNo(), deliveryVoucher.getVoucherCode(),
                		deliveryVoucher.getId(), goodsInOutRequests.indexOf(goodsInOutRequest),now,request.getCreatedByFullName(),now,request.getCreatedByFullName());

                goodsInOutDto.add(CommonMapper.map(goodsInOut, GoodsInOutDto.class));
            }
        }
        
        DeliveryVoucherDto deliveryVoucherDto = CommonMapper.map(deliveryVoucher, DeliveryVoucherDto.class);
        deliveryVoucherDto.setAccNo(accountVoucherCode.getAccNo());
        return deliveryVoucherDto;
    }

    @Override
    public DeliveryVoucherDto updateDeliveryVoucher(String id, UpdateDeliveryVoucherRequest request) {
        Optional<DeliveryVoucher> optional = deliveryVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu xuất kho không tồn tại");
        }
            DeliveryVoucher deliveryVoucher = optional.get();
            Instant now = Instant.now();
            if(!request.getVoucherAt().equals(deliveryVoucher.getVoucherAt())) {
                AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(deliveryVoucher.getFactoryId(),
                		VoucherEnum.VOUCHER_CODE_XUAT_XE, request.getVoucherAt());
//                deliveryVoucher.setNo(no);
                
                AccountVoucherCode oldAccountVoucherCode = accountVoucherCodeRepository.findById(id).orElse(null);

                if(oldAccountVoucherCode != null) {
                    String updateBy = oldAccountVoucherCode.getUpdateBy();
                    Instant updateAt = oldAccountVoucherCode.getUpdateAt();
                    //tạo account voucher code mới
                    voucherUtils.createAccountVoucherCode(oldAccountVoucherCode, UUID.randomUUID().toString().replaceAll("-", ""), oldAccountVoucherCode.getOldVoucherId(),
                            false , updateAt, updateBy);
                    //update lại account voucher code cũ
                    voucherUtils.createAccountVoucherCode(accountVoucherCode, oldAccountVoucherCode.getVoucherId(), oldAccountVoucherCode.getOldVoucherId() ,
                            true , now, request.getUpdateByFullName());

                }         
            }
            
            CommonMapper.copyPropertiesIgnoreNull(request, deliveryVoucher);

            deliveryVoucher.setUpdateAt(now);
            deliveryVoucher.setTruckDriverId(request.getTruckDriverId());
            deliveryVoucher.setSalesmanId(request.getSalesmanId());
            deliveryVoucher.setSalesmanFullName(request.getSalesmanFullName());
            deliveryVoucherRepository.save(deliveryVoucher);

            goodsInOutRepository.deleteByVoucherId(id);
            List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();
            if (goodsInOutRequests.size() != 0) {
                for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                    goodsInOutRequest.setCompanyId(deliveryVoucher.getCompanyId());
                    goodsInOutRequest.setVoucherAt(deliveryVoucher.getVoucherAt());
                    goodsInOutRequest.setTruckDriverId(deliveryVoucher.getTruckDriverId());
                    goodsInOutRequest.setOutQuantity(BigDecimal.ZERO);
                    goodsInOutRequest.setTruckLicensePlateNumber(deliveryVoucher.getTruckLicensePlateNumber());
                    GoodsInOut goodsInOut = new GoodsInOut();
                    CommonMapper.copyPropertiesIgnoreNull(goodsInOutRequest, goodsInOut);
                    goodsInOutRequest.setFactoryId(deliveryVoucher.getFactoryId());
                    createGoodsInOut(goodsInOutRequest, deliveryVoucher.getNo(), deliveryVoucher.getVoucherCode(), deliveryVoucher.getId(),
                    		goodsInOutRequests.indexOf(goodsInOutRequest),deliveryVoucher.getCreatedAt(),deliveryVoucher.getCreatedByFullName(),now,request.getUpdateByFullName());
                }
            }
            return CommonMapper.map(deliveryVoucher, DeliveryVoucherDto.class);
    }

    @Override
    public void deleteDeliveryVoucher(String id) {
    	
    	Optional<DeliveryVoucher> optional = deliveryVoucherRepository.findById(id);
    	
        if (!optional.isPresent()) {
            throw new NotFoundException("Phiếu xuất kho theo xe không tồn tại");
        }
        
        DeliveryVoucher deliveryVoucher = new DeliveryVoucher();
        deliveryVoucher.setId(id);
        deliveryVoucher.setFactoryId(optional.get().getFactoryId());
        deliveryVoucher.setVoucherCode(optional.get().getVoucherCode());
        deliveryVoucher.setVoucherAt(optional.get().getVoucherAt());
        deliveryVoucher.setTotalGoods(BigDecimal.ZERO);
        deliveryVoucherRepository.save(deliveryVoucher);
        accountVoucherCodeRepository.deleteByVoucherId(id);
        
        // khi xoá phiếu xuất kho theo xe thì xóa phiếu xuất bán hàng theo xe tương ứng
        List<SoldDeliveryVoucher> soldDeliveryVouchers = soldDeliveryVoucherRepository.findAllByDeliveryVoucherId(id);
        
        List<String> soldDeliveryVoucherIds = new ArrayList<String>();
        for(SoldDeliveryVoucher item : soldDeliveryVouchers) {
        	soldDeliveryVoucherIds.add(item.getId());
            SoldDeliveryVoucher soldDeliveryVoucher = new SoldDeliveryVoucher();
            soldDeliveryVoucher.setId(item.getId());
            soldDeliveryVoucher.setFactoryId(item.getFactoryId());
            soldDeliveryVoucher.setVoucherCode(item.getVoucherCode());
            soldDeliveryVoucher.setVoucherAt(item.getVoucherAt());
            soldDeliveryVoucher.setTotalGoods(BigDecimal.ZERO);
            soldDeliveryVoucher.setTotalGoodsReturn(BigDecimal.ZERO);
            soldDeliveryVoucher.setTotalPaymentReceived(BigDecimal.ZERO);
            soldDeliveryVoucher.setTotalReceivable(BigDecimal.ZERO);
        	soldDeliveryVoucherRepository.save(soldDeliveryVoucher);
            accountVoucherCodeRepository.deleteByVoucherId(item.getId());
        }
        
        // khi xoá phiếu xuất kho theo xe thì xóa phiếu nhập kho thu hồi theo xe tương ứng
        List<RecallVoucher> recallVouchers = recallVoucherRepository.findAllByDeliveryVoucherId(id);
        
        List<String> recallVoucherIds = new ArrayList<String>();
        for(RecallVoucher item : recallVouchers) {
        	recallVoucherIds.add(item.getId());
            RecallVoucher recallVoucher = new RecallVoucher();
            recallVoucher.setId(item.getId());
            recallVoucher.setFactoryId(item.getFactoryId());
            recallVoucher.setVoucherCode(item.getVoucherCode());
            recallVoucher.setVoucherAt(item.getVoucherAt());
            recallVoucher.setTotalGoodsReturn(BigDecimal.ZERO);
        	recallVoucherRepository.save(recallVoucher);
            accountVoucherCodeRepository.deleteByVoucherId(item.getId());
        }
        
        // khi xoá phiếu xuất kho theo xe thì xóa phiếu thanh toán tương ứng
        List<PaymentVoucher> paymentVouchers = paymentVoucherRepository.findByVoucherIdIn(soldDeliveryVoucherIds);
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

        List<ReturnVoucher> returnVouchers = returnVoucherRepository.findAllByDeliveryVoucherId(id);
        List<String> returnVoucherIds = new ArrayList<String>();
        for(ReturnVoucher item : returnVouchers) {
            returnVoucherIds.add(item.getId());
            ReturnVoucher returnVoucher = new ReturnVoucher();
            returnVoucher.setId(item.getId());
            returnVoucher.setFactoryId(item.getFactoryId());
            returnVoucher.setVoucherCode(item.getVoucherCode());
            returnVoucher.setVoucherAt(item.getVoucherAt());
            returnVoucher.setTotalGoods(BigDecimal.ZERO);
            returnVoucherRepository.save(returnVoucher);
            accountVoucherCodeRepository.deleteByVoucherId(item.getId());
        }
        
//        List<String> soldDeliveryVoucherIds = soldDeliveryVoucherRepository.findIdByDeliveryVoucherId(id);
//        List<String> recallVoucherIds = recallVoucherRepository.findIdByDeliveryVoucherId(id);
//        List<String> paymentVoucherIds = paymentVoucherRepository.findIdByVoucherIdIn(soldDeliveryVoucherIds);
//        soldDeliveryVoucherRepository.deleteByIdIn(soldDeliveryVoucherIds);
//        recallVoucherRepository.deleteByIdIn(recallVoucherIds);
//        paymentVoucherRepository.deleteByIdIn(paymentVoucherIds);
        List<String> ids = new ArrayList<>();
        ids.addAll(soldDeliveryVoucherIds);
        ids.addAll(recallVoucherIds);
        ids.addAll(paymentVoucherIds);
        ids.addAll(returnVoucherIds);
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
        goodsInOut.setCreateAt(createAt);
        goodsInOut.setCreateByFullName(createBy);
        goodsInOutRepository.save(goodsInOut);
    }

}
