package com.ceti.wholesale.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
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
import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.CreateGoodsInOutRequest;
import com.ceti.wholesale.controller.api.request.CreateRecallVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateRecallVoucherRequest;
import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.dto.RecallVoucherDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.mapper.RecallVoucherMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.RecallVoucher;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.RecallVoucherRepository;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.RecallVoucherService;

@Service
@Transactional
public class RecallVoucherServiceImpl implements RecallVoucherService {

    @Value(value = "${ceti.service.zoneId}")
    private String zoneId;

    @Autowired
    private RecallVoucherRepository recallVoucherRepository;

    @Autowired
    private GoodsInOutService goodsInOutService;

    @Autowired
    private GoodsInOutRepository goodsInOutRepository;

    @Autowired
    private RecallVoucherMapper recallVoucherMapper;

    @Autowired
    private  VoucherUtils voucherUtils;
    
    @Autowired
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

//    @Override
//    public Page<RecallVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
//        ResultPage<Object[]> page = recallVoucherDetailRepository.findAllWithFilter(pageable, where);
//        List<RecallVoucherDto> recallVoucherDtos = new ArrayList<>();
//        for (Object[] object : page.getPageList()) {
//            RecallVoucher rv = (RecallVoucher) object[0];
//            RecallVoucherDto recallVoucherDto = CommonMapper.map(rv, RecallVoucherDto.class);
//
//            if (object[1] != null) {
//                TruckDriver truckDriver = (TruckDriver) object[1];
//                TruckDriverDto truckDriverDto = CommonMapper.map(truckDriver, TruckDriverDto.class);
//                recallVoucherDto.setTruckDriver(truckDriverDto);
//            }
//
//            if (object[2] != null) {
//                Truck truck = (Truck) object[2];
//                TruckDto truckDto = CommonMapper.map(truck, TruckDto.class);
//                recallVoucherDto.setTruck(truckDto);
//            }
//
//            if (object[3] != null) {
//                Company company = (Company) object[3];
//                CompanyDto companyDto = CommonMapper.map(company, CompanyDto.class);
//                recallVoucherDto.setCompany(companyDto);
//            }
//
//            recallVoucherDtos.add(recallVoucherDto);
//        }
//        Instant voucherAtFrom = where.getFirst("voucher_at_from") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_from")));
//        Instant voucherAtTo = where.getFirst("voucher_at_to") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_to")));
//        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "rv");
//        List<RecallVoucherDto> recallVoucherDtos = recallVoucherMapper.getList(where.getFirst("product_id"),where.getFirst("company_id"),
//                where.getFirst("truck_driver_id"),where.getFirst("truck_license_plate_number"),
//                where.getFirst("factory_id"),where.getFirst("goods_in_out.type"),voucherAtFrom,voucherAtTo,pagingStr);
//        long totalItems = recallVoucherMapper.countList(where.getFirst("product_id"),where.getFirst("company_id"),
//                where.getFirst("truck_driver_id"),where.getFirst("truck_license_plate_number"),
//                where.getFirst("factory_id"),where.getFirst("goods_in_out.type"),voucherAtFrom,voucherAtTo);
//        return new PageImpl<>(recallVoucherDtos, pageable, totalItems);
//    }

    @Override
    public RecallVoucherDto createRecallVoucher(CreateRecallVoucherRequest request, String factoryId) {
        if (request.getGoodsInOut().isEmpty()) {
            throw new BadRequestException("Phải có hàng mới tạo được phiếu");
        }
        Instant now = Instant.now();
        String id = voucherUtils.genereateVoucherNO(factoryId, VoucherEnum.VOUCHER_CODE_NHAP_THU_HOI.getCode(),
                i -> recallVoucherRepository.countRecallVoucherInDay(i));

        AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryId, VoucherEnum.VOUCHER_CODE_NHAP_THU_HOI,
        		request.getVoucherAt());
        
