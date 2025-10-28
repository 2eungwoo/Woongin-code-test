package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.controller.dto.request.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.GetProductListRequest;
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
    public ResponseEntity<Product> getProductById(@PathVariable(name = "productId") Long productId){
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @PostMapping(value = "/products")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest dto){
        Product product = productService.createProduct(dto);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping(value = "/products/{productId}")
    public ResponseEntity<Boolean> deleteProductById(@PathVariable(name = "productId") Long productId){
        productService.deleteProductById(productId);
        return ResponseEntity.ok(true);
    }

    @PutMapping(value = "/products/{productId}")
    public ResponseEntity<Product> updateProductById(@RequestBody UpdateProductRequest dto){
        Product product = productService.updateProductById(dto);
        return ResponseEntity.ok(product);
    }

    @GetMapping(value = "/products")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@RequestBody GetProductListRequest dto){
        Page<Product> productList = productService.getProductListByCategory(dto);
        return ResponseEntity.ok(new ProductListResponse(productList.getContent(), productList.getTotalPages(), productList.getTotalElements(), productList.getNumber()));
    }

    @GetMapping(value = "/products/categories")
    public ResponseEntity<List<String>> getUniqueProductListByCategories(){
        List<String> uniqueCategories = productService.getUniqueProductListByCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}