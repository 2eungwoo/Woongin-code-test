package com.wjc.codetest.product.model.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "Product")
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "name", nullable = false)
    private String name;

    private Product(String category, String name) {
        this.category = category;
        this.name = name;
    }

    public static Product createProduct(String category, String name) {
        return new Product(category, name);
    }

    public void updateProduct(String category, String name) {
        this.category = category;
        this.name = name;
    }
}
