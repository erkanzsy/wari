package com.wari.db;

import com.wari.model.Islem;
import com.wari.model.Musteri;
import com.wari.model.Odeme;
import com.wari.model.User;
import com.wari.repository.MusteriRepository;
import com.wari.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;

@Service
public class InitDB implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MusteriRepository musteriRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){
        if (userRepository.count() == 0 ){
            ekle();
        }
    }

    private void ekle(){
        User user = new User();
        user.setAd("Ali");
        user.setSoyad("Veli");
        user.setTc("1234567890");
        user.setTelefonNumarasi("53552535533");
        user.setAdres("OrasıBurası/FalanFilan");
        user.setEmail("ali@veli.com");
        user.setParola(passwordEncoder.encode("av"));
        user.setRole("ADMIN,SUPER");
        user.setAktifMi(true);

        User user1 = new User();
        user1.setAd("Elif");
        user1.setSoyad("Veli");
        user1.setTelefonNumarasi("35533");
        user1.setAdres("Adana/FalanFilan");
        user1.setEmail("elif@veli.com");
        user1.setParola(passwordEncoder.encode("ev"));
        user1.setTc("90");
        user1.setRole("USER");
        user1.setAktifMi(true);

        User user12 = new User();
        user12.setAd("Erkan");
        user12.setSoyad("Öz");
        user12.setTelefonNumarasi("355123433");
        user12.setAdres("Bartin/FalanFilan");
        user12.setEmail("er@kan.com");
        user12.setParola(passwordEncoder.encode("ek"));
        user12.setTc("902345");
        user12.setRole("USER");
        user12.setAktifMi(false);

        userRepository.saveAll(Arrays.asList(user,user1,user12));

        Musteri musteri = new Musteri();
        musteri.setAd("Cengiz");
        musteri.setAdres("Corum/World");
        musteri.setAciklama("Deneme");
        musteri.setEmail("cen@giz.com");
        musteri.setSoyad("Insa");
        musteri.setKayitTarihi(LocalDate.of(2019,12,20));
        musteri.setTc("234543234566532");
        musteri.setTelefon("8238238832");

        Islem islem1 = new Islem();
        islem1.setAy(3);
        islem1.setTarih(LocalDate.of(2019,8,20));
        islem1.setTutar(200);
        islem1.setAciklama("Saat");
        islem1.setMusteri(musteri);

        Odeme odemeForIslem1 = new Odeme();
        odemeForIslem1.setIslemTarihi(LocalDate.of(2019,8,20));
        odemeForIslem1.setOdemeAlan(user);
        odemeForIslem1.setOdenenTutar(99);
        odemeForIslem1.setIslem(islem1);

        Odeme odeme1ForIslem1 = new Odeme();
        odeme1ForIslem1.setIslemTarihi(LocalDate.of(2019,9,20));
        odeme1ForIslem1.setOdemeAlan(user);
        odeme1ForIslem1.setOdenenTutar(100);
        odeme1ForIslem1.setIslem(islem1);

        islem1.setOdemeler(Arrays.asList(odemeForIslem1,odeme1ForIslem1));

        Musteri musteri1 = new Musteri();
        musteri1.setAd("Ceyhan");
        musteri1.setEmail("cey@han.com");
        musteri1.setAdres("Togo/World");
        musteri1.setAciklama("DenemeAçıklama");
        musteri1.setSoyad("Ma");
        musteri1.setKayitTarihi(LocalDate.now());
        musteri1.setTc("234543234566532");
        musteri1.setTelefon("2323232323");

        Islem islem2 = new Islem();
        islem2.setAy(4);
        islem2.setTarih(LocalDate.of(2019,9,20));
        islem2.setTutar(200);
        islem2.setBitti(true);
        islem2.setAciklama("Monster");
        islem2.setMusteri(musteri1);

        Odeme odeme3 = new Odeme();
        odeme3.setIslemTarihi(LocalDate.of(2019,10,20));
        odeme3.setOdemeAlan(user);
        odeme3.setOdenenTutar(100);
        odeme3.setIslem(islem2);

        Odeme odeme4 = new Odeme();
        odeme4.setIslemTarihi(LocalDate.now());
        odeme4.setOdemeAlan(user);
        odeme4.setOdenenTutar(100);
        odeme4.setIslem(islem2);

        islem2.setOdemeler(Arrays.asList(odeme3, odeme4));

        musteri1.setIslemler(Arrays.asList(islem1,islem2));

        musteriRepository.saveAll(Arrays.asList(musteri,musteri1));
    }
}
