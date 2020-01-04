package com.wari.services;

import com.wari.model.Islem;
import com.wari.model.Odeme;
import com.wari.repository.IslemRepository;
import com.wari.repository.MusteriRepository;
import com.wari.repository.OdemeRepository;
import com.wari.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class OdemeService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OdemeRepository odemeRepository;

    @Autowired
    MusteriRepository musteriRepository;

    @Autowired
    IslemRepository islemRepository;


    public int bugunkiKazanc(){
       return odemeRepository.findAllByIslemTarihi(LocalDate.now()).stream().mapToInt(odeme -> odeme.getOdenenTutar()).sum();
    }

    public List<Odeme> bugunkiOdemeler(){
        List<Odeme> odemes = odemeRepository.findAllByIslemTarihi(LocalDate.now());
        Collections.reverse(odemes);
        return odemes;
    }

    public List<Odeme> findAllByOdemeAlanId(int id){
        return odemeRepository.findAllByOdemeAlanId(id);
    }

    public void saveOdemeByMusteriIdAndIslemId(int musteri_id, int islem_index, Odeme odeme, Principal principal) {
        Islem islem = islemRepository.findByMusteriId(musteri_id).get(islem_index);
        odeme.setIslemTarihi(LocalDate.now());
        odeme.setOdemeAlan(userRepository.findByEmail(principal.getName()));
        odeme.setIslem(islem);
        odemeRepository.save(odeme);
    }
}
