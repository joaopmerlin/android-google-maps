package br.edu.utfpr.mapa.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import br.edu.utfpr.mapa.entity.Area;

import java.util.List;

@Dao
public interface AreaDao {

    @Query("SELECT * FROM Area")
    List<Area> findAll();

    @Query("SELECT * FROM area where id = :id")
    Area findById(Integer id);

    @Insert
    long insert(Area area);

    @Update
    void update(Area area);

}
