package com.example.ShopCart.Controllers;

import com.example.ShopCart.models.Role;
import com.example.ShopCart.models.Users;
import com.example.ShopCart.repo.UsersPerository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    private UsersPerository usersPerository;

    @GetMapping("/registration")
    public String registration(){
        return ("registration");
    }

    @PostMapping("/registration")
    public String addUser(Users user, Model model){
       Users userFromDb = usersPerository.findByUsername(user.getUsername());

        if (userFromDb !=null){
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        usersPerository.save(user);

        return "redirect:/login";
    }
}
