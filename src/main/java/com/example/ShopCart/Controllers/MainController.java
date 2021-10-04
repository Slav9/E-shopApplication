//Controller for catalog and start page
package com.example.ShopCart.Controllers;

import com.example.ShopCart.Service.FileService;
import com.example.ShopCart.models.Users;
import com.example.ShopCart.models.Tovar;
import com.example.ShopCart.repo.TovarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    private TovarRepository allgoodsRepository;
    @Autowired
    private FileService fileService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Model model) {
        return "StartPage";
    }


    @GetMapping("/catalog")
    public String catalog(Model model) {

        Iterable<Tovar> tovars = allgoodsRepository.findAll();
        model.addAttribute("tovars", tovars);
        return "catalog";
    }


    @PreAuthorize("hasAuthority('VENDOR')")
    @GetMapping("/catalog/add")
    public String catalogAdd(Model model) {

        model.addAttribute("tovar", new Tovar());
        return "catalog-add";
    }

    @PreAuthorize("hasAuthority('VENDOR')")
    @PostMapping("/catalog/add")
    public String addTovar(@AuthenticationPrincipal Users user,
                           @Valid Tovar tovar,
                           BindingResult bindingResult,
                           Model model,
                           @RequestParam("file") MultipartFile file) throws IOException {

        tovar.setVendor(user);
        if (bindingResult.hasErrors()) {
            return "catalog-add";
        } else {
            fileService.CheckFileDirectoryAndAddFileName(tovar, file);
            allgoodsRepository.save(tovar);
        }
        return "redirect:/catalog";
    }

    @PreAuthorize("hasAuthority('VENDOR')")
    @GetMapping("/catalog/{id}/edit")
    public String catalogEdit(@PathVariable(value = "id") long id, Model model) {

        Optional<Tovar> tovar = allgoodsRepository.findById(id);
        ArrayList<Tovar> tov = new ArrayList<>();
        tovar.ifPresent(tov::add);
        model.addAttribute("tovarList", tov);
        model.addAttribute("Tovar", new Tovar());
        return "catalog-edit";
    }

    @PreAuthorize("hasAuthority('VENDOR')")
    @PostMapping("/catalog/{id}/edit")
    public String edit(@AuthenticationPrincipal Users user,
                       @Valid Tovar tovar, BindingResult bindingResult, Model model,
                       @RequestParam("file") MultipartFile file) throws IOException {

        if (bindingResult.hasErrors()) {
            return "catalog-edit";
        } else {
            fileService.CheckFileDirectoryAndAddFileName(tovar, file);
            //if admin redacting vendor's staff, vendorname became "admin", can't understand how to fix it atm
            tovar.setVendor(user);
            allgoodsRepository.save(tovar);
        }
        return "redirect:/catalog";
    }

}
