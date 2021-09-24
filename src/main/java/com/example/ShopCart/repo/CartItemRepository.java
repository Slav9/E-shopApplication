package com.example.ShopCart.repo;

import com.example.ShopCart.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long>{
}
