package com.example.ShopCart.Controllers;

import com.example.ShopCart.Service.CartService;
import com.example.ShopCart.models.Cart;
import com.example.ShopCart.models.CartItem;
import com.example.ShopCart.repo.allGoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private allGoodsRepository allGoodsRepository ;
    @Autowired
    private CartService cartService;


    @GetMapping("/cart")
    public String cart(Model model) {
        return "cart";
    }

    @PostMapping("/addtocart")
    public String addToCart(HttpSession session, Model model,  @RequestParam("id") Long id,
                            @RequestParam("quantity") int quantity){

        String sessionValue = (String) session.getAttribute("sessionValue");
        if(sessionValue==null) {
            sessionValue=session.getId();
            session.setAttribute("sessionValue",sessionValue);
            cartService.addCart(id, sessionValue, quantity);
        }
        else{
            cartService.addToShoppingCart(id,sessionValue,quantity);
        }


        return "redirect:/catalog";
    }
}
