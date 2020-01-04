package com.wari.controller;

import com.wari.model.Musteri;
import com.wari.services.MusteriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MusteriController {



    @Autowired
    MusteriService musteriService;

    @RequestMapping(value = "/musteribull", method = RequestMethod.GET)
    ResponseEntity<List<Musteri>> find(@RequestParam(value = "search", required = false) String q){
        System.out.println(q);
        return ResponseEntity.ok(musteriService.findByNameOrSurname(q.split("-")[0],q.split("-")[1]));
    }

}
