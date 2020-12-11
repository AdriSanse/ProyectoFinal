package com.example.proyectofinal.Adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.proyectofinal.Objects.Sucesos;
import com.example.proyectofinal.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class AdaptadorSucesos extends FirestoreRecyclerAdapter<Sucesos, AdaptadorSucesos.ViewHolderSucesos> {

    ArrayList<Sucesos> misSucesos;
    private Context miContexto;

    public AdaptadorSucesos(@NonNull FirestoreRecyclerOptions<Sucesos> options, Context miContexto) {

        super(options);
        this.miContexto = miContexto;
    }

    @NonNull
    @Override
    public ViewHolderSucesos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View miView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sucesos_lista,parent,false);
        ViewHolderSucesos holder = new ViewHolderSucesos(miView);
        return holder;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolderSucesos holder, int position, @NonNull Sucesos suceso) {

        holder.asuntoTxt.setText(miContexto.getString(R.string.elAsuntofue)+" "+suceso.getAsunto()+" "+miContexto.getString(R.string.elDiaDe)+" "+suceso.getFecha()+" "+miContexto.getString(R.string.conUn)+" "+suceso.getGastoIngreso()+" "+miContexto.getString(R.string.de)+" "+suceso.getDinero()+"€");
        holder.nombreTxt.setText(suceso.getUsuario());
    }

    public class ViewHolderSucesos extends RecyclerView.ViewHolder {

        TextView nombreTxt, asuntoTxt;

        public ViewHolderSucesos(@NonNull View itemView) {
            super(itemView);
            nombreTxt = itemView.findViewById(R.id.nombreUsuario);
            asuntoTxt = itemView.findViewById(R.id.ArgumentoSuceso);

        }
    }
}