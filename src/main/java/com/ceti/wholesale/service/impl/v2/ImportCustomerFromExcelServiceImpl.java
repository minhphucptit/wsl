package com.ceti.wholesale.service.impl.v2;

import com.ceti.wholesale.common.enums.ProductCategoryEnum;
import com.ceti.wholesale.model.Customer;
import com.ceti.wholesale.repository.CompanyRepository;
import com.ceti.wholesale.repository.CustomerGroupRepository;
import com.ceti.wholesale.repository.CustomerRepository;
import com.ceti.wholesale.service.v2.ImportCustomerFromExcelService;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportCustomerFromExcelServiceImpl implements ImportCustomerFromExcelService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CustomerGroupRepository customerGroupRepository;

    @Override
    public String importCustomerFromExcel(MultipartFile multipartFile,String factoryId) throws IOException {
        String message ="Thêm khách hàng thành công!";
        Workbook wb = new XSSFWorkbook(multipartFile.getInputStream());
        Sheet sheet = wb.getSheet("Sheet1");
        List<Integer> rowFail = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();
        int rowTotal = sheet.getLastRowNum();
        if(rowTotal>0||(sheet.getPhysicalNumberOfRows()>0)){
            rowTotal++;
        }
        for(int i=2;i<rowTotal;i++){
            String id,companyId,name,address=null,phoneNumber=null,groupId,taxCode=null,category,vat="0";
            String code=null;
            Row row = sheet.getRow(i);
            Customer customer;
            id = formatter.formatCellValue(row.getCell(0));
            if(customerRepository.existsByIdAndFactoryId(id,factoryId)){
                rowFail.add(i+1);
                continue;
            }
            companyId = formatter.formatCellValue(row.getCell(1));
            if (!companyRepository.existsById(companyId)){
                rowFail.add(i+1);
                continue;
            }
            name = formatter.formatCellValue(row.getCell(2));
            try {
                address = formatter.formatCellValue(row.getCell(3));
            }catch (NullPointerException e){}

            try {
              phoneNumber= formatter.formatCellValue(row.getCell(4)) ;

            }catch (NullPointerException e){}

            category = formatter.formatCellValue(row.getCell(5));
            if(!ProductCategoryEnum.isMember(category)){
                rowFail.add(i+1);
                continue;
            }
            try {
                taxCode = formatter.formatCellValue(row.getCell(6));
            }catch (NullPointerException e){}

            groupId = formatter.formatCellValue(row.getCell(7));
            if(!customerGroupRepository.existsById(groupId)){
                rowFail.add(i+1);
                continue;
            }
            try {
                vat = formatter.formatCellValue(row.getCell(8));
            }catch (NullPointerException e){}
            try {
            	code = formatter.formatCellValue(row.getCell(9));
            }catch (NullPointerException e){}
            
            customer = new Customer(id,code,companyId,name,address,phoneNumber,taxCode,category,groupId,factoryId,true,new BigDecimal(vat),null,null,false);
            customerRepository.save(customer);
        }
        wb.close();
        if(!rowFail.isEmpty()){
            message = "Thêm khách hàng lỗi ở các dòng: ";
            String temp="";
            for (Integer item:rowFail){
                temp+=item+", ";
            }
            message +=temp;
        }
        return message;
    }
}
