package com.vinikuria.the20first;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Mainpage extends Fragment {

ImageView menu,group,search,chat,notification,notifheart,notifdot;
View searchbar;
RecyclerView main_recyclerview;

    public Mainpage() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mainpage, container, false);
        menu=view.findViewById(R.id.menu);
        group=view.findViewById(R.id.group);
        search=view.findViewById(R.id.search);
        chat=view.findViewById(R.id.chat);
        notification=view.findViewById(R.id.notification);
        notifheart=view.findViewById(R.id.notifheart);
        notifdot=view.findViewById(R.id.notifdot);
        main_recyclerview=view.findViewById(R.id.main_recyclerview);
        searchbar=view.findViewById(R.id.searchbar);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.setVisibility(View.GONE);
                group.setVisibility(View.GONE);
                chat.setVisibility(View.GONE);
                notification.setVisibility(View.GONE);
                notifheart.setVisibility(View.GONE);
                notifdot.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) search.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_TOP,R.id.searchbar);
                params.addRule(RelativeLayout.ALIGN_END,R.id.searchbar);
                params.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.searchbar);
                params.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.searchbar);
                params.setMarginEnd(15);
                search.setLayoutParams(params);
            }
        });

        return view;
    }


}