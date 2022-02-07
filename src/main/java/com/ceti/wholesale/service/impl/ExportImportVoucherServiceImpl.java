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
import com.ceti.wholesale.common.enums.GoodsInOutTypeEnum;
import com.ceti.wholesale.common.enums.ProductTypeEnum;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.controller.api.request.CreateExportImportVoucherRequest;
import com.ceti.wholesale.controller.api.request.CreateExportVoucherRequest;
import com.ceti.wholesale.controller.api.request.CreateGoodsInOutRequest;
import com.ceti.wholesale.controller.api.request.CreateImportVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateExportImportVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateExportVoucherRequest;
import com.ceti.wholesale.controller.api.request.UpdateImportVoucherRequest;
import com.ceti.wholesale.dto.CompanyDto;
import com.ceti.wholesale.dto.CustomerDto;
import com.ceti.wholesale.dto.ExportImportVoucherDto;
import com.ceti.wholesale.dto.ExportVoucherDto;
import com.ceti.wholesale.dto.GoodsInOutDto;
import com.ceti.wholesale.dto.ImportVoucherDto;
import com.ceti.wholesale.dto.ProductAccessoryDto;
import com.ceti.wholesale.dto.SalesmanDto;
import com.ceti.wholesale.dto.TruckDriverDto;
import com.ceti.wholesale.dto.TruckDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.Company;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.model.CylinderDebt;
import com.ceti.wholesale.model.ExportVoucher;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.ImportVoucher;
import com.ceti.wholesale.model.Salesman;
import com.ceti.wholesale.model.Truck;
import com.ceti.wholesale.model.TruckDriver;
import com.ceti.wholesale.repository.CylinderDebtRepository;
import com.ceti.wholesale.repository.ExportImportVoucherDetailRepository;
import com.ceti.wholesale.repository.ExportVoucherRepository;
import com.ceti.wholesale.repository.GoodsInOutRepository;
import com.ceti.wholesale.repository.ImportVoucherRepository;
import com.ceti.wholesale.service.CylinderDebtService;
import com.ceti.wholesale.service.ExportImportVoucherService;
import com.ceti.wholesale.service.GoodsInOutService;
import com.ceti.wholesale.service.ProductAccessoryService;

@Service
@Transactional
public class ExportImportVoucherServiceImpl implements ExportImportVoucherService {

    @Autowired
    ExportImportVoucherDetailRepository exportImportVoucherDetailRepository;

    @Autowired
    ImportVoucherRepository importVoucherRepository;

    @Autowired
    ExportVoucherRepository exportVoucherRepository;

    @Autowired
    private GoodsInOutRepository goodsInOutRepository;

    @Autowired
    private CylinderDebtService cylinderDebtService;

    @Autowired
    private ProductAccessoryService productAccessoryService;

    @Autowired
    private GoodsInOutService goodsInOutService;

    @Autowired
    private CylinderDebtRepository cylinderDebtRepository;

    @Autowired
    private  VoucherUtils voucherUtils;

    @Override
    public ExportImportVoucherDto getDetail(String importVoucherId, String exportVoucherId) {
        ImportVoucherDto importVoucherDto = null;
        ExportVoucherDto exportVoucherDto = null;
        if (importVoucherId != null) {
            Object[] importVoucherArray = exportImportVoucherDetailRepository.getDetailImportVoucher(importVoucherId);
            if (importVoucherArray[0] != null) {
                ImportVoucher importVoucher = (ImportVoucher) importVoucherArray[0];
                importVoucherDto = CommonMapper.map(importVoucher, ImportVoucherDto.class);
                List<GoodsInOutDto> listGoodsInOutImport =
                        goodsInOutService.getGoodsInOutByVoucherId(importVoucherDto.getId(), true);
                importVoucherDto.setGoodsInOut(listGoodsInOutImport);

                if (importVoucherArray[1] != null) {
                    Company company = (Company) importVoucherArray[1];
                    CompanyDto companyDto = CommonMapper.map(company, CompanyDto.class);
                    importVoucherDto.setCompany(companyDto);
                }

                if (importVoucherArray[2] != null) {
                    Customer customer = (Customer) importVoucherArray[2];
                    CustomerDto customerDto = CommonMapper.map(customer, CustomerDto.class);
                    importVoucherDto.setCustomer(customerDto);
                }

                if (importVoucherArray[3] != null) {
                    TruckDriver truckDriver = (TruckDriver) importVoucherArray[3];
                    TruckDriverDto truckDriverDto = CommonMapper.map(truckDriver, TruckDriverDto.class);
                    importVoucherDto.setTruckDriver(truckDriverDto);
                }

                if (importVoucherArray[4] != null) {
                    Truck truck = (Truck) importVoucherArray[4];
                    TruckDto truckDto = CommonMapper.map(truck, TruckDto.class);
                    importVoucherDto.setTruck(truckDto);
                }

                if (importVoucherArray[5] != null) {
                    Salesman salesman = (Salesman) importVoucherArray[5];
                    SalesmanDto salesmanDto = CommonMapper.map(salesman, SalesmanDto.class);
                    importVoucherDto.setSalesman(salesmanDto);
                }
            }
        }
        if (exportVoucherId != null) {
            Object[] exportVoucherArray = exportImportVoucherDetailRepository.getDetailExportVoucher(exportVoucherId);
            if (exportVoucherArray[0] != null) {
                ExportVoucher exportVoucher = (ExportVoucher) exportVoucherArray[0];
                exportVoucherDto = CommonMapper.map(exportVoucher, ExportVoucherDto.class);
                List<GoodsInOutDto> listGoodsInOutExport =
                        goodsInOutService.getGoodsInOutByVoucherId(exportVoucherDto.getId(), true);
                exportVoucherDto.setGoodsInOut(listGoodsInOutExport);
                if (exportVoucherArray[1] != null) {
                    Company company = (Company) exportVoucherArray[1];
                    CompanyDto companyDto = CommonMapper.map(company, CompanyDto.class);
                    exportVoucherDto.setCompany(companyDto);
                }
                if (exportVoucherArray[2] != null) {
                    Customer customer = (Customer) exportVoucherArray[2];
                    CustomerDto customerDto = CommonMapper.map(customer, CustomerDto.class);
                    exportVoucherDto.setCustomer(customerDto);
                }
                if (exportVoucherArray[3] != null) {
                    TruckDriver truckDriver = (TruckDriver) exportVoucherArray[3];
                    TruckDriverDto truckDriverDto = CommonMapper.map(truckDriver, TruckDriverDto.class);
                    exportVoucherDto.setTruckDriver(truckDriverDto);
                }
                if (exportVoucherArray[4] != null) {
                    Truck truck = (Truck) exportVoucherArray[4];
                    TruckDto truckDto = CommonMapper.map(truck, TruckDto.class);
                    exportVoucherDto.setTruck(truckDto);
                }
                if (exportVoucherArray[5] != null) {
                    Salesman salesman = (Salesman) exportVoucherArray[5];
                    SalesmanDto salesmanDto = CommonMapper.map(salesman, SalesmanDto.class);
                    exportVoucherDto.setSalesman(salesmanDto);
                }
            }
        }
        return new ExportImportVoucherDto(importVoucherDto, exportVoucherDto);
    }

