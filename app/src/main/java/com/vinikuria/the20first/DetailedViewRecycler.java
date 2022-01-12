package com.vinikuria.the20first;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DetailedViewRecycler extends RecyclerView.Adapter {
    Context context;
    String ADVIEW="advert";
    FirebaseDatabase database=FirebaseDatabase.getInstance();

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
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        if (viewType==0){
            View view=layoutInflater.inflate(R.layout.adview2,parent,false);
            return new Adview(view);
        }else {
            View view=layoutInflater.inflate(R.layout.single_recycler2,parent,false);
            return new SingleRecycler2(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (StoreLoadedData.linkedList.get(position).equals(ADVIEW)){

        }else {
            SingleRecycler2 singleRecycler2= (SingleRecycler2) holder;
            Shimmer shimmer=new Shimmer.ColorHighlightBuilder()
                    .setFixedHeight(200)
                    .setHighlightColor(Color.parseColor("#FCD3FF"))
                    .setHighlightAlpha(1)
                    .setDropoff(50)
                    .build();

            ShimmerDrawable shimmerDrawable=new ShimmerDrawable();
            shimmerDrawable.setShimmer(shimmer);
            singleRecycler2.shimmerFrameLayout.startShimmer();
            Glide.with(context).load(shimmerDrawable).into(singleRecycler2.imageview2);

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
                            singleRecycler2.shimmerFrameLayout.stopShimmer();
                            singleRecycler2.shimmerFrameLayout.setVisibility(View.GONE);
                            return false;
                        }
                    }).placeholder(shimmerDrawable).into(singleRecycler2.imageview2);
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
                            ((SingleRecycler2) holder).status_dot.setVisibility(View.VISIBLE);
                        }else {
                            ((SingleRecycler2) holder).status_dot.setVisibility(View.INVISIBLE);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            database.getReference()
                    .child("USERS")
                    .child(user)
                    .child("PROFILE")
                    .child("Name")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            singleRecycler2.name_detail.setText((String) snapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            database.getReference()
                    .child("USERS")
                    .child(user)
                    .child("PROFILE")
                    .child("Age")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            singleRecycler2.age_detail.setText((String) snapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            database.getReference()
                    .child("USERS")
                    .child(user)
                    .child("PROFILE")
                    .child("Height")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            singleRecycler2.height_detail.setText((String) snapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            database.getReference()
                    .child("USERS")
                    .child(user)
                    .child("PROFILE")
                    .child("Profile image url")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Glide.with(context).load(snapshot.getValue()).placeholder(R.drawable.ic_account_circle).into(singleRecycler2.profile_image1);
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

    class Adview extends RecyclerView.ViewHolder{

        public Adview(@NonNull View itemView) {
            super(itemView);
        }
    }

    class SingleRecycler2 extends RecyclerView.ViewHolder{
        ImageView imageview2,after_like,status_dot;
        ShapeableImageView profile_image1;
        TextView name_detail,age_detail,height_detail;
        ShimmerFrameLayout shimmerFrameLayout;
        public SingleRecycler2(@NonNull View itemView) {
            super(itemView);
            imageview2=itemView.findViewById(R.id.imageview2);
            after_like=itemView.findViewById(R.id.after_like);
            status_dot=itemView.findViewById(R.id.status_dot);
            profile_image1=itemView.findViewById(R.id.profile_image1);
            name_detail=itemView.findViewById(R.id.name_detail);
            age_detail=itemView.findViewById(R.id.age_detail);
            height_detail=itemView.findViewById(R.id.height_detail);
            shimmerFrameLayout=itemView.findViewById(R.id.shimmerFrame2);
        }
    }
}
