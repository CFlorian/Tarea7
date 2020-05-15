package com.cksolutions.tarea7.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cksolutions.tarea7.R;
import com.cksolutions.tarea7.model.FotoModel;

import java.util.ArrayList;

public class ListCustomAdapter extends ArrayAdapter<FotoModel> {

    private ArrayList<FotoModel> dataSet;
    Context mContext;


    private static class ViewHolder {
        ImageView ivNombre;
    }


    public ListCustomAdapter (Context context, ArrayList<FotoModel> fotos){
        super(context, R.layout.list_item, fotos);
        this.dataSet = fotos;
        this.mContext=context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FotoModel fotoModel = getItem(position);
        ListCustomAdapter.ViewHolder viewHolder;

        final View result;
        if (convertView == null){
            viewHolder = new ListCustomAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder.ivNombre = convertView.findViewById(R.id.ivFoto);

            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ListCustomAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }


        viewHolder.ivNombre.setImageURI(Uri.parse(fotoModel.getNombre()));
        return convertView;
    }
}