        RecallVoucher recallVoucher = CommonMapper.map(request, RecallVoucher.class);
        recallVoucher.setCreatedAt(now);
        recallVoucher.setId(id);
        recallVoucher.setUpdateAt(now);
        recallVoucher.setUpdateByFullName(request.getCreatedByFullName());
        recallVoucher.setNo(id);
        recallVoucher.setVoucherCode(VoucherEnum.VOUCHER_CODE_NHAP_THU_HOI.getCode());
        recallVoucher.setFactoryId(factoryId);
        recallVoucherRepository.save(recallVoucher);

        voucherUtils.createAccountVoucherCode(accountVoucherCode, id, id ,true , now, request.getCreatedByFullName());

        if (!request.getGoodsInOut().isEmpty()) {
            StringBuilder err = new StringBuilder();
            List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();

            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                goodsInOutRequest.setVoucherAt(recallVoucher.getVoucherAt());
                goodsInOutRequest.setCompanyId(recallVoucher.getCompanyId());
                goodsInOutRequest.setXbxOutQuantity(BigDecimal.valueOf(0));
                goodsInOutRequest.setXxeOutQuantity(BigDecimal.valueOf(0));
                goodsInOutRequest.setTruckDriverId(recallVoucher.getTruckDriverId());
                goodsInOutRequest.setTruckLicensePlateNumber(recallVoucher.getTruckLicensePlateNumber());
                BigDecimal max = goodsInOutRepository
                        .getMaxXBXOutQuantityOrInQuantityOneProduct(request.getDeliveryVoucherId(),
                                goodsInOutRequest.getProductId(),goodsInOutRequest.getStt() );
                if (goodsInOutRequest.getNxeInQuantity() != null && goodsInOutRequest.getNxeInQuantity().doubleValue() > max.doubleValue()) {
                    err.append("Số lượng nhập thu hồi của sản phẩm ").append(goodsInOutRequest.getProductName()).append(" phải nhỏ hơn hoặc bằng ").append(max).append("\n");
                } else {
                    goodsInOutRequest.setFactoryId(factoryId);
                    createGoodsInOut(goodsInOutRequest, accountVoucherCode.getAccNo(), recallVoucher.getVoucherCode(), recallVoucher.getId(),
                            goodsInOutRequest.getStt(),now,request.getCreatedByFullName(),now,request.getCreatedByFullName());
                    goodsInOutRepository.updateNxeInQuantityXXEOneProduct(request.getDeliveryVoucherId(),
                            goodsInOutRequest.getProductId(),goodsInOutRequest.getStt() );
                }

            }
            if (err.length() > 0) {
                throw new BadRequestException(err.toString());
            }
        }
        RecallVoucherDto recallVoucherDto = CommonMapper.map(recallVoucher, RecallVoucherDto.class);
        recallVoucherDto.setAccNo(accountVoucherCode.getAccNo());
        return recallVoucherDto;
    }

    @Override
    @Transactional
    public RecallVoucherDto updateRecallVoucher(String id, UpdateRecallVoucherRequest request) {
        Optional<RecallVoucher> optional = recallVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu nhập kho không tồn tại");
        } else {
            RecallVoucher recallVoucher = optional.get();
            Instant now = Instant.now();
            if(!request.getVoucherAt().equals(recallVoucher.getVoucherAt())) {
                AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(recallVoucher.getFactoryId(),
                		VoucherEnum.VOUCHER_CODE_NHAP_THU_HOI, request.getVoucherAt());
                                
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
            
            CommonMapper.copyPropertiesIgnoreNull(request, recallVoucher);
            
            recallVoucher.setUpdateAt(now);
            recallVoucherRepository.save(recallVoucher);
            goodsInOutRepository.deleteByVoucherId(id);
            List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();
            if (goodsInOutRequests.size() != 0) {
                StringBuilder err = new StringBuilder();
                for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                    goodsInOutRequest.setVoucherAt(recallVoucher.getVoucherAt());
                    goodsInOutRequest.setTruckDriverId(recallVoucher.getTruckDriverId());
                    goodsInOutRequest.setTruckLicensePlateNumber(recallVoucher.getTruckLicensePlateNumber());
                    goodsInOutRepository.updateNxeInQuantityXXEOneProduct(recallVoucher.getDeliveryVoucherId(), goodsInOutRequest.getProductId(),goodsInOutRequest.getStt() );
                    BigDecimal max = goodsInOutRepository.getMaxXBXOutQuantityOrInQuantityOneProduct(recallVoucher.getDeliveryVoucherId(), goodsInOutRequest.getProductId(),goodsInOutRequest.getStt());
                    if (goodsInOutRequest.getNxeInQuantity() != null && goodsInOutRequest.getNxeInQuantity().doubleValue() > max.doubleValue()) {
                        err.append("Số lượng nhập thu hồi của sản phẩm ").append(goodsInOutRequest.getProductName()).append(" phải nhỏ hơn hoặc bằng ").append(max).append("\n");
                    } else {
                        goodsInOutRequest.setFactoryId(recallVoucher.getFactoryId());
                        createGoodsInOut(goodsInOutRequest, recallVoucher.getNo(), recallVoucher.getVoucherCode(), recallVoucher.getId(),
                                goodsInOutRequest.getStt(),recallVoucher.getCreatedAt(),recallVoucher.getCreatedByFullName(),now,request.getUpdateByFullName());
                        goodsInOutRepository.updateNxeInQuantityXXEOneProduct(recallVoucher.getDeliveryVoucherId(),
                                goodsInOutRequest.getProductId(), goodsInOutRequest.getStt());
                    }
                    if (err.length() > 0) {
                        throw new BadRequestException(err.toString());
                    }
                }
            }
            return CommonMapper.map(recallVoucher, RecallVoucherDto.class);
        }
    }

    @Override
    public void deleteRecallVoucher(String id) {
        Optional<RecallVoucher> optional = recallVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu nhập kho không tồn tại");
        }
        RecallVoucher oldRecallVoucher = optional.get();
        String deliveryVoucherId = oldRecallVoucher.getDeliveryVoucherId();
        RecallVoucher recallVoucher = new RecallVoucher();
        recallVoucher.setId(id);
        recallVoucher.setFactoryId(oldRecallVoucher.getFactoryId());
        recallVoucher.setVoucherCode(oldRecallVoucher.getVoucherCode());
        recallVoucher.setVoucherAt(oldRecallVoucher.getVoucherAt());
        recallVoucher.setTotalGoodsReturn(BigDecimal.ZERO);
        recallVoucherRepository.save(recallVoucher);
        accountVoucherCodeRepository.deleteByVoucherId(id);
        goodsInOutRepository.deleteByVoucherId(id);

        // cập nhật lại số lượng đã thu hồi mỗi sản phẩm ở phiếu xuất xe
        List<GoodsInOut> goodsInOuts = goodsInOutRepository.findByVoucherId(deliveryVoucherId);
        goodsInOuts.stream().forEach(goodsInOut -> { goodsInOutRepository.updateNxeInQuantityXXEOneProduct(deliveryVoucherId, goodsInOut.getProductId(), goodsInOut.getStt()); });
    }
    
    private void createGoodsInOut(CreateGoodsInOutRequest request, String voucherNo, String voucherCode, String voucherId, int stt,Instant createAt,String createBy,Instant updateAt, String updateBy) {
        GoodsInOut goodsInOut = CommonMapper.map(request, GoodsInOut.class);
        goodsInOut.setVoucherNo(voucherNo);
        goodsInOut.setVoucherCode(voucherCode);
        goodsInOut.setVoucherId(voucherId);
        goodsInOut.setUpdateAt(updateAt);
        goodsInOut.setUpdateByFullName(updateBy);
        goodsInOut.setCreateByFullName(createBy);
        goodsInOut.setCreateAt(createAt);
        goodsInOut.setStt(stt);
        goodsInOutRepository.save(goodsInOut);
    }

}
