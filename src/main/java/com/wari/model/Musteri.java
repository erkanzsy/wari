package com.wari.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "musteriler")
public class Musteri {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String ad;

    @NotNull
    private String soyad;

    @NotNull
    private String tc;

    @NotNull
    private String telefon;

    private String aciklama;

    private String adres;

    private String email;

    private LocalDate kayitTarihi;


    @JsonIgnore
    @OneToMany(mappedBy = "musteri", cascade = CascadeType.ALL)
    private List<Islem> islemler;
}
