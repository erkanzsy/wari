package com.wari.services;

import com.wari.model.Islem;
import com.wari.repository.IslemRepository;
import com.wari.repository.MusteriRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IslemService {

    @Autowired
    IslemRepository islemRepository;

    @Autowired
    MusteriRepository musteriRepository;

    public List<Islem> sorunluIslemler(){
        return islemRepository.odemeBitmemisveBirAydirOdemeYapmayanlar();
    }

    public List<Islem> finfByMusteriId(int id){ return islemRepository.findByMusteriId(id);}

    public void save(Islem islem, int musteri_id) {
        islem.setMusteri(musteriRepository.findById(musteri_id).get());
        islem.setTarih(LocalDate.now());
        islemRepository.save(islem);
    }

    public List<Islem> findLastFive(){
        List<Islem> sonIslemlerSirali = islemRepository.findTop5ByOrderByIdDesc();
        return sonIslemlerSirali;
    }
}
