package com.example.ShopCart.Controllers;

import com.example.ShopCart.models.Users;
import com.example.ShopCart.models.tovar;
import com.example.ShopCart.repo.allGoodsRepository;
import org.hibernate.annotations.OrderBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class MainController {

    @GetMapping("/")
    public String greeting(Model model) {
        return "StartPage";
    }

    @Autowired
    private allGoodsRepository allgoodsRepository;


    @GetMapping("/catalog")
    public String catalog(Model model) {
        Iterable<tovar> goods = allgoodsRepository.findAll();
        model.addAttribute("tovars",goods);
        return "catalog";
    }

    @GetMapping("/catalog/add")
    public String catalogAdd(Model model) {
        return "catalog-add";
    }

    @PostMapping ("/catalog/add")
    public String addTovar(@AuthenticationPrincipal Users user, @RequestParam String name, @RequestParam String art, @RequestParam int price, @RequestParam String articul, Model model){
        tovar tovar = new tovar(name,art,price,articul,user);
        allgoodsRepository.save(tovar);
        return "redirect:/catalog";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        return "cart";
    }

    @PostMapping ("remove{id}")
    public String remove(@PathVariable(value = "id") long id, Model model){
        tovar tovar = allgoodsRepository.findById(id).orElseThrow();
        allgoodsRepository.delete(tovar);
        return "redirect:/catalog";
    }

    @GetMapping("/catalog/{id}/edit")
    public String catalogEdit(@PathVariable(value = "id") long id, Model model) {
        Optional <tovar> tovar= allgoodsRepository.findById(id);
        ArrayList <tovar> tov = new ArrayList<>();
        tovar.ifPresent(tov::add);
        model.addAttribute("tovar",tov);
        return "catalog-edit";
    }

    @PostMapping ("/catalog/{id}/edit")
    public String edit(@PathVariable(value = "id") long id, @RequestParam String name,@RequestParam String art,@RequestParam String articul,@RequestParam int price, Model model){
        tovar tovar = allgoodsRepository.findById(id).orElseThrow();
        tovar.setName(name);
        tovar.setArt(art);
        tovar.setArticul(articul);
        tovar.setPrice(price);
        allgoodsRepository.save(tovar);
        return "redirect:/catalog";
    }

}
