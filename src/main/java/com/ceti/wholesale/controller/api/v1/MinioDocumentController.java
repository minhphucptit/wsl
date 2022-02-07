package com.ceti.wholesale.controller.api.v1;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateMinioDocumentRequest;
import com.ceti.wholesale.controller.api.request.UpdateMinioDocumentRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.MinioDocumentDto;
import com.ceti.wholesale.service.MinioDocumentService;

@RestController
@RequestMapping("/v1")
@Validated
public class MinioDocumentController {

    @Autowired
    private MinioDocumentService minioDocumentService;

    @GetMapping(value = "/minio-documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<MinioDocumentDto>> getByEntityId(
            @RequestParam(name = "entity_id") String entityId,
            Pageable pageable
    ){
        Page<MinioDocumentDto> minioDocumentDtoList = minioDocumentService.findAll(entityId, pageable);
        ResponseBodyDto<MinioDocumentDto> res = new ResponseBodyDto<>(pageable, minioDocumentDtoList,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @PostMapping(value = "/minio-documents", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<MinioDocumentDto>> createMinioDocument(
            @Valid @RequestBody CreateMinioDocumentRequest request,
            @RequestHeader(name = "user_id") String userId) {

        MinioDocumentDto minioDocumentDto = minioDocumentService.create(request, userId);
        ResponseBodyDto<MinioDocumentDto> res = new ResponseBodyDto<>(minioDocumentDto,
                ResponseCodeEnum.R_201, "Created");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PatchMapping(value = "/minio-documents/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<MinioDocumentDto>> update(
            @PathVariable(name = "id") String id,
            @Valid @RequestBody UpdateMinioDocumentRequest request,
            @RequestHeader(name = "user_id") String userId) {

        MinioDocumentDto minioDocumentDto = minioDocumentService.update(id, request, userId);
        ResponseBodyDto<MinioDocumentDto> res = new ResponseBodyDto<>(minioDocumentDto,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    
    @DeleteMapping(value = "/minio-documents/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<MinioDocumentDto>> delete(
            @PathVariable(name = "id") String id) {

        minioDocumentService.deleteById(id);
        ResponseBodyDto<MinioDocumentDto> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
