package com.wari.controller;

import com.wari.model.Islem;
import com.wari.model.Odeme;
import com.wari.repository.IslemRepository;
import com.wari.services.IslemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IslemController {

    @Autowired
    IslemService islemService;

    @Autowired
    IslemRepository islemRepository;

    @RequestMapping(value = "sorunlu-islemler", method = RequestMethod.GET)
    public ResponseEntity<List<Islem>> sorunluIslemler(){
        return ResponseEntity.ok(islemService.sorunluIslemler());
    }

    @RequestMapping("odemesi-bitmeyenler")
    public ResponseEntity<List<Islem>> odemesiBitmeyenler(){
        return ResponseEntity.ok(islemService.odemeBitmeyenler());
    }

    @GetMapping("odemepddf/{musteri_id}/{islem_index}")
    public ResponseEntity<List<Odeme>> odemeByMusteriIdAndOdemeIndex(@PathVariable("musteri_id") int musteri_id, @PathVariable("islem_index") int islem_index){
        Islem islem = islemRepository.findByMusteriId(musteri_id).get(islem_index);

        return ResponseEntity.ok(islem.getOdemeler());
    }


}
