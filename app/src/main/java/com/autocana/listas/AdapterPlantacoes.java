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
import com.autocana.cadastros.CadPlantacoes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterPlantacoes extends RecyclerView.Adapter<AdapterPlantacoes.MyViewHolder> {

    Context context;
    ArrayList<UserListPlantacoes> userListArrayList;
    FirebaseFirestore db;
    CollectionReference collectionReference;

    public void setListaFiltrada(ArrayList<UserListPlantacoes> listaFiltrada){
        this.userListArrayList = listaFiltrada;
        notifyDataSetChanged();
    }

    public AdapterPlantacoes(Context context, ArrayList<UserListPlantacoes> userListArrayList) {
        this.context = context;
        this.userListArrayList = userListArrayList;
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("plantacoes");
    }

    @NonNull
    @Override
    public AdapterPlantacoes.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_plantacoes, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPlantacoes.MyViewHolder holder, int position) {

        UserListPlantacoes userList = userListArrayList.get(position);

        holder.nome.setText(userList.nome);
        holder.cidade.setText(userList.cidade);
        holder.estado.setText(userList.estado);
        holder.area.setText(userList.area);
        holder.descricao.setText(userList.descricao);


        holder.btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(userList.nome, userList.cidade, userList.estado,userList.area, userList.descricao);
            }
        });

        holder.btn_excluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                excluir(userList.getNome());
            }
        });

    }

    @Override
    public int getItemCount() {
        return userListArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nome, descricao, area, estado, cidade;
        ImageButton btn_excluir, btn_editar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            area = itemView.findViewById(R.id.tvArea);
            descricao = itemView.findViewById(R.id.tvdescricao);
            nome = itemView.findViewById(R.id.tvNome);
            estado = itemView.findViewById(R.id.tvEstado);
            cidade = itemView.findViewById(R.id.tvCidade);

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
                                    Toast.makeText(context, "Plantação deletado com sucesso!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(context, "Erro ao exluir o plantação!", Toast.LENGTH_SHORT).show();
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

    private void editar(String nome, String cidade, String estado, String area, String descricao){
        Intent i = new Intent(context, CadPlantacoes.class);
        i.putExtra("nome", nome);
        i.putExtra("cidade", cidade);
        i.putExtra("estado", estado);
        i.putExtra("area", area);
        i.putExtra("descricao", descricao);
        context.startActivity(i);
    }
}
