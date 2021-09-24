// service for Cart model (all methods act like named)
package com.example.ShopCart.Service;

import com.example.ShopCart.models.Cart;
import com.example.ShopCart.models.CartItem;
import com.example.ShopCart.models.Tovar;
import com.example.ShopCart.repo.CartItemRepository;
import com.example.ShopCart.repo.CartRepository;
import com.example.ShopCart.repo.TovarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private TovarRepository TovarRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    public Cart addCart(Long id, String sessionValue, int quantity) {

        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(quantity);
        cartItem.setTovar(TovarRepository.findById(id).orElseThrow());
        cart.getItems().add(cartItem);
        cart.setSessionValue(sessionValue);
         return cartRepository.save(cart);
    }


    public Cart addToShoppingCart(Long id, String sessionValue, int quantity) {

        Cart cart = cartRepository.findBySessionValue(sessionValue);
        Tovar t = TovarRepository.findById(id).orElseThrow();
        boolean productIsInCart = false;
        if (cart!=null) {
            Set<CartItem> items=cart.getItems();
            for (CartItem item : items) {
                if (item.getTovar().equals(t)) {
                    productIsInCart=true;
                    item.setQuantity(item.getQuantity()+quantity);
                    cart.setItems(items);
                    return cartRepository.saveAndFlush(cart);
                }
            }
        }
        if(!productIsInCart && cart != null) {
            CartItem cartItem1 = new CartItem();
            cartItem1.setQuantity(quantity);
            cartItem1.setTovar(t);
            cart.getItems().add(cartItem1);
            return cartRepository.saveAndFlush(cart);
        }

        return this.addCart(id, sessionValue, quantity);
    }

    public Cart getCartBySessionValue(String sessionValue) {
        return cartRepository.findBySessionValue(sessionValue);
    }

    //this method update quantity of item in cart if it's already exists
    public CartItem updateCartItem(Long id, int quantity) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow();
        cartItem.setQuantity(quantity);
        return cartItemRepository.saveAndFlush(cartItem);
    }

    public Cart removeCartItemFromCart(Long id, String sessionValue) {

        Cart cart = cartRepository.findBySessionValue(sessionValue);
        Set<CartItem> items = cart.getItems();
        CartItem cartItem = null;
        for(CartItem item: items){
            if(item.getId().equals(id)){
                cartItem=item;

            }
        }
        items.remove(cartItem);
        cartItemRepository.delete(cartItem);
        cart.setItems(items);
        return cartRepository.save(cart);
    }

    public void clearCart(String sessionValue) {

        Cart cart = cartRepository.findBySessionValue(sessionValue);
        cartRepository.delete(cart);
    }
}
