package com.example.ShopCart.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name="shoppingcart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Transient
    private int totalPrice;
    @Transient
    private int itemsNumber;

    @OneToMany(cascade = CascadeType.ALL)
    private Collection <CartItem> items;

    private String sessionValue;

    public Cart(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotalPrice() {
        int sum =0;
        for(CartItem item: this.items ){
            sum+=item.getTovar().getPrice();
        }
        return sum;
    }


    public int getItemsNumber() {
        return this.items.size();
    }


    public Collection<CartItem> getItems() {
        return items;
    }

    public void setItems(Collection<CartItem> items) {
        this.items = items;
    }

    public String getSessionValue() {
        return sessionValue;
    }

    public void setSessionValue(String sessionValue) {
        this.sessionValue = sessionValue;
    }
}
