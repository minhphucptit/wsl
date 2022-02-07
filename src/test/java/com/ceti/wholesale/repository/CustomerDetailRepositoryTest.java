package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.util.VoucherUtils;
import com.ceti.wholesale.model.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CustomerDetailRepositoryTest {
    @TestConfiguration
    public static class CustomerDetailRepositoryTestConfiguration{
        @Bean
        CustomerDetailRepository customerDetailRepository(){
            return new CustomerDetailRepository();
        }
    }
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerDetailRepository customerDetailRepository;
    @MockBean
    private VoucherUtils voucherUtils;
    @Test
    public void fillAllWithFilter()throws Exception{
        Pageable pageable= PageRequest.of(0,20);
        Customer customer= new Customer().setId("A").setName("Bien").setCategory("DAILY")
                .setAddress("address")
                .setCompanyId("C").setGroupId("G").setCode("BM203");
        Customer customer1= new Customer().setId("B").setName("Bien1").setCategory("CONGTY")
                .setAddress("address1")
                .setCompanyId("C1").setGroupId("G1");
        customer= customerRepository.save(customer);
        customerRepository.save(customer1);
        MultiValueMap<String,String> where= new LinkedMultiValueMap<>();
        where.add("category","DAILY");
        where.add("code", "BM20");	// LIKE search
        ResultPage<Customer> rs= customerDetailRepository.findAllWithFilter(pageable,where);
        Customer returnCustomer = rs.getPageList().get(0);
        assertEquals(customer.getId(),returnCustomer.getId());
        assertEquals(1,rs.getTotalItems());
    }
}
