package com.vinikuria.the20first;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.security.PKCS12Attribute;

public class Mainpage extends Fragment {

    ImageView menu,group,search,chat,notification,notifheart,notifdot;
    View searchbar;
    RecyclerView main_recyclerview;
    static LinearLayout menu_dropdown;
    EditText search_area;
    TextView menu_crt_grp_date,menu_post_photo,menu_settings,menu_logout;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    SwipeRefreshLayout swipeRefreshLayout;
    boolean menuIsUp=false;
    private ViewPager2 viewPager2;

    public Mainpage() {
        // Required empty public constructor
    }

    public Mainpage(ViewPager2 viewPager2) {
        this.viewPager2 = viewPager2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.search_open=false;
        View view=inflater.inflate(R.layout.fragment_mainpage, container, false);
        menu_crt_grp_date=view.findViewById(R.id.create_group_date);
        menu_post_photo=view.findViewById(R.id.post_photo);
        menu_settings=view.findViewById(R.id.settings);
        menu_logout=view.findViewById(R.id.logout);
        menu=view.findViewById(R.id.menu);
        group=view.findViewById(R.id.group);
        search=view.findViewById(R.id.search);
        chat=view.findViewById(R.id.chat);
        notification=view.findViewById(R.id.notification);
        notifheart=view.findViewById(R.id.notifheart);
        notifdot=view.findViewById(R.id.notifdot);
        main_recyclerview=view.findViewById(R.id.main_recyclerview);
        searchbar=view.findViewById(R.id.searchbar);
        search_area=view.findViewById(R.id.search_area);
        menu_dropdown=view.findViewById(R.id.menu_dropdown);
        swipeRefreshLayout=view.findViewById(R.id.swipeMainpage);
        main_recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        main_recyclerview.setAdapter(new MainpageRecycerAdapter(getActivity()));


        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#260752"),Color.parseColor("#A600B2"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /**
                 *
                 *
                 * */
            }
        });

        menu_post_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu_dropdown.setVisibility(View.GONE);
                menuIsUp=false;
                startActivity(new Intent(getContext(),DisplaystorageActivity.class));
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MessagesActivity.class);
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),NotificationsActivity.class);
                startActivity(intent);
            }
        });

        menu_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_dropdown.getVisibility()==View.VISIBLE){
                    menu_dropdown.setVisibility(View.GONE);
                }
                MainActivity.search_open=true;
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
               // params.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.searchbar);
                params.setMarginEnd(15);
                search.setLayoutParams(params);

                RelativeLayout.LayoutParams params1= (RelativeLayout.LayoutParams) search_area.getLayoutParams();
                params1.addRule(RelativeLayout.ALIGN_TOP,R.id.searchbar);
                params1.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.searchbar);
                params1.setMargins(20,15,0,15);
                params1.addRule(RelativeLayout.ALIGN_START,R.id.searchbar);
                params1.addRule(RelativeLayout.ALIGN_LEFT,R.id.search);
                search_area.setLayoutParams(params1);
                search_area.setVisibility(View.VISIBLE);
            }
        });

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(1);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuIsUp){
                    menu_dropdown.setVisibility(View.GONE);
                    menuIsUp=false;
                } else{
                    menu_dropdown.setVisibility(View.VISIBLE);
                    menuIsUp=true;
                }

            }
        });

        return view;
    }
}