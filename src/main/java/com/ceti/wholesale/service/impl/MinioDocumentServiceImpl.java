package com.ceti.wholesale.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateMinioDocumentRequest;
import com.ceti.wholesale.controller.api.request.UpdateMinioDocumentRequest;
import com.ceti.wholesale.dto.MinioDocumentDto;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.MinioDocument;
import com.ceti.wholesale.repository.MinioDocumentRepository;
import com.ceti.wholesale.service.MinioDocumentService;

@Service
public class MinioDocumentServiceImpl implements MinioDocumentService {

    @Autowired
    private MinioDocumentRepository minioDocumentRepository;

    @Override
    public Page<MinioDocumentDto> findAll(String entityId, Pageable pageable) {
        return CommonMapper.toPage(minioDocumentRepository.findAll(entityId, pageable), MinioDocumentDto.class, pageable);
    }

    @Override
    public MinioDocumentDto create(CreateMinioDocumentRequest request, String userId) {

        MinioDocument minioDocument = CommonMapper.map(request, MinioDocument.class);
        minioDocument.setUploadBy(userId);
        return CommonMapper.map(minioDocumentRepository.save(minioDocument), MinioDocumentDto.class);
    }

    @Override
    public MinioDocumentDto update(String id, UpdateMinioDocumentRequest request, String userId) {

        Optional<MinioDocument> optional = minioDocumentRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("document không tồn tại");
        }
        MinioDocument minioDocument = optional.get();
        CommonMapper.copyPropertiesIgnoreNull(request, minioDocument);
        minioDocument.setUploadBy(userId);
        return CommonMapper.map(minioDocumentRepository.save(minioDocument), MinioDocumentDto.class);
    }

    @Override
	public void deleteById(String id) {
    	
    	if(!minioDocumentRepository.existsById(id)) {
    		throw new NotFoundException("Tài liệu không tồn tại");
    	}
		minioDocumentRepository.deleteById(id);
	}
}
