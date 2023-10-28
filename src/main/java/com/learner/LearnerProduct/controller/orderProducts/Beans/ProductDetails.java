package com.learner.LearnerProduct.controller.orderProducts.Beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetails {
    private String productImageUrl;
    private String productName;
    private int productPrice;
    private int quantity;
}
