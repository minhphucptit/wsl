package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.model.*;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.ProductDetailRepository;
import com.ceti.wholesale.repository.ProductPriceRepository;
import com.ceti.wholesale.repository.ProductRepository;
import com.ceti.wholesale.service.ProductService;
import com.ceti.wholesale.service.impl.ProductServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("ProductControllerTest")
public class ProductControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    @TestConfiguration
    public static class ServiceTestConfiguration{
        @Bean
        ProductService productService(){return new ProductServiceImpl();
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FactoryRepository factoryRepository;
    @MockBean
    private ProductDetailRepository productDetailRepository;

    @MockBean
    private ProductPriceRepository productPriceRepository;

    @MockBean
    private ProductRepository productRepository;

    private String jsonCreateRequest= " {\n" +
            "            \"id\": \"P01\",\n" +
            "            \"name\": \"Cafein\",\n" +
            "            \"category_id\": \"nhans\",\n" +
            "            \"unit\": \"BO\",\n" +
            "            \"quantity\": 2121.0,\n" +
            "            \"purpose\": \"HANGHOA\",\n" +
            "            \"reference_product_id\": \"No\",\n" +
            "            \"buy_price\": 100000.0,\n" +
            "            \"sale_price\": 300.0\n" +
            "        }";
    private String jsonUpdateRequest= " {\n" +
            "            \"name\": \"Cafein\",\n" +
            "            \"category_id\": \"nhans\",\n" +
            "            \"unit\": \"BO\",\n" +
            "            \"weight\": 80,\n" +
            "            \"cylinder_category\": 80,\n" +
            "            \"purpose\": \"HANGHOA\",\n" +
            "            \"reference_product_id\": \"No\",\n" +
            "            \"buy_price\": 100000.0,\n" +
            "            \"sale_price\": 300.0\n" +

            "        }";

    @Test
    public void testGetProductListSuccess()throws Exception{
        List<Product> products = new ArrayList<>();
        List<Object[]> objects = new ArrayList<>();
        for(int i=0;i<5;i++){
            Product product = new Product().setId("A"+i).setName("name"+i).setUnit("unit"+i)
                    .setCategoryId("category"+i).setPurpose("purpose"+i).setReferenceProductId("ref"+i).setFactoryId("f1");
            products.add(product);
            ProductCategory productCategory = new ProductCategory().setProductTypeId("id"+i).setId("id"+i).setName("name"+i);
            Object[] object = new Object[10];
            object[0] = product;
            object[1] = productCategory;
            objects.add(object);
        }

        ResultPage<Object[]> result = new ResultPage<Object[]>();
        result.setPageList(objects);
        result.setTotalItems(5);


        MultiValueMap<String, String> where_ = new LinkedMultiValueMap<>();
        where_.add("embed_products","true" );
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id","f1");
        List<String> embed = new ArrayList<>(Arrays.asList("customer_product_price","customer_product_discount"));
        where_.add("embed","customer_product_price,customer_product_discount");
        given(productDetailRepository.findAllWithEmbed(isA(Pageable.class), Mockito.any(),Mockito.any())).willReturn(result);
        mockMvc.perform(get("/v1/products")
                .header("department_id","f1")
        		.params(where_)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));
    }
    @Test
    public void testGetProductListEmbedNull()throws Exception{
        List<Product> products = new ArrayList<>();
        List<Object[]> objects = new ArrayList<>();
        for(int i=0;i<5;i++){
            Product product = new Product().setId("A"+i).setName("name"+i).setUnit("unit"+i)
                    .setCategoryId("category"+i).setPurpose("purpose"+i).setReferenceProductId("ref"+i).setFactoryId("f1");
            products.add(product);
            ProductCategory productCategory = new ProductCategory().setProductTypeId("id"+i).setId("id"+i).setName("name"+i);
            Brand brand = new Brand().setId("Id" + i).setName("Name" + i);
            ProductType productType = new ProductType().setId("Id" + i).setName("Name" + i).setIsActive(true);
            Color color = new Color().setId("Id" + i).setName("Name" + i);
            ProductUnit productUnit = new ProductUnit().setId("Id" + i).setName("Name" + i);
            Object[] object = new Object[10];
            object[0] = product;
            object[1] = productCategory;
            object[2] = brand;
            object[3] = productType;
            object[4] = color;
            object[5] = productUnit;
            objects.add(object);
        }

        ResultPage<Object[]> result = new ResultPage<Object[]>();
        result.setPageList(objects);
        result.setTotalItems(5);

        MultiValueMap<String, String> where_ = new LinkedMultiValueMap<>();
        where_.add("embed_products","true" );
        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id","f1");
        List<String> embed = new ArrayList<>(Arrays.asList(""));
        where_.add("embed","");
        given(productDetailRepository.findAllWithEmbed(isA(Pageable.class), Mockito.any(),Mockito.any())).willReturn(result);
        mockMvc.perform(get("/v1/products")
                        .header("department_id","f1")
                        .params(where_)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));
    }

    @Test
    public void testGetAllProductId() throws Exception {
        List<String> listProductId = new ArrayList<>();
        for(int i =0 ;i <10;i++){
            listProductId.add("productId"+i);
        }
        MultiValueMap<String,String> where = new LinkedMultiValueMap<>();
        given(productRepository.getAllProductId(null,null)).willReturn(listProductId);
        mockMvc.perform(get("/v1/products/all")
                .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(10));
    }

    @Test
    public void testGetAllListWeights() throws Exception {
        List<String> listWeight = new ArrayList<>();
        for(int i =0 ;i <10;i++){
            listWeight.add("listWeight"+i);
        }
        MultiValueMap<String,String> where = new LinkedMultiValueMap<>();
        given(productRepository.getListWeight("f1")).willReturn(listWeight);
        mockMvc.perform(get("/v1/products/weights").header("department_id","f1")
                        .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(10));
    }

    @Test
    public void testGetListEmbedProduct() throws Exception {
        List<Product> products = new ArrayList<>();
        for(int i=0;i<5;i++){
            Product product = new Product().setId("A"+i).setName("name"+i).setUnit("unit"+i)
                    .setCategoryId("category"+i).setPurpose("purpose"+i).setReferenceProductId("ref"+i).setFactoryId("f1");
            products.add(product);
        }

        ResultPage<Product> result = new ResultPage<Product>();
        result.setPageList(products);
        result.setTotalItems(5);

        MultiValueMap<String, String> where = new LinkedMultiValueMap<>();
        where.add("factory_id","f1");

        given(productDetailRepository.findAllProduct(isA(Pageable.class), Mockito.any())).willReturn(result);
        mockMvc.perform(get("/v1/products")
                        .header("department_id","f1")
                        .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(5))
                .andExpect(jsonPath("$.items[0].id").value("A0"));
    }

    @Test
    public void testCreateProductSuccess() throws Exception {
        given(productRepository.save(isA(Product.class))).willAnswer(i -> i.getArgument(0));
        given(productRepository.existsByIdAndFactoryId("P01", "FAC1")).willReturn(false);
        given(productRepository.existsByReferenceProductIdAndFactoryId("No", "FAC1")).willReturn(false);
        mockMvc.perform(post("/v1/products")
                .header("department_id","FAC1")
        		.contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));

    }
    @Test
    public void testCreateProductFalseWithProductIdAndFactoryIdExists()throws Exception{
        given(productRepository.existsByIdAndFactoryId("P01", "F01")).willReturn(true);
        given(productRepository.existsByReferenceProductIdAndFactoryId("No", "FAC1")).willReturn(false);
        mockMvc.perform(post("/v1/products")
                .header("department_id","F01")
        		.contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Nhà máy đã tồn tại hàng hóa này"));

    }
    @Test
    public void testCreateProductFalseWithReferenceProductIdExists()throws Exception{
        given(productRepository.existsByIdAndFactoryId("P01", "FAC1")).willReturn(false);
        given(productRepository.existsByReferenceProductIdAndFactoryId("No", "FAC1")).willReturn(true);
        mockMvc.perform(post("/v1/products")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8).content(jsonCreateRequest))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã bao bì này đã được chọn cho 1 hàng hóa khác"));

    }
    @Test
    public void testUpdateProductSuccess() throws Exception {
        Product product = new Product().setId("P01").setFactoryId("F01").setName("Bien").setPurpose("HANGHOA").setReferenceProductId("REF")
                .setCategoryId("category").setUnit("BINH");
        ProductPrice productPrice = new ProductPrice().setId("1");
        given(productRepository.findByIdAndFactoryId("P01", "F01")).willReturn(Optional.of(product));
        given(productPriceRepository.save(isA(ProductPrice.class))).willAnswer(i->i.getArgument(0));
        given(productPriceRepository.findLatestProductPrice("P01", "F01")).willReturn(productPrice);
        given(productRepository.save(isA(Product.class))).willAnswer(i -> i.getArgument(0));
        mockMvc.perform(patch("/v1/products/{product-id}","P01")
                .header("department_id","F01")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));

    }
    @Test
    public void testUpdatePeoductFalseWithProductIdNotExists() throws Exception {
        given(productRepository.findByIdAndFactoryId("P100", "F01")).willReturn(Optional.empty());
        mockMvc.perform(patch("/v1/products/{product-id}","P100")
                .header("department_id","F01")
                .contentType(APPLICATION_JSON_UTF8).content(jsonUpdateRequest))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_404"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã hàng hóa không tồn tại"));

    }

    @Test
    public void testUpdatePeoductFalseWithReferenceProductIdNotExists() throws Exception {
        Product product = new Product();
        product.setId("1");
        product.setReferenceProductId("N33o");
        product.setFactoryId("F01");
        given(productRepository.findByIdAndFactoryId("P100", "F01")).willReturn(Optional.of(product));
        given(productRepository.existsByReferenceProductIdAndFactoryId("No", "F01")).willReturn(true);
        mockMvc.perform(patch("/v1/products/{product-id}","P100")
                .header("department_id","F01")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonUpdateRequest))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Mã bao bì này đã được chọn cho 1 hàng hóa khác"));

    }

    @Test
    public void findProductById() throws Exception {
        Product product = new Product();
        product.setId("1");
        given(productRepository.findById("1")).willReturn(Optional.of(product));

        mockMvc.perform(get("/v1/products/{id}", "1").contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
    }

    @Test
    public void testCreateProductSuccessTo() throws Exception {
        given(productRepository.existsById("P01")).willReturn(true);
        given(productRepository.save(isA(Product.class))).willAnswer(i -> i.getArgument(0));
        mockMvc.perform(post("/v1/products")
                .header("department_id","FAC1")
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_201"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));

    }

    @Test
    public void testUpdateProductPriceByWeight() throws Exception {
        MultiValueMap<String,String> where = new LinkedMultiValueMap<>();
        mockMvc.perform(get("/v1/products/product-prices").header("department_id","f1")
                        .params(where)).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("R_200"))
                .andExpect(jsonPath("$.total_items").value(0));
    }

//    @Test
//    public void testUpdateTargetProduction() throws Exception{
//
//        //testUpdateTargetProductionByCustomerCodeAndMonthAndYear Success
//        Product targetProduction = new Product();
//
//        given(productRepository.updateProductPriceByWeight(BigDecimal.valueOf(23),"nhat","nguyennhat",BigDecimal.valueOf(23))).willReturn(targetProduction);
//
//        mockMvc.perform(patch("/v1/products/product-prices")
//                        .header("department_id","FAC1")
//                        .contentType(APPLICATION_JSON_UTF8)
//                        .content(jsonUpdateRequest)).andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("R_200"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"));
//
//    }

}
