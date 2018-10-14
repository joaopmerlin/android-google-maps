package br.edu.utfpr.mapa.service.impl;

import android.content.Context;
import br.edu.utfpr.mapa.dao.AreaDao;
import br.edu.utfpr.mapa.dao.PontoDao;
import br.edu.utfpr.mapa.database.AppDatabase;
import br.edu.utfpr.mapa.entity.Area;
import br.edu.utfpr.mapa.entity.Ponto;
import br.edu.utfpr.mapa.service.AreaService;

import java.util.ArrayList;
import java.util.List;

public class AreaServiceImpl implements AreaService {

    private Context context;
    private AreaDao areaDao;
    private PontoDao pontoDao;

    public AreaServiceImpl(Context context) {
        this.context = context;
        this.areaDao = AppDatabase.get(context).areaDao();
        this.pontoDao = AppDatabase.get(context).pontoDao();
    }

    @Override
    public List<Area> findAll() {
        return areaDao.findAll();
    }

    @Override
    public Area findById(Integer id) {
        Area area = areaDao.findById(id);
        area.setPontos(new ArrayList<>(pontoDao.findByAreaId(id)));
        return area;
    }

    @Override
    public void save(Area area) {
        if (area.getId() == null) {
            Long id = areaDao.insert(area);

            for (Ponto ponto : area.getPontos()) {
                ponto.setAreaId(id.intValue());
            }

            pontoDao.insert(area.getPontos().toArray(new Ponto[]{}));
        } else {
            areaDao.update(area);
            pontoDao.deleteByAreaId(area.getId());

            for (Ponto ponto : area.getPontos()) {
                ponto.setAreaId(area.getId());
            }

            pontoDao.insert(area.getPontos().toArray(new Ponto[]{}));
        }
    }
}
