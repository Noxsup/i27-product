package com.learner.LearnerProduct.controller.orderProducts.OrderController;

import com.learner.LearnerProduct.controller.orderProducts.Beans.OrderBean;
import com.learner.LearnerProduct.controller.orderProducts.UserById;
import com.learner.LearnerProduct.controller.orderProducts.UserClient;
import com.learner.LearnerProduct.entity.Order;
import com.learner.LearnerProduct.repository.OrderRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestTemplate restTemplate;

    // Create an order
    @CrossOrigin("*")
    @PostMapping("/place-order")
    public ResponseEntity<?> saveOrder(@RequestBody OrderBean orderBean) {
        if (orderBean == null || orderBean.getEmailId() == null || orderBean.getProducts() == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            orderBean.getProducts().forEach(item -> {
                Order order = new Order();
                order.setEmailId(orderBean.getEmailId());
                order.setProductName(item.getProductName());
                order.setProductPrice(item.getProductPrice());
                order.setProductImageUrl(item.getProductImageUrl());
                order.setQuantity(item.getQuantity());

                this.orderRepository.save(order);
            });
            return ResponseEntity.status(HttpStatus.CREATED).body("Order Created");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save order");
        }
    }

    // Get Order using userId
    @CrossOrigin("*")
    @GetMapping("get-order-details")
    public ResponseEntity<Object> getOrders(@RequestParam long userId, HttpServletResponse response) {
        UserClient userClient = new UserClient();
        UserById user = userClient.getUserDetails(restTemplate, userId);
        if (user == null) {
            return ResponseEntity.noContent().build();
        } else {
            try {
                // code to get order details using the user object
                List<Order> ordersList = getOrderDetails(user.getEmail());
                return ResponseEntity.ok(ordersList);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
    }

    private List<Order> getOrderDetails(String emailId) {
        return this.orderRepository.findAllByEmailId(emailId);
    }
}
