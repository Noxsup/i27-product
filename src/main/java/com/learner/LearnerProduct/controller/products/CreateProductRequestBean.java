package com.learner.LearnerProduct.controller.products;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CreateProductRequestBean {
    private String title;
    private List<ItemsList> items = new ArrayList<>();
}
