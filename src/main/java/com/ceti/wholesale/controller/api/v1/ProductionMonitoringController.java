package com.ceti.wholesale.controller.api.v1;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.common.util.PageableProcess;
import com.ceti.wholesale.controller.api.request.CreateProductionMonitoringRequest;
import com.ceti.wholesale.controller.api.request.v2.UpdateProductionMonitoringRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ProductionMonitoringDto;
import com.ceti.wholesale.dto.plan.ProductionMonitoringStatisticDataDto;
import com.ceti.wholesale.dto.plan.ProductionMonitoringStatisticDto;
import com.ceti.wholesale.mapper.ProductionMonitoringMapper;
import com.ceti.wholesale.service.ProductionMonitoringService;

@RestController
@RequestMapping("/v1")
@Validated
public class ProductionMonitoringController {
	
	@Autowired
	private ProductionMonitoringService productionMonitoringService;
	
	@Autowired
	private ProductionMonitoringMapper productionMonitoringMapper;
	
    @PostMapping(value = "/production-monitorings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductionMonitoringDto>> create(
    @RequestBody CreateProductionMonitoringRequest request,
    @RequestHeader(name = "user_id") String userId){
          List<ProductionMonitoringDto> result =
        		  productionMonitoringService.create(request, userId);

          ResponseBodyDto<ProductionMonitoringDto> response = new ResponseBodyDto<>(result, ResponseCodeEnum.R_201,"CREATED",
        		  result.size());

          return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping(value = "/production-monitorings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductionMonitoringDto>> getList(
    @RequestParam(value = "customer_id",required = false) String customerId,
    @RequestParam(value = "voucher_at_from",required = false) Long voucherAtFrom,
    @RequestParam(value = "voucher_at_to",required = false) Long voucherAtTo,
    Pageable pageable){
    	String page = PageableProcess.PageToSqlQuery(pageable, "production_monitoring");
    	List<ProductionMonitoringDto> data = productionMonitoringMapper.getList(customerId, voucherAtFrom == null ? null : Instant.ofEpochSecond(voucherAtFrom),
    			voucherAtTo == null ? null : Instant.ofEpochSecond(voucherAtTo), page);

    	long totalItems = productionMonitoringMapper.countList(customerId, voucherAtFrom == null ? null : Instant.ofEpochSecond(voucherAtFrom),
    			voucherAtTo == null ? null : Instant.ofEpochSecond(voucherAtTo));
		ResponseBodyDto<ProductionMonitoringDto> res = new ResponseBodyDto<>(data, ResponseCodeEnum.R_200,
				"OK", totalItems);
		return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    
    
    @PatchMapping(value = "/production-monitorings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductionMonitoringDto>> update(
    @RequestBody UpdateProductionMonitoringRequest request,
    @RequestHeader(name = "user_id") String userId){
    	
          ProductionMonitoringDto result = productionMonitoringService.update(request, userId);

          ResponseBodyDto<ProductionMonitoringDto> response = new ResponseBodyDto<>(result, ResponseCodeEnum.R_200,"OK");

          return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    //lam commit ngày 07/12/2021 bỏ do trên front end bỏ 
//    
//    @GetMapping(value = "/production-monitoring-from-wholesale-data", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ResponseBodyDto<ProductionMonitoringDto>> getListFromWholesaleData(
//    @RequestParam(value = "customer_code",required = false) String customerCode,
//    @RequestParam(value = "voucher_at_from",required = false) Long voucherAtFrom,
//    @RequestParam(value = "voucher_at_to",required = false) Long voucherAtTo){
//    	List<ProductionMonitoringDto> data = productionMonitoringMapper.getListFromWholesaleData(customerCode, voucherAtFrom == null ? null : Instant.ofEpochSecond(voucherAtFrom),
//    			voucherAtTo == null ? null : Instant.ofEpochSecond(voucherAtTo));
//		ResponseBodyDto<ProductionMonitoringDto> res = new ResponseBodyDto<>(data, ResponseCodeEnum.R_200,
//				"OK", data.size());
//		return ResponseEntity.status(HttpStatus.OK).body(res);
//    }
    
    @GetMapping(value = "/production-monitoring-statistic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductionMonitoringStatisticDto>> getStatistic(
    @RequestParam(value = "customer_code",required = false) String customerCode,
    @RequestParam(value = "region_id",required = false) String regionId,
    @RequestParam(value = "company_id",required = false) String companyId,
	@RequestParam(value = "owner_company",required = false) String ownerCompany,
	@RequestParam(value = "is_wholesale_customer",required = false) Boolean isWholesaleCustomer,
    @RequestParam(value = "customer_category",required = false) String customerCategory,
    @RequestParam(value = "is_target_production") Boolean isTargetProduction,
    @RequestParam(value = "voucher_at_from") Long voucherAtFrom,
    @RequestParam(value = "voucher_at_to") Long voucherAtTo){
    	List<List<ProductionMonitoringStatisticDataDto>> data = productionMonitoringMapper.getStatistic(customerCode, Instant.ofEpochSecond(voucherAtFrom),
    			Instant.ofEpochSecond(voucherAtTo), regionId, companyId,ownerCompany,isWholesaleCustomer, customerCategory,isTargetProduction);
    	ProductionMonitoringStatisticDto productionMonitoringStatisticDto =new ProductionMonitoringStatisticDto(data.get(0), data.get(1));
		ResponseBodyDto<ProductionMonitoringStatisticDto> res = new ResponseBodyDto<>(productionMonitoringStatisticDto, ResponseCodeEnum.R_200, "OK");
		return ResponseEntity.status(HttpStatus.OK).body(res);
    }

	@DeleteMapping(value = "/production-monitorings", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseBodyDto<String>> delete(
			@RequestParam(value = "customer_id", required = true) String customerId,
			@RequestParam(value = "voucher_at", required = true) Long voucherAt) {
		Instant voucherAts = voucherAt == null ? null : Instant.ofEpochSecond(voucherAt);
		productionMonitoringService.delete(customerId, voucherAts);
		ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
				ResponseCodeEnum.R_200, "Deleted");
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}
    
}
