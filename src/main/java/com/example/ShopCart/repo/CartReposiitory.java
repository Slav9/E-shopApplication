package com.example.ShopCart.repo;

import com.example.ShopCart.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartReposiitory extends JpaRepository<Cart,Long> {
}
