package com.learner.LearnerProduct.repository;

import com.learner.LearnerProduct.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByEmailId(String emailId);
}
