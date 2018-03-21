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
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by javaman on 18.12.2017.
 * Jaluzi Dialog
 */

public class JalouiseCurtain extends DialogFragment implements View.OnClickListener{

    @BindView(R.id.btnCancel) ImageButton btnCancel;
    @BindView(R.id.btnSave) ImageButton btnSave;
    @BindView(R.id.btnCalculate) ImageButton btnCalculate;
    @BindView(R.id.editTextWidth) EditText etWidth;
    @BindView(R.id.editTextHeight) EditText etHeight;
    @BindView(R.id.editTextJalousieUnitPrice) EditText etUnitPrice;
    @BindView(R.id.editTextJalousieTotalPrice) EditText etTotalPrice;
    @BindView(R.id.textViewJalousieM2) TextView tvTotalM2;
    double totalPrice,unitPrice,totalM2;

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
        View view=inflater.inflate(R.layout.jalouise_curtain,null);
        ButterKnife.bind(this,view);

        setCancelable(false);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }


    @Override
    @OnClick({R.id.btnSave,R.id.btnCalculate,R.id.btnCancel})
    public void onClick(View view) {
        if (view.getId()==R.id.btnSave){
            dismiss();
        }else if(view.getId()==R.id.btnCancel){
            dismiss();
        }else{

            if(!etWidth.getText().toString().equals("") &&
                    !etHeight.getText().toString().equals("") &&
                    !etUnitPrice.getText().toString().equals("")){

                double width=Double.parseDouble(etWidth.getText().toString());
                double height=Double.parseDouble(etHeight.getText().toString());
                double widthLast = calculateWidth(width);
                double heightLast = calculateHeight(height);

                unitPrice = Double.parseDouble(etUnitPrice.getText().toString());

                totalPrice = calculateTotalPrice(width,height , unitPrice);
                totalM2=widthLast*heightLast;

                tvTotalM2.setText(String.format("%.2f",totalM2));
                etTotalPrice.setText(String.format("%.2f", totalPrice));

            }else{
                Toast.makeText(getActivity(), "Gerekli alanları doldurunuz", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public double calculateTotalPrice(double width, double height, double unitPrice) {
        double widthLast = calculateWidth(width);
        double heightLast = calculateHeight(height);
        return widthLast * heightLast * unitPrice;
    }

    public double calculateWidth(double width) {
        if (width <= 100) {
            width = 100;

        } else {
            if (width % 10 != 0) {
                width = Math.round((width + 5) / 10) * 10;
            }
        }
        return width/100;
    }

    public double calculateHeight(double height) {

        if (height <= 200) {
            height = 200;
        } else if (height <= 260 && height > 200) {
            height = 260;
        } else if (height <= 300 && height > 260) {
            height = 300;
        }

        return height/100;
    }
}