    @Override
    public Page<ExportImportVoucherDto> getAllByCondition(MultiValueMap<String, String> where, Pageable pageable,String factoryId) {
        ResultPage<Object[]> page = exportImportVoucherDetailRepository.findAllWithFilter(pageable, where, factoryId);
        List<ExportImportVoucherDto> exportImportVoucherDtos = new ArrayList<>();
        for (Object[] object : page.getPageList()) {
            ExportImportVoucherDto exportImportVoucherDto = new ExportImportVoucherDto();
            ImportVoucherDto importVoucherDto = new ImportVoucherDto();
            ExportVoucherDto exportVoucherDto = new ExportVoucherDto();

            if(object[0] != null){
                ImportVoucher importVoucher = (ImportVoucher) object[0];
                importVoucherDto = CommonMapper.map(importVoucher, ImportVoucherDto.class);
                exportImportVoucherDto.setImportDto(importVoucherDto);
            }
            if (object[1] != null) {
                ExportVoucher exportVoucher = (ExportVoucher) object[1];
                exportVoucherDto = CommonMapper.map(exportVoucher, ExportVoucherDto.class);
                exportImportVoucherDto.setExportDto(exportVoucherDto);
            }
            if (object[2] != null) {
                Customer customer = (Customer) object[2];
                CustomerDto customerDto = CommonMapper.map(customer, CustomerDto.class);
                exportVoucherDto.setCustomer(customerDto);
            }

            exportImportVoucherDtos.add(exportImportVoucherDto);
        }
        return new PageImpl<>(exportImportVoucherDtos, pageable, page.getTotalItems());
    }

    @Override
    public ExportImportVoucherDto createImportExportVoucher(CreateExportImportVoucherRequest createExportImportVoucherRequest, String factoryId) {
        ExportImportVoucherDto exportImportVoucherDto = new ExportImportVoucherDto();
        ImportVoucher importVoucher = new ImportVoucher();
        ExportVoucher exportVoucher = new ExportVoucher();

        if (createExportImportVoucherRequest.getCreateImportVoucher()) {
            CreateImportVoucherRequest createImportVoucherRequest = createExportImportVoucherRequest.getCreateImportVoucherRequest();
            if (createImportVoucherRequest.getGoodsInOut().isEmpty()) {
                throw new BadRequestException("Phải có hàng mới tạo được phiếu nhập");
            }
            Instant now = Instant.now();
            String noImport = voucherUtils.genereateVoucherNO(factoryId,ConstantText.VOUCHER_CODE_PHIEU_NHAP_HANG,
                    i -> importVoucherRepository.countImportVoucherInDay(i));
            importVoucher = CommonMapper.map(createImportVoucherRequest, ImportVoucher.class);
            importVoucher.setCreatedAt(now);
            importVoucher.setUpdateAt(now);
            importVoucher.setId(noImport);
            importVoucher.setUpdateByFullName(importVoucher.getCreatedByFullName());
            importVoucher.setNo(noImport);
            importVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_PHIEU_NHAP_HANG);
            importVoucher.setFactoryId(factoryId);
            createImportGoodsInOut(factoryId, importVoucher, createImportVoucherRequest);
            importVoucherRepository.save(importVoucher);
            exportImportVoucherDto.setImportDto(CommonMapper.map(importVoucher, ImportVoucherDto.class));
        }

