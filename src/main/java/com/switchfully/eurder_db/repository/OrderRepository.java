package com.switchfully.eurder_db.repository;

import com.switchfully.eurder_db.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomerId(Long customerId);
    List<Order> findAllByOrderLines_shippingDate(LocalDate shippingDate);
}
