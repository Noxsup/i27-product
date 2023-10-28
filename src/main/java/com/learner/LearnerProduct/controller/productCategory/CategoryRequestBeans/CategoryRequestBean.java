package com.learner.LearnerProduct.controller.productCategory.CategoryRequestBeans;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryRequestBean {
    List<CategoryBean> categoryList = new ArrayList<>();

}