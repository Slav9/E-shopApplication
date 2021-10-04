package com.example.ShopCart.repo;

import com.example.ShopCart.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    Users findByActivationCode(String code);
}
