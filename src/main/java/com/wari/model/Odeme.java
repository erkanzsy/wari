package com.wari.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "odemeler")
public class Odeme {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn
    private User odemeAlan;

    @ManyToOne
    @JoinColumn
    private Islem islem;

    private int odenenTutar;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    private LocalDate islemTarihi;

    public Odeme() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getOdemeAlan() {
        return odemeAlan;
    }

    public void setOdemeAlan(User odemeAlan) {
        this.odemeAlan = odemeAlan;
    }

    public Islem getIslem() {
        return islem;
    }

    public void setIslem(Islem islem) {
        this.islem = islem;
    }

    public int getOdenenTutar() {
        return odenenTutar;
    }

    public void setOdenenTutar(int odenenTutar) {
        this.odenenTutar = odenenTutar;
    }

    public LocalDate getIslemTarihi() {
        return islemTarihi;
    }

    public void setIslemTarihi(LocalDate islemTarihi) {
        this.islemTarihi = islemTarihi;
    }
}
