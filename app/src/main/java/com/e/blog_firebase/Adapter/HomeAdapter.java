package com.e.blog_firebase.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.e.blog_firebase.Model.Postagem;
import com.e.blog_firebase.Model.Usuario;
import com.e.blog_firebase.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private Context context;
    private ArrayList<Postagem> postagemList;

    public HomeAdapter (Context context, ArrayList<Postagem> postagemList){
        this.context = context;
        this.postagemList = postagemList;
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageItem;
        private CircleImageView imagePerfilItem;
        private TextView txtTituloItem;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            imageItem = itemView.findViewById(R.id.imageItem);
            imagePerfilItem = itemView.findViewById(R.id.imagePerfilItem);
            txtTituloItem = itemView.findViewById(R.id.txtTituloItem);

        }
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_main,parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Postagem postagem = postagemList.get(position);
        holder.txtTituloItem.setText(postagem.getTitulo());
        //Glide.with(context).load(postagemList.get(position).getImage()).into(holder.imageItem);
        String imgPostagem = postagem.getImage();
        Picasso.get().load(imgPostagem).into(holder.imageItem);






    }

    @Override
    public int getItemCount() {
        return postagemList.size();
    }


}
