package com.ceti.wholesale.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceti.wholesale.common.constant.ConstantText;
import com.ceti.wholesale.common.enums.ProductTypeEnum;
import com.ceti.wholesale.common.enums.VoucherEnum;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.CreateGoodsInOutRequest;
import com.ceti.wholesale.controller.api.request.CreateMaintainVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateMaintainVoucherRequest;
import com.ceti.wholesale.dto.MaintainVoucherDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.AccountVoucherCode;
import com.ceti.wholesale.model.CylinderDebt;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.GoodsInOutMaintainDetail;
import com.ceti.wholesale.model.MaintainVoucher;
import com.ceti.wholesale.repository.AccountVoucherCodeRepository;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.GoodsInOutMaintailDetailRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.MaintainVoucherRepository;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.MaintainVoucherService;

@Service
@Transactional
public class MaintainVoucherServiceImpl implements MaintainVoucherService {

    @Autowired
    private MaintainVoucherRepository maintainVoucherRepository;

    @Autowired
    private GoodsInOutRepository goodsInOutRepository;

    @Autowired
    private GoodsInOutMaintailDetailRepository goodsInOutMaintailDetailRepository;

    @Autowired
    private CylinderDebtRepository cylinderDebtRepository;

    @Value(value = "${ceti.service.zoneId}")
    public String zoneId;
    
    @Autowired
    private CylinderDebtService cylinderDebtService;

    @Autowired
    private  VoucherUtils voucherUtils;
    
    @Autowired
    private AccountVoucherCodeRepository accountVoucherCodeRepository;

//    @Override
//    public Page<MaintainVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable) {
//        ResultPage<Object[]> page = maintainVoucherDetailRepository.findAllWithFilter(pageable, where);
//        List<MaintainVoucherDto> maintainVoucherDtos = new ArrayList<>();
//        for (Object[] object : page.getPageList()) {
//            MaintainVoucher fev = (MaintainVoucher) object[0];
//            MaintainVoucherDto maintainVoucherDto = CommonMapper.map(fev, MaintainVoucherDto.class);
//
//            if (object[1] != null) {
//                Company company = (Company) object[1];
//                CompanyDto companyDto = CommonMapper.map(company, CompanyDto.class);
//                maintainVoucherDto.setCompany(companyDto);
//            }
//
//            if (object[2] != null) {
//                Customer customer = (Customer) object[2];
//                CustomerDto customerDto = CommonMapper.map(customer, CustomerDto.class);
//                maintainVoucherDto.setCustomer(customerDto);
//            }
//
//            if (object[3] != null) {
//                TruckDriver truckDriver = (TruckDriver) object[3];
//                TruckDriverDto truckDriverDto = CommonMapper.map(truckDriver, TruckDriverDto.class);
//                maintainVoucherDto.setTruckDriver(truckDriverDto);
//            }
//
//            if (object[4] != null) {
//                Truck truck = (Truck) object[4];
//                TruckDto truckDto = CommonMapper.map(truck, TruckDto.class);
//                maintainVoucherDto.setTruck(truckDto);
//            }
//
//            if (object[5] != null) {
//                Salesman salesman = (Salesman) object[5];
//                SalesmanDto salesmanDto = CommonMapper.map(salesman, SalesmanDto.class);
//                maintainVoucherDto.setSalesman(salesmanDto);
//            }
//
//            maintainVoucherDtos.add(maintainVoucherDto);
//        }
//        Instant voucherAtFrom = where.getFirst("voucher_at_from") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_from")));
//        Instant voucherAtTo = where.getFirst("voucher_at_to") == null ? null : Instant.ofEpochSecond(Long.parseLong(where.getFirst("voucher_at_to")));
//        String pagingStr = PageableProcess.PageToSqlQuery(pageable, "mv");
//        List<MaintainVoucherDto>maintainVoucherDtos = maintainVoucherMapper.getList(where.getFirst("product_id"),where.getFirst("id"),where.getFirst("company_id"),
//                where.getFirst("customer_id"),where.getFirst("truck_driver_id"),where.getFirst("truck_license_plate_number"),
//                where.getFirst("factory_id"),where.getFirst("goods_in_out.type"),voucherAtFrom,voucherAtTo,pagingStr);
//        long totalItems = maintainVoucherMapper.countList(where.getFirst("product_id"),where.getFirst("id"),where.getFirst("company_id"),
//                where.getFirst("customer_id"),where.getFirst("truck_driver_id"),where.getFirst("truck_license_plate_number"),
//                where.getFirst("factory_id"),where.getFirst("goods_in_out.type"),voucherAtFrom,voucherAtTo);
//        return new PageImpl<>(maintainVoucherDtos, pageable, totalItems);
//    }

