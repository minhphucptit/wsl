package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.model.Company;
import com.ceti.wholesale.model.CustomerContact;
import com.ceti.wholesale.model.GoodsInOut;
import com.ceti.wholesale.model.ProductAccessory;
import com.ceti.wholesale.model.SoldVoucher;
import com.ceti.wholesale.repository.CompanyRepository;
import com.ceti.wholesale.repository.CustomerContactRepository;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.service.CompanyService;
import com.ceti.wholesale.service.CustomerContactService;
import com.ceti.wholesale.service.impl.CompanyServiceImpl;
import com.ceti.wholesale.service.impl.CustomerContactServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(CustomerContactController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class CustomerContactControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @TestConfiguration
    public static class ServiceTestConfiguration {
        @Bean
        CustomerContactService customerContactService() {
            return new CustomerContactServiceImpl();
        }

    }
    @MockBean
    private FactoryRepository factoryRepository;
    @Autowired
    private MockMvc mockMvc;

    //repository
    @MockBean
    private CustomerContactRepository customerContactRepository;

    private String jsonUpdateRequest= "{\n" +
            "    \"customer_contact\":" +
            "[{ \"customer_id\":\"1\", \"email\":\"hung\"," +
            "   \"name\":\"hung\", " +
            "\"telephone_number\":\"04657897\"" +
            "}]}";

    @Test
    public void testGetListCustomerContact() throws Exception {
        List<CustomerContact> customerContactList = new ArrayList<>();
        for(int i=0;i<5;i++){
        CustomerContact contact = new CustomerContact().setId("A"+i).setName("Công ty "+i).setCustomerId("1");
        customerContactList.add(contact);
        }

        given(customerContactRepository.findByCustomerId("1")).willReturn( customerContactList);
        mockMvc.perform(get("/v1/customer-contacts/{customer-id}", "1"))
                .andExpect(status().isOk())

                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_items").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id").value("A0"));
    }

    @Test
    public void testUpdateSoldVoucherSuccess() throws Exception{
        given(customerContactRepository.save(isA(CustomerContact.class))).willAnswer(i-> i.getArgument(0));
        mockMvc.perform(patch("/v1/customer-contacts/{customer-id}", "1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"));
    }


}
