package com.example.ShopCart.Service;

import com.example.ShopCart.models.Cart;
import com.example.ShopCart.models.CartItem;
import com.example.ShopCart.models.tovar;
import com.example.ShopCart.repo.CartReposiitory;
import com.example.ShopCart.repo.allGoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class CartService {

    @Autowired
    private CartReposiitory cartReposiitory;
    @Autowired
    private allGoodsRepository allGoodsRepository;


    public Cart addCart(Long id, String sessionValue, int quantity) {
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(quantity);
        cartItem.setTovar(allGoodsRepository.findById(id).orElseThrow());
        cart.getItems().add(cartItem);
        cart.setSessionValue(sessionValue);
         return cartReposiitory.save(cart);
    }


    public Cart addToShoppingCart(Long id, String sessionValue, int quantity) {
        Cart cart = cartReposiitory.findBySessionValue(sessionValue);
        tovar t = allGoodsRepository.findById(id).orElseThrow();
        for(CartItem item: cart.getItems()){
            if(t.getId().equals(item.getTovar().getId())){
                item.setQuantity(item.getQuantity()+quantity);
                return cartReposiitory.save(cart);
            }
        }
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(quantity);
        cartItem.setTovar(t);
        cart.getItems().add(cartItem);
        return cartReposiitory.save(cart);

    }
}
