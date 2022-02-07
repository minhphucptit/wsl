package com.ceti.wholesale.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.common.constant.ConstantText;
import com.ceti.wholesale.common.enums.ProductTypeEnum;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.CreateGoodsInventoryRequest;
import com.ceti.wholesale.controller.api.request.CreateInventoryVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateInventoryVoucherRequest;
import com.ceti.wholesale.dto.InventoryVoucherDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.GoodsInventory;
import com.ceti.wholesale.model.InventoryVoucher;
import com.ceti.wholesale.repository.FactoryImportVoucherRepository;
import com.ceti.wholesale.repository.GasRefuelingVoucherRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.GoodsInventoryRepository;
import com.ceti.wholesale.repository.InventoryVoucherRepository;
import com.ceti.wholesale.service.InventoryVoucherService;

@Service
@Transactional
public class InventoryVoucherServiceImpl implements InventoryVoucherService {

    @Autowired
    private InventoryVoucherRepository inventoryVoucherRepository;

    @Autowired
    private GoodsInventoryRepository goodsInventoryRepository;
    
    @Autowired
    private GoodsInOutRepository goodsInOutRepository;
    
    @Autowired
    private FactoryImportVoucherRepository FactoryImportVoucherRepository;
    
    @Autowired
    private GasRefuelingVoucherRepository gasRefuelingVoucherRepository;

    @Autowired
    private VoucherUtils voucherUtils;

    @Override
    public Page<InventoryVoucherDto> getAllByCondition(Long voucherAtFrom, Long voucherAtTo,String no, String factoryId, Pageable pageable) {
        Instant from = voucherAtFrom == null ? null : Instant.ofEpochSecond(voucherAtFrom);
        Instant to = voucherAtTo == null ? null : Instant.ofEpochSecond(voucherAtTo);
        Page<InventoryVoucher> page = inventoryVoucherRepository.getAllByConditions(from, to,no, factoryId, pageable);
        return CommonMapper.toPage(page, InventoryVoucherDto.class, pageable);
    }

    @Override
    public InventoryVoucherDto createInventoryVoucher(CreateInventoryVoucherRequest request, String factoryId, Boolean isCreated) {
    	
        if (request.getGoodsInventories().isEmpty()) {
            throw new BadRequestException("Phải có hàng mới tạo được phiếu");
        }
        Instant now = Instant.now();
        String no = voucherUtils.genereateVoucherNOWithVoucherAt(factoryId, ConstantText.VOUCHER_NO_KIEM_KE_PREFIX, request.getVoucherAt());
        InventoryVoucher inventoryVoucher = CommonMapper.map(request, InventoryVoucher.class);
        inventoryVoucher.setCreateAt(now);
        inventoryVoucher.setUpdateAt(now);
        inventoryVoucher.setUpdateByFullName(request.getCreateByFullName());
        inventoryVoucher.setId(no);
        inventoryVoucher.setNo(no);
        inventoryVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_KIEM_KE);
        inventoryVoucher.setFactoryId(factoryId);
        if (inventoryVoucherRepository.existsById(inventoryVoucher.getId())) {
            throw new BadRequestException("Hôm nay đã tạo phiếu kiểm kê");
        }
        if (!request.getGoodsInventories().isEmpty()) {

            List<CreateGoodsInventoryRequest> goodsInventoryRequests = request.getGoodsInventories();
            for (CreateGoodsInventoryRequest goodsInventoryRequest : goodsInventoryRequests) {
                GoodsInventory goodsInventory = CommonMapper.map(goodsInventoryRequest, GoodsInventory.class);
                goodsInventory.setVoucherAt(request.getVoucherAt());
                goodsInventory.setFactoryId(factoryId);
                goodsInventory.setVoucherId(inventoryVoucher.getId());
                goodsInventory.setVoucherCode(inventoryVoucher.getVoucherCode());
                goodsInventory.setVoucherNo(inventoryVoucher.getNo());
                goodsInventory.setStt(goodsInventoryRequests.indexOf(goodsInventoryRequest));
                goodsInventoryRepository.save(goodsInventory);
                if(ProductTypeEnum.GAS.name().equals(goodsInventoryRequest.getProductType())){
                    inventoryVoucher.setTotalLpg(inventoryVoucher.getTotalLpg().add(goodsInventoryRequest.getInventory().multiply(goodsInventoryRequest.getWeight())));
                }

            }

        }

