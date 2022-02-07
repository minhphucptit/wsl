
package com.ceti.wholesale.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ceti.wholesale.controller.api.request.CreateMinioDocumentRequest;
import com.ceti.wholesale.controller.api.request.UpdateMinioDocumentRequest;
import com.ceti.wholesale.dto.MinioDocumentDto;

public interface MinioDocumentService {

    MinioDocumentDto create(CreateMinioDocumentRequest request, String userId);

    MinioDocumentDto update(String id, UpdateMinioDocumentRequest request, String userId);

    Page<MinioDocumentDto> findAll(String entityId, Pageable pageable);
    
    void deleteById(String id);
}