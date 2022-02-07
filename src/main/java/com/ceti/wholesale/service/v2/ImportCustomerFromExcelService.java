package com.ceti.wholesale.service.v2;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImportCustomerFromExcelService {
    String importCustomerFromExcel(MultipartFile file,String factoryId) throws IOException;
}
