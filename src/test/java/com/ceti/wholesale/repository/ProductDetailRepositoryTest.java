package com.ceti.wholesale.repository;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.Product;
import com.ceti.wholesale.model.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ProductDetailRepositoryTest {
    @TestConfiguration
    public static class ProductDetailRepositoryTestConfiguration{
        @Bean
        ProductDetailRepository productDetailRepository(){return new ProductDetailRepository();}
    }
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Test
    public void testFillAllWithFilter()throws Exception{
        Pageable pageable= PageRequest.of(0,20);
        Product product = new Product().setId("id").setName("ct1").setUnit("BINH").setPurpose("HANGHOA")
                .setReferenceProductId("ref").setWeight(BigDecimal.valueOf(12)).setCategoryId("categoryId").setFactoryId("F01");
        product= productRepository.save(product);
        MultiValueMap<String ,String> where = new LinkedMultiValueMap<>();
        where.add("unit","BINH");
        List<String> embed = new ArrayList<>(Arrays.asList("product_category"));
        ResultPage<Object[]> resultPage = productDetailRepository.findAllWithEmbed(pageable,where,embed);
        Object[] returnObject = (Object[]) resultPage.getPageList().get(0);
        Product returnProduct = (Product) returnObject[0];
        ProductCategory returnProductCategory = (ProductCategory) returnObject[1];
        assertEquals(product.getCategoryId(),returnProduct.getCategoryId());

    }
}
