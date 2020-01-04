package com.wari.repository;

import com.wari.model.Islem;
import com.wari.model.Musteri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IslemRepository extends JpaRepository<Islem,Integer> {

    @Query(
            value = "SELECT * FROM wari.islemler where islemler.tutar > (SELECT SUM(odenen_tutar) FROM wari.odemeler where odemeler.islem_id = islemler.id )",
            nativeQuery = true)
    List<Islem> odemesiBitmeyenler();

    @Query(
            value = "SELECT * FROM wari.islemler where islemler.tutar > (SELECT SUM(odenen_tutar) FROM wari.odemeler where odemeler.islem_id = islemler.id ) and ((select max(odemeler.islem_tarihi) from odemeler where odemeler.islem_id = islemler.id) < date_sub(now(), interval 1 month));",
            nativeQuery = true)
    List<Islem> odemeBitmemisveBirAydirOdemeYapmayanlar();
    
    List<Islem> findByMusteriId(int id);

    List<Islem> findTop5ByOrderByIdDesc();

}
