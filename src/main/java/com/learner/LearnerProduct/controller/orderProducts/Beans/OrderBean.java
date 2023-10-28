package com.learner.LearnerProduct.controller.orderProducts.Beans;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderBean {
    private String emailId;
    private List<ProductDetails> products = new ArrayList<>();
}