    @Override
    public MaintainVoucherDto createMaintainVoucher(
            CreateMaintainVoucherRequest createMaintainVoucherRequest, String factoryId) {
        if (createMaintainVoucherRequest.getGoodsInOut().isEmpty()) {
            throw new BadRequestException("Phải có hàng mới tạo được phiếu");
        }
        MaintainVoucher maintainVoucher = CommonMapper.map(createMaintainVoucherRequest, MaintainVoucher.class);
        Instant now = Instant.now();
        String id = null;
        AccountVoucherCode accountVoucherCode = null;
        if (createMaintainVoucherRequest.getType().equalsIgnoreCase(ConstantText.IMPORT_MAINTAIN_VOUCHER_TYPE)) {
            id = voucherUtils.genereateVoucherNO(factoryId, VoucherEnum.VOUCHER_CODE_IMPORT_MAINTAIN_VOUCHER.getCode(),
                    i -> maintainVoucherRepository.countMaintainVoucherInDay(i));

            accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryId, VoucherEnum.VOUCHER_CODE_IMPORT_MAINTAIN_VOUCHER,
            		createMaintainVoucherRequest.getVoucherAt());
            
            maintainVoucher.setId(id);
            maintainVoucher.setVoucherCode(VoucherEnum.VOUCHER_CODE_IMPORT_MAINTAIN_VOUCHER.getCode());
            maintainVoucher.setNo(id);

            voucherUtils.createAccountVoucherCode(accountVoucherCode, id, id ,true , now, maintainVoucher.getCreatedByFullName());

        } else {
            id = voucherUtils.genereateVoucherNO(factoryId, VoucherEnum.VOUCHER_CODE_EXPORT_MAINTAIN_VOUCHER.getCode(),
                    i -> maintainVoucherRepository.countMaintainVoucherInDay(i));

            accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(factoryId, VoucherEnum.VOUCHER_CODE_EXPORT_MAINTAIN_VOUCHER,
            		createMaintainVoucherRequest.getVoucherAt());
            
            maintainVoucher.setId(id);
            maintainVoucher.setVoucherCode(VoucherEnum.VOUCHER_CODE_EXPORT_MAINTAIN_VOUCHER.getCode());
            maintainVoucher.setNo(id);

            voucherUtils.createAccountVoucherCode(accountVoucherCode, id, id ,true , now, maintainVoucher.getCreatedByFullName());

        }
        maintainVoucher.setCreatedAt(now);
        maintainVoucher.setUpdateAt(now);
        maintainVoucher.setUpdateByFullName(maintainVoucher.getCreatedByFullName());
        maintainVoucher.setFactoryId(factoryId);

        maintainVoucherRepository.save(maintainVoucher);
        
