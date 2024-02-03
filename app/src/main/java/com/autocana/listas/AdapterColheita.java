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
import com.autocana.cadastros.CadColheita;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterColheita extends RecyclerView.Adapter<AdapterColheita.MyViewHolder> {

    Context context;
    ArrayList<UserListColheita> userListArrayList;
    FirebaseFirestore db;
    CollectionReference collectionReference;

    public void setListaFiltrada(ArrayList<UserListColheita> listaFiltrada){
        this.userListArrayList = listaFiltrada;
        notifyDataSetChanged();
    }

    public AdapterColheita(Context context, ArrayList<UserListColheita> userListArrayList) {
        this.context = context;
        this.userListArrayList = userListArrayList;
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("colheita");
    }

    @NonNull
    @Override
    public AdapterColheita.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_colheita, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterColheita.MyViewHolder holder, int position) {

        UserListColheita userList = userListArrayList.get(position);

        holder.plantacao.setText(userList.plantacao);
        holder.anotacoes.setText(userList.anotacoes);
        holder.unidade.setText(userList.unidade);
        holder.quantidade.setText(userList.quantidade);
        holder.placa.setText(userList.placa);
        holder.dataColheita.setText(userList.dataColheita);

        holder.btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(userList.plantacao, userList.anotacoes, userList.unidade,userList.quantidade, userList.placa, userList.dataColheita);
            }
        });

        holder.btn_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluir(userList.getPlantacao());
            }
        });

    }

    @Override
    public int getItemCount() {
        return userListArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView plantacao, anotacoes, placa, unidade, quantidade, dataColheita;
        ImageButton btn_excluir, btn_editar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            plantacao = itemView.findViewById(R.id.tvPlantacao);
            anotacoes = itemView.findViewById(R.id.tvAnotacoes);
            placa = itemView.findViewById(R.id.tvPlaca);
            quantidade = itemView.findViewById(R.id.tvQuantidade);
            unidade = itemView.findViewById(R.id.tvUnidade);
            dataColheita = itemView.findViewById(R.id.tvDataColheita);

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
                                    Toast.makeText(context, "Colheita deletado com sucesso!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(context, "Erro ao exluir o colheita!", Toast.LENGTH_SHORT).show();
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

    private void editar(String plantacao, String anotacoes, String unidade, String quantidade, String placa, String dataColheita){
        Intent i = new Intent(context, CadColheita.class);
        i.putExtra("plantacao", plantacao);
        i.putExtra("anotacoes", anotacoes);
        i.putExtra("unidade", unidade);
        i.putExtra("quantidade", quantidade);
        i.putExtra("placa", placa);
        i.putExtra("dataColheita", dataColheita);
        context.startActivity(i);
    }
}
