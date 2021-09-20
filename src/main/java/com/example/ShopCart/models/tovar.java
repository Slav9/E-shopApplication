package com.example.ShopCart.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
public class tovar {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Please fill the name field")
    @Length(max = 40, min = 5, message = "Name must be between 5 and 40 characters")
    private String name;
    @NotBlank(message = "Please fill the articul field")
    @Length(max = 6,min = 6,message = "Articul is combination of 6 characters")
    private String articul;
    private String filename;
    @Min(value = 1,message = "Minimal price is 1₽")
    private int price;


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public tovar(){}

    public tovar(String name,int price, String articul, Users vendor) {
        this.name = name;
        this.price = price;
        this.articul = articul;
        this.vendor = vendor;
    }

}
