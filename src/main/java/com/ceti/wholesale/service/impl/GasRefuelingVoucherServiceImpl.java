package com.ceti.wholesale.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.common.constant.ConstantText;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.common.util.DatetimeUtil;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.CreateGasRefuelingVoucherRequest;
import com.ceti.wholesale.controller.api.request.CreateGoodsInOutRequest;
import com.ceti.wholesale.dto.GasRefuelingVoucherDto;
import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.GasRefuelingVoucher;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.repository.GasRefuelingVoucherRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.service.GasRefuelingVoucherService;

@Service
@Transactional
public class GasRefuelingVoucherServiceImpl implements GasRefuelingVoucherService {
    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

    @Autowired
    private GasRefuelingVoucherRepository gasRefuelingVoucherRepository;
    @Autowired
    private GoodsInOutRepository goodsInOutRepository;
    @Autowired
    private VoucherUtils voucherUtils;
    @Override
    public GasRefuelingVoucherDto createVoucher(CreateGasRefuelingVoucherRequest request, String factoryId) {
        String no = voucherUtils
                .genereateVoucherNOWithVoucherAt(factoryId, ConstantText.VOUCHER_CODE_CHIET_NAP_PREFIX, request.getVoucherAt());
        Instant now = Instant.now();
        GasRefuelingVoucher gasRefuelingVoucher = new GasRefuelingVoucher();
        gasRefuelingVoucher.setNo(no);
        gasRefuelingVoucher.setId(no);
        if (gasRefuelingVoucherRepository.existsById(gasRefuelingVoucher.getId())) {
            throw new BadRequestException("Hôm nay đã tạo phiếu chiết nạp");
        }
        gasRefuelingVoucher.setCreatedAt(now);
        gasRefuelingVoucher.setUpdateAt(now);
        gasRefuelingVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_CHIET_NAP_PREFIX);
        gasRefuelingVoucher.setFactoryId(factoryId);
        CommonMapper.copyPropertiesIgnoreNull(request, gasRefuelingVoucher);
        gasRefuelingVoucherRepository.save(gasRefuelingVoucher);
        List<CreateGoodsInOutRequest> createGoodsInOutRequests = request.getCreateGoodsInOutRequests();
        if(createGoodsInOutRequests.size()!=0){
            for (CreateGoodsInOutRequest importGio : createGoodsInOutRequests) {
                GoodsInOut goodsInOut = new GoodsInOut();
                goodsInOut.setFactoryId(factoryId);
                goodsInOut.setVoucherId(gasRefuelingVoucher.getId());
                goodsInOut.setVoucherNo(gasRefuelingVoucher.getNo());
                goodsInOut.setVoucherCode(gasRefuelingVoucher.getVoucherCode());
                goodsInOut.setVoucherAt(gasRefuelingVoucher.getVoucherAt());
                goodsInOut.setStt(createGoodsInOutRequests.indexOf(importGio));
                goodsInOut.setUpdateAt(now);
                goodsInOut.setUpdateByFullName(request.getUpdateByFullName());
                goodsInOut.setCreateByFullName(request.getCreatedByFullName());
                goodsInOut.setCreateAt(now);
                CommonMapper.copyPropertiesIgnoreNull(importGio, goodsInOut);
                goodsInOutRepository.save(goodsInOut);
            }
        }
        return CommonMapper.map(gasRefuelingVoucher, GasRefuelingVoucherDto.class);
    }

    @Override
    public GasRefuelingVoucherDto updateVoucher(String id, CreateGasRefuelingVoucherRequest request, String factoryId) {
        Optional<GasRefuelingVoucher> optional = gasRefuelingVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu chiết nạp không tồn tại");
        }
        GasRefuelingVoucher gasRefuelingVoucher = optional.get();
        if(!request.getVoucherAt().equals(gasRefuelingVoucher.getVoucherAt())){
            throw new BadRequestException("Không thể sửa ngày của phiếu chiết nạp GAS");
        }
        Instant now = Instant.now();
        gasRefuelingVoucher.setUpdateAt(now);
        CommonMapper.copyPropertiesIgnoreNull(request, gasRefuelingVoucher);
        gasRefuelingVoucherRepository.save(gasRefuelingVoucher);
        goodsInOutRepository.deleteByVoucherId(id);
        List<CreateGoodsInOutRequest> createGoodsInOutRequests = request.getCreateGoodsInOutRequests();
        if(createGoodsInOutRequests.size()!=0){
            for (CreateGoodsInOutRequest importGio : createGoodsInOutRequests) {
                GoodsInOut goodsInOut = new GoodsInOut();
                goodsInOut.setFactoryId(factoryId);
                goodsInOut.setVoucherId(gasRefuelingVoucher.getId());
                goodsInOut.setVoucherCode(gasRefuelingVoucher.getVoucherCode());
                goodsInOut.setVoucherNo(gasRefuelingVoucher.getNo());
                goodsInOut.setVoucherAt(gasRefuelingVoucher.getVoucherAt());
                goodsInOut.setStt(createGoodsInOutRequests.indexOf(importGio));
                goodsInOut.setUpdateAt(now);
                goodsInOut.setUpdateByFullName(request.getUpdateByFullName());
                goodsInOut.setCreateAt(gasRefuelingVoucher.getCreatedAt());
                goodsInOut.setCreateByFullName(gasRefuelingVoucher.getCreatedByFullName());
                CommonMapper.copyPropertiesIgnoreNull(importGio, goodsInOut);
                goodsInOutRepository.save(goodsInOut);
            }
        }
        return CommonMapper.map(gasRefuelingVoucher, GasRefuelingVoucherDto.class);
    }

    @Override
    public void deleteVoucher(String id) {
        if (!gasRefuelingVoucherRepository.existsById(id)) {
            throw new NotFoundException("Phiếu chiết nạp không tồn tại");
        }
        gasRefuelingVoucherRepository.deleteById(id);
        goodsInOutRepository.deleteByVoucherId(id);
    }

	@Override
	public List<GoodsInOutDto> suggestionCreateVoucher(Instant voucherAt, String factoryId) {
        if (gasRefuelingVoucherRepository.existsByVoucherAtAndFactoryId(voucherAt,factoryId)){
        	throw new BadRequestException("Phiếu đã tồn tại vào ngày " + DatetimeUtil.formatLongToString( zoneId, voucherAt));
        }

        List<GoodsInOut> goodsInOuts = goodsInOutRepository.getListSuggestion(voucherAt, factoryId, null, false);
	
        return CommonMapper.toList(goodsInOuts, GoodsInOutDto.class);
	}

	@Override
	public void recalculation(Instant voucherAt, String factoryId, String gasRefuelingVoucherId) {
		
        if (!gasRefuelingVoucherRepository.existsById(gasRefuelingVoucherId)){
        	throw new BadRequestException("Phiếu không tồn tại vào ngày " + DatetimeUtil.formatLongToString( zoneId, voucherAt));
        }
        gasRefuelingVoucherRepository.genarateGasRefuleingVoucher(voucherAt, factoryId,
        		gasRefuelingVoucherId, true);
	}

}
