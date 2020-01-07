package com.wari.controller;

import com.wari.model.Islem;
import com.wari.model.Odeme;
import com.wari.repository.IslemRepository;
import com.wari.services.IslemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class IslemController {

    @Autowired
    IslemService islemService;

    @PostMapping("islemEkle/{musteri_id}")
    public String islemEkle(@ModelAttribute Islem islem, @PathVariable("musteri_id") int musteri_id){
        islemService.save(islem, musteri_id);
        return "redirect:/musteri/"+ musteri_id+"?islem";
    }

}