        inventoryVoucherRepository.save(inventoryVoucher);
        if(isCreated) {
        	gasRefuelingVoucherRepository.genarateGasRefuleingVoucher(request.getVoucherAt(), factoryId, null, true);
        }
        return CommonMapper.map(inventoryVoucher, InventoryVoucherDto.class);
    }

    @Override
    public InventoryVoucherDto updateInventoryVoucher(UpdateInventoryVoucherRequest request, String factoryId, String id, Boolean isCreated) {
    	
    	Optional<InventoryVoucher> optional=inventoryVoucherRepository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException("không tồn tại phiếu kiểm kê này");
        }
        if (request.getGoodsInventories().isEmpty()) {
            throw new BadRequestException("Phải có hàng mới tạo được phiếu");
        }        
        InventoryVoucher inventoryVoucher = optional.get();
        CommonMapper.copyPropertiesIgnoreNull(request, inventoryVoucher);
        Instant now = Instant.now();
        inventoryVoucher.setUpdateAt(now);
        inventoryVoucher.setUpdateByFullName(request.getUpdateByFullName());
        inventoryVoucher.setTotalLpg(BigDecimal.ZERO);

        goodsInventoryRepository.deleteByVoucherId(id);
        if (!request.getGoodsInventories().isEmpty()) {
            List<CreateGoodsInventoryRequest> goodsInventoryRequests = request.getGoodsInventories();
            for (CreateGoodsInventoryRequest goodsInventoryRequest : goodsInventoryRequests) {
                GoodsInventory goodsInventory = CommonMapper.map(goodsInventoryRequest, GoodsInventory.class);
                goodsInventory.setVoucherAt(request.getVoucherAt());
                goodsInventory.setFactoryId(factoryId);
                goodsInventory.setVoucherId(inventoryVoucher.getId());
                goodsInventory.setVoucherCode(inventoryVoucher.getVoucherCode());
                goodsInventory.setVoucherNo(inventoryVoucher.getNo());
                goodsInventory.setStt(goodsInventoryRequests.indexOf(goodsInventoryRequest));
                goodsInventoryRepository.save(goodsInventory);
                if(ProductTypeEnum.GAS.name().equals(goodsInventoryRequest.getProductType())){
                    inventoryVoucher.setTotalLpg(inventoryVoucher.getTotalLpg().add(goodsInventoryRequest.getInventory().multiply(goodsInventoryRequest.getWeight())));
                }

            }
        }

        inventoryVoucherRepository.save(inventoryVoucher);
        if(isCreated) {
        	gasRefuelingVoucherRepository.genarateGasRefuleingVoucher(request.getVoucherAt(), factoryId, null, true);
        }
        return CommonMapper.map(inventoryVoucher, InventoryVoucherDto.class);
    }
    
    @Override
    public void deleteInventoryVoucher(String id) {
     	Optional<InventoryVoucher> optional=inventoryVoucherRepository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException("không tồn tại phiếu kiểm kê này");
        }
        InventoryVoucher inventoryVoucher = optional.get();
        inventoryVoucherRepository.deleteById(id);
        goodsInventoryRepository.deleteByVoucherId(id);
        FactoryImportVoucherRepository.deleteById(inventoryVoucher.getFactoryImportVoucherId());
        goodsInOutRepository.deleteByVoucherId(inventoryVoucher.getFactoryImportVoucherId());
    }
}
