package br.edu.utfpr.mapa.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import br.edu.utfpr.mapa.entity.Ponto;

import java.util.List;

@Dao
public interface PontoDao {

    @Query("select * from Ponto where areaId = :areaId")
    List<Ponto> findByAreaId(Integer areaId);

    @Query("delete from Ponto where areaId = :areaId")
    void deleteByAreaId(Integer areaId);

    @Insert
    void insert(Ponto... pontos);

}
