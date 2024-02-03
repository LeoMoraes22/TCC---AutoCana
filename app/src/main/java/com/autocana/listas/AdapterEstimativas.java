package com.autocana.listas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.autocana.Estimativas;
import com.autocana.R;
import com.autocana.cadastros.CadEstimativas;
import com.autocana.cadastros.CadPlantacoes;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterEstimativas extends RecyclerView.Adapter<AdapterEstimativas.MyViewHolder> {

    Context context;
    ArrayList<UserListEstimativas> userListArrayList;
    FirebaseFirestore db;
    CollectionReference collectionReference;

    public void setListaFiltrada(ArrayList<UserListEstimativas> listaFiltrada){
        this.userListArrayList = listaFiltrada;
        notifyDataSetChanged();
    }

    public AdapterEstimativas(Context context, ArrayList<UserListEstimativas> userListArrayList) {
        this.context = context;
        this.userListArrayList = userListArrayList;
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("estimativa");
    }

    @NonNull
    @Override
    public AdapterEstimativas.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_estimativa, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEstimativas.MyViewHolder holder, int position) {

        UserListEstimativas userList = userListArrayList.get(position);

        holder.nomePlantacao.setText(userList.nomePlantacao);
        holder.nomeInsumo.setText(userList.nomeInsumo);
        holder.quantProducao.setText(userList.quantProducao);
        holder.quantInsumo.setText(userList.quantInsumo);
        holder.hectares.setText(userList.hectares);
        holder.alqueires.setText(userList.alqueires);


        holder.btn_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluir(userList.getNomePlantacao());
            }
        });

    }

    @Override
    public int getItemCount() {
        return userListArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomePlantacao, nomeInsumo, quantProducao, quantInsumo, hectares, alqueires;
        ImageButton btn_excluir;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomePlantacao = itemView.findViewById(R.id.tvnomePlantacao);
            nomeInsumo = itemView.findViewById(R.id.tvnomeInsumo);
            quantProducao = itemView.findViewById(R.id.tvquantProducao);
            quantInsumo = itemView.findViewById(R.id.tvquantInsumo);
            hectares = itemView.findViewById(R.id.tvhectares);
            alqueires = itemView.findViewById(R.id.tvalqueires);

            btn_excluir = itemView.findViewById(R.id.btn_excluir);
        }
    }

    private void excluir(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Deseja exluir este registro?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                collectionReference.document(id).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(context, "Insumo deletado com sucesso!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(context, "Erro ao exluir o insumo!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
