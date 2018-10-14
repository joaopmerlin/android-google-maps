package br.edu.utfpr.mapa.service;

import br.edu.utfpr.mapa.entity.Area;

import java.util.List;

public interface AreaService {

    List<Area> findAll();

    Area findById(Integer id);

    void save(Area area);
}
