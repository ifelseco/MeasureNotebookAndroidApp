package com.javaman.olcudefteri.add_order.curtain_type_dialog;

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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.javaman.olcudefteri.R;

/**
 * Created by javaman on 18.12.2017.
 * Tül perde dialog
 */
public class NetCurtain extends DialogFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener {

    EditText etWidth, etHeight, etVariant, etPattern, etAlias, etOtherPile, etUnitprice,etTotalPrice,etDesc;
    TextView tvTotalMeter;
    Button btnSave, btnCancel,btnCalculate;
    RadioGroup radioGroupPile;


    double totalPrice ;
    double unitPrice ;
    double totalM;
    double pile;

    private void resetRadioButton() {

        radioGroupPile.clearCheck();

    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.net_curtain_layout, null);

        etWidth = view.findViewById(R.id.editTextWidth);
        etHeight = view.findViewById(R.id.editTextHeight);
        etVariant = view.findViewById(R.id.editTextVariant);
        etPattern = view.findViewById(R.id.editTextPattern);
        etAlias = view.findViewById(R.id.editTextAlias);
        etOtherPile = view.findViewById(R.id.editTextOtherPile);
        etUnitprice = view.findViewById(R.id.editTextNetUnitPrice);
        etTotalPrice=view.findViewById(R.id.editTextNetTotalPrice);
        tvTotalMeter = view.findViewById(R.id.textViewNetM);
        radioGroupPile = view.findViewById(R.id.radiGroupPile);



        radioGroupPile.setOnCheckedChangeListener(this);
        etOtherPile.setOnFocusChangeListener(this);


        btnSave = view.findViewById(R.id.btnSave);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnCalculate=view.findViewById(R.id.btnCalculate);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnCalculate.setOnClickListener(this);


        setCancelable(false);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        return view;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btnSave) {
            dismiss();
        }else if(view.getId() == R.id.btnCancel){
            dismiss();
        }else{
            if(!etWidth.getText().toString().equals("") &&
                    !etUnitprice.getText().toString().equals("") &&
                    (radioGroupPile.getCheckedRadioButtonId()!=-1 || !etOtherPile.getText().toString().equals(""))){

                unitPrice=Double.parseDouble(etUnitprice.getText().toString());

                if(radioGroupPile.getCheckedRadioButtonId()!=-1){
                    int checkedId=radioGroupPile.getCheckedRadioButtonId();
                    if(checkedId==R.id.radioButton2){
                        pile=2;
                    }else if(checkedId==R.id.radioButton2_5){
                        pile=2.5;
                    }else{
                        pile=3;
                    }
                }else{
                    pile=Double.parseDouble(etOtherPile.getText().toString());
                }

                double netWidth=Double.parseDouble(etWidth.getText().toString());

                totalPrice=calculate(netWidth,pile,unitPrice);
                totalM=(netWidth/100)*pile;

                tvTotalMeter.setText(String.format("%.2f",totalM));
                etTotalPrice.setText(String.format("%.2f",totalPrice));

            }else{
                Toast.makeText(getActivity(), "Gerekli alanları doldurunuz.", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.radioButton2:
                etOtherPile.clearFocus();
                etOtherPile.setText("");
                break;

            case R.id.radioButton2_5:
                etOtherPile.clearFocus();
                etOtherPile.setText("");
                break;

            case R.id.radioButton3:
                etOtherPile.clearFocus();
                etOtherPile.setText("");
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v.getId()==R.id.editTextOtherPile){
            if (hasFocus) {
                resetRadioButton();
            }
        }else{

        }
    }

    public double calculate(double width , double pile , double unitPrice){
        double widthLast=width/100;
        return widthLast*pile*unitPrice;
    }

}
