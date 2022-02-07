package com.ceti.wholesale.service.impl;

import com.ceti.wholesale.common.constant.ResultPage;
import com.ceti.wholesale.common.error.BadRequestException;
import com.ceti.wholesale.common.error.NotFoundException;
import com.ceti.wholesale.controller.api.request.CreateProductRequest;
import com.ceti.wholesale.controller.api.request.UpdateProductRequest;
import com.ceti.wholesale.dto.*;
import com.ceti.wholesale.dto.mapper.CommonMapper;
import com.ceti.wholesale.model.*;
import com.ceti.wholesale.repository.FactoryRepository;
import com.ceti.wholesale.repository.ProductDetailRepository;
import com.ceti.wholesale.repository.ProductPriceRepository;
import com.ceti.wholesale.repository.ProductRepository;
import com.ceti.wholesale.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FactoryRepository factoryRepository;

    @Override
    public ProductDto createProduct(CreateProductRequest request, String factoryId) throws BadRequestException {
        if (productRepository.existsByIdAndFactoryId(request.getId(), factoryId)) {
            throw new BadRequestException("Nhà máy đã tồn tại hàng hóa này");
        }
        if (!StringUtils.isEmpty(request.getReferenceProductId()) && productRepository.existsByReferenceProductIdAndFactoryId(request.getReferenceProductId(), factoryId)) {
            throw new BadRequestException("Mã bao bì này đã được chọn cho 1 hàng hóa khác");
        }
        Product product = CommonMapper.map(request, Product.class);
        product.setFactoryId(factoryId);
        //them product price
        Product productSave = productRepository.save(product);
        ProductPrice productPrice = new ProductPrice();
        productPrice.setProductId(productSave.getId());
        productPrice.setFactoryId(productSave.getFactoryId());
        productPrice.setBuyPrice(productSave.getBuyPrice());
        productPrice.setSalePrice(productSave.getSalePrice());
        productPrice.setUpdateAt(Instant.now());
        productPrice.setUpdateByFullName(request.getUpdateByFullName());
        productPriceRepository.save(productPrice);
        return CommonMapper.map(productSave, ProductDto.class);
    }
    @Override
    public Page<ProductDto> getListProduct(MultiValueMap<String, String> where, Pageable pageable) {
            ResultPage<Product> productResultPage = productDetailRepository.findAllProduct(pageable, where);
            List<ProductDto> productDto = new ArrayList<>();
            for (Product product : productResultPage.getPageList()) {
                productDto.add(CommonMapper.map(product, ProductDto.class));
            }
            return new PageImpl<>(productDto, pageable, productResultPage.getTotalItems());
    }

    @Override
    public Page<ProductDto> findAllWithEmbed(MultiValueMap<String, String> where, Pageable pageable,String[] embed) {
        if (embed==null){
            ResultPage<Object[]> productResultPage = productDetailRepository.findAll(pageable,where);
            long total = productResultPage.getTotalItems();
            List<ProductDto> productDtoList = new ArrayList<>();
            for (Object[]objects:productResultPage.getPageList()){
                Product product = (Product) objects[0];
                ProductDto productDto = CommonMapper.map(product,ProductDto.class);
                if(objects[1]!=null){
                    ProductCategory productCategory = (ProductCategory) objects[1];
                    ProductCategoryDto productCategoryDto = CommonMapper.map(productCategory,ProductCategoryDto.class);
                    productDto.setProductCategory(productCategoryDto);
                }
                if(objects[2]!=null){
                    Brand brand = (Brand) objects[2];
                    BrandDto brandDto = CommonMapper.map(brand,BrandDto.class);
                    productDto.setBrand(brandDto);
                }
                if(objects[3]!=null){
                    ProductType productType = (ProductType) objects[3];
                    ProductTypeDto productTypeDto = CommonMapper.map(productType,ProductTypeDto.class);
                    productDto.setProductType(productTypeDto);
                }
                if(objects[4]!=null){
                    Color color = (Color) objects[4];
                    ColorDto colorDto = CommonMapper.map(color,ColorDto.class);
                    productDto.setColor(colorDto);
                }
                if(objects[5]!=null){
                    Factory factory = (Factory) objects[5];
                    FactoryDto factoryDto = CommonMapper.map(factory,FactoryDto.class);
                    productDto.setFactory(factoryDto);
                }
                if(objects[6]!=null){
                    ProductUnit productUnit = (ProductUnit) objects[6];
                    ProductUnitDto productUnitDto = CommonMapper.map(productUnit, ProductUnitDto.class);
                    productDto.setProductUnit(productUnitDto);
                }
                productDtoList.add(productDto);
            }
            return new PageImpl<>(productDtoList,pageable,total);

        }
        List<String> embedTable = new ArrayList<>();
        List<String> allTable = new ArrayList<>(Arrays.asList("customer_product_price","customer_product_discount"));
        for(String i: embed){
            if(!allTable.contains(i)){
                throw new BadRequestException("Table name \'" + i + "\' cannot be obtained!");
            }
            if (!embedTable.contains(i)) {
                embedTable.add(i);
            }
        }

        ResultPage<Object[]> page = productDetailRepository.findAllWithEmbed(pageable, where,embedTable);
        List<Object[]>productDetail = page.getPageList();
        long total = page.getTotalItems();
        List<ProductDto> productDtoList = new ArrayList<>();
        for(Object[] objects:productDetail){
            if (objects[0]!=null){
                Product product = (Product) objects[0];
                ProductDto productDto = CommonMapper.map(product,ProductDto.class);
                if(objects[1]!=null){
                    ProductCategory productCategory = (ProductCategory)objects[1];
                    productDto.setProductCategory(CommonMapper.map(productCategory,ProductCategoryDto.class));
                }
                int temp = 1;
                if(embedTable.contains("customer_product_price")&&objects[temp++]!=null){
                    CustomerProductPrice customerProductPrice = (CustomerProductPrice) objects[temp++];
                    if(customerProductPrice!=null){
                        productDto.setCustomerProductPrice(CommonMapper.map(customerProductPrice,CustomerProductPriceDto.class));
                    }
                }
                if(embedTable.contains("customer_product_discount")){
                    CustomerProductDiscount customerProductDiscount = (CustomerProductDiscount) objects[temp++];
                    if(customerProductDiscount!=null){
                        productDto.setCustomerProductDiscount(CommonMapper.map(customerProductDiscount, CustomerProductDiscountDto.class));
                    }
                }
                productDtoList.add(productDto);
            }
        }
        return new PageImpl<>(productDtoList, pageable, total);
    }

    @Override
    public List<String> getAllListWeights(String factoryId) {
        // valid
        return productRepository.getListWeight(factoryId);
    }


    @Override
    public ProductDto updateProduct(String productId, String factoryId, UpdateProductRequest request) {
        // valid
        Optional<Product> optionalProduct = productRepository.findByIdAndFactoryId(productId, factoryId);
        if (optionalProduct.isEmpty()) {
            throw new NotFoundException("Mã hàng hóa không tồn tại");
        }
        Product product = optionalProduct.get();
        //kiểm tra thay đổi giá sản phẩm
        boolean checkChangeProductPrice = false;
        if(request.getBuyPrice().compareTo(product.getBuyPrice()) != 0 || request.getSalePrice().compareTo(product.getSalePrice()) != 0){
            checkChangeProductPrice = true;
        }
        if (!StringUtils.isEmpty(request.getReferenceProductId()) && !request.getReferenceProductId().equals(product.getReferenceProductId())
                && productRepository.existsByReferenceProductIdAndFactoryId(request.getReferenceProductId(), factoryId)) {
            throw new BadRequestException("Mã bao bì này đã được chọn cho 1 hàng hóa khác");
        }
        CommonMapper.copyPropertiesIgnoreNull(request, product);
        product.setFactoryId(factoryId);
        //them product price
        Product productSave = productRepository.save(product);
        if(checkChangeProductPrice){
            ProductPrice productPrice = new ProductPrice();
            productPrice.setProductId(productSave.getId());
            productPrice.setFactoryId(productSave.getFactoryId());
            productPrice.setBuyPrice(productSave.getBuyPrice());
            productPrice.setSalePrice(productSave.getSalePrice());
            productPrice.setUpdateAt(Instant.now());
            productPrice.setUpdateByFullName(request.getUpdateByFullName());
            productPriceRepository.save(productPrice);
        }
        return CommonMapper.map(productSave, ProductDto.class);
    }

    @Override
    public ProductDto findProductById(String productId) {
        Product product = new Product();
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            product = CommonMapper.map(productOptional.get(), Product.class);
        }
        return CommonMapper.map(product, ProductDto.class);

    }

    @Override
    public void updateProductPriceByWeight(String factoryId, UpdateProductRequest request) {
        productRepository.updateProductPriceByWeight(request.getWeight(), factoryId, request.getUpdateByFullName(),
                request.getSalePrice());    }

    @Override
    public List<String> getAllProductId(String id,String factoryId) {
        return productRepository.getAllProductId(id,factoryId);
    }
}
