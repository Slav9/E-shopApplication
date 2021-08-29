package com.example.ShopCart.models;

import javax.persistence.*;

@Entity
public class tovar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name, art, articul;
    private int price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users vendor;

    public String getVendorName(){
        return vendor !=null ? vendor.getUsername(): "<none>";
    }

    public Users getVendor() {
        return vendor;
    }

    public void setVendor(Users vendor) {
        this.vendor = vendor;
    }

    public String getArticul() {
        return articul;
    }

    public void setArticul(String articul) {
        this.articul = articul;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    tovar(){}

    public tovar(String name, String art,int price, String articul, Users vendor) {
        this.name = name;
        this.art = art;
        this.price = price;
        this.articul = articul;
        this.vendor = vendor;
    }
}
