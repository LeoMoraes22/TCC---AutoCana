package com.autocana.listas;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autocana.R;
import com.autocana.cadastros.CadEstimativas;
import com.autocana.cadastros.CadInsumos;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterProcInsumos extends RecyclerView.Adapter<AdapterProcInsumos.MyViewHolder> {

    Context context;
    ArrayList<UserListProcInsumos> userListArrayList;
    FirebaseFirestore db;
    CollectionReference collectionReference;

    public void setListaFiltrada(ArrayList<UserListProcInsumos> listaFiltrada){
        this.userListArrayList = listaFiltrada;
        notifyDataSetChanged();
    }

    public AdapterProcInsumos(Context context, ArrayList<UserListProcInsumos> userListArrayList) {
        this.context = context;
        this.userListArrayList = userListArrayList;
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("insumo");
    }

    @NonNull
    @Override
    public AdapterProcInsumos.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_proc_insumos, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProcInsumos.MyViewHolder holder, int position) {

        UserListProcInsumos userList = userListArrayList.get(position);

        holder.nomeInsumo.setText(userList.nomeInsumo);
        holder.descricao.setText(userList.descricao);
        holder.concentracao.setText(userList.concentracao);
        holder.unidade.setText(userList.unidade);

        holder.btn_selecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionar(userList.nomeInsumo, userList.concentracao, userList.unidade, userList.descricao);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userListArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeInsumo, descricao, concentracao, unidade;
        ImageButton btn_selecionar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeInsumo = itemView.findViewById(R.id.tvNomeInsumo);
            descricao = itemView.findViewById(R.id.tvDescricao);
            concentracao = itemView.findViewById(R.id.tvConcentracao);
            unidade = itemView.findViewById(R.id.tvUnidade);

            btn_selecionar = itemView.findViewById(R.id.btn_selecionar);
        }
    }


    private void selecionar(String nomeInsumo, String concentracao, String unidade, String descricao){
        Intent i = new Intent(context, CadEstimativas.class);
        i.putExtra("nomeInsumo", nomeInsumo);
        i.putExtra("concentracao", concentracao);
        i.putExtra("unidade", unidade);
        i.putExtra("descricao", descricao);
        context.startActivity(i);
    }

}
