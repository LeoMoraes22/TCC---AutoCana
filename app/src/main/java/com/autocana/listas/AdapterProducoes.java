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
import com.autocana.cadastros.CadProducoes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterProducoes extends RecyclerView.Adapter<AdapterProducoes.MyViewHolder> {

    Context context;
    ArrayList<UserListProducoes> userListArrayList;
    FirebaseFirestore db;
    CollectionReference collectionReference;

    public void setListaFiltrada(ArrayList<UserListProducoes> listaFiltrada){
        this.userListArrayList = listaFiltrada;
        notifyDataSetChanged();
    }

    public AdapterProducoes(Context context, ArrayList<UserListProducoes> userListArrayList) {
        this.context = context;
        this.userListArrayList = userListArrayList;
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("producoes");
    }

    @NonNull
    @Override
    public AdapterProducoes.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_producoes, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterProducoes.MyViewHolder holder, int position) {

        UserListProducoes userList = userListArrayList.get(position);

        holder.nomePlantacao.setText(userList.nomePlantacao);
        holder.colhido.setText(userList.colhido);
        holder.dataInicial.setText(userList.dataInicial);
        holder.dataFinal.setText(userList.dataFinal);
        holder.descricao.setText(userList.descricao);

        holder.btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(userList.nomePlantacao, userList.dataInicial, userList.dataFinal,userList.colhido, userList.descricao);
            }
        });

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

        TextView nomePlantacao, dataInicial, dataFinal, colhido, descricao;
        ImageButton btn_excluir, btn_editar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomePlantacao = itemView.findViewById(R.id.tvNomePlantacao);
            dataFinal = itemView.findViewById(R.id.tvDataFinal);
            dataInicial = itemView.findViewById(R.id.tvDataInicial);
            colhido = itemView.findViewById(R.id.tvColhido);
            descricao = itemView.findViewById(R.id.tvDescricao);

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
                                    Toast.makeText(context, "Produção deletado com sucesso!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(context, "Erro ao exluir a produção!", Toast.LENGTH_SHORT).show();
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

    private void editar(String nomePlantacao, String dataInicial, String dataFinal, String colhido, String descricao){
        Intent i = new Intent(context, CadProducoes.class);
        i.putExtra("nomePlantacao", nomePlantacao);
        i.putExtra("dataInicial", dataInicial);
        i.putExtra("dataFinal", dataFinal);
        i.putExtra("colhido", colhido);
        i.putExtra("descricao", descricao);
        context.startActivity(i);
    }
}
