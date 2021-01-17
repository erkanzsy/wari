package com.wari.repository;

import com.wari.model.Musteri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MusteriRepository extends JpaRepository<Musteri,Integer> {

    List<Musteri> findAllByKayitTarihi(LocalDate localDate);

    List<Musteri> findByAdContainingOrSoyadContaining(String name, String surname);

    List<Musteri> findByAdContaining(String name);

    List<Musteri> findTop5ByOrderByIdDesc();
}
