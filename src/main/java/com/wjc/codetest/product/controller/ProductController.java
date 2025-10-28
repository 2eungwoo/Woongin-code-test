package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.controller.dto.request.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.GetProductListRequest;
import com.wjc.codetest.product.controller.dto.response.ProductResponse;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.controller.dto.request.UpdateProductRequest;
import com.wjc.codetest.product.controller.dto.response.ProductListResponse;
import com.wjc.codetest.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping(value = "/products/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable(name = "productId") Long productId){
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(ProductResponse.from(product));
    }

    @PostMapping(value = "/products")
    public ResponseEntity<Long> createProduct(@RequestBody CreateProductRequest dto){
        Product product = productService.createProduct(dto);
        ProductResponse productResponse = ProductResponse.from(product);
        return ResponseEntity.ok(productResponse.id());
    }

    @DeleteMapping(value = "/products/{productId}")
    public ResponseEntity<Long> deleteProductById(@PathVariable(name = "productId") Long productId){
        Long targetId = productService.deleteProductById(productId);
        return ResponseEntity.ok(targetId);
    }

    @PutMapping(value = "/products/{productId}")
    public ResponseEntity<Product> updateProductById(@PathVariable(name = "productId") Long productId,
                                                     @RequestBody UpdateProductRequest dto) {
        Product product = productService.updateProductById(productId, dto);
        return ResponseEntity.ok(product);
    }

    @GetMapping(value = "/products")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto) {
        Page<Product> productList = productService.getProductListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
    }

    @GetMapping(value = "/products/categories")
    public ResponseEntity<List<String>> getUniqueCategories(){
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}