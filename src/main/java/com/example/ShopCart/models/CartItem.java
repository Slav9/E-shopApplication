package com.example.ShopCart.models;

import javax.persistence.*;

@Entity
@Table(name="cartitem")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private int quantity;


    @ManyToOne(fetch = FetchType.EAGER)
    private tovar tovar;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public com.example.ShopCart.models.tovar getTovar() {
        return tovar;
    }

    public void setTovar(com.example.ShopCart.models.tovar tovar) {
        this.tovar = tovar;
    }



}
