package com.wjc.codetest.product.service;

import com.wjc.codetest.product.controller.dto.request.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.controller.dto.request.UpdateProductRequest;
import com.wjc.codetest.product.repository.ProductRepository;
import com.wjc.codetest.product.service.validator.ProductValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    @Transactional
    public Product createProduct(CreateProductRequest dto) {
        Product product = Product.createProduct(dto.category(), dto.name());
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        return productValidator.validateExistOrThrow(productId);
    }

    @Transactional
    public Product updateProductById(Long productId, UpdateProductRequest dto) {
        Product targetProduct = productValidator.validateExistOrThrow(productId);
        targetProduct.updateProduct(dto.category(), dto.name());
        return targetProduct;

    }

    @Transactional
    public void deleteProductById(Long productId) {
        Product targetProduct = productValidator.validateExistOrThrow(productId);
        productRepository.delete(targetProduct);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductList(GetProductListRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.page(), dto.size(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllByCategory(dto.category(), pageRequest);
    }

    @Transactional(readOnly = true)
    public List<String> getUniqueProductListByCategories() {
        return productRepository.findDistinctCategories();
    }
}