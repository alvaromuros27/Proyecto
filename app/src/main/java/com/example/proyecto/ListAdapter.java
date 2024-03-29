package com.example.proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Registro> {
    public ListAdapter(Context context, List<Registro> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.list_item,
                    parent,
                    false);

            holder = new ViewHolder();

            holder.creacion = (TextView) convertView.findViewById(R.id.creacion);
            holder.nivel = (TextView) convertView.findViewById(R.id.nivel);
            holder.insulina = (TextView) convertView.findViewById(R.id.insulina);
            holder.medicamentos = (TextView) convertView.findViewById(R.id.medicamentos);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Registro registro = getItem(position);
        System.out.println(registro.getId());
        holder.creacion.setText(registro.getCreacion());
        holder.nivel.setText("Nivel: " + registro.getNivel()+" mg/dL");
        holder.insulina.setText("Insulina: " + registro.getInsulina()+" unidades");
        holder.medicamentos.setText("Medicamentos: " + registro.getMedicamentos());


        return convertView;
    }

    static class ViewHolder {


        TextView creacion;
        TextView nivel;
        TextView insulina;
        TextView medicamentos;

    }
}
