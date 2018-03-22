package com.javaman.olcudefteri.orders.curtain_type_dialog;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by javaman on 18.12.2017.
 * Güneşlik dialog
 */

public class SunBlindCurtain extends DialogFragment implements View.OnClickListener{


    @BindView(R.id.btnCancel) ImageButton btnCancel;
    @BindView(R.id.btnSave) ImageButton btnSave;
    @BindView(R.id.btnCalculate) ImageButton btnCalculate;
    @BindView(R.id.editTextWidth) EditText etWidth;
    @BindView(R.id.editTextHeight) EditText etHeight;
    @BindView(R.id.editTextSunBlindUnitPrice) EditText etUnitPrice;
    @BindView(R.id.editTextSunBlindTotalPrice) EditText etTotalPrice;
    @BindView(R.id.progress_bar_save) ProgressBar progressBarSave;
    @BindView(R.id.progress_bar_calc) ProgressBar progressBarCalc;
    @BindView(R.id.radi_group_color) RadioGroup radioGroupColor;
    private double unitPrice;


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
        View view=inflater.inflate(R.layout.sun_blid_curtain,null);
        ButterKnife.bind(this,view);
        setCancelable(false);
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }

    @Override
    @OnClick({R.id.btnSave , R.id.btnCancel , R.id.btnCalculate})
    public void onClick(View view) {
        if (view.getId()==R.id.btnSave){
            dismiss();
        }else if(view.getId()==R.id.btnCancel){
            dismiss();
        }else{

            if(!etWidth.getText().toString().equals("") &&
                    !etUnitPrice.getText().toString().equals("")){

                double width=Double.parseDouble(etWidth.getText().toString());

                unitPrice=Double.parseDouble(etUnitPrice.getText().toString());

                double totalPrice=calculate(width,unitPrice);

                etTotalPrice.setText(String.format("%.2f",totalPrice));

            }else{
                Toast.makeText(getActivity(), "Gerekli alanları doldurunuz.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public double calculate(double width , double unitPrice){
        double widthLast=(width+20)/100;
        return widthLast*unitPrice;
    }

}

