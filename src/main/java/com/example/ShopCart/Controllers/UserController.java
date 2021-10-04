//Controller for admin control user page and user profile page
package com.example.ShopCart.Controllers;

import com.example.ShopCart.Service.UserService;
import com.example.ShopCart.models.Role;
import com.example.ShopCart.models.Users;
import com.example.ShopCart.repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/users")

public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UsersRepository usersRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {

        model.addAttribute("users", userService.findAll());
        return "usersList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{users}")
    public String userEditForm(@PathVariable Users users, Model model) {

        model.addAttribute("users", users);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("UserID") Users users) {

        userService.saveUser(users, username, form);
        return "redirect:/users";
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal Users users) {
        model.addAttribute("users", users);
        return ("profile");
    }

    @PostMapping("profile")
    public String editProfile(@AuthenticationPrincipal Users users, @RequestParam String password, @RequestParam String email) {

        userService.editProfile(users, password, email);
        return ("redirect:/users/profile");
    }

    @PostMapping("updateBalance")
    public String updateBal(@AuthenticationPrincipal Users users, @RequestParam("amount") int amount, Model model) {
        //balance correctly updates only when user relogin, absolutely 0 ideas why this is happening
        model.addAttribute("users", users);
        userService.topUpBalance(users, amount);
        return ("profile");
    }

    @GetMapping("refill")
    public String refill(@AuthenticationPrincipal Users users, Model model) {
        model.addAttribute("users", users);
        return "refill";
    }
}
