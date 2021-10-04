package com.example.ShopCart.repo;

import com.example.ShopCart.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findBySessionValue(String sessionValue);
}
