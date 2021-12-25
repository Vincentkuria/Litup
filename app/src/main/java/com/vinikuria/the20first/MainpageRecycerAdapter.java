package com.vinikuria.the20first;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainpageRecycerAdapter extends RecyclerView.Adapter {

    Context context;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    String ADVIEW="advert";

    public MainpageRecycerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (StoreLoadedData.linkedList.get(position).equals(ADVIEW)){
            return 0;
        }else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view;
        if (viewType==0){
            view=layoutInflater.inflate(R.layout.adview1,parent,false);
            return new AdView1(view);
        }else{
            view= layoutInflater.inflate(R.layout.single_recycler1,parent,false);
            return new SingleRecycler(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (StoreLoadedData.linkedList.get(position).equals(ADVIEW)){

        }else {
            database.getReference()
                    .child("USERS")
                    .child(StoreLoadedData.linkedList.get(position))
                    .child("POSTS").limitToLast(1).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String url="";
                    HashMap<String,String> hashMap= (HashMap<String, String>) snapshot.getValue();
                    Iterator iterator=hashMap.entrySet().iterator();
                    Map.Entry mapEntry= (Map.Entry) iterator.next();
                    url= (String) mapEntry.getValue();
                    Glide.with(context).load(url).into(((SingleRecycler) holder).imageView1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            database.getReference()
                    .child("USERS")
                    .child(StoreLoadedData.linkedList.get(position))
                    .child("PROFILE")
                    .child("Online Status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.getValue()=="true"){
                        ((SingleRecycler) holder).online_status.setVisibility(View.VISIBLE);
                    }else {
                        ((SingleRecycler) holder).online_status.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




    }

    @Override
    public int getItemCount() {
        return StoreLoadedData.linkedList.size();
    }

    class SingleRecycler extends RecyclerView.ViewHolder{

        ShapeableImageView imageView1;
        ImageView online_status;

        public SingleRecycler(@NonNull View itemView) {
            super(itemView);
            imageView1=itemView.findViewById(R.id.imageView1);
            online_status=itemView.findViewById(R.id.online_status);
        }
    }

    class AdView1 extends RecyclerView.ViewHolder{

        public AdView1(@NonNull View itemView) {
            super(itemView);
        }
    }

}
