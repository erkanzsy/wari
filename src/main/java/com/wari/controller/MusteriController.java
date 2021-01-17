package com.wari.controller;

import com.wari.model.Islem;
import com.wari.model.Musteri;
import com.wari.model.Odeme;
import com.wari.repository.IslemRepository;
import com.wari.services.IslemService;
import com.wari.services.MusteriService;
import com.wari.services.OdemeService;
import com.wari.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class MusteriController {

    @Autowired
    MusteriService musteriService;

    @Autowired
    IslemService islemService;

    @Autowired
    OdemeService odemeService;

    @Autowired
    IslemRepository islemRepository;

    @Autowired
    UserService userService;

    @RequestMapping("/musteriler")
    public String musteriler(Model model){
        model.addAttribute("musteri", new Musteri());
        model.addAttribute("searchresult", musteriService.findLastRecord());
        return "musteriler";
    }

    @PostMapping("kayit")
    public String musteriKayit(@Valid Musteri musteri){
        musteriService.save(musteri);
        return "redirect:/musteri/"+musteri.getId()+"?kayit";
    }

    @RequestMapping("/musteri/{id}")
    public String musteriPage(@PathVariable("id") int id, Model model){
        model.addAttribute("musteri", musteriService.getMusteri(id));
        model.addAttribute("islemler",islemService.finfByMusteriId(id) );
        model.addAttribute("odeme", new Odeme());
        model.addAttribute("islem", new Islem());
        return "musteri.html";
    }

    @PostMapping("/musteri/{id}/update")
    public String musteriUpdate(@PathVariable("id") int id,@ModelAttribute Musteri musteri){
        musteriService.save(id,musteri);
        return "redirect:/musteri/"+id+"?updated";
    }

    @RequestMapping(value = "/bul", produces= MediaType.TEXT_PLAIN_VALUE)
    public String musteribul(@RequestParam(value = "keyword", required = false) String keyword, Model model){

        if (keyword.contains(" ")){
            model.addAttribute("searchresult", musteriService.findByNameOrSurname(keyword.split(" ")[0],keyword.split(" ")[1]));
        }else {
            model.addAttribute("searchresult", musteriService.findByNameContaining(keyword));
        }
        model.addAttribute("musteri", new Musteri());

        return "musteriler";
    }
}
