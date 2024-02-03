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
import com.autocana.cadastros.CadProducoes;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterProcPlantacoesProducoes extends RecyclerView.Adapter<AdapterProcPlantacoesProducoes.MyViewHolder> {

    Context context;
    ArrayList<UserListProcPlantacoesProducoes> userListArrayList;
    FirebaseFirestore db;
    CollectionReference collectionReference;

    public void setListaFiltrada(ArrayList<UserListProcPlantacoesProducoes> listaFiltrada){
        this.userListArrayList = listaFiltrada;
        notifyDataSetChanged();
    }

    public AdapterProcPlantacoesProducoes(Context context, ArrayList<UserListProcPlantacoesProducoes> userListArrayList) {
        this.context = context;
        this.userListArrayList = userListArrayList;
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("plantacoes");
    }

    @NonNull
    @Override
    public AdapterProcPlantacoesProducoes.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_proc_plantacoes_producoes, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProcPlantacoesProducoes.MyViewHolder holder, int position) {

        UserListProcPlantacoesProducoes userList = userListArrayList.get(position);

        holder.nome.setText(userList.nome);
        holder.cidade.setText(userList.cidade);
        holder.estado.setText(userList.estado);
        holder.area.setText(userList.area);
        holder.descricao.setText(userList.descricao);


        holder.btn_selecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionar(userList.nome);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userListArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nome, descricao, area, estado, cidade;
        ImageButton btn_selecionar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            area = itemView.findViewById(R.id.tvArea);
            descricao = itemView.findViewById(R.id.tvdescricao);
            nome = itemView.findViewById(R.id.tvNome);
            estado = itemView.findViewById(R.id.tvEstado);
            cidade = itemView.findViewById(R.id.tvCidade);

            btn_selecionar = itemView.findViewById(R.id.btn_selecionar);
        }
    }

    private void selecionar(String nome){
        Intent i = new Intent(context, CadProducoes.class);
        i.putExtra("nome", nome);
        context.startActivity(i);
    }
}
