package com.wari.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name = "islemler")
@Getter
@Setter
public class Islem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "musteri_id")
    private Musteri musteri;

    @JsonIgnore
    @OneToMany(mappedBy = "islem", cascade = CascadeType.ALL)
    private List<Odeme> odemeler;

    private String aciklama;

    private int tutar;

    private int ay;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    private LocalDate tarih;
}