        if (!createMaintainVoucherRequest.getGoodsInOut().isEmpty()) {
            List<CreateGoodsInOutRequest> goodsInOutRequests = createMaintainVoucherRequest.getGoodsInOut();

            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                GoodsInOut goodsInOut = new GoodsInOut();
                GoodsInOutMaintainDetail goodsInOutMaintainDetail = CommonMapper.map(goodsInOutRequest.getGoodsInOutMaintainDetail(), GoodsInOutMaintainDetail.class);
                CommonMapper.copyPropertiesIgnoreNull(goodsInOutRequest, goodsInOut);

                goodsInOut.setVoucherNo(maintainVoucher.getNo());
                goodsInOut.setVoucherCode(maintainVoucher.getVoucherCode());
                goodsInOut.setVoucherId(maintainVoucher.getId());
                goodsInOut.setCompanyId(maintainVoucher.getCompanyId());
                goodsInOut.setCustomerId(maintainVoucher.getCustomerId());
                goodsInOut.setVoucherAt(maintainVoucher.getVoucherAt());
                goodsInOut.setIsMainProduct(true);
                goodsInOut.setInFactory(createMaintainVoucherRequest.getInFactory());
                goodsInOut.setFactoryId(factoryId);
                goodsInOut.setTruckDriverId(maintainVoucher.getTruckDriverId());
                goodsInOut.setTruckLicensePlateNumber(maintainVoucher.getTruckLicensePlateNumber());
                goodsInOut.setSalesmanId(maintainVoucher.getSalesmanId());
                goodsInOut.setStt(goodsInOutRequests.indexOf(goodsInOutRequest));
                goodsInOut.setUpdateAt(now);
                goodsInOut.setUpdateByFullName(maintainVoucher.getCreatedByFullName());
                goodsInOut.setCreateAt(now);
                goodsInOut.setCreateByFullName(maintainVoucher.getCreatedByFullName());
                goodsInOutRepository.save(goodsInOut);
                goodsInOutMaintainDetail.setId(goodsInOut.getId());
                goodsInOutMaintailDetailRepository.save(goodsInOutMaintainDetail);
                if(goodsInOut.getProductType().equals(ProductTypeEnum.VO.name())) {
                    cylinderDebtService
                            .createCylinderDebt(new CylinderDebt(maintainVoucher.getId(), goodsInOut.getProductId(),
                                    maintainVoucher.getVoucherCode(), maintainVoucher.getVoucherAt(),
                                    maintainVoucher.getNo(), goodsInOut.getInQuantity(),
                                    goodsInOut.getOutQuantity(), maintainVoucher.getCustomerId(),
                                    maintainVoucher.getCompanyId(), maintainVoucher.getNote(),
                                    maintainVoucher.getCreatedAt(), maintainVoucher.getCreatedByFullName(),
                                    maintainVoucher.getUpdateAt(), maintainVoucher.getUpdateByFullName(),
                                    factoryId).setGoodsInOutType(goodsInOutRequest.getType()));
                }


            }
        }
        
        MaintainVoucherDto maintainVoucherDto = CommonMapper.map(maintainVoucher, MaintainVoucherDto.class);
        maintainVoucherDto.setAccNo(accountVoucherCode.getAccNo());
        return maintainVoucherDto;
    }

    @Override
    public MaintainVoucherDto updateMaintainVoucher(String id, UpdateMaintainVoucherRequest request) {
        Optional<MaintainVoucher> optional = maintainVoucherRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Phiếu xuất nhập bảo dưỡng không tồn tại");
        }
        MaintainVoucher maintainVoucher = optional.get();
        Instant now = Instant.now();
        
        if(!request.getVoucherAt().equals(maintainVoucher.getVoucherAt())) {
            AccountVoucherCode accountVoucherCode = voucherUtils.genereateVoucherNOWithVoucherAtAndVoucherEnum(maintainVoucher.getFactoryId(), VoucherEnum.getEnumFromCode(maintainVoucher.getVoucherCode()),
            		request.getVoucherAt());
            
//            maintainVoucher.setNo(no);
            
            AccountVoucherCode oldAccountVoucherCode = accountVoucherCodeRepository.findById(id).orElse(null);
            
            if(oldAccountVoucherCode != null) {
                String updateBy = oldAccountVoucherCode.getUpdateBy();
                Instant updateAt = oldAccountVoucherCode.getUpdateAt();

                //tạo account voucher code mới
                voucherUtils.createAccountVoucherCode(oldAccountVoucherCode,UUID.randomUUID().toString().replaceAll("-", ""), oldAccountVoucherCode.getOldVoucherId()
                         ,false , updateAt, updateBy);
                //update lại account voucher code cũ
                voucherUtils.createAccountVoucherCode(accountVoucherCode, oldAccountVoucherCode.getVoucherId(), oldAccountVoucherCode.getOldVoucherId() ,true , now, request.getUpdateByFullName());

            } 
            
        }
        
        CommonMapper.copyPropertiesIgnoreNull(request, maintainVoucher);
        maintainVoucher.setUpdateAt(now);
        maintainVoucher.setTruckDriverId(request.getTruckDriverId());
        maintainVoucher.setSalesmanId(request.getSalesmanId());
        maintainVoucherRepository.save(maintainVoucher);
        List<String> gio = goodsInOutRepository.findAllId(id);
        if(gio != null) {
            for (String gioId : gio) {
                goodsInOutMaintailDetailRepository.deleteById(gioId);
            }
        }
        cylinderDebtRepository.deleteByVoucherId(id);
        goodsInOutRepository.deleteByVoucherId(id);
        List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();
        if (!goodsInOutRequests.isEmpty()) {
            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                GoodsInOut goodsInOut = new GoodsInOut();
                GoodsInOutMaintainDetail goodsInOutMaintainDetail = CommonMapper.map(goodsInOutRequest.getGoodsInOutMaintainDetail(), GoodsInOutMaintainDetail.class);
                CommonMapper.copyPropertiesIgnoreNull(goodsInOutRequest, goodsInOut);
                goodsInOut.setVoucherNo(maintainVoucher.getNo());
                goodsInOut.setVoucherCode(maintainVoucher.getVoucherCode());
                goodsInOut.setVoucherId(maintainVoucher.getId());
                goodsInOut.setCompanyId(maintainVoucher.getCompanyId());
                goodsInOut.setCustomerId(maintainVoucher.getCustomerId());
                goodsInOut.setVoucherAt(maintainVoucher.getVoucherAt());
                goodsInOut.setIsMainProduct(true);
                goodsInOut.setInFactory(request.getInFactory());
                goodsInOut.setFactoryId(maintainVoucher.getFactoryId());
                goodsInOut.setTruckDriverId(maintainVoucher.getTruckDriverId());
                goodsInOut.setTruckLicensePlateNumber(maintainVoucher.getTruckLicensePlateNumber());
                goodsInOut.setSalesmanId(maintainVoucher.getSalesmanId());
                goodsInOut.setStt(goodsInOutRequests.indexOf(goodsInOutRequest));
                goodsInOut.setUpdateAt(now);
                goodsInOut.setUpdateByFullName(request.getUpdateByFullName());
                goodsInOut.setCreateAt(maintainVoucher.getCreatedAt());
                goodsInOut.setCreateByFullName(maintainVoucher.getCreatedByFullName());
                goodsInOutRepository.save(goodsInOut);
                goodsInOutMaintainDetail.setId(goodsInOut.getId());
                goodsInOutMaintailDetailRepository.save(goodsInOutMaintainDetail);
                if(goodsInOut.getProductType().equals(ProductTypeEnum.VO.name())) {
                    cylinderDebtService
                            .createCylinderDebt(new CylinderDebt(maintainVoucher.getId(), goodsInOut.getProductId(),
                                    maintainVoucher.getVoucherCode(), maintainVoucher.getVoucherAt(),
                                    maintainVoucher.getNo(), goodsInOut.getInQuantity(),
                                    goodsInOut.getOutQuantity(), maintainVoucher.getCustomerId(),
                                    maintainVoucher.getCompanyId(), maintainVoucher.getNote(),
                                    maintainVoucher.getCreatedAt(), maintainVoucher.getCreatedByFullName(),
                                    maintainVoucher.getUpdateAt(), maintainVoucher.getUpdateByFullName(),
                                    maintainVoucher.getFactoryId()).setGoodsInOutType(goodsInOutRequest.getType()));
                }
            }
        }

        return CommonMapper.map(maintainVoucher, MaintainVoucherDto.class);
    }

    @Override
    public void deleteMaintainVoucher(String id) {
    	Optional<MaintainVoucher> optional = maintainVoucherRepository.findById(id);
    	
        if (!optional.isPresent()) {
            throw new NotFoundException("Phiếu xuất nhập bảo dưỡng không tồn tại");
        }
        MaintainVoucher maintainVoucher = new MaintainVoucher();
        maintainVoucher.setId(id);
        maintainVoucher.setFactoryId(optional.get().getFactoryId());
        maintainVoucher.setVoucherCode(optional.get().getVoucherCode());
        maintainVoucher.setVoucherAt(optional.get().getVoucherAt());
        maintainVoucher.setTotalGoods(BigDecimal.ZERO);
        maintainVoucherRepository.save(maintainVoucher);
        accountVoucherCodeRepository.deleteByVoucherId(id);
        List<String> gio = goodsInOutRepository.findAllId(id);
        if(gio != null) {
            for (String gioId : gio) {
                goodsInOutMaintailDetailRepository.deleteById(gioId);
            }
        }
        cylinderDebtRepository.deleteByVoucherId(id);
        goodsInOutRepository.deleteByVoucherId(id);
    }
}
