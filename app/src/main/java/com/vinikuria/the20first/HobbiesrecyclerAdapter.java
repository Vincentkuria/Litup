package com.vinikuria.the20first;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class HobbiesrecyclerAdapter extends RecyclerView.Adapter {

   String[] hobbies= new String[]{"swimming", "dancing", "singing","chess","draft","american football","parting","sky diving","mountain climbing","sex","alcohol drinking"};
   static LinkedList<String> Oldhobbies=new LinkedList<>();
   static LinkedList<String> Myhobbies=new LinkedList<>();
   int a=0;
   DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
   FirebaseAuth mAuth=FirebaseAuth.getInstance();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (a==0){
            Myhobbies.clear();
            Oldhobbies.clear();
            String emailId = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail().replace(".", "").trim();
            databaseReference.child("USERS").child(emailId.toUpperCase()).child("PROFILE").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() && snapshot.hasChild("Hobbies")){
                        snapshot.child("Hobbies").getChildren();
                        for (DataSnapshot dss:snapshot.child("Hobbies").getChildren()){
                            Myhobbies.add(dss.getValue(String.class));
                        }
                        Log.d("wati", String.valueOf(Myhobbies.size()));
                        Oldhobbies.addAll(Myhobbies);
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            a++;
        }
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.singlehobb_resycler,parent,false);
        return new HobbiesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        HobbiesHolder hobbiesholder= (HobbiesHolder) holder;
        hobbiesholder.checkBox.setText(hobbies[position]);
        hobbiesholder.checkBox.setChecked(Myhobbies.contains(hobbies[position]));
    }

    @Override
    public int getItemCount() {
        return hobbies.length;
    }


    public class HobbiesHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public HobbiesHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    if (checkBox.isChecked()){
                        if (!Myhobbies.contains(hobbies[position])){
                            Myhobbies.add(hobbies[position]);
                        }
                    }else {
                        Myhobbies.remove(hobbies[position]);
                    }

                    if (Oldhobbies.containsAll(Myhobbies) && Oldhobbies.size()==Myhobbies.size() ){
                        Getdetails.hobbies_save.setVisibility(View.INVISIBLE);
                    } else {
                        Getdetails.hobbies_save.setVisibility(View.VISIBLE);
                    }

                }
            });

        }
    }


}
