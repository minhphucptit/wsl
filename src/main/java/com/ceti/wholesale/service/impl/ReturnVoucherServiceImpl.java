package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.constant.ConstantText;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.*;
import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.dto.ReturnVoucherDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.DeliveryVoucher;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.ReturnVoucher;
import com.ceti.wholesale.repository.*;
import com.ceti.wholesale.service.ReturnVoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReturnVoucherServiceImpl implements ReturnVoucherService {

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;
    @Autowired
    private ReturnVoucherRepository returnVoucherRepository;
    @Autowired
    private GoodsInOutRepository goodsInOutRepository;
    @Autowired
    private  VoucherUtils voucherUtils;
    @Autowired
    private AccountVoucherCodeRepository accountVoucherCodeRepository;
    
    private void createGoodsInOut(CreateGoodsInOutRequest request, String voucherNo, String voucherCode, String voucherId, int stt,Instant createAt,String createBy,Instant updateAt, String updateBy) {
        GoodsInOut goodsInOut = CommonMapper.map(request, GoodsInOut.class);
        goodsInOut.setVoucherNo(voucherNo);
        goodsInOut.setVoucherCode(voucherCode);
        goodsInOut.setVoucherId(voucherId);
        goodsInOut.setUpdateByFullName(updateBy);
        goodsInOut.setUpdateAt(updateAt);
        goodsInOut.setCreateAt(createAt);
        goodsInOut.setCreateByFullName(createBy);
        goodsInOut.setStt(stt);
        goodsInOutRepository.save(goodsInOut);
    }

    @Override
    public ReturnVoucherDto createReturnVoucher(CreateReturnVoucherRequest request, String factoryId) {
        if (request.getGoodsInOut().isEmpty()) {
            throw new BadRequestException("Phải có hàng mới tạo được phiếu");
        }
        Instant now = Instant.now();
        String no = voucherUtils.genereateVoucherNO(factoryId, ConstantText.VOUCHER_CODE_NHAP_XE, i -> returnVoucherRepository.countReturnVoucherInDay(i));
        ReturnVoucher returnVoucher = CommonMapper.map(request, ReturnVoucher.class);
        returnVoucher.setCreatedAt(now);
        returnVoucher.setUpdateAt(now);
        returnVoucher.setId(no);
        returnVoucher.setUpdateByFullName(request.getCreatedByFullName());
        returnVoucher.setNo(no);
        returnVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_NHAP_XE);
        returnVoucher.setFactoryId(factoryId);
        returnVoucherRepository.save(returnVoucher);
        List<GoodsInOutDto> goodsInOutDto = new ArrayList<>();
        if (!request.getGoodsInOut().isEmpty()) {
            List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();

            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                goodsInOutRequest.setCompanyId(returnVoucher.getCompanyId());
                goodsInOutRequest.setVoucherAt(returnVoucher.getVoucherAt());
                goodsInOutRequest.setTruckDriverId(returnVoucher.getTruckDriverId());
                goodsInOutRequest.setTruckLicensePlateNumber(returnVoucher.getTruckLicensePlateNumber());
                GoodsInOut goodsInOut = new GoodsInOut();

                CommonMapper.map(goodsInOutRequest, GoodsInOut.class);
                goodsInOutRequest.setFactoryId(factoryId);
                createGoodsInOut(goodsInOutRequest, returnVoucher.getNo(), returnVoucher.getVoucherCode(),
                        returnVoucher.getId(), goodsInOutRequests.indexOf(goodsInOutRequest),now,request.getCreatedByFullName(),now,request.getCreatedByFullName());

                goodsInOutDto.add(CommonMapper.map(goodsInOut, GoodsInOutDto.class));
            }
        }
        return CommonMapper.map(returnVoucher, ReturnVoucherDto.class);
    }

    @Override
    public ReturnVoucherDto updateReturnVoucher(String id, UpdateReturnVoucherRequest request) {
        Optional<ReturnVoucher> optional = returnVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu nhập kho theo xe không tồn tại");
        } else {
            ReturnVoucher returnVoucher = optional.get();
            CommonMapper.copyPropertiesIgnoreNull(request, returnVoucher);
            Instant now = Instant.now();
            returnVoucher.setUpdateAt(now);
            returnVoucher.setTruckDriverId(request.getTruckDriverId());
            returnVoucherRepository.save(returnVoucher);

            goodsInOutRepository.deleteByVoucherId(id);
            List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();
            if (goodsInOutRequests.size() != 0) {
                for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                    goodsInOutRequest.setCompanyId(returnVoucher.getCompanyId());
                    goodsInOutRequest.setVoucherAt(returnVoucher.getVoucherAt());
                    goodsInOutRequest.setTruckDriverId(returnVoucher.getTruckDriverId());
                    goodsInOutRequest.setTruckLicensePlateNumber(returnVoucher.getTruckLicensePlateNumber());
                    GoodsInOut goodsInOut = new GoodsInOut();
                    CommonMapper.copyPropertiesIgnoreNull(goodsInOutRequest, goodsInOut);
                    goodsInOutRequest.setFactoryId(returnVoucher.getFactoryId());
                    createGoodsInOut(goodsInOutRequest, returnVoucher.getNo(), returnVoucher.getVoucherCode(), returnVoucher.getId(),
                            goodsInOutRequests.indexOf(goodsInOutRequest),returnVoucher.getCreatedAt(),returnVoucher.getCreatedByFullName(),now,request.getUpdateByFullName());
                }
            }
            return CommonMapper.map(returnVoucher, ReturnVoucherDto.class);
        }
    }

    @Override
    public void deleteReturnVoucher(String id) {
    	
    	Optional<ReturnVoucher> optional = returnVoucherRepository.findById(id);
    	
        if (!optional.isPresent()) {
            throw new NotFoundException("Phiếu nhập kho theo xe không tồn tại");
        }
        
        ReturnVoucher returnVoucher = new ReturnVoucher();
        returnVoucher.setId(id);
        returnVoucher.setFactoryId(optional.get().getFactoryId());
        returnVoucher.setVoucherCode(optional.get().getVoucherCode());
        returnVoucher.setVoucherAt(optional.get().getVoucherAt());
        returnVoucher.setTotalGoods(BigDecimal.ZERO);
        returnVoucherRepository.save(returnVoucher);
        accountVoucherCodeRepository.deleteByVoucherId(id);
        
        List<String> ids = new ArrayList<>();
        ids.add(id);
        goodsInOutRepository.deleteByVoucherIdIn(ids);
    }
}