        if (createExportImportVoucherRequest.getCreateExportVoucher()) {
            CreateExportVoucherRequest createExportVoucherRequest =
                    createExportImportVoucherRequest.getCreateExportVoucherRequest();
            if (createExportVoucherRequest.getGoodsInOut().isEmpty()) {
                throw new BadRequestException("Phải có hàng mới tạo được phiếu xuất");
            }
            Instant now = Instant.now();
            String noExport = voucherUtils.genereateVoucherNO(factoryId, ConstantText.VOUCHER_CODE_PHIEU_XUAT_HANG,
                    i -> exportVoucherRepository.countExportVoucherInDay(i));
            exportVoucher = CommonMapper.map(createExportVoucherRequest, ExportVoucher.class);
            exportVoucher.setCreatedAt(now);
            exportVoucher.setUpdateAt(now);
            exportVoucher.setId(noExport + "-" + ConstantText.VOUCHER_CODE_PHIEU_XUAT_HANG + "-" + factoryId);
            exportVoucher.setUpdateByFullName(exportVoucher.getCreatedByFullName());
            exportVoucher.setNo(noExport);
            exportVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_PHIEU_XUAT_HANG);
            exportVoucher.setFactoryId(factoryId);
            // save id của phiếu xuất cho phiếu nhập nếu có khởi tạo phiếu nhập
            if(createExportImportVoucherRequest.getCreateImportVoucher()){
                importVoucher.setExportVoucherId(exportVoucher.getId());
                importVoucherRepository.save(importVoucher);
            }
            exportVoucherRepository.save(exportVoucher);
            createExportGoodsInOut(factoryId, exportVoucher, createExportVoucherRequest);
            exportImportVoucherDto.setExportDto(CommonMapper.map(exportVoucher, ExportVoucherDto.class));
        }
        return exportImportVoucherDto;
    }

    @Override
    public void deleteExportImportVoucher(String importVoucherId, String exportVoucherId) {
//        if (!importVoucherRepository.existsById(importVoucherId)) {
//            throw new NotFoundException("Phiếu nhập không tồn tại");
//        }
//        if (!exportVoucherRepository.existsById(exportVoucherId)) {
//            throw new NotFoundException("Phiếu xuất không tồn tại");
//        }
        importVoucherRepository.deleteById(importVoucherId);
        exportVoucherRepository.deleteById(exportVoucherId);
        List<String> ids = new ArrayList<>();
        ids.add(importVoucherId);
        ids.add(exportVoucherId);
        goodsInOutRepository.deleteByVoucherIdIn(ids);
        cylinderDebtRepository.deleteByVoucherIdIn(ids);
    }

    @Override
    public ExportImportVoucherDto updateImportExportVoucher(String factoryId, UpdateExportImportVoucherRequest updateExportImportVoucherRequest) {
        ExportImportVoucherDto exportImportVoucherDto = new ExportImportVoucherDto();
        ExportVoucher exportVoucherUpdate = new ExportVoucher();
        ImportVoucher importVoucher = new ImportVoucher();
        ExportVoucher exportVoucher = new ExportVoucher();
        String idImportVoucher = null;
        String idExportVoucher;
        Instant now = Instant.now();
        // check bên nhập
        if(updateExportImportVoucherRequest.getUpdateImportVoucher()){
            idImportVoucher = updateExportImportVoucherRequest.getUpdateImportVoucherRequest().getId();
            // lúc đầu có xuất, k nhập
            if (idImportVoucher == null) {
                CreateImportVoucherRequest createImportVoucherRequest = updateExportImportVoucherRequest.getCreateImportVoucherRequest();
                if (createImportVoucherRequest.getGoodsInOut().isEmpty()) {
                    throw new BadRequestException("Phải có hàng mới tạo được phiếu nhập");
                }
                String noImport = voucherUtils.genereateVoucherNO(factoryId,ConstantText.VOUCHER_CODE_PHIEU_NHAP_HANG,
                        i -> importVoucherRepository.countImportVoucherInDay(i));
                importVoucher = CommonMapper.map(createImportVoucherRequest, ImportVoucher.class);
                importVoucher.setCreatedAt(now);
                importVoucher.setUpdateAt(now);
                importVoucher.setId(noImport + "-" + ConstantText.VOUCHER_CODE_PHIEU_NHAP_HANG + "-" + factoryId);
                importVoucher.setUpdateByFullName(importVoucher.getCreatedByFullName());
                importVoucher.setNo(noImport);
                importVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_PHIEU_NHAP_HANG);
                importVoucher.setFactoryId(factoryId);
                importVoucher.setExportVoucherId(updateExportImportVoucherRequest.getUpdateExportVoucherRequest().getId());
                importVoucherRepository.save(importVoucher);
                createImportGoodsInOut(factoryId, importVoucher, createImportVoucherRequest);
                exportImportVoucherDto.setImportDto(CommonMapper.map(importVoucher, ImportVoucherDto.class));
            // lúc đầu có nhập
            }
            else{
                Optional<ImportVoucher> optional = importVoucherRepository.findById(idImportVoucher);
                importVoucher = optional.get();
                UpdateImportVoucherRequest request = updateExportImportVoucherRequest.getUpdateImportVoucherRequest();
                CommonMapper.copyPropertiesIgnoreNull(request, importVoucher);
                importVoucher.setUpdateAt(now);
                importVoucher.setTruckDriverId(updateExportImportVoucherRequest.getUpdateImportVoucherRequest().getTruckDriverId());
                importVoucher.setSalesmanId(updateExportImportVoucherRequest.getUpdateImportVoucherRequest().getSalesmanId());
                importVoucherRepository.save(importVoucher);
                updateImportGoodsInOut(importVoucher, request);
                exportImportVoucherDto.setImportDto(CommonMapper.map(importVoucher, ImportVoucherDto.class));
            }
        }
        //check bên xuất
        if(updateExportImportVoucherRequest.getUpdateExportVoucher()){
            idExportVoucher = updateExportImportVoucherRequest.getUpdateExportVoucherRequest().getId();
            // lúc đầu có nhập không xuất
            if (idExportVoucher == null) {
                CreateExportVoucherRequest createExportVoucherRequest =
                        updateExportImportVoucherRequest.getCreateExportVoucherRequest();
                if (createExportVoucherRequest.getGoodsInOut().isEmpty()) {
                    throw new BadRequestException("Phải có hàng mới tạo được phiếu xuất");
                }
                String noExport = voucherUtils.genereateVoucherNO(factoryId, ConstantText.VOUCHER_CODE_PHIEU_XUAT_HANG,
                        i -> exportVoucherRepository.countExportVoucherInDay(i));
                exportVoucher = CommonMapper.map(createExportVoucherRequest, ExportVoucher.class);
                exportVoucher.setCreatedAt(now);
                exportVoucher.setUpdateAt(now);
                exportVoucher.setId(noExport + "-" + ConstantText.VOUCHER_CODE_PHIEU_XUAT_HANG + "-" + factoryId);
                exportVoucher.setUpdateByFullName(exportVoucher.getCreatedByFullName());
                exportVoucher.setNo(noExport);
                exportVoucher.setVoucherCode(ConstantText.VOUCHER_CODE_PHIEU_XUAT_HANG);
                exportVoucher.setFactoryId(factoryId);

                // khi khởi tạo phiếu xuất mới thì sẽ save id của phiếu xuất đó vào phiếu nhập cũ
                if(importVoucher.getId()!=null) {
                importVoucher.setExportVoucherId(exportVoucher.getId());
                importVoucherRepository.save(importVoucher);
                }
                exportVoucherRepository.save(exportVoucher);
                createExportGoodsInOut(factoryId, exportVoucher, createExportVoucherRequest);
                exportImportVoucherDto.setExportDto(CommonMapper.map(exportVoucher, ExportVoucherDto.class));
            }
            // lúc đầu không xuất
            else{
                Optional<ExportVoucher> optional = exportVoucherRepository.findById(idExportVoucher);
                exportVoucherUpdate = optional.get();
                UpdateExportVoucherRequest request = updateExportImportVoucherRequest.getUpdateExportVoucherRequest();
                CommonMapper.copyPropertiesIgnoreNull(request, exportVoucherUpdate);
                exportVoucherUpdate.setUpdateAt(now);
                exportVoucherUpdate.setTruckDriverId(updateExportImportVoucherRequest.getUpdateExportVoucherRequest().getTruckDriverId());
                exportVoucherUpdate.setSalesmanId(updateExportImportVoucherRequest.getUpdateExportVoucherRequest().getSalesmanId());
                updateExportGoodsInOut(exportVoucherUpdate, request);
                exportImportVoucherDto.setExportDto(CommonMapper.map(exportVoucherUpdate, ExportVoucherDto.class));
            }
        }
        return exportImportVoucherDto;
    }

    private void createImportGoodsInOut(String factoryId, ImportVoucher importVoucher,
                                        CreateImportVoucherRequest createImportVoucherRequest) {
        List<GoodsInOutDto> goodsInOutDto = new ArrayList<>();
        if (!createImportVoucherRequest.getGoodsInOut().isEmpty()) {
            List<CreateGoodsInOutRequest> goodsInOutRequests = createImportVoucherRequest.getGoodsInOut();

            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                GoodsInOut goodsInOut = new GoodsInOut();

                CommonMapper.copyPropertiesIgnoreNull(goodsInOutRequest, goodsInOut);
                goodsInOut.setCompanyId(importVoucher.getCompanyId());
                goodsInOut.setCustomerId(importVoucher.getCustomerId());
                goodsInOut.setVoucherAt(importVoucher.getVoucherAt());
                goodsInOut.setVoucherNo(importVoucher.getNo());
                goodsInOut.setVoucherCode(importVoucher.getVoucherCode());
                goodsInOut.setVoucherId(importVoucher.getId());
                goodsInOut.setIsMainProduct(true);
                goodsInOut.setFactoryId(factoryId);
                goodsInOut.setTruckDriverId(importVoucher.getTruckDriverId());
                goodsInOut.setTruckLicensePlateNumber(importVoucher.getTruckLicensePlateNumber());
                goodsInOut.setSalesmanId(importVoucher.getSalesmanId());
                goodsInOut.setStt(goodsInOutRequests.indexOf(goodsInOutRequest));
                
                goodsInOutRepository.save(goodsInOut);

                // nếu sản phẩm là gas thì xuất cả cấu hình, ghi nhận công nợ vỏ
                if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.GAS.name())) {
                    List<ProductAccessoryDto> list = productAccessoryService.getByMainProductIdAndFactoryId(goodsInOutRequest.getProductId(), factoryId);
                    for (ProductAccessoryDto pad : list) {
                        GoodsInOut gio = new GoodsInOut();
                        gio.setVoucherNo(importVoucher.getNo());
                        gio.setVoucherAt(importVoucher.getVoucherAt());
                        gio.setVoucherCode(importVoucher.getVoucherCode());
                        gio.setVoucherId(importVoucher.getId());
                        gio.setCustomerId(importVoucher.getCustomerId());
                        gio.setCompanyId(importVoucher.getCompanyId());
                        gio.setType(goodsInOutRequest.getType());
                        gio.setProductId(pad.getSubProductId());
                        gio.setProductName(pad.getSubProductName());
                        gio.setProductType((pad.getSubProductType()));
                        gio.setInQuantity(goodsInOutRequest.getInQuantity().multiply(pad.getSubProductQuantity()));
                        gio.setIsMainProduct(false);
                        gio.setFactoryId(factoryId);
                        gio.setTruckDriverId(importVoucher.getTruckDriverId());
                        gio.setTruckLicensePlateNumber(importVoucher.getTruckLicensePlateNumber());
                        gio.setSalesmanId(importVoucher.getSalesmanId());
                        //20/10/2021 Nhan: bỏ xuất nhập cấu hình trong phiếu xuất nhập
//                        goodsInOutRepository.save(gio);
                        if (gio.getProductType().equals(ProductTypeEnum.VO.name())) {
                            cylinderDebtService
                                    .createCylinderDebt(new CylinderDebt(importVoucher.getId(), gio.getProductId(),
                                            importVoucher.getVoucherCode(), importVoucher.getVoucherAt(),
                                            importVoucher.getNo(),
                                            gio.getInQuantity(), BigDecimal.valueOf(0),
                                            importVoucher.getCustomerId(), importVoucher.getCompanyId(),
                                            importVoucher.getNote(), importVoucher.getCreatedAt(),
                                            importVoucher.getCreatedByFullName(), importVoucher.getUpdateAt(),
                                            importVoucher.getUpdateByFullName(), factoryId));
                        }
                    }
                }
                if (goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.NDVO.name())
                        && goodsInOutRequest.getProductType().equals(ProductTypeEnum.VO.name())) {
                    cylinderDebtService
                            .createCylinderDebt(new CylinderDebt(importVoucher.getId(), goodsInOutRequest.getProductId(),
                                    importVoucher.getVoucherCode(), importVoucher.getVoucherAt(),
                                    importVoucher.getNo(),
                                    goodsInOutRequest.getInQuantity(), BigDecimal.valueOf(0),
                                    importVoucher.getCustomerId(), importVoucher.getCompanyId(),
                                    importVoucher.getNote(), importVoucher.getCreatedAt(),
                                    importVoucher.getCreatedByFullName(), importVoucher.getUpdateAt(),
                                    importVoucher.getUpdateByFullName(), factoryId).setGoodsInOutType(goodsInOutRequest.getType()));
                }
                goodsInOutDto.add(CommonMapper.map(goodsInOut, GoodsInOutDto.class));
            }
        }
    }

    private void createExportGoodsInOut(String factoryId, ExportVoucher exportVoucher,
                                        CreateExportVoucherRequest createExportVoucherRequest){
        if (!createExportVoucherRequest.getGoodsInOut().isEmpty()) {
            List<CreateGoodsInOutRequest> goodsInOutRequests = createExportVoucherRequest.getGoodsInOut();
            List<GoodsInOutDto> goodsInOutDto = new ArrayList<>();
            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                GoodsInOut goodsInOut = new GoodsInOut();
                CommonMapper.copyPropertiesIgnoreNull(goodsInOutRequest, goodsInOut);

                goodsInOut.setVoucherNo(exportVoucher.getNo());
                goodsInOut.setVoucherCode(exportVoucher.getVoucherCode());
                goodsInOut.setVoucherId(exportVoucher.getId());
                goodsInOut.setCompanyId(exportVoucher.getCompanyId());
                goodsInOut.setCustomerId(exportVoucher.getCustomerId());
                goodsInOut.setVoucherAt(exportVoucher.getVoucherAt());
                goodsInOut.setIsMainProduct(true);
                goodsInOut.setFactoryId(factoryId);
                goodsInOut.setTruckDriverId(exportVoucher.getTruckDriverId());
                goodsInOut.setTruckLicensePlateNumber(exportVoucher.getTruckLicensePlateNumber());
                goodsInOut.setSalesmanId(exportVoucher.getSalesmanId());
                goodsInOut.setStt(goodsInOutRequests.indexOf(goodsInOutRequest));
                
                goodsInOutRepository.save(goodsInOut);

                // nếu sản phẩm là gas thì xuất cả cấu hình, ghi nhận công nợ vỏ
                if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.GAS.name())) {
                    List<ProductAccessoryDto> list = productAccessoryService.getByMainProductIdAndFactoryId(goodsInOutRequest.getProductId(), factoryId);
                    for (ProductAccessoryDto pad : list) {
                        GoodsInOut gio = new GoodsInOut();
                        gio.setVoucherNo(exportVoucher.getNo());
                        gio.setVoucherAt(exportVoucher.getVoucherAt());
                        gio.setVoucherCode(exportVoucher.getVoucherCode());
                        gio.setVoucherId(exportVoucher.getId());
                        gio.setCustomerId(exportVoucher.getCustomerId());
                        gio.setCompanyId(exportVoucher.getCompanyId());
                        gio.setType(goodsInOutRequest.getType());
//                        if (goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.XTTH.name()) && pad.getSubProductId().equals(ProductIdEnum.GBONAPCAO.name())) {
//                            gio.setProductId(ProductIdEnum.GASTHUHOI.name());
//                        } else {
//                            gio.setProductId(pad.getSubProductId());
//                        }
                        gio.setProductId(pad.getSubProductId());
                        gio.setProductName(pad.getSubProductName());
                        gio.setProductType((pad.getSubProductType()));
                        gio.setOutQuantity(goodsInOutRequest.getOutQuantity().multiply(pad.getSubProductQuantity()));
                        gio.setIsMainProduct(false);
                        gio.setFactoryId(factoryId);
                        gio.setTruckDriverId(exportVoucher.getTruckDriverId());
                        gio.setTruckLicensePlateNumber(exportVoucher.getTruckLicensePlateNumber());
                        gio.setSalesmanId(exportVoucher.getSalesmanId());
                        //20/10/2021 Nhan: bỏ xuất nhập cấu hình trong phiếu xuất nhập
//                        goodsInOutRepository.save(gio);
                        if (gio.getProductType().equals(ProductTypeEnum.VO.name())) {
                            cylinderDebtService
                                    .createCylinderDebt(new CylinderDebt(exportVoucher.getId(), gio.getProductId(),
                                            exportVoucher.getVoucherCode(), exportVoucher.getVoucherAt(),
                                            exportVoucher.getNo(), BigDecimal.valueOf(0),
                                            gio.getOutQuantity(), exportVoucher.getCustomerId(),
                                            exportVoucher.getCompanyId(), exportVoucher.getNote(),
                                            exportVoucher.getCreatedAt(), exportVoucher.getCreatedByFullName(),
                                            exportVoucher.getUpdateAt(), exportVoucher.getUpdateByFullName(),
                                            factoryId).setGoodsInOutType(goodsInOutRequest.getType()));
                        }
                    }
                }
                if (goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.XDVO.name())
                        || goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.XTVO.name())
                        && goodsInOutRequest.getProductType().equals(ProductTypeEnum.VO.name())
                        && !goodsInOutRequest.getOutQuantity().equals(BigDecimal.valueOf(0))) {
                    cylinderDebtService
                            .createCylinderDebt(new CylinderDebt(exportVoucher.getId(), goodsInOutRequest.getProductId(),
                                    exportVoucher.getVoucherCode(), exportVoucher.getVoucherAt(),
                                    exportVoucher.getNo(), BigDecimal.valueOf(0),
                                    goodsInOutRequest.getOutQuantity(), exportVoucher.getCustomerId(),
                                    exportVoucher.getCompanyId(), exportVoucher.getNote(),
                                    exportVoucher.getCreatedAt(), exportVoucher.getCreatedByFullName(),
                                    exportVoucher.getUpdateAt(), exportVoucher.getUpdateByFullName(),
                                    factoryId).setGoodsInOutType(goodsInOutRequest.getType()));
                }
                goodsInOutDto.add(CommonMapper.map(goodsInOut, GoodsInOutDto.class));
            }
        }
    }

    private void updateImportGoodsInOut(ImportVoucher importVoucherUpdate, UpdateImportVoucherRequest request){
        goodsInOutRepository.deleteByVoucherId(importVoucherUpdate.getId());
        cylinderDebtRepository.deleteByVoucherId(importVoucherUpdate.getId());
        List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();
        if (goodsInOutRequests.size() != 0) {
            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                GoodsInOut goodsInOut = CommonMapper.map(goodsInOutRequest, GoodsInOut.class);

                goodsInOut.setCompanyId(importVoucherUpdate.getCompanyId());
                goodsInOut.setCustomerId(importVoucherUpdate.getCustomerId());
                goodsInOut.setVoucherAt(importVoucherUpdate.getVoucherAt());
                goodsInOut.setVoucherNo(importVoucherUpdate.getNo());
                goodsInOut.setVoucherCode(importVoucherUpdate.getVoucherCode());
                goodsInOut.setVoucherId(importVoucherUpdate.getId());
                goodsInOut.setIsMainProduct(true);
                goodsInOut.setFactoryId(importVoucherUpdate.getFactoryId());
                goodsInOut.setTruckDriverId(importVoucherUpdate.getTruckDriverId());
                goodsInOut.setTruckLicensePlateNumber(importVoucherUpdate.getTruckLicensePlateNumber());
                goodsInOut.setSalesmanId(importVoucherUpdate.getSalesmanId());
                goodsInOut.setStt(goodsInOutRequests.indexOf(goodsInOutRequest));

                goodsInOutRepository.save(goodsInOut);

                // insert cau hinh
                if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.GAS.name())) {
                    List<ProductAccessoryDto> list = productAccessoryService.getByMainProductIdAndFactoryId(goodsInOutRequest.getProductId(), importVoucherUpdate.getFactoryId());
                    for (ProductAccessoryDto pad : list) {
                        GoodsInOut gio = new GoodsInOut();
                        gio.setVoucherNo(importVoucherUpdate.getNo());
                        gio.setVoucherAt(importVoucherUpdate.getVoucherAt());
                        gio.setVoucherCode(importVoucherUpdate.getVoucherCode());
                        gio.setVoucherId(importVoucherUpdate.getId());
                        gio.setCustomerId(importVoucherUpdate.getCustomerId());
                        gio.setCompanyId(importVoucherUpdate.getCompanyId());
                        gio.setType(goodsInOutRequest.getType());
                        gio.setProductId(pad.getSubProductId());
                        gio.setProductName(pad.getSubProductName());
                        gio.setProductType((pad.getSubProductType()));
                        gio.setInQuantity(goodsInOutRequest.getInQuantity().multiply(pad.getSubProductQuantity()));
                        gio.setIsMainProduct(false);
                        gio.setFactoryId(importVoucherUpdate.getFactoryId());
                        gio.setTruckDriverId(importVoucherUpdate.getTruckDriverId());
                        gio.setTruckLicensePlateNumber(importVoucherUpdate.getTruckLicensePlateNumber());
                        gio.setSalesmanId(importVoucherUpdate.getSalesmanId());


                        //20/10/2021 Nhan: bỏ xuất nhập cấu hình trong phiếu xuất nhập
//                        goodsInOutRepository.save(gio);
                        if(gio.getProductType().equals(ProductTypeEnum.VO.name())){
                            cylinderDebtService
                                    .createCylinderDebt(new CylinderDebt(importVoucherUpdate.getId(), gio.getProductId(),
                                            importVoucherUpdate.getVoucherCode(), importVoucherUpdate.getVoucherAt(),
                                            importVoucherUpdate.getNo(),
                                            gio.getInQuantity(), BigDecimal.valueOf(0),
                                            importVoucherUpdate.getCustomerId(), importVoucherUpdate.getCompanyId(),
                                            importVoucherUpdate.getNote(), importVoucherUpdate.getCreatedAt(),
                                            importVoucherUpdate.getCreatedByFullName(), importVoucherUpdate.getUpdateAt(),
                                            importVoucherUpdate.getUpdateByFullName(), importVoucherUpdate.getFactoryId()).setGoodsInOutType(goodsInOutRequest.getType()));
                        }
                    }

                }
                if (goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.NDVO.name()) && goodsInOutRequest.getProductType().equals(ProductTypeEnum.VO.name())) {
                    cylinderDebtService
                            .createCylinderDebt(new CylinderDebt(importVoucherUpdate.getId(), goodsInOutRequest.getProductId(),
                                    importVoucherUpdate.getVoucherCode(), importVoucherUpdate.getVoucherAt(),
                                    importVoucherUpdate.getNo(),
                                    goodsInOutRequest.getInQuantity(), BigDecimal.valueOf(0),
                                    importVoucherUpdate.getCustomerId(), importVoucherUpdate.getCompanyId(),
                                    importVoucherUpdate.getNote(), importVoucherUpdate.getCreatedAt(),
                                    importVoucherUpdate.getCreatedByFullName(), importVoucherUpdate.getUpdateAt(),
                                    importVoucherUpdate.getUpdateByFullName(), importVoucherUpdate.getFactoryId()).setGoodsInOutType(goodsInOutRequest.getType()));
                }
            }
        }
    }

    private void updateExportGoodsInOut(ExportVoucher exportVoucherUpdate, UpdateExportVoucherRequest request){
        goodsInOutRepository.deleteByVoucherId(exportVoucherUpdate.getId());
        cylinderDebtRepository.deleteByVoucherId(exportVoucherUpdate.getId());
        List<CreateGoodsInOutRequest> goodsInOutRequests = request.getGoodsInOut();
        if (goodsInOutRequests.size() != 0) {
            for (CreateGoodsInOutRequest goodsInOutRequest : goodsInOutRequests) {
                GoodsInOut goodsInOut = new GoodsInOut();
                CommonMapper.copyPropertiesIgnoreNull(goodsInOutRequest, goodsInOut);
                goodsInOut.setVoucherNo(exportVoucherUpdate.getNo());
                goodsInOut.setVoucherCode(exportVoucherUpdate.getVoucherCode());
                goodsInOut.setVoucherId(exportVoucherUpdate.getId());
                goodsInOut.setCompanyId(exportVoucherUpdate.getCompanyId());
                goodsInOut.setCustomerId(exportVoucherUpdate.getCustomerId());
                goodsInOut.setVoucherAt(exportVoucherUpdate.getVoucherAt());
                goodsInOut.setIsMainProduct(true);
                goodsInOut.setFactoryId(exportVoucherUpdate.getFactoryId());
                goodsInOut.setTruckDriverId(exportVoucherUpdate.getTruckDriverId());
                goodsInOut.setTruckLicensePlateNumber(exportVoucherUpdate.getTruckLicensePlateNumber());
                goodsInOut.setSalesmanId(exportVoucherUpdate.getSalesmanId());
                goodsInOut.setStt(goodsInOutRequests.indexOf(goodsInOutRequest));

                goodsInOutRepository.save(goodsInOut);

                if (goodsInOutRequest.getProductType().equals(ProductTypeEnum.GAS.name())) {
                    List<ProductAccessoryDto> list = productAccessoryService.getByMainProductIdAndFactoryId(goodsInOutRequest.getProductId(), exportVoucherUpdate.getFactoryId());
                    for (ProductAccessoryDto pad : list) {
                        GoodsInOut gio = new GoodsInOut();
                        gio.setVoucherNo(exportVoucherUpdate.getNo());
                        gio.setVoucherAt(exportVoucherUpdate.getVoucherAt());
                        gio.setVoucherCode(exportVoucherUpdate.getVoucherCode());
                        gio.setVoucherId(exportVoucherUpdate.getId());
                        gio.setCustomerId(exportVoucherUpdate.getCustomerId());
                        gio.setCompanyId(exportVoucherUpdate.getCompanyId());
                        gio.setType(goodsInOutRequest.getType());
//                        if (goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.XTTH.name()) && pad.getSubProductId().equals(ProductIdEnum.GBONAPCAO.name())) {
//                            gio.setProductId(ProductIdEnum.GASTHUHOI.name());
//                        } else {
//                            gio.setProductId(pad.getSubProductId());
//                        }
                        gio.setProductId(pad.getSubProductId());
                        gio.setProductName(pad.getSubProductName());
                        gio.setProductType((pad.getSubProductType()));
                        gio.setOutQuantity(goodsInOutRequest.getOutQuantity().multiply(pad.getSubProductQuantity()));
                        gio.setIsMainProduct(false);
                        gio.setFactoryId(exportVoucherUpdate.getFactoryId());
                        gio.setTruckDriverId(exportVoucherUpdate.getTruckDriverId());
                        gio.setTruckLicensePlateNumber(exportVoucherUpdate.getTruckLicensePlateNumber());
                        gio.setSalesmanId(exportVoucherUpdate.getSalesmanId());

                        //20/10/2021 Nhan: bỏ xuất nhập cấu hình trong phiếu xuất nhập
//                        goodsInOutRepository.save(gio);
                        if(gio.getProductType().equals(ProductTypeEnum.VO.name())) {
                            cylinderDebtService
                                    .createCylinderDebt(new CylinderDebt(exportVoucherUpdate.getId(), gio.getProductId(),
                                            exportVoucherUpdate.getVoucherCode(), exportVoucherUpdate.getVoucherAt(),
                                            exportVoucherUpdate.getNo(), BigDecimal.valueOf(0),
                                            gio.getOutQuantity(), exportVoucherUpdate.getCustomerId(),
                                            exportVoucherUpdate.getCompanyId(), exportVoucherUpdate.getNote(),
                                            exportVoucherUpdate.getCreatedAt(), exportVoucherUpdate.getCreatedByFullName(),
                                            exportVoucherUpdate.getUpdateAt(), exportVoucherUpdate.getUpdateByFullName(),
                                            exportVoucherUpdate.getFactoryId()).setGoodsInOutType(goodsInOutRequest.getType()));
                        }
                    }
                }

                if (goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.XDVO.name()) || goodsInOutRequest.getType().equals(GoodsInOutTypeEnum.XTVO.name()) && goodsInOutRequest.getProductType().equals(ProductTypeEnum.VO.name()) && !goodsInOutRequest.getOutQuantity().equals(BigDecimal.valueOf(0))) {
                    cylinderDebtService
                            .createCylinderDebt(new CylinderDebt(exportVoucherUpdate.getId(), goodsInOutRequest.getProductId(),
                                    exportVoucherUpdate.getVoucherCode(), exportVoucherUpdate.getVoucherAt(),
                                    exportVoucherUpdate.getNo(), BigDecimal.valueOf(0),
                                    goodsInOutRequest.getOutQuantity(), exportVoucherUpdate.getCustomerId(),
                                    exportVoucherUpdate.getCompanyId(), exportVoucherUpdate.getNote(),
                                    exportVoucherUpdate.getCreatedAt(), exportVoucherUpdate.getCreatedByFullName(),
                                    exportVoucherUpdate.getUpdateAt(), exportVoucherUpdate.getUpdateByFullName(),
                                    exportVoucherUpdate.getFactoryId()).setGoodsInOutType(goodsInOutRequest.getType()));

                }
            }
        }
    }
}

