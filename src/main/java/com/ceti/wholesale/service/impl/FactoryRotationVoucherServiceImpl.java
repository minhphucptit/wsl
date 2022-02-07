package com.ceti.wholesale.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.common.constant.ConstantText;
import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.CreateFactoryRotationVoucherRequest;
import com.ceti.wholesale.controller.api.request.CreateGoodsInOutRequest;
import com.ceti.wholesale.controller.api.request.UpdateFactoryRotationVoucherRequest;
import com.ceti.wholesale.dto.FactoryRotationVoucherDto;
import com.ceti.wholesale.dto.TruckDriverDto;
import com.ceti.wholesale.dto.TruckDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.FactoryRotationVoucher;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.Truck;
import com.ceti.wholesale.model.TruckDriver;
import com.ceti.wholesale.repository.FactoryRotationVoucherDetailRepository;
import com.ceti.wholesale.repository.FactoryRotationVoucherRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.service.FactoryRotationVoucherService;
import com.ceti.wholesale.service.GoodsInOutService;

@Service
@Transactional
public class FactoryRotationVoucherServiceImpl implements FactoryRotationVoucherService {
    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;

    @Autowired
    private FactoryRotationVoucherRepository factoryRotationVoucherRepository;

    @Autowired
    private GoodsInOutService goodsInOutService;

    @Autowired
    private GoodsInOutRepository goodsInOutRepository;

    @Autowired
    private FactoryRotationVoucherDetailRepository factoryRotationVoucherDetailRepository;

    @Autowired
    private  VoucherUtils voucherUtils;

    @Override
    public FactoryRotationVoucherDto createFactoryRotationVoucher(
            CreateFactoryRotationVoucherRequest createFactoryRotationVoucherRequest, String factoryId) {
        if (createFactoryRotationVoucherRequest.getGoodsInOut().isEmpty()) {
            throw new BadRequestException("Phải có hàng mới tạo được phiếu");
        }
        Instant now = Instant.now();
        String no = voucherUtils.genereateVoucherNO(factoryId, ConstantText.VOUCHER_CODE_PHIEU_LUAN_CHUYEN_NHA_MAY,
                i -> factoryRotationVoucherRepository.countFactoryRotationVoucherInDay(i));
        FactoryRotationVoucher factoryRotationVoucher = CommonMapper.map(createFactoryRotationVoucherRequest,
                FactoryRotationVoucher.class);
        factoryRotationVoucher.setCreatedAt(now);
        factoryRotationVoucher.setUpdateAt(now);
        factoryRotationVoucher
                .setId(no);
        factoryRotationVoucher.setUpdateByFullName(factoryRotationVoucher.getCreatedByFullName());
        factoryRotationVoucher.setNo(no);
        factoryRotationVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_PHIEU_LUAN_CHUYEN_NHA_MAY);
        factoryRotationVoucher.setFactoryId(factoryId);

