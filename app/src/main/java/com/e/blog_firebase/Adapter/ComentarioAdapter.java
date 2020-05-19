package com.e.blog_firebase.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.blog_firebase.Model.Comentario;
import com.e.blog_firebase.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder> {
    private Context context;
    private ArrayList<Comentario> comentarioArrayList;

    public ComentarioAdapter(Context context, ArrayList<Comentario> comentarioArrayList){
        this.context = context;
        this.comentarioArrayList = comentarioArrayList;
    }

    class ComentarioViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgPerfil;
        TextView txtNome, txtComentario;
        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPerfil = itemView.findViewById(R.id.imageViewPerfilComentario);
            txtNome = itemView.findViewById(R.id.txtNomeComentario);
            txtComentario = itemView.findViewById(R.id.txtComentario);
        }
    }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comentario, parent, false);
        return new ComentarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        Comentario comentario = comentarioArrayList.get(position);
        holder.txtNome.setText(comentario.getUserName());
        holder.txtComentario.setText(comentario.getComentario());
        String imgUser = comentario.getImgUser();
        Picasso.get().load(imgUser).into(holder.imgPerfil);


    }

    @Override
    public int getItemCount() {
        return comentarioArrayList.size();
    }


}
