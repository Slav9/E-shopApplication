package com.example.ShopCart.Controllers;

import com.example.ShopCart.models.Role;
import com.example.ShopCart.models.Users;
import com.example.ShopCart.repo.UsersPerository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UsersPerository usersPerository;

    @GetMapping
    public String userList (Model model){
        model.addAttribute("users",usersPerository.findAll());
        return "usersList";
    }

    @GetMapping("{users}")
    public String userEditForm(@PathVariable Users users, Model model){
        model.addAttribute("users",users);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping
    public String userSave (
            @RequestParam String username,
            @RequestParam Map<String,String> form,
            @RequestParam ("UserID") Users users){
        users.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        users.getRoles().clear();

        for(String key: form.keySet()){
            if(roles.contains(key)){
                users.getRoles().add(Role.valueOf(key));
            }
        }

        usersPerository.save(users);
        return "redirect:/users";
    }
}
