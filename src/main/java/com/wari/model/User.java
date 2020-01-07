package com.wari.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "kullanicilar")
public class User {

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

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getTelefonNumarasi() {
        return telefonNumarasi;
    }

    public void setTelefonNumarasi(String telefonNumarasi) {
        this.telefonNumarasi = telefonNumarasi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAktifMi() {
        return aktifMi;
    }

    public void setAktifMi(boolean aktifMi) {
        this.aktifMi = aktifMi;
    }

    public List<Odeme> getAlinanOdenemeler() {
        return alinanOdenemeler;
    }

    public void setAlinanOdenemeler(List<Odeme> alinanOdenemeler) {
        this.alinanOdenemeler = alinanOdenemeler;
    }
}
