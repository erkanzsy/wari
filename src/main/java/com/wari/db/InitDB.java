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
        user1.setAd("Gazmor");
        user1.setSoyad("Ab");
        user1.setTelefonNumarasi("35533");
        user1.setAdres("Arnavutluk/FalanFilan");
        user1.setEmail("gaz@more.com");
        user1.setParola(passwordEncoder.encode("ga"));
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
        musteri.setAd("Mohamed");
        musteri.setAdres("Mali/World");
        musteri.setAciklama("Deneme");
        musteri.setEmail("tro@tro.com");
        musteri.setSoyad("Tro");
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
        musteri1.setAd("Marwane");
        musteri1.setEmail("mar@wane.com");
        musteri1.setAdres("Togo/World");
        musteri1.setAciklama("DenemeAçıklama");
        musteri1.setSoyad("Ma");
        musteri1.setKayitTarihi(LocalDate.now());
        musteri1.setTc("234543234566532");
        musteri1.setTelefon("2323232323");

        Islem islemGİl = new Islem();
        islemGİl.setAy(4);
        islemGİl.setTarih(LocalDate.of(2019,9,20));
        islemGİl.setTutar(200);
        islemGİl.setBitti(true);
        islemGİl.setAciklama("Monster");
        islemGİl.setMusteri(musteri1);

        Odeme odemeForGil = new Odeme();
        odemeForGil.setIslemTarihi(LocalDate.of(2019,10,20));
        odemeForGil.setOdemeAlan(user);
        odemeForGil.setOdenenTutar(100);
        odemeForGil.setIslem(islemGİl);

        Odeme odeme1ForGil = new Odeme();
        odeme1ForGil.setIslemTarihi(LocalDate.now());
        odeme1ForGil.setOdemeAlan(user);
        odeme1ForGil.setOdenenTutar(100);
        odeme1ForGil.setIslem(islemGİl);


        islemGİl.setOdemeler(Arrays.asList(odemeForGil, odeme1ForGil));

        musteri1.setIslemler(Arrays.asList(islem1,islemGİl));


        musteriRepository.saveAll(Arrays.asList(musteri,musteri1));


    }
}
