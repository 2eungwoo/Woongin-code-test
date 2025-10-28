package com.wjc.codetest.product.service;

import com.wjc.codetest.product.controller.dto.request.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.GetProductListRequest;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.controller.dto.request.UpdateProductRequest;
import com.wjc.codetest.product.repository.ProductRepository;
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

    public Product createProduct(CreateProductRequest dto) {
        Product product = Product.createProduct(dto.category(), dto.name());
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("product not found");
        }
        return productOptional.get();
    }

    public Product updateProductById(Long productId, UpdateProductRequest dto) {
        Product product = getProductById(productId);
        product.updateProduct(dto.category(), dto.name());
        return productRepository.save(product);

    }

    public void deleteProductById(Long productId) {
        Product product = getProductById(productId);
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductList(GetProductListRequest dto) {
        PageRequest pageRequest = PageRequest.of(dto.page(), dto.size(), Sort.by(Sort.Direction.ASC, "category"));
        return productRepository.findAllProducts(dto.category(), pageRequest);
    }

    @Transactional(readOnly = true)
    public List<String> getUniqueProductListByCategories() {
        return productRepository.findDistinctProductsByCategories();
    }
}