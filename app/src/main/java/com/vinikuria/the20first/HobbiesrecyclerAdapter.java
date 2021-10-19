package com.vinikuria.the20first;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

public class HobbiesrecyclerAdapter extends RecyclerView.Adapter {

   String[] hobbies= new String[]{"swimming", "dancing", "singing"};
   static LinkedList<String> Oldhobbies=new LinkedList<>();
   static LinkedList<String> Myhobbies=new LinkedList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /**download myhobbies from database to start using**/
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
