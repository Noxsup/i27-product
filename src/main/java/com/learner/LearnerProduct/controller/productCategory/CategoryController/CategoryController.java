package com.learner.LearnerProduct.controller.productCategory.CategoryController;

import com.learner.LearnerProduct.controller.productCategory.CategoryRequestBeans.CategoryRequestBean;
import com.learner.LearnerProduct.entity.Category;
import com.learner.LearnerProduct.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    public static String getString(@RequestBody CategoryRequestBean categoryRequestBean, CategoryRepository categoryRepository) {
        categoryRequestBean.getCategoryList().forEach(item -> {
            Category category = new Category();
            category.setCategoryTitle(item.getTitle());
            category.setCategoryImageUrl(item.getImageUrl());
            category.setCategoryRoute(item.getRoute());
            categoryRepository.save(category);
        });

        return "Created";
    }

    @PostMapping("/create-categories")
    public String createCategory(@RequestBody CategoryRequestBean categoryRequestBean) {
        return getString(categoryRequestBean, this.categoryRepository);
    }

    @CrossOrigin("*")
    @GetMapping("/get-category-details")
    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }
}
