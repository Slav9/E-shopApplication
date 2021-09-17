package com.example.ShopCart.repo;

import com.example.ShopCart.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CartReposiitory extends JpaRepository<Cart,Long> {
        Cart findBySessionValue(String sessionValue);
}
