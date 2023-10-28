package com.learner.LearnerProduct.controller.products;

import com.learner.LearnerProduct.entity.Product;
import com.learner.LearnerProduct.exception.ResourceNotFoundException;
import com.learner.LearnerProduct.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // get all products
    @CrossOrigin("*")
    @GetMapping("get-products")
    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    // get product by id
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable(value = "id") long productId) {
        return this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id :" + productId));
    }

    // create Product
    @PostMapping("add-product")
    public String createProduct(@RequestBody CreateProductRequestBean createProductRequestBean) {
        createProductRequestBean.getItems().forEach(item -> {
            Product product = new Product();
            product.setProductCategory(createProductRequestBean.getTitle());
            product.setProductPrice(item.getPrice() * 100);
            product.setProductName(item.getName());
            product.setProductImageUrl(item.getImageUrl());
            this.productRepository.save(product);
        });
        return "Created";
    }

    // update Product
    @PutMapping("/{id}")
    public Product updateProduct(@RequestBody Product product, @PathVariable("id") long productId) {
        Product existingProduct = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id :" + productId));
        existingProduct.setProductCategory(product.getProductCategory());
        existingProduct.setProductName(product.getProductName());
        existingProduct.setProductImageUrl(product.getProductImageUrl());
        existingProduct.setProductPrice(product.getProductPrice());
        return this.productRepository.save(existingProduct);
    }

    // delete Product by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") long productId) {
        Product existingProduct = this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id :" + productId));
        this.productRepository.delete(existingProduct);
        return ResponseEntity.ok().build();
    }
}
