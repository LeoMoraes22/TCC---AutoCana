package com.autocana.listas;

import androidx.appcompat.app.AlertDialog;
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
import androidx.recyclerview.widget.RecyclerView;

import com.autocana.R;
import com.autocana.cadastros.CadInsumos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterInsumos extends RecyclerView.Adapter<AdapterInsumos.MyViewHolder> {

    Context context;
    ArrayList<UserListInsumos> userListArrayList;
    FirebaseFirestore db;
    CollectionReference collectionReference;

    public void setListaFiltrada(ArrayList<UserListInsumos> listaFiltrada){
        this.userListArrayList = listaFiltrada;
        notifyDataSetChanged();
    }

    public AdapterInsumos(Context context, ArrayList<UserListInsumos> userListArrayList) {
        this.context = context;
        this.userListArrayList = userListArrayList;
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("insumo");
    }

    @NonNull
    @Override
    public AdapterInsumos.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_insumos, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterInsumos.MyViewHolder holder, int position) {

        UserListInsumos userList = userListArrayList.get(position);

        holder.nomeInsumo.setText(userList.nomeInsumo);
        holder.descricao.setText(userList.descricao);
        holder.concentracao.setText(userList.concentracao);
        holder.unidade.setText(userList.unidade);

        holder.btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(userList.nomeInsumo, userList.concentracao, userList.unidade, userList.descricao);
            }
        });

        holder.btn_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluir(userList.getNomeInsumo());
            }
        });

    }

    @Override
    public int getItemCount() {
        return userListArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeInsumo, descricao, concentracao, unidade;
        ImageButton btn_excluir, btn_editar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeInsumo = itemView.findViewById(R.id.tvNomeInsumo);
            descricao = itemView.findViewById(R.id.tvDescricao);
            concentracao = itemView.findViewById(R.id.tvConcentracao);
            unidade = itemView.findViewById(R.id.tvUnidade);

            btn_editar = itemView.findViewById(R.id.btn_editar);
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

    private void editar(String nomeInsumo, String concentracao, String unidade, String descricao){
        Intent i = new Intent(context, CadInsumos.class);
        i.putExtra("nomeInsumo", nomeInsumo);
        i.putExtra("concentracao", concentracao);
        i.putExtra("unidade", unidade);
        i.putExtra("descricao", descricao);
        context.startActivity(i);
    }

}
