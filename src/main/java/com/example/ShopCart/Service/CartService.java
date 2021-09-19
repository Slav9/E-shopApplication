package com.example.ShopCart.Service;

import com.example.ShopCart.models.Cart;
import com.example.ShopCart.models.CartItem;
import com.example.ShopCart.models.tovar;
import com.example.ShopCart.repo.CartReposiitory;
import com.example.ShopCart.repo.allGoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Set;

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
        Boolean productIsInCart = false;
        if (cart!=null) {
            Set<CartItem> items=cart.getItems();
            for (CartItem item : items) {
                if (item.getTovar().equals(t)) {
                    productIsInCart=true;
                    item.setQuantity(item.getQuantity()+quantity);
                    cart.setItems(items);
                    return cartReposiitory.saveAndFlush(cart);
                }
            }
        }
        if(!productIsInCart&&(cart!=null)) {
            CartItem cartItem1 = new CartItem();
            cartItem1.setQuantity(quantity);
            cartItem1.setTovar(t);
            cart.getItems().add(cartItem1);
            return cartReposiitory.saveAndFlush(cart);
        }

        return this.addCart(id, sessionValue, quantity);
    }

    public Cart getCartBySessionValue(String sessionValue) {
        return cartReposiitory.findBySessionValue(sessionValue);
    }
}
