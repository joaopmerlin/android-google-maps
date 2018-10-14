package br.edu.utfpr.mapa.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import br.edu.utfpr.mapa.dao.AreaDao;
import br.edu.utfpr.mapa.dao.PontoDao;
import br.edu.utfpr.mapa.entity.Area;
import br.edu.utfpr.mapa.entity.Ponto;
import br.edu.utfpr.mapa.util.Constants;

@Database(entities = {Area.class, Ponto.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AreaDao areaDao();
    public abstract PontoDao pontoDao();

    public static AppDatabase get(Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                Constants.DATABASE
        ).build();
    }

    public static AppDatabase getAllowMainThreadQueries(Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                Constants.DATABASE
        ).allowMainThreadQueries().build();
    }

}
