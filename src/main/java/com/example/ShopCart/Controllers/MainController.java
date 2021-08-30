package com.example.ShopCart.Controllers;

import com.example.ShopCart.models.Users;
import com.example.ShopCart.models.tovar;
import com.example.ShopCart.repo.allGoodsRepository;
import org.hibernate.annotations.OrderBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
public class MainController {

    @Autowired
    private allGoodsRepository allgoodsRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Model model) {
        return "StartPage";
    }


    @GetMapping("/catalog")
    public String catalog(Model model) {
        Iterable<tovar> goods = allgoodsRepository.findAll();
        model.addAttribute("tovars",goods);
        return "catalog";
    }

    @GetMapping("/cart")
    public String cart(Model model) {
        return "cart";
    }

    @PreAuthorize("hasAuthority('VENDOR')")
    @GetMapping("/catalog/add")
    public String catalogAdd(Model model) {
        return "catalog-add";
    }

    @PreAuthorize("hasAuthority('VENDOR')")
    @PostMapping ("/catalog/add")
    public String addTovar(@AuthenticationPrincipal Users user,
                           @RequestParam String name, @RequestParam int price, @RequestParam String articul,
                           @RequestParam("file") MultipartFile file,
                           Model model) throws IOException {
        tovar tovar = new tovar(name,price,articul,user);

        if(!file.isEmpty()){
            File uploadDir = new File(uploadPath);

            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            tovar.setFilename(resultFilename);
        }

        allgoodsRepository.save(tovar);
        return "redirect:/catalog";
    }


    @PreAuthorize("hasAuthority('VENDOR')")
    @PostMapping ("remove{id}")
    public String remove(@PathVariable(value = "id") long id, Model model){
        tovar tovar = allgoodsRepository.findById(id).orElseThrow();
        allgoodsRepository.delete(tovar);
        return "redirect:/catalog";
    }

    @PreAuthorize("hasAuthority('VENDOR')")
    @GetMapping("/catalog/{id}/edit")
    public String catalogEdit(@PathVariable(value = "id") long id, Model model) {
        Optional <tovar> tovar= allgoodsRepository.findById(id);
        ArrayList <tovar> tov = new ArrayList<>();
        tovar.ifPresent(tov::add);
        model.addAttribute("tovar",tov);
        return "catalog-edit";
    }

    @PreAuthorize("hasAuthority('VENDOR')")
    @PostMapping ("/catalog/{id}/edit")
    public String edit(@PathVariable(value = "id") long id, @RequestParam String name,@RequestParam String articul,@RequestParam int price, Model model){
        tovar tovar = allgoodsRepository.findById(id).orElseThrow();
        tovar.setName(name);
        tovar.setArticul(articul);
        tovar.setPrice(price);
        allgoodsRepository.save(tovar);
        return "redirect:/catalog";
    }

}
