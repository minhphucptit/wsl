package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateListCustomerContactRequest;
import com.ceti.wholesale.controller.api.request.CreateListProductAccessoryRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.CustomerContactDto;
import com.ceti.wholesale.dto.ProductAccessoryDto;
import com.ceti.wholesale.model.CustomerContact;
import com.ceti.wholesale.service.CustomerContactService;
import com.ceti.wholesale.service.ProductAccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class CustomerContactController {

    @Autowired
    private CustomerContactService customerContactService;

    // Get list customer contact
    @GetMapping(value = "/customer-contacts/{customer-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerContactDto>> getCustomerContacts(
            @PathVariable("customer-id") String customerId) {
        List<CustomerContactDto> list = customerContactService.getAllByCustomerId(customerId);

        ResponseBodyDto<CustomerContactDto> res = new ResponseBodyDto<>(list, ResponseCodeEnum.R_200, "OK", list.size());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    //update customer contact
    @PatchMapping(value = "/customer-contacts/{customer-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<CustomerContactDto>> updateCustomerContact(
            @Valid @RequestBody CreateListCustomerContactRequest request,
            @PathVariable("customer-id") String customerId) {
        List<CustomerContactDto> list = customerContactService.updateByCustomerId(request, customerId);
        ResponseBodyDto<CustomerContactDto> res = new ResponseBodyDto<>(list, ResponseCodeEnum.R_200, "OK", list.size());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
