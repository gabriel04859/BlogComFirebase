package com.e.blog_firebase.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.blog_firebase.DetalhesPostActivity;
import com.e.blog_firebase.Model.PostUser;
import com.e.blog_firebase.Model.Postagem;
import com.e.blog_firebase.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostagensPerfilAdatper extends RecyclerView.Adapter<PostagensPerfilAdatper.PostagensViewHolder> {
    private Context context;
    private ArrayList<PostUser> postUsers;

    public PostagensPerfilAdatper(Context context, ArrayList<PostUser> postUsers ){
        this.context = context;
        this.postUsers = postUsers;
    }

    class PostagensViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public PostagensViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgPostagem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetalhesPostActivity.class);
                    int position = getAdapterPosition();

                }
            });
        }
    }


    @NonNull
    @Override
    public PostagensViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_perfil,parent, false);
        return new PostagensViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostagensViewHolder holder, int position) {
        PostUser postUser = postUsers.get(position);
        Picasso.get().load(postUser.getImgUrl()).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return postUsers.size();
    }






}
