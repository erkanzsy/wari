package com.wari.repository;

import com.wari.model.Odeme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OdemeRepository extends JpaRepository<Odeme,Integer> {

    List<Odeme> findAllByIslemTarihi(LocalDate localDate);

    List<Odeme> findAllByOdemeAlanId(int id);




}
