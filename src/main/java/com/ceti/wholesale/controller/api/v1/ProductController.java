package com.ceti.wholesale.controller.api.v1;

import com.ceti.wholesale.common.enums.ResponseCodeEnum;
import com.ceti.wholesale.controller.api.request.CreateProductRequest;
import com.ceti.wholesale.controller.api.request.UpdateProductRequest;
import com.ceti.wholesale.controller.response.ResponseBodyDto;
import com.ceti.wholesale.dto.ProductDto;
import com.ceti.wholesale.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.util.List;


@RestController
@RequestMapping("/v1")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Create new Product
    @PostMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductDto>> createProduct(
            @RequestBody CreateProductRequest request,
            @RequestHeader(name = "department_id") String factoryId) {
        ProductDto productDto = productService.createProduct(request, factoryId);
        ResponseBodyDto<ProductDto> res = new ResponseBodyDto<>(productDto,
                ResponseCodeEnum.R_201, "CREATED");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    // Get list Product
    //embed_products : false sẽ chỉ lấy bảng list product, không join với bảng
    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductDto>> getProducts(
            @RequestParam MultiValueMap<String, String> where,
//            @RequestHeader(name = "department_id") String factoryId,
            @RequestParam(value = "embed_products", required = false) boolean embedProducts,
            @QueryParam("embed") String[] embed
            , Pageable pageable) {
//        where.add("factory_id", factoryId);
        Page<ProductDto> page;
        if (embedProducts) {
            page = productService.findAllWithEmbed(where, pageable, embed);
        }else {
            page = productService.getListProduct(where, pageable);
        }
        ResponseBodyDto<ProductDto> res = new ResponseBodyDto<>(pageable, page,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    // Get list All Product id
    @GetMapping(value = "/products/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> getAllProducts(
            @RequestParam(value = "id",required = false)String id,
            @RequestParam(value = "factory_id",required = false)String factoryId) {
        List<String> productIds = productService.getAllProductId(id,factoryId);
        ResponseBodyDto<String> res = new ResponseBodyDto<String>( productIds,
                ResponseCodeEnum.R_200, "OK",productIds.size());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // Get list Product
    @GetMapping(value = "/products/weights", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> getProductsWeight(
            @RequestHeader(name = "department_id") String factoryId) {
        List<String> listProduct = productService.getAllListWeights(factoryId);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(listProduct,ResponseCodeEnum.R_200, "OK", listProduct.size());
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // Update Product
    @PatchMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductDto>> updateProduct(
            @PathVariable(name = "id") String productId,
            @RequestHeader(name = "department_id") String factoryId,
            @RequestBody UpdateProductRequest request
    ) {
        ProductDto productDto = productService.updateProduct(productId, factoryId, request);

        ResponseBodyDto<ProductDto> res = new ResponseBodyDto<>(productDto,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // Get list factory Product
    @GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<ProductDto>> existProductById(
            @PathVariable(name = "id") String productId) {

        ProductDto product = productService.findProductById(productId);

        ResponseBodyDto<ProductDto> res = new ResponseBodyDto<>(product,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // update product price by weight
    @PatchMapping(value = "/products/product-prices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseBodyDto<String>> updateProductPriceByWeight(
            @RequestBody UpdateProductRequest request,
            @RequestHeader(name = "department_id") String factoryId) {
        productService.updateProductPriceByWeight(factoryId, request);
        ResponseBodyDto<String> res = new ResponseBodyDto<>(null,
                ResponseCodeEnum.R_200, "OK");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

}
