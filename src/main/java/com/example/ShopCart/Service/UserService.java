package com.example.ShopCart.Service;

import com.example.ShopCart.repo.UsersPerository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UsersPerository usersPerository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersPerository.findByUsername(username);
    }
}
