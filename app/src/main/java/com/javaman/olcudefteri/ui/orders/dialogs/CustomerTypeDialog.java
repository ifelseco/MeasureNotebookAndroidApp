package com.javaman.olcudefteri.ui.orders.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.javaman.olcudefteri.R;
import com.javaman.olcudefteri.ui.home.HomeActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomerTypeDialog extends DialogFragment implements View.OnClickListener{

    @BindView(R.id.btn_new_customer)
    Button btnNewCust;

    @BindView(R.id.btn_saved_customer)
    Button btnSavedCust;

    @BindView(R.id.btn_close)
    ImageButton buttonClose;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_type_dialog_layout, null);
        ButterKnife.bind(this, view);
        setCancelable(false);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }

    @Override
    @OnClick({R.id.btn_new_customer,R.id.btn_saved_customer,R.id.btn_close})
    public void onClick(View v) {
        if(v.getId()==R.id.btn_saved_customer){
            Intent customers = new Intent(getActivity(), HomeActivity.class);
            customers.putExtra("init-key", "get-customers-fragment");
            startActivity(customers);
        }else if(v.getId()==R.id.btn_new_customer){
            dismiss();
        }else if(v.getId()==R.id.btn_close){
            startActivity(new Intent(getActivity(),HomeActivity.class));
        }
    }


}
