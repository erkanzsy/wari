package com.wari.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity(name = "kullanicilar")
@Getter
@Setter
public class User {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String ad;

    private String soyad;

    private String tc;

    private String parola;
    private String telefonNumarasi;
    private String email;
    private String adres;
    private String role;
    private boolean aktifMi;

    @OneToMany(mappedBy = "odemeAlan", cascade = CascadeType.ALL)
    private List<Odeme> alinanOdenemeler;

}
