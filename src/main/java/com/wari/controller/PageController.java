package com.wari.controller;

import com.wari.services.IslemService;
import com.wari.services.MusteriService;
import com.wari.services.OdemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @Autowired
    MusteriService musteriService;

    @Autowired
    IslemService islemService;

    @Autowired
    OdemeService odemeService;


    @RequestMapping("/")
    public String anasayfa(Model model) {
        model.addAttribute("musteri_sayisi", musteriService.musteriSayisi());
        model.addAttribute("bugunki_kazanc", odemeService.bugunkiKazanc());
        model.addAttribute("bugunki_kayit", musteriService.bugunkiKayitSayisi());
        model.addAttribute("son_islemler", islemService.findLastFive());
        model.addAttribute("sorunlu_islemler", islemService.sorunluIslemler());
        model.addAttribute("son_odemeler", odemeService.bugunkiSonOdemeler() );
        return "anasayfa.html";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }




}
