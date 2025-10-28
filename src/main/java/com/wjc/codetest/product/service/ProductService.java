package com.wjc.codetest.product.service;

import com.wjc.codetest.product.controller.dto.request.CreateProductRequest;
import com.wjc.codetest.product.controller.dto.request.GetProductListRequest;
import com.wjc.codetest.product.controller.dto.request.UpdateProductRequest;
import com.wjc.codetest.product.controller.dto.response.ProductListResponse;
import com.wjc.codetest.product.controller.dto.response.ProductResponse;
import com.wjc.codetest.product.model.domain.Product;
import com.wjc.codetest.product.repository.ProductRepository;
import com.wjc.codetest.product.service.validator.ProductValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest dto) {
        Product product = Product.createProduct(dto.category(), dto.name());
        Product createdProduct = productRepository.save(product);

        return ProductResponse.from(createdProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productValidator.validateExistOrThrow(productId);

        return ProductResponse.from(product);
    }

    @Transactional
    public ProductResponse updateProductById(Long productId, UpdateProductRequest dto) {
        Product targetProduct = productValidator.validateExistOrThrow(productId);
        targetProduct.updateProduct(dto.category(), dto.name());

        return ProductResponse.from(targetProduct);
    }

    @Transactional
    public ProductResponse deleteProductById(Long productId) {
        Product targetProduct = productValidator.validateExistOrThrow(productId);
        productRepository.delete(targetProduct);

        return ProductResponse.from(targetProduct);
    }

    @Transactional(readOnly = true)
    public ProductListResponse getProductListByCategory(GetProductListRequest dto) {

        PageRequest pageRequest = PageRequest.of(
            dto.page(),
            dto.size(),
            Sort.by(Sort.Direction.ASC, "category")
        );

        Page<Product> products = productRepository.findAllByCategory(dto.category(), pageRequest);
        List<ProductResponse> responseList = products.getContent()
                                             .stream()
                                             .map(ProductResponse::from)
                                             .toList();

        return new ProductListResponse(
            responseList,
            products.getTotalPages(),
            products.getTotalElements(),
            products.getNumber()
        );
    }

    @Transactional(readOnly = true)
    public List<String> getUniqueCategories() {
        return productRepository.findDistinctCategories();
    }
}