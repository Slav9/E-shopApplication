// controller for cart page
package com.example.ShopCart.Controllers;

import com.example.ShopCart.Service.CartService;
import com.example.ShopCart.Service.UserService;
import com.example.ShopCart.models.Cart;
import com.example.ShopCart.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
// "session value" is a stuck, because I didn't quite understand spring session

@Controller
public class CartController {


    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;


    @GetMapping("/cart")
    public String showCart(@AuthenticationPrincipal Users users, HttpSession session, Model model) {
        model.addAttribute("users", users);
        String sessionValue = (String) session.getAttribute("sessionValue");
        if (sessionValue == null) {
            model.addAttribute("cart", new Cart());
        } else {
            Cart cart = cartService.getCartBySessionValue(sessionValue);
            model.addAttribute("cart", cart);
        }
        return "cart";
    }

    @PostMapping("/addtocart")
    public String addToCart(HttpSession session, Model model, @RequestParam("id") Long id,
                            @RequestParam("quantity") int quantity) {

        String sessionValue = (String) session.getAttribute("sessionValue");
        if (sessionValue == null) {
            sessionValue = session.getId();
            session.setAttribute("sessionValue", sessionValue);
            cartService.addCart(id, sessionValue, quantity);
        } else {
            cartService.addToShoppingCart(id, sessionValue, quantity);
        }


        return "redirect:/catalog";
    }

    @PostMapping("/updateCart")
    public String updateCartItem(@RequestParam("item_id") Long id, @RequestParam("quantity") int quantity) {

        cartService.updateCartItem(id, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/removeCartItem/{id}")
    public String removeItem(@PathVariable("id") Long id, HttpSession session) {

        String sessionValue = (String) session.getAttribute("sessionValue");
        cartService.removeCartItemFromCart(id, sessionValue);
        return "redirect:/cart";
    }

    @GetMapping("/clearCart")
    public String clearCart(HttpSession session) {

        String sessionValue = (String) session.getAttribute("sessionValue");
        session.removeAttribute("sessionValue");
        cartService.clearCart(sessionValue);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        return "checkout";
    }

    @PostMapping("/checkout")
    public String updateUserBalanceOnCheckout(@AuthenticationPrincipal Users users, HttpSession session) {
        String sessionValue = (String) session.getAttribute("sessionValue");
        Cart cart = cartService.getCartBySessionValue(sessionValue);
        userService.updateUserBalanceOnCheckout(users, cart);
        session.removeAttribute("sessionValue");
        cartService.clearCart(sessionValue);
        return "redirect:/checkout";
    }


}
