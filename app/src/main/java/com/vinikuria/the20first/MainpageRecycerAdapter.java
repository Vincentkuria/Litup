package com.vinikuria.the20first;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.facebook.shimmer.ShimmerFrameLayout;
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
        Log.d("LitupDebug","getItemViewType");
        if (StoreLoadedData.linkedList.get(position).equals(ADVIEW)){
            return 0;
        }else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("LitupDebug","onCreateViewHolder");
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
        Log.d("LitupDebug","onBindViewHolder");
        SingleRecycler singleRecycler= (SingleRecycler) holder;
        if (StoreLoadedData.linkedList.get(position).equals(ADVIEW)){

        }else {

            Shimmer shimmer=new Shimmer.ColorHighlightBuilder()
                    .setFixedHeight(200)
                    .setHighlightColor(Color.parseColor("#FCD3FF"))
                    .setHighlightAlpha(1)
                    .setDropoff(50)
                    .build();

            ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
            shimmerDrawable.setShimmer(shimmer);
            singleRecycler.shimmerFrameLayout.startShimmer();
            Glide.with(context).load(shimmerDrawable).into(singleRecycler.imageView1);

                String user=StoreLoadedData.linkedList.get(position);
                database.getReference()
                        .child("USERS")
                        .child(user)
                        .child("POSTS").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String,String> hashMap= (HashMap<String, String>) snapshot.getValue();
                        Iterator iterator=hashMap.entrySet().iterator();
                        Map.Entry mapEntry= (Map.Entry) iterator.next();
                        String s3 = (String) mapEntry.getValue();
                        Glide.with(context).load(s3).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                singleRecycler.shimmerFrameLayout.stopShimmer();
                                singleRecycler.shimmerFrameLayout.setVisibility(View.GONE);
                                return false;
                            }
                        }).placeholder(shimmerDrawable).into(singleRecycler.imageView1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            database.getReference()
                    .child("USERS")
                    .child(user)
                    .child("PROFILE")
                    .child("Online Status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        if (snapshot.getValue().equals("true")){
                            ((SingleRecycler) holder).online_status.setVisibility(View.VISIBLE);
                        }else {
                            ((SingleRecycler) holder).online_status.setVisibility(View.INVISIBLE);
                        }
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
        Log.d("LitupDebug","getItemCount");
        Log.d("LitupDebug",String.valueOf(StoreLoadedData.linkedList.size()));
        return StoreLoadedData.linkedList.size();
    }

    class SingleRecycler extends RecyclerView.ViewHolder{

        ShapeableImageView imageView1;
        ImageView online_status;
        ShimmerFrameLayout shimmerFrameLayout;

        public SingleRecycler(@NonNull View itemView) {
            super(itemView);
            imageView1=itemView.findViewById(R.id.imageView1);
            online_status=itemView.findViewById(R.id.online_status);
            shimmerFrameLayout=itemView.findViewById(R.id.shimmerFrame1);
        }
    }

    class AdView1 extends RecyclerView.ViewHolder{

        public AdView1(@NonNull View itemView) {
            super(itemView);
        }
    }

}
