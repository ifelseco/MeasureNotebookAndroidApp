package com.javaman.olcudefteri.notification;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.javaman.olcudefteri.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationFragment extends Fragment {

    @BindView(R.id.fragment_text)
    TextView textViewFrag;
    String text;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments().containsKey("text")){
            text=getArguments().getString("text");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        if(!TextUtils.equals(text,"")){
            textViewFrag.setText(text);
        }

    }






}
