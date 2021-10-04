package com.example.ShopCart.Controllers;

import com.example.ShopCart.Service.UserService;
import com.example.ShopCart.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {

        model.addAttribute("users", new Users());
        return ("registration");
    }

    @PostMapping("/registration")
    public String addUser(@Valid Users user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors() || !userService.addUser(user)) {
            return "registration";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {

        boolean isActivated = userService.activateUser(code);
        return "login";
    }
}
