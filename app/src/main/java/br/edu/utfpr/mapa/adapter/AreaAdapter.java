package br.edu.utfpr.mapa.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.edu.utfpr.mapa.R;
import br.edu.utfpr.mapa.entity.Area;

import java.util.List;

public class AreaAdapter extends BaseAdapter {

    private final List<Area> list;
    private final Activity act;

    public AreaAdapter(List<Area> list, Activity act) {
        this.list = list;
        this.act = act;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.item_list, parent, false);

        Area area = list.get(i);

        TextView nome = view.findViewById(R.id.nome);
        nome.setText(area.getNome());

        return view;
    }

}
