package com.wari.services;

import com.wari.model.Musteri;
import com.wari.model.Odeme;
import com.wari.repository.MusteriRepository;
import com.wari.repository.OdemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MusteriService {

    @Autowired
    MusteriRepository musteriRepository;

    @Autowired
    OdemeRepository odemeRepository;

    public List<Musteri> sorunluMusteri(){
        return null;
    }

    public void save(Musteri musteri){
        musteriRepository.save(musteri);
    }

    public Musteri getMusteri(int id){
        return musteriRepository.findById(id).get();
    }

    public int musteriSayisi(){ return (int) musteriRepository.count(); };

    public int bugunkiKayitSayisi() {
        return musteriRepository.findAllByKayitTarihi(LocalDate.now()).size();
    }

    public List<Musteri> findByNameOrSurname(String name, String surname){
        return musteriRepository.findByAdLikeOrSoyadLike(name, surname);
    }

    public List<Musteri> findByName(String name){
        return musteriRepository.findByAdLike(name);
    }

    public List<Musteri> findByNameContaining(String name){
        return musteriRepository.findByAdContaining(name);
    }

    public List<Musteri> findLastRecord(){
        return musteriRepository.findTop5ByOrderByIdDesc();
    }

    public void save(int id, Musteri musteri) {

        Musteri dbMusteri = musteriRepository.findById(id).get();
        dbMusteri.setTelefon(musteri.getTelefon());
        dbMusteri.setTc(musteri.getTc());
        dbMusteri.setAd(musteri.getAd());
        dbMusteri.setSoyad(musteri.getSoyad());
        dbMusteri.setAciklama(musteri.getAciklama());
        dbMusteri.setAdres(musteri.getAdres());
        dbMusteri.setEmail(musteri.getEmail());

        musteriRepository.save(musteri);
    }
}