        factoryRotationVoucherRepository.save(factoryRotationVoucher);
        //add GoodsInOut
        List<CreateGoodsInOutRequest> goodsInOutRequests = createFactoryRotationVoucherRequest.getGoodsInOut();
        if (!goodsInOutRequests.isEmpty()) {
            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                goodsInOutRequest.setCompanyId(factoryRotationVoucher.getCompanyExportId());
                goodsInOutRequest.setVoucherAt(factoryRotationVoucher.getVoucherAt());
                goodsInOutRequest.setFactoryId(factoryId);
                goodsInOutRequest.setTruckDriverId(factoryRotationVoucher.getTruckDriverId());
                goodsInOutRequest.setTruckLicensePlateNumber(factoryRotationVoucher.getTruckLicensePlateNumber());
                createGoodsInOut(goodsInOutRequest, factoryRotationVoucher.getNo(), factoryRotationVoucher.getVoucherCode(), 
                		factoryRotationVoucher.getId(), goodsInOutRequests.indexOf(goodsInOutRequest),now,createFactoryRotationVoucherRequest.getCreatedByFullName(),now,createFactoryRotationVoucherRequest.getCreatedByFullName());
                //swap quantity
                BigDecimal out = goodsInOutRequest.getOutQuantity();
                BigDecimal in = goodsInOutRequest.getInQuantity();
                goodsInOutRequest.setInQuantity(out);
                goodsInOutRequest.setOutQuantity(in);
                goodsInOutRequest.setCompanyId(factoryRotationVoucher.getCompanyImportId());

                createGoodsInOut(goodsInOutRequest, factoryRotationVoucher.getNo(), factoryRotationVoucher.getVoucherCode(), 
                		factoryRotationVoucher.getId(), goodsInOutRequests.indexOf(goodsInOutRequest),now,createFactoryRotationVoucherRequest.getCreatedByFullName(),now,createFactoryRotationVoucherRequest.getCreatedByFullName());

            }
        }
        return CommonMapper.map(factoryRotationVoucher, FactoryRotationVoucherDto.class);

    }

    @Override
    public Page<FactoryRotationVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
        ResultPage<Object[]> page = factoryRotationVoucherDetailRepository.findAllWithFilter(pageable, where);
        List<FactoryRotationVoucherDto> factoryImportVoucherDtos = new ArrayList<>();
        for (Object[] object : page.getPageList()) {
            FactoryRotationVoucher factoryRotationVoucher = (FactoryRotationVoucher) object[0];
            FactoryRotationVoucherDto factoryImportVoucherDto = CommonMapper.map(factoryRotationVoucher, FactoryRotationVoucherDto.class);
            if (object[1] != null) {
                TruckDriver truckDriver = (TruckDriver) object[1];
                TruckDriverDto truckDriverDto = CommonMapper.map(truckDriver, TruckDriverDto.class);
                factoryImportVoucherDto.setTruckDriver(truckDriverDto);
            }
            if (object[2] != null) {
                Truck truck = (Truck) object[2];
                TruckDto truckDto = CommonMapper.map(truck, TruckDto.class);
                factoryImportVoucherDto.setTruck(truckDto);
            }
            factoryImportVoucherDtos.add(factoryImportVoucherDto);
        }
        return new PageImpl<>(factoryImportVoucherDtos, pageable, page.getTotalItems());
    }

    @Override
    public FactoryRotationVoucherDto updateFactoryRotationVoucher(String id, UpdateFactoryRotationVoucherRequest request) {
        Optional<FactoryRotationVoucher> optional = factoryRotationVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu luân chuyển hàng hóa không tồn tại");
        }
        FactoryRotationVoucher factoryRotationVoucher = optional.get();
        CommonMapper.copyPropertiesIgnoreNull(request, factoryRotationVoucher);
        Instant now = Instant.now();
        factoryRotationVoucher.setUpdateAt(now);
        factoryRotationVoucher.setTruckDriverId(request.getTruckDriverId());
        factoryRotationVoucherRepository.save(factoryRotationVoucher);

        //delete GIO
        goodsInOutRepository.deleteByVoucherId(id);
        List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();
        if (!goodsInOutRequests.isEmpty()) {
            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                goodsInOutRequest.setCompanyId(factoryRotationVoucher.getCompanyExportId());
                goodsInOutRequest.setVoucherAt(factoryRotationVoucher.getVoucherAt());
                goodsInOutRequest.setFactoryId(factoryRotationVoucher.getFactoryId());
                goodsInOutRequest.setTruckDriverId(factoryRotationVoucher.getTruckDriverId());
                goodsInOutRequest.setTruckLicensePlateNumber(factoryRotationVoucher.getTruckLicensePlateNumber());
                createGoodsInOut(goodsInOutRequest, factoryRotationVoucher.getNo(), factoryRotationVoucher.getVoucherCode(),
                		factoryRotationVoucher.getId(), goodsInOutRequests.indexOf(goodsInOutRequest),factoryRotationVoucher.getCreatedAt(),factoryRotationVoucher.getCreatedByFullName(),now,request.getUpdateByFullName());
                //swap quantity
                BigDecimal out = goodsInOutRequest.getOutQuantity();
                BigDecimal in = goodsInOutRequest.getInQuantity();
                goodsInOutRequest.setInQuantity(out);
                goodsInOutRequest.setOutQuantity(in);
                goodsInOutRequest.setCompanyId(factoryRotationVoucher.getCompanyImportId());

                createGoodsInOut(goodsInOutRequest, factoryRotationVoucher.getNo(), factoryRotationVoucher.getVoucherCode(), 
                		factoryRotationVoucher.getId(), goodsInOutRequests.indexOf(goodsInOutRequest),factoryRotationVoucher.getCreatedAt(),factoryRotationVoucher.getCreatedByFullName(),now,request.getUpdateByFullName());

            }
        }

        return CommonMapper.map(factoryRotationVoucher, FactoryRotationVoucherDto.class);
    }

    @Override
    public void deleteFactoryRotationVoucher(String id) {
        if (!factoryRotationVoucherRepository.existsById(id)) {
            throw new NotFoundException("Phiếu xuất nhập luân chuyển không tồn tại");
        }
        factoryRotationVoucherRepository.deleteById(id);
        goodsInOutRepository.deleteByVoucherId(id);
    }
    
    private void createGoodsInOut(CreateGoodsInOutRequest request, String voucherNo, String voucherCode, String voucherId, int stt,Instant createAt,String createBy,Instant updateAt,String updateBy) {
        GoodsInOut goodsInOut = CommonMapper.map(request, GoodsInOut.class);
        goodsInOut.setVoucherNo(voucherNo);
        goodsInOut.setVoucherCode(voucherCode);
        goodsInOut.setVoucherId(voucherId);
        goodsInOut.setStt(stt);
        goodsInOut.setUpdateByFullName(updateBy);
        goodsInOut.setUpdateAt(updateAt);
        goodsInOut.setCreateAt(createAt);
        goodsInOut.setCreateByFullName(createBy);
        goodsInOutRepository.save(goodsInOut);
    }
}
