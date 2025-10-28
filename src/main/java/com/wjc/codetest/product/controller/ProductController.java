package com.wjc.codetest.product.controller;

import com.wjc.codetest.product.controller.dto.request.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.GetProductListRequest;
import com.wjc.codetest.product.service.dto.response.ProductResponse;
import com.wjc.codetest.product.controller.dto.request.UpdateProductRequest;
import com.wjc.codetest.product.service.dto.response.ProductListResponse;
import com.wjc.codetest.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
        ProductResponse responseDto = productService.getProductById(productId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(value = "/products")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid CreateProductRequest dto){
        ProductResponse responseDto = productService.createProduct(dto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping(value = "/products/{productId}")
    public ResponseEntity<ProductResponse> deleteProductById(@PathVariable(name = "productId") Long productId){
        ProductResponse responseDto = productService.deleteProductById(productId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping(value = "/products/{productId}")
    public ResponseEntity<ProductResponse> updateProductById(@PathVariable(name = "productId") Long productId,
                                                             @RequestBody @Valid UpdateProductRequest dto)
    {
        ProductResponse responseDto = productService.updateProductById(productId, dto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping(value = "/products")
    public ResponseEntity<ProductListResponse> getProductListByCategory(@ModelAttribute GetProductListRequest dto) {
        return ResponseEntity.ok(productService.getProductListByCategory(dto));
    }

    @GetMapping(value = "/products/categories")
    public ResponseEntity<List<String>> getUniqueCategories(){
        List<String> uniqueCategories = productService.getUniqueCategories();
        return ResponseEntity.ok(uniqueCategories);
    }
}