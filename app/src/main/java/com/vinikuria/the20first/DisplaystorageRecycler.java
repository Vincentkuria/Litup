package com.vinikuria.the20first;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.util.ArrayList;

public class DisplaystorageRecycler extends RecyclerView.Adapter {
    static ArrayList<File> files;
    Context context;
    static int clicked;

    public DisplaystorageRecycler(ArrayList<File> files, Context context) {
        this.files = files;
        this.context = context;
    }

    public DisplaystorageRecycler() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_storerecycler,parent,false);
        return new DisplayItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DisplayItem displayItem= (DisplayItem) holder;
        /**put placeholder on glide*/
        Glide.with(context).load(files.get(position)).into(displayItem.shapeableImageView);
        if (clicked==position+1){
            displayItem.imageView.setVisibility(View.VISIBLE);
        }else {
            displayItem.imageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class DisplayItem extends RecyclerView.ViewHolder{

        ShapeableImageView shapeableImageView;
        ImageView imageView;
        public DisplayItem(@NonNull View itemView) {
            super(itemView);
            shapeableImageView=itemView.findViewById(R.id.displayStoreItem);
            imageView=itemView.findViewById(R.id.selected1);

            shapeableImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clicked==getAdapterPosition()+1){
                        clicked=0;
                        imageView.setVisibility(View.INVISIBLE);
                        DisplaystorageActivity.postRelativeLayout.setVisibility(View.GONE);
                    }else {
                        clicked=getAdapterPosition()+1;
                        notifyDataSetChanged();
                        DisplaystorageActivity.postRelativeLayout.setVisibility(View.VISIBLE);
                        Glide.with(context).load(files.get(clicked-1)).placeholder(R.drawable.error).into(DisplaystorageActivity.selectedItem);
                    }

                }
            });
        }
    }
}
