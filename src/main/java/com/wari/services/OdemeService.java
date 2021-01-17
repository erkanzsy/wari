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
import java.util.*;

@Service
public class OdemeService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OdemeRepository odemeRepository;

    @Autowired
    IslemRepository islemRepository;

    public int bugunkiKazanc(){
       return odemeRepository.findAllByIslemTarihi(LocalDate.now()).stream().mapToInt(odeme -> odeme.getOdenenTutar()).sum();
    }

    public List<Odeme> bugunkiSonOdemeler(){
        List<Odeme> odemes = odemeRepository.findAllByIslemTarihi(LocalDate.now());
        Collections.reverse(odemes);

        if (odemes.size() > 5){
            return Arrays.asList(odemes.get(0),odemes.get(1),odemes.get(2),odemes.get(3),odemes.get(4));
        }

        return odemes;
    }

    public List<Odeme> findAllByOdemeAlanId(int id){
        List<Odeme> odemes = odemeRepository.findAllByOdemeAlanId(id);
        Collections.reverse(odemes);
        return odemes;
    }

    public boolean saveOdemeByMusteriIdAndIslemId(int musteri_id, int islem_index, Odeme odeme, Principal principal) {
        Islem islem = islemRepository.findByMusteriId(musteri_id).get(islem_index);
        int toplam = islem.getOdemeler().stream().mapToInt(odeme1 -> odeme1.getOdenenTutar()).sum();
        int kalan = islem.getTutar()-toplam;

        if (islem.getTutar() >= odeme.getOdenenTutar() && odeme.getOdenenTutar() <= kalan){

            toplam += odeme.getOdenenTutar();

            odeme.setIslemTarihi(LocalDate.now());
            odeme.setOdemeAlan(userRepository.findByEmail(principal.getName()));

            odeme.setIslem(islem);
            odemeRepository.save(odeme);

            if (islem.getTutar() <= toplam){
                islem.setBitti(true);
                islemRepository.save(islem);
            }
            return true;
        }else{
            return false;
        }
    }
}
